package de.demarks.wms.common.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import de.demarks.wms.common.service.Interface.StockStorageManageService;
import de.demarks.wms.common.util.ExcelUtil;
import de.demarks.wms.dao.GoodsMapper;
import de.demarks.wms.dao.RepositoryBatchMapper;
import de.demarks.wms.dao.RepositoryMapper;
import de.demarks.wms.dao.StockStorageMapper;
import de.demarks.wms.domain.Goods;
import de.demarks.wms.domain.Repository;
import de.demarks.wms.domain.RepositoryBatch;
import de.demarks.wms.domain.StockStorage;
import de.demarks.wms.exception.StorageManageServiceException;
import de.demarks.wms.util.aop.UserOperation;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 待检测库存信息管理 service 实现类
 *
 * @author huanyingcool
 */
@Service
public class StockStorageManageServiceImpl implements StockStorageManageService {

    @Autowired
    private StockStorageMapper stockStorageMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private RepositoryBatchMapper repositoryBatchMapper;
    @Autowired
    private RepositoryMapper repositoryMapper;
    @Autowired
    private ExcelUtil excelUtil;

    /**
     * 返回所有的库存记录
     *
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    @Override
    public Map<String, Object> selectAll(Integer batchID, Integer repository) throws StorageManageServiceException {
        return selectAll(batchID, repository, -1, -1);
    }

    /**
     * 分页返回所有的库存记录
     *
     * @param offset 分页偏移值
     * @param limit  分页大小
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    @Override
    public Map<String, Object> selectAll(Integer batchID, Integer repositoryID, int offset, int limit) throws StorageManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        List<StockStorage> stockStorageList;
        long total = 0;
        boolean isPagination = true;

        // validate
        if (offset < 0 || limit < 0)
            isPagination = false;
        if (batchID<0)
            batchID = null;
        if (repositoryID<0)
            repositoryID = null;

        // query
        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                stockStorageList = stockStorageMapper.selectAll(batchID, repositoryID);
                if (stockStorageList != null) {
                    PageInfo<StockStorage> pageInfo = new PageInfo<>(stockStorageList);
                    total = pageInfo.getTotal();
                } else
                    stockStorageList = new ArrayList<>();
            } else {
                stockStorageList = stockStorageMapper.selectAll(batchID,repositoryID);
                if (stockStorageList != null)
                    total = stockStorageList.size();
                else
                    stockStorageList = new ArrayList<>();
            }
        } catch (PersistenceException e) {
            throw new StorageManageServiceException(e);
        }

        resultSet.put("data", stockStorageList);
        resultSet.put("total", total);
        return resultSet;
    }

    /**
     * 返回指定货物ID的库存记录
     *
     * @param goodsID 指定的货物ID
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    @Override
    public Map<String, Object> selectByGoodsID(Integer goodsID, Integer batchID, Integer repository) throws StorageManageServiceException {
        return selectByGoodsID(goodsID, batchID, repository, -1, -1);
    }

    /**
     * 分页返回指定的货物库存记录
     *
     * @param goodsID 指定的货物ID
     * @param offset  分页偏移值
     * @param limit   分页大小
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    @Override
    public Map<String, Object> selectByGoodsID(Integer goodsID, Integer batchID, Integer repositoryID, int offset, int limit) throws StorageManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        List<StockStorage> stockStorageList;
        long total = 0;
        boolean isPagination = true;

        // validate
        if (offset < 0 || limit < 0)
            isPagination = false;
        if (goodsID<0)
            goodsID = null;
        if (batchID<0)
            batchID = null;
        if (repositoryID < 0)
            repositoryID = null;

        // query
        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                stockStorageList = stockStorageMapper.selectByGoodsID(goodsID, batchID, repositoryID);
                if (stockStorageList != null) {
                    PageInfo<StockStorage> pageInfo = new PageInfo<>(stockStorageList);
                    total = pageInfo.getTotal();
                } else
                    stockStorageList = new ArrayList<>();
            } else {
                stockStorageList = stockStorageMapper.selectByGoodsID(goodsID, batchID, repositoryID);
                if (stockStorageList != null)
                    total = stockStorageList.size();
                else
                    stockStorageList = new ArrayList<>();
            }
        } catch (PersistenceException e) {
            throw new StorageManageServiceException(e);
        }

        resultSet.put("data", stockStorageList);
        resultSet.put("total", total);
        return resultSet;
    }

    /**
     * 返回指定货物名称的库存记录
     *
     * @param goodsName 货物名称
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    @Override
    public Map<String, Object> selectByGoodsName(String goodsName, Integer batchID, Integer repository) throws StorageManageServiceException {
        return selectByGoodsName(goodsName, batchID, repository, -1, -1);
    }

    /**
     * 分页返回指定货物名称的库存记录
     *
     * @param goodsName 货物名称
     * @param offset    分页偏移值
     * @param limit     分页大小
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    @Override
    public Map<String, Object> selectByGoodsName(String goodsName, Integer batchID, Integer repositoryID, int offset, int limit) throws StorageManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        List<StockStorage> stockStorageList;
        long total = 0;
        boolean isPagination = true;

        // validate
        if (offset < 0 || limit < 0)
            isPagination = false;
        if (batchID < 0)
            batchID = null;
        if (repositoryID < 0)
            repositoryID = null;

        // query
        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                stockStorageList = stockStorageMapper.selectByGoodsName(goodsName, batchID, repositoryID);
                if (stockStorageList != null) {
                    PageInfo<StockStorage> pageInfo = new PageInfo<>(stockStorageList);
                    total = pageInfo.getTotal();
                } else
                    stockStorageList = new ArrayList<>();
            } else {
                stockStorageList = stockStorageMapper.selectByGoodsName(goodsName, batchID, repositoryID);
                if (stockStorageList != null)
                    total = stockStorageList.size();
                else
                    stockStorageList = new ArrayList<>();
            }
        } catch (PersistenceException e) {
            throw new StorageManageServiceException(e);
        }

        resultSet.put("data", stockStorageList);
        resultSet.put("total", total);
        return resultSet;
    }

    /**
     * 添加一条库存记录
     *
     * @param goodsID      指定的货物ID
     * @param batchID      指定的批次ID
     * @param repositoryID 指定的仓库ID
     * @param number       库存数量
     * @return 返回一个boolean值，值为true代表更新成功，否则代表失败
     */
    @UserOperation(value = "添加库存记录")
    @Override
    public boolean addStorage(Integer goodsID, Integer batchID, Integer repositoryID, long number) throws StorageManageServiceException {
        try {
            boolean isAvailable = true;

            // validate
            Goods goods = goodsMapper.selectById(goodsID);
            RepositoryBatch repositoryBatch = repositoryBatchMapper.selectByID(batchID,repositoryID);
            Repository repository = repositoryMapper.selectByID(repositoryID);
            if (goods == null)
                isAvailable = false;
            if (repositoryBatch == null)
                isAvailable = false;
            if (repository == null)
                isAvailable = false;
            if (number < 0)
                isAvailable = false;
            List<StockStorage> stockStorageList = stockStorageMapper.selectByGoodsID(goodsID, batchID, repositoryID);
            if (!(stockStorageList != null && stockStorageList.isEmpty()))
                isAvailable = false;

            if (isAvailable) {
                // insert
                StockStorage stockStorage = new StockStorage();
                stockStorage.setGoodsID(goodsID);
                stockStorage.setBatchID(batchID);
                stockStorage.setRepositoryID(repositoryID);
                stockStorage.setNumber(number);
                stockStorageMapper.insert(stockStorage);
            }
            return isAvailable;
        } catch (PersistenceException e) {
            throw new StorageManageServiceException(e);
        }
    }

    /**
     * 更新一条库存记录
     *
     * @param goodsID      指定的货物ID
     * @param batchID      指定的批次ID
     * @param repositoryID 指定的仓库ID
     * @param number       更新的库存数量
     * @return 返回一个boolean值，值为true代表更新成功，否则代表失败
     */
    @UserOperation(value = "修改库存记录")
    @Override
    public boolean updateStorage(Integer goodsID, Integer batchID, Integer repositoryID, long number) throws StorageManageServiceException {
        try {
            boolean isUpdate = false;
            // validate
            List<StockStorage> stockStorageList = stockStorageMapper.selectByGoodsID(goodsID, batchID, repositoryID);
            if (stockStorageList != null && !stockStorageList.isEmpty()) {
                if (number >= 0) {
                    // update
                    StockStorage stockStorage = stockStorageList.get(0);
                    stockStorage.setNumber(number);
                    stockStorageMapper.update(stockStorage);
                    isUpdate = true;
                }
            }

            return isUpdate;
        } catch (PersistenceException e) {
            throw new StorageManageServiceException(e);
        }
    }

    /**
     * 删除一条库存记录
     * 货物ID与批次ID与仓库ID可唯一确定一条库存记录
     *
     * @param goodsID      指定的货物ID
     * @param batchID      指定的批次ID
     * @param repositoryID 指定的仓库ID
     * @return 返回一个boolean值，值为true代表更新成功，否则代表失败
     */
    @UserOperation(value = "删除库存记录")
    @Override
    public boolean deleteStorage(Integer goodsID, Integer batchID, Integer repositoryID) throws StorageManageServiceException {
        try {
            boolean isDelete = false;

            // validate
            List<StockStorage> stockStorageList = stockStorageMapper.selectByGoodsID(goodsID, batchID, repositoryID);
            if (stockStorageList != null && !stockStorageList.isEmpty()) {
                // delete
                stockStorageMapper.delete(goodsID, batchID, repositoryID);
                isDelete = true;
            }

            return isDelete;
        } catch (PersistenceException e) {
            throw new StorageManageServiceException(e);
        }
    }

    /**
     * 导入库存记录
     *
     * @param file 保存有的库存记录的文件
     * @return 返回一个Map，其中：key为total代表导入的总记录数，key为available代表有效导入的记录数
     */
    @UserOperation(value = "导入库存记录")
    @Override
    public Map<String, Object> importStorage(MultipartFile file) throws StorageManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        int total = 0;
        int available = 0;

        // 从文件中读取
        List<Object> storageList = excelUtil.excelReader(StockStorage.class, file);
        if (storageList != null) {
            total = storageList.size();

            try {
                StockStorage stockStorage;
                boolean isAvailable;
                List<StockStorage> availableList = new ArrayList<>();
                Goods goods;
                RepositoryBatch repositoryBatch;
                Repository repository;
                for (Object object : storageList) {
                    isAvailable = true;
                    stockStorage = (StockStorage) object;

                    // validate
                    goods = goodsMapper.selectById(stockStorage.getGoodsID());
                    repositoryBatch = repositoryBatchMapper.selectByID(stockStorage.getBatchID(),null);
                    repository = repositoryMapper.selectByID(stockStorage.getRepositoryID());
                    if (goods == null)
                        isAvailable = false;
                    if (repositoryBatch == null)
                        isAvailable = false;
                    if (repository == null)
                        isAvailable = false;
                    if (stockStorage.getNumber() < 0)
                        isAvailable = false;
                    List<StockStorage> temp = stockStorageMapper.selectByGoodsID(stockStorage.getGoodsID(), stockStorage.getBatchID(), stockStorage.getRepositoryID());
                    if (!(temp != null && temp.isEmpty()))
                        isAvailable = false;

                    if (isAvailable) {
                        availableList.add(stockStorage);
                    }
                }
                // 保存到数据库
                available = availableList.size();
                System.out.println(available);
                if (available > 0)
                    stockStorageMapper.insertBatch(availableList);
            } catch (PersistenceException e) {
                throw new StorageManageServiceException(e);
            }
        }

        resultSet.put("total", total);
        resultSet.put("available", available);
        return resultSet;
    }

    /**
     * 导出库存记录
     *
     * @param stockStorageList 保存有库存记录的List
     * @return excel 文件
     */
    @UserOperation(value = "导出库存记录")
    @Override
    public File exportStorage(List<StockStorage> stockStorageList) {
        if (stockStorageList == null)
            return null;
        return excelUtil.excelWriter(StockStorage.class, stockStorageList);
    }

    /**
     * 为指定的货物库存记录增加指定数目
     *
     * @param goodsID      货物ID
     * @param batchID      批次ID
     * @param repositoryID 仓库ID
     * @param number       增加的数量
     * @return 返回一个 boolean 值，若值为true表示数目增加成功，否则表示增加失败
     */
    @Override
    public boolean storageIncrease(Integer goodsID, Integer batchID, Integer repositoryID, long number) throws StorageManageServiceException {

        // 检查货物库存增加数目的有效性
        if (number < 0)
            return false;

        synchronized (this) {
            // 检查对应的库存记录是否存在
            StockStorage stockStorage = getStorage(goodsID, batchID, repositoryID);
            if (stockStorage != null) {
                long newStorage = stockStorage.getNumber() + number;
                updateStorage(goodsID, batchID, repositoryID, newStorage);
            } else {
                addStorage(goodsID, batchID, repositoryID, number);
            }
        }
        return true;
    }

    /**
     * 为指定的货物库存记录减少指定的数目
     *
     * @param goodsID      货物ID
     * @param batchID      批次ID
     * @param repositoryID 仓库ID
     * @param number       减少的数量
     * @return 返回一个 boolean 值，若值为 true 表示数目减少成功，否则表示减少失败
     */
    @Override
    public boolean storageDecrease(Integer goodsID, Integer batchID, Integer repositoryID, long number) throws StorageManageServiceException {

        synchronized (this) {
            // 检查对应的库存记录是否存在
            StockStorage stockStorage = getStorage(goodsID, batchID, repositoryID);
            if (null != stockStorage) {
                // 检查库存减少数目的范围是否合理
                if (number < 0 || stockStorage.getNumber() < number)
                    return false;

                long newStorage = stockStorage.getNumber() - number;
                updateStorage(goodsID, batchID, repositoryID, newStorage);
                return true;
            } else
                return false;
        }
    }

    /**
     * 获取指定货物ID，仓库ID对应的库存记录
     *
     * @param goodsID      货物ID
     * @param batchID      批次ID
     * @param repositoryID 仓库ID
     * @return 若存在则返回对应的记录，否则返回null
     */
    private StockStorage getStorage(Integer goodsID, Integer batchID, Integer repositoryID) {
        StockStorage stockStorage = null;
        List<StockStorage> stockStorageList = stockStorageMapper.selectByGoodsID(goodsID, batchID, repositoryID);
        if (!stockStorageList.isEmpty())
            stockStorage = stockStorageList.get(0);
        return stockStorage;
    }

}
