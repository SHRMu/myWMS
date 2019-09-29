package de.demarks.wms.common.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import de.demarks.wms.common.service.Interface.*;
import de.demarks.wms.common.util.ExcelUtil;
import de.demarks.wms.dao.*;
import de.demarks.wms.domain.*;
import de.demarks.wms.exception.PacketManageServiceException;
import de.demarks.wms.exception.PacketStorageManageServiceException;
import de.demarks.wms.exception.PreStockManageServiceException;
import de.demarks.wms.util.aop.UserOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 预报库存管理 Service
 * @author huanyingcool
 */
@Service
public class PacketStorageManageServiceImpl implements PacketStorageManageService {

    @Autowired
    private ExcelUtil excelUtil;
    @Autowired
    private PacketMapper packetMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private RepositoryMapper repositoryMapper;
    @Autowired
    private PacketStorageMapper packetStorageMapper;

    @Override
    public Map<String, Object> selectAll(Integer packetID, String packetStatus, Integer repositoryID) throws PacketStorageManageServiceException {
        return  selectAll(packetID, packetStatus, repositoryID, -1, -1);
    }

    @Override
    public Map<String, Object> selectAll(Integer packetID, String packetStatus, Integer repositoryID, Integer offset, Integer limit) throws PacketStorageManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        List<PacketStorage> packetStorageList;
        long total = 0;
        boolean isPagination = true;

        // validate
        if (offset < 0 || limit < 0)
            isPagination = false;
        if (packetID < 0)
            packetID = null;
        if (packetStatus.equals(""))
            packetStatus = null;
        if (repositoryID <0)
            repositoryID = null;

        // query
        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                packetStorageList = packetStorageMapper.selectAll(packetID, packetStatus, repositoryID);
                if (packetStorageList != null) {
                    PageInfo<PacketStorage> pageInfo = new PageInfo<>(packetStorageList);
                    total = pageInfo.getTotal();
                } else
                    packetStorageList = new ArrayList<>();
            } else {
                packetStorageList = packetStorageMapper.selectAll(packetID, packetStatus, repositoryID);
                if (packetStorageList != null)
                    total = packetStorageList.size();
                else
                    packetStorageList = new ArrayList<>();
            }
        } catch (PersistenceException e) {
            throw new PacketStorageManageServiceException(e);
        }

        resultSet.put("data", packetStorageList);
        resultSet.put("total", total);
        return resultSet;
    }


    @Override
    public Map<String, Object> selectByTrace(String trace, String status, Integer repositoryID) throws PacketStorageManageServiceException {
        return selectByTrace(trace,status,repositoryID, -1, -1);
    }

    @Override
    public Map<String, Object> selectByTrace(String trace, String status, Integer repositoryID, Integer offset, Integer limit) throws PacketStorageManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        List<PacketStorage> packetStorageList;

        long total = 0;
        boolean isPagination = true;

        // validate
        if (offset < 0 || limit < 0)
            isPagination = false;

        if (status.equals(""))
            status = null;
        if (repositoryID < 0)
            repositoryID = null;

        // query
        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                packetStorageList = packetStorageMapper.selectByPacketTrace(trace, status, repositoryID);
                if (packetStorageList != null) {
                    PageInfo<PacketStorage> pageInfo = new PageInfo<>(packetStorageList);
                    total = pageInfo.getTotal();
                } else
                    packetStorageList = new ArrayList<>();
            } else {
                packetStorageList = packetStorageMapper.selectByPacketTrace(trace, status, repositoryID);
                if (packetStorageList != null)
                    total = packetStorageList.size();
                else
                    packetStorageList = new ArrayList<>();
            }
        } catch (PersistenceException e) {
            throw new PacketStorageManageServiceException(e);
        }

        resultSet.put("data", packetStorageList);
        resultSet.put("total", total);
        return resultSet;
    }

    @Override
    public Map<String, Object> selectByGoodsID(Integer goodsID, Integer packetID, Integer repositoryID) throws PacketStorageManageServiceException {
        return  selectByGoodsID(goodsID, packetID, repositoryID, -1 ,-1);
    }

    @Override
    public Map<String, Object> selectByGoodsID(Integer goodsID, Integer packetID,Integer repositoryID,
                                               Integer offset, Integer limit) throws PacketStorageManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        List<PacketStorage> packetStorageList;

        long total = 0;
        boolean isPagination = true;

        // validate
        if (offset < 0 || limit < 0)
            isPagination = false;

        if (goodsID < 0)
            goodsID = null;
        if (packetID < 0)
            packetID = null;
        if (repositoryID < 0)
            repositoryID = null;

        // query
        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                packetStorageList = packetStorageMapper.selectByGoodsID(goodsID, packetID, repositoryID);
                if (packetStorageList != null) {
                    PageInfo<PacketStorage> pageInfo = new PageInfo<>(packetStorageList);
                    total = pageInfo.getTotal();
                } else
                    packetStorageList = new ArrayList<>();
            } else {
                packetStorageList = packetStorageMapper.selectByGoodsID(goodsID, packetID, repositoryID);
                if (packetStorageList != null)
                    total = packetStorageList.size();
                else
                    packetStorageList = new ArrayList<>();
            }
        } catch (PersistenceException e) {
            throw new PacketStorageManageServiceException(e);
        }

        resultSet.put("data", packetStorageList);
        resultSet.put("total", total);
        return resultSet;
    }

    @Override
    public Map<String, Object> selectByGoodsIDandStatus(Integer goodsID, String packetStatus, Integer repositoryID) throws PacketStorageManageServiceException {
        return  selectByGoodsIDandStatus(goodsID, packetStatus, repositoryID, -1 ,-1);
    }

    @Override
    public Map<String, Object> selectByGoodsIDandStatus(Integer goodsID, String packetStatus, Integer repositoryID,
                                               Integer offset, Integer limit) throws PacketStorageManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        List<PacketStorage> packetStorageList;

        long total = 0;
        boolean isPagination = true;

        // validate
        if (offset < 0 || limit < 0)
            isPagination = false;

        if (goodsID < 0)
            goodsID = null;
        if (packetStatus.equals(""))
            packetStatus = null;
        if (repositoryID < 0)
            repositoryID = null;

        // query
        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                packetStorageList = packetStorageMapper.selectByGoodsIDandStatus(goodsID, packetStatus, repositoryID);
                if (packetStorageList != null) {
                    PageInfo<PacketStorage> pageInfo = new PageInfo<>(packetStorageList);
                    total = pageInfo.getTotal();
                } else
                    packetStorageList = new ArrayList<>();
            } else {
                packetStorageList = packetStorageMapper.selectByGoodsIDandStatus(goodsID, packetStatus, repositoryID);
                if (packetStorageList != null)
                    total = packetStorageList.size();
                else
                    packetStorageList = new ArrayList<>();
            }
        } catch (PersistenceException e) {
            throw new PacketStorageManageServiceException(e);
        }

        resultSet.put("data", packetStorageList);
        resultSet.put("total", total);
        return resultSet;
    }

    @Override
    public Map<String, Object> selectByGoodsNameAndStatus(String goodsName, String packetStatus, Integer repositoryID) throws PacketStorageManageServiceException {
        return selectByGoodsNameAndStatus(goodsName, packetStatus, repositoryID, -1, -1);
    }

    @Override
    public Map<String, Object> selectByGoodsNameAndStatus(String goodsName, String packetStatus, Integer repositoryID, Integer offset, Integer limit) throws PacketStorageManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        List<PacketStorage> packetStorageList;

        long total = 0;
        boolean isPagination = true;

        // validate
        if (offset < 0 || limit < 0)
            isPagination = false;

        if (packetStatus.equals(""))
            packetStatus = null;
        if (repositoryID < 0)
            repositoryID = null;

        // query
        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                packetStorageList = packetStorageMapper.selectByGoodsNameAndStatus(goodsName, packetStatus, repositoryID);
                if (packetStorageList != null) {
                    PageInfo<PacketStorage> pageInfo = new PageInfo<>(packetStorageList);
                    total = pageInfo.getTotal();
                } else
                    packetStorageList = new ArrayList<>();
            } else {
                packetStorageList = packetStorageMapper.selectByGoodsNameAndStatus(goodsName, packetStatus, repositoryID);
                if (packetStorageList != null)
                    total = packetStorageList.size();
                else
                    packetStorageList = new ArrayList<>();
            }
        } catch (PersistenceException e) {
            throw new PacketStorageManageServiceException(e);
        }

        resultSet.put("data", packetStorageList);
        resultSet.put("total", total);
        return resultSet;
    }


    @UserOperation(value = "添加包裹库存记录")
    @Override
    public boolean addPacketStorage(Integer goodsID, Integer packetID,  Integer repositoryID, long number, long storage) throws PacketStorageManageServiceException {
        try {
            boolean isAvailable = true;

            // validate
            PacketDO packetDO = packetMapper.selectByPacketID(packetID);
            Goods goods = goodsMapper.selectById(goodsID);
            Repository repository = repositoryMapper.selectByID(repositoryID);
            if (packetDO == null)
                return false;
            if (goods == null)
                isAvailable = false;
            if (repository == null)
                isAvailable = false;
            if (number < 0)
                isAvailable = false;
            List<PacketStorage> packetStorageList = packetStorageMapper.selectByGoodsID(packetID, goodsID, repositoryID);
            if (!(packetStorageList != null && packetStorageList.isEmpty()))
                isAvailable = false;

            if (isAvailable) {
                // insert
                PacketStorage packetStorage = new PacketStorage();
                packetStorage.setPacketID(packetID);
                packetStorage.setGoodsID(goodsID);
                packetStorage.setRepositoryID(repositoryID);
                packetStorage.setNumber(number);
                packetStorage.setStorage(storage);
                packetStorageMapper.insert(packetStorage);
            }
            return isAvailable;
        } catch (PersistenceException e) {
            throw new PacketStorageManageServiceException(e);
        }
    }

    @UserOperation(value = "更新包裹库存记录")
    @Override
    public boolean updatePacketStorage(Integer goodsID, Integer packetID, Integer repositoryID, long number, long storage) throws PacketStorageManageServiceException {
        try {
            boolean isUpdate = false;
            // validate
            List<PacketStorage> packetStorageList = packetStorageMapper.selectByGoodsID(goodsID, packetID, repositoryID);
            if (packetStorageList != null && !packetStorageList.isEmpty()) {
                // update
                PacketStorage packetStorage = packetStorageList.get(0);
                packetStorage.setNumber(number);
                packetStorage.setStorage(storage); //设置新的未到货数量
                packetStorageMapper.update(packetStorage);
                isUpdate = true;
            }

            return isUpdate;
        } catch (PersistenceException e) {
            throw new PacketStorageManageServiceException(e);
        }
    }

    @UserOperation(value = "删除包裹库存记录")
    @Override
    public boolean deletePacketStorage(Integer goodsID, Integer packetID, Integer repositoryID) throws PacketStorageManageServiceException {
        try {
            boolean isDelete = false;

            // validate
            List<PacketStorage> packetStorageList = packetStorageMapper.selectByGoodsID(goodsID, packetID, repositoryID);
            if (packetStorageList != null && !packetStorageList.isEmpty()) {
                // delete
                packetStorageMapper.deleteByID(goodsID, packetID, repositoryID);
                isDelete = true;
            }

            return isDelete;
        } catch (PersistenceException e) {
            throw new PacketStorageManageServiceException(e);
        }
    }

    @Override
    public boolean packetStorageIncrease(Integer goodsID, Integer packetID, Integer repositoryID, long number) throws PacketStorageManageServiceException {

        synchronized (this) {
            // 检查对应的库存记录是否存在
            PacketStorage packetStorage = getPacketStorage(goodsID, packetID, repositoryID);
            if (null != packetStorage) {
                // 检查库存减少数目的范围是否合理
                if (number < 0)
                    return false;
                long newNumber = packetStorage.getNumber();
                long newStorage = packetStorage.getStorage() + number;
                updatePacketStorage(goodsID, packetID, repositoryID, newNumber, newStorage);
                return true;
            } else
                return false;
        }
    }

    @UserOperation(value = "导入预报记录")
    @Override
    public Map<String, Object> importPacketStorage(MultipartFile file) throws PacketStorageManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        int total = 0;
        int available = 0;

        // 从文件中读取
        List<Object> packetStorageList = excelUtil.excelReader(PacketStorage.class, file);
        if (packetStorageList != null) {
            total = packetStorageList.size();

            try {
                PacketStorage packetStorage;
                boolean isAvailable;
                List<PacketStorage> availableList = new ArrayList<>();
                PacketDO packetDO;
                Goods goods;
                RepositoryBatch repositoryBatch;
                Repository repository;
                for (Object object : packetStorageList) {
                    isAvailable = true;
                    packetStorage = (PacketStorage) object;

                    // validate
                    packetDO = packetMapper.selectByPacketID(packetStorage.getPacketID());
                    goods = goodsMapper.selectById(packetStorage.getGoodsID());
                    repository = repositoryMapper.selectByID(packetStorage.getRepositoryID());
                    if (packetDO == null)
                        isAvailable = false;
                    if (goods == null)
                        isAvailable = false;
                    if (repository == null)
                        isAvailable = false;
                    if (packetStorage.getNumber() < 0)
                        isAvailable = false;
                    List<PacketStorage> temp = packetStorageMapper.selectByGoodsID(packetStorage.getPacketID(), packetStorage.getGoodsID(), packetStorage.getRepositoryID());
                    if (!(temp != null && temp.isEmpty()))
                        isAvailable = false;

                    if (isAvailable) {
                        availableList.add(packetStorage);
                    }
                }
                // 保存到数据库
                available = availableList.size();
                System.out.println(available);
                if (available > 0)
                    packetStorageMapper.insertBatch(availableList);
            } catch (PersistenceException e) {
                throw new PacketStorageManageServiceException(e);
            }
        }

        resultSet.put("total", total);
        resultSet.put("available", available);
        return resultSet;
    }

    @UserOperation(value = "导出预报记录")
    @Override
    public File exportPacketStorage(List<PacketStorage> packetStorageList) {
        if (packetStorageList == null)
            return null;
        return excelUtil.excelWriter(PacketStorage.class, packetStorageList);
    }

    /**
     *
     * @param goodsID
     * @param packetID
     * @param repositoryID
     * @return
     */
    private PacketStorage getPacketStorage(Integer goodsID, Integer packetID, Integer repositoryID) {
        PacketStorage packetStorage = null;
        List<PacketStorage> packetStorageList = packetStorageMapper.selectByGoodsID(goodsID, packetID, repositoryID);
        if (!packetStorageList.isEmpty())
            packetStorage = packetStorageList.get(0);
        return packetStorage;
    }

    @Override
    public Map<String, Object> selectPacketRecord(Integer packetID, Integer repositoryID, String startDateStr, String endDateStr) throws PacketStorageManageServiceException {
        return selectPacketRecord(packetID,repositoryID,startDateStr,endDateStr,-1,-1);
    }

    @Override
    public Map<String, Object> selectPacketRecord(Integer packetID, Integer repositoryID, String startDateStr, String endDateStr, int offset, int limit) throws PacketStorageManageServiceException {
        // 初始化结果集
        Map<String, Object> result = new HashMap<>();
        List<PacketStorage> packetStorageList;
        long total = 0;
        boolean isPagination = true;

        // 检查是否需要分页查询
        if (offset < 0 || limit < 0)
            isPagination = false;

        // 检查传入参数
        if (packetID<0)
            packetID = null;
        if (repositoryID<0)
            repositoryID = null;

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
            throw new PacketStorageManageServiceException(e);
        }

        // 查询记录
        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                packetStorageList = packetStorageMapper.selectByDate(packetID, repositoryID, startDate, endDate);
                if (packetStorageList != null)
                    total = new PageInfo<>(packetStorageList).getTotal();
                else
                    packetStorageList = new ArrayList<>(10);
            } else {
                packetStorageList = packetStorageMapper.selectByDate(packetID, repositoryID, startDate, endDate);
                if (packetStorageList != null)
                    total = packetStorageList.size();
                else
                    packetStorageList = new ArrayList<>(10);
            }
        } catch (PersistenceException e) {
            throw new PacketStorageManageServiceException(e);
        }

        List<PacketDTO> packetDTOS = new ArrayList<>();
        if (packetStorageList != null)
            packetStorageList.forEach(packetStorage -> packetDTOS.add(packetStorageConvertToPacketDTO(packetStorage)));

        result.put("data", packetDTOS);
        result.put("total", total);
        return result;
    }

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm");

    private PacketDTO packetStorageConvertToPacketDTO(PacketStorage packetStorage){
        PacketDTO packetDTO = new PacketDTO();
        packetDTO.setId(packetStorage.getPacketID());
        packetDTO.setTrace(packetStorage.getPacketTrace());
        packetDTO.setTime(packetStorage.getPacketTrace());
        packetDTO.setStatus(packetStorage.getPacketStatus());
        packetDTO.setTime(dateFormat.format(packetStorage.getPacketTime()));
        packetDTO.setDesc(packetStorage.getPacketDesc());
        packetDTO.setGoodsID(packetStorage.getGoodsID());
        packetDTO.setGoodsName(packetStorage.getGoodsName());
        packetDTO.setGoodsNumber(packetStorage.getNumber());
        packetDTO.setGoodsStorage(packetStorage.getStorage());
        return packetDTO;
    }

}
