package de.demarks.wms.common.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import de.demarks.wms.common.service.Interface.DetectStorageService;
import de.demarks.wms.common.service.Interface.PacketStorageManageService;
import de.demarks.wms.common.service.Interface.StockRecordManageService;
import de.demarks.wms.common.service.Interface.StockStorageManageService;
import de.demarks.wms.dao.*;
import de.demarks.wms.domain.*;
import de.demarks.wms.exception.*;
import de.demarks.wms.util.aop.UserOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class StockRecordManageServiceImpl implements StockRecordManageService {

    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private PacketMapper packetMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private RepositoryBatchMapper repositoryBatchMapper;
    @Autowired
    private RepositoryMapper repositoryMapper;
    @Autowired
    private PacketStorageManageService packetStorageManageService;
    @Autowired
    private StockStorageManageService stockStorageManageService;
    @Autowired
    private DetectStorageService detectStorageService;
    @Autowired
    private StockInMapper stockinMapper;
    @Autowired
    private StockOutMapper stockOutMapper;
    @Autowired
    private DetectStorageMapper detectStorageMapper;

    /**
     * 入库操作
     * @param packetID
     * @param goodsID
     * @param batchID
     * @param repositoryID
     * @param number
     * @param personInCharge
     * @return
     * @throws StockRecordManageServiceException
     */
    @UserOperation(value = "货物入库")
    @Override
    public boolean stockInOperation(Integer packetID, Integer customerID, Integer goodsID, Integer batchID, Integer repositoryID,
                                    long number, String personInCharge) throws StockRecordManageServiceException {

        // ID对应的记录是否存在
        if (!(packetValidate(packetID) && customerValidate(customerID) && goodsValidate(goodsID)))
            return false;

        if (personInCharge == null)
            return false;

        // 检查入库数量有效性
        if (number < 0)
            return false;

        try {
            // 减少包裹未到货storage
            boolean  packetSuccess, storageSuccess;
            packetSuccess = packetStorageManageService.packetStorageIncrease(goodsID, packetID, repositoryID, number);

            // 增加待检测库存量
            storageSuccess = stockStorageManageService.storageIncrease(goodsID, batchID, repositoryID, number);

            // 保存入库记录
            if (packetSuccess && storageSuccess ) {
                StockInDO stockInDO = new StockInDO();
                stockInDO.setGoodsID(goodsID);
                stockInDO.setBatchID(batchID);
                stockInDO.setCustomerID(customerID);
                stockInDO.setNumber(number);
                stockInDO.setPersonInCharge(personInCharge);
                stockInDO.setTime(new Date());
                stockInDO.setRepositoryID(repositoryID);
                stockinMapper.insert(stockInDO);
            }
            return packetSuccess && storageSuccess;
        } catch (PersistenceException | PacketStorageManageServiceException | StorageManageServiceException e) {
            throw new StockRecordManageServiceException(e);
        }
    }

    /**
     * 出库操作
     * @param packetID
     * @param customerID
     * @param goodsID
     * @param batchID
     * @param repositoryID
     * @param number
     * @param personInCharge
     * @return
     * @throws StockRecordManageServiceException
     */
    @UserOperation(value = "货物出库")
    @Override
    public boolean stockOutOperation(Integer packetID, Integer customerID, Integer goodsID, Integer batchID, Integer repositoryID,
                                     long number, String personInCharge) throws StockRecordManageServiceException {

        // 检查ID对应的记录是否存在
        if (!(packetValidate(packetID) && customerValidate(customerID) && goodsValidate(goodsID)))
            return false;
        List<DetectStorage> detectStorageList = detectStorageMapper.selectByGoodsID(goodsID, batchID, repositoryID);
        if (detectStorageList.isEmpty())
            return false;
        DetectStorage detectStorage = detectStorageList.get(0);
        long passed = detectStorage.getPassed();
        if (number < 0 || passed < number)
            return false;

        try {
            // 更新库存信息
            boolean isSuccess;
            // 从带发货良品中减去库存
            isSuccess = detectStorageService.detectStoragePassedDecrease(goodsID, batchID, repositoryID, number);

            // 保存出库记录
            if (isSuccess) {
                StockOutDO stockOutDO = new StockOutDO();
                stockOutDO.setCustomerID(customerID);
                stockOutDO.setGoodsID(goodsID);
                stockOutDO.setBatchID(batchID);
                stockOutDO.setNumber(number);
                stockOutDO.setPersonInCharge(personInCharge);
                stockOutDO.setRepositoryID(repositoryID);
                stockOutDO.setTime(new Date());
                stockOutMapper.insert(stockOutDO);
            }

            return isSuccess;
        } catch (PersistenceException | DetectStorageServiceException e) {
            throw new StockRecordManageServiceException(e);
        }
    }

    /**
     * 查询出入库记录
     *
     * @param batchID      批次ID
     * @param repositoryID 仓库ID
     * @param endDateStr   查询记录起始日期
     * @param startDateStr 查询记录结束日期
     * @param searchType   记录查询方式
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    @Override
    public Map<String, Object> selectStockRecord(Integer batchID, Integer repositoryID, String startDateStr, String endDateStr, String searchType) throws StockRecordManageServiceException {
        return selectStockRecord(batchID, repositoryID, startDateStr, endDateStr, searchType, -1, -1);
    }

    /**
     * 分页查询出入库记录
     *
     * @param batchID      批次ID
     * @param repositoryID 仓库ID
     * @param endDateStr   查询记录起始日期
     * @param startDateStr 查询记录结束日期
     * @param searchType   记录查询方式
     * @param offset       分页偏移值
     * @param limit        分页大小
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    @SuppressWarnings("unchecked")
    @Override
    public Map<String, Object> selectStockRecord(Integer batchID, Integer repositoryID, String startDateStr, String endDateStr, String searchType, int offset, int limit) throws StockRecordManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        long total = 0;

        // 检查传入参数
        if (batchID == null || repositoryID == null || searchType == null)
            throw new StockRecordManageServiceException("exception");

        // 转换 Date 对象
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        Date newEndDate = null;
        try {
            if (StringUtils.isNotEmpty(startDateStr))
                startDate = dateFormat.parse(startDateStr);
            if (StringUtils.isNotEmpty(endDateStr))
            {
                endDate = dateFormat.parse(endDateStr);
                newEndDate = new Date(endDate.getTime()+(24*60*60*1000)-1);
            }
        } catch (ParseException e) {
            throw new StockRecordManageServiceException(e);
        }

        // 根据查询模式执行查询
        List<StockRecordDTO> stockRecordDTOS = new ArrayList<>();
        Map<String, Object> stockInTemp;
        Map<String, Object> stockOutTemp;
        List<StockInDO> stockInRecordDOS = null;
        List<StockOutDO> stockOutRecordDOS = null;
        switch (searchType) {
            case "all": {
                if (offset < 0 || limit < 0) {
                    stockInTemp = selectStockInRecord(batchID, repositoryID, startDate, endDate, offset, limit);
                    stockOutTemp = selectStockOutRecord(batchID, repositoryID, startDate, endDate, offset, limit);
                    stockInRecordDOS = (List<StockInDO>) stockInTemp.get("data");
                    stockOutRecordDOS = (List<StockOutDO>) stockOutTemp.get("data");
                } else {
                    int stockInRecordOffset = offset / 2;
                    int stockOutRecordOffset = stockInRecordOffset * 2 < offset ? stockInRecordOffset + 1 : stockInRecordOffset;
                    int stockInRecordLimit = limit / 2;
                    int stockOutRecordLimit = stockInRecordLimit * 2 < limit ? stockInRecordLimit + 1 : stockInRecordLimit;

                    stockInTemp = selectStockInRecord(batchID, repositoryID, startDate, newEndDate, stockInRecordOffset, limit);
                    stockOutTemp = selectStockOutRecord(batchID, repositoryID, startDate, newEndDate, stockOutRecordOffset, limit);

                    stockInRecordDOS = (List<StockInDO>) stockInTemp.get("data");
                    stockOutRecordDOS = (List<StockOutDO>) stockOutTemp.get("data");

                    int stockInRecordDosSize = stockInRecordDOS.size();
                    int stockOutRecordDoSize = stockOutRecordDOS.size();
                    if (stockInRecordDosSize >= stockInRecordLimit && stockOutRecordDoSize >= stockOutRecordLimit) {
                        stockInRecordDOS = stockInRecordDOS.subList(0, stockInRecordLimit);
                        stockOutRecordDOS = stockOutRecordDOS.subList(0, stockOutRecordLimit);
                    } else if (stockInRecordDosSize < stockInRecordLimit && stockOutRecordDoSize > stockOutRecordLimit) {
                        int appendSize = (stockOutRecordDoSize - stockOutRecordLimit) > (stockInRecordLimit - stockInRecordDosSize) ?
                                (stockInRecordLimit - stockInRecordDosSize) : (stockOutRecordDoSize - stockOutRecordLimit);
                        stockOutRecordDOS = stockOutRecordDOS.subList(0, stockInRecordLimit + appendSize - 1);
                    } else if (stockOutRecordDoSize < stockOutRecordLimit && stockInRecordDosSize > stockInRecordLimit) {
                        int appendSize = (stockInRecordDosSize - stockInRecordLimit) > (stockOutRecordLimit - stockOutRecordDoSize) ?
                                (stockOutRecordLimit - stockOutRecordDoSize) : (stockInRecordDosSize - stockInRecordLimit);
                        stockInRecordDOS = stockInRecordDOS.subList(0, stockInRecordLimit + appendSize);
                    }
                }
                long stockInRecordDOSTotal = (long) stockInTemp.get("total");
                long stockOutRecordDOSTotal = (long) stockOutTemp.get("total");
                total = stockInRecordDOSTotal + stockOutRecordDOSTotal;
                break;
            }
            case "stockInOnly": {
                stockInTemp = selectStockInRecord(batchID,repositoryID, startDate, newEndDate, offset, limit);
                total = (long) stockInTemp.get("total");
                stockInRecordDOS = (List<StockInDO>) stockInTemp.get("data");
                break;
            }
            case "stockOutOnly": {
                stockOutTemp = selectStockOutRecord(batchID, repositoryID, startDate, newEndDate, offset, limit);
                total = (long) stockOutTemp.get("total");
                stockOutRecordDOS = (List<StockOutDO>) stockOutTemp.get("data");
                break;
            }
            case "none": {
                break;
            }
        }

        if (stockInRecordDOS != null)
            stockInRecordDOS.forEach(stockInDO -> stockRecordDTOS.add(stockInRecordConvertToStockRecordDTO(stockInDO)));
        if (stockOutRecordDOS != null)
            stockOutRecordDOS.forEach(stockOutDO -> stockRecordDTOS.add(stockOutDoConvertToStockRecordDTO(stockOutDO)));

        resultSet.put("data", stockRecordDTOS);
        resultSet.put("total", total);
        return resultSet;
    }

    /**
     * 查询入库记录
     *
     * @param batchID      入库批次ID
     * @param repositoryID 入库仓库ID
     * @param startDate    入库记录起始日期
     * @param endDate      入库记录结束日期
     * @param offset       分页偏移值
     * @param limit        分页大小
     * @return 返回所有符合要求的入库记录
     */
    private Map<String, Object> selectStockInRecord(Integer batchID, Integer repositoryID, Date startDate, Date endDate, int offset, int limit) throws StockRecordManageServiceException {
        Map<String, Object> result = new HashMap<>();
        List<StockInDO> stockInRecords;
        long stockInTotal = 0;
        boolean isPagination = true;

        // 检查是否需要分页查询
        if (offset < 0 || limit < 0)
            isPagination = false;

        // 查询记录
        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                stockInRecords = stockinMapper.selectByDate(batchID, repositoryID, startDate, endDate);
                if (stockInRecords != null)
                    stockInTotal = new PageInfo<>(stockInRecords).getTotal();
                else
                    stockInRecords = new ArrayList<>(10);
            } else {
                stockInRecords = stockinMapper.selectByDate(batchID, repositoryID, startDate, endDate);
                if (stockInRecords != null)
                    stockInTotal = stockInRecords.size();
                else
                    stockInRecords = new ArrayList<>(10);
            }
        } catch (PersistenceException e) {
            throw new StockRecordManageServiceException(e);
        }

        result.put("data", stockInRecords);
        result.put("total", stockInTotal);
        return result;
    }

    /**
     * 查询出库记录
     *
     * @param batchID      出库批次ID
     * @param repositoryID 出库仓库ID
     * @param startDate    出库记录起始日期
     * @param endDate      出库记录结束日期
     * @param offset       分页偏移值
     * @param limit        分页大小
     * @return 返回所有符合要求的出库记录
     */
    private Map<String, Object> selectStockOutRecord(Integer batchID, Integer repositoryID, Date startDate, Date endDate, int offset, int limit) throws StockRecordManageServiceException {
        Map<String, Object> result = new HashMap<>();
        List<StockOutDO> stockOutRecords;
        long stockOutRecordTotal = 0;
        boolean isPagination = true;

        // 检查是否需要分页
        if (offset < 0 || limit < 0)
            isPagination = false;

        // 查询记录
        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                stockOutRecords = stockOutMapper.selectByDate(batchID, repositoryID, startDate, endDate);
                if (stockOutRecords != null)
                    stockOutRecordTotal = new PageInfo<>(stockOutRecords).getTotal();
                else
                    stockOutRecords = new ArrayList<>(10);
            } else {
                stockOutRecords = stockOutMapper.selectByDate(batchID, repositoryID, startDate, endDate);
                if (stockOutRecords != null)
                    stockOutRecordTotal = stockOutRecords.size();
                else
                    stockOutRecords = new ArrayList<>(10);
            }
        } catch (PersistenceException e) {
            throw new StockRecordManageServiceException(e);
        }

        result.put("data", stockOutRecords);
        result.put("total", stockOutRecordTotal);
        return result;
    }

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm");

    /**
     * 将 StockInDO 转换为 StockRecordDTO
     *
     * @param stockInDO StockInDO 对象
     * @return 返回 StockRecordDTO 对象
     */
    private StockRecordDTO stockInRecordConvertToStockRecordDTO(StockInDO stockInDO) {
        StockRecordDTO stockRecordDTO = new StockRecordDTO();
        stockRecordDTO.setRecordID(stockInDO.getId());
        stockRecordDTO.setBatchCode(stockInDO.getBatchCode());
        stockRecordDTO.setSupplierOrCustomerName(stockInDO.getCustomerName());
        stockRecordDTO.setGoodsName(stockInDO.getGoodName());
        stockRecordDTO.setNumber(stockInDO.getNumber());
        stockRecordDTO.setTime(dateFormat.format(stockInDO.getTime()));
        stockRecordDTO.setRepositoryID(stockInDO.getRepositoryID());
        stockRecordDTO.setPersonInCharge(stockInDO.getPersonInCharge());
        stockRecordDTO.setType("入库");
        return stockRecordDTO;
    }

    /**
     * 将 StockOutDO 转换为 StockRecordDTO 对象
     *
     * @param stockOutDO StockOutDO 对象
     * @return 返回 StockRecordDTO 对象
     */
    private StockRecordDTO stockOutDoConvertToStockRecordDTO(StockOutDO stockOutDO) {
        StockRecordDTO stockRecordDTO = new StockRecordDTO();
        stockRecordDTO.setRecordID(stockOutDO.getId());
        stockRecordDTO.setBatchCode(stockOutDO.getBatchCode());
        stockRecordDTO.setSupplierOrCustomerName(stockOutDO.getCustomerName());
        stockRecordDTO.setGoodsName(stockOutDO.getGoodName());
        stockRecordDTO.setNumber(stockOutDO.getNumber());
        stockRecordDTO.setTime(dateFormat.format(stockOutDO.getTime()));
        stockRecordDTO.setRepositoryID(stockOutDO.getRepositoryID());
        stockRecordDTO.setPersonInCharge(stockOutDO.getPersonInCharge());
        stockRecordDTO.setType("出库");
        return stockRecordDTO;
    }

    /**
     *
     * @param packetID
     * @return
     * @throws PacketStorageManageServiceException
     */
    private boolean packetValidate(Integer packetID) throws StockRecordManageServiceException {
        try {
            PacketDO packetDO = packetMapper.selectByPacketID(packetID);
            return packetDO != null;
        } catch (PersistenceException e) {
            throw new StockRecordManageServiceException(e);
        }
    }

    /**
     * 检查货物ID对应的记录是否存在
     *
     * @param goodsID 货物ID
     * @return 若存在则返回true，否则返回false
     */
    private boolean goodsValidate(Integer goodsID) throws StockRecordManageServiceException {
        try {
            Goods goods = goodsMapper.selectById(goodsID);
            return goods != null;
        } catch (PersistenceException e) {
            throw new StockRecordManageServiceException(e);
        }
    }

    /**
     * 检查批次ID对应的记录是否存在
     *
     * @param batchID      批次ID
     * @return 若存在则返回true，否则返回false
     */
    private boolean batchValidate(Integer batchID) throws StockRecordManageServiceException {
        try {
            RepositoryBatch repositoryBatch = repositoryBatchMapper.selectByID(batchID,null);
            return repositoryBatch != null;
        } catch (PersistenceException e) {
            throw new StockRecordManageServiceException(e);
        }
    }

    /**
     * 检查仓库ID对应的记录是否存在
     *
     * @param repositoryID 仓库ID
     * @return 若存在则返回true，否则返回false
     */
    private boolean repositoryValidate(Integer repositoryID) throws StockRecordManageServiceException {
        try {
            Repository repository = repositoryMapper.selectByID(repositoryID);
            return repository != null;
        } catch (PersistenceException e) {
            throw new StockRecordManageServiceException(e);
        }
    }

    /**
     * 检查客户ID对应的记录是否存在
     *
     * @param cumtomerID 客户ID
     * @return 若存在则返回true，否则返回false
     */
    private boolean customerValidate(Integer cumtomerID) throws StockRecordManageServiceException {
        try {
            Customer customer = customerMapper.selectById(cumtomerID);
            return customer != null;
        } catch (PersistenceException e) {
            throw new StockRecordManageServiceException(e);
        }
    }

}
