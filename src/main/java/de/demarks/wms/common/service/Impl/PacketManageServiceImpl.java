package de.demarks.wms.common.service.Impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import de.demarks.wms.common.service.Interface.PacketManageService;
import de.demarks.wms.common.service.Interface.PacketRefMangeService;
import de.demarks.wms.common.util.ExcelUtil;
import de.demarks.wms.common.util.StatusUtil;
import de.demarks.wms.dao.*;
import de.demarks.wms.domain.*;
import de.demarks.wms.exception.PacketManageServiceException;
import de.demarks.wms.util.aop.UserOperation;
import org.apache.ibatis.exceptions.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  包裹预报服务实现
 *
 * @author huanyingcool
 */

@Service
public class PacketManageServiceImpl implements PacketManageService {

    @Autowired
    private ExcelUtil excelUtil;
    @Autowired
    private PacketInMapper packetInMapper;
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private RepositoryMapper repositoryMapper;
    @Autowired
    private PacketStorageMapper packetStorageMapper;
    @Autowired
    private PacketRefMangeService packetRefMangeService;

    /**
     * 选择全部信息
     * @param customerID
     * @param repositoryID
     * @return
     * @throws PacketManageServiceException
     */
    @Override
    public Map<String, Object> selectAll(Integer customerID, Integer repositoryID) throws PacketManageServiceException {
        return selectAll(customerID, repositoryID, -1, -1);
    }

    /**
     * 分页 选择全部信息
     * @param customerID
     * @param repositoryID
     * @param offset 分页的偏移值
     * @param limit  分页的大小
     * @return
     * @throws PacketManageServiceException
     */
    @Override
    public Map<String, Object> selectAll(Integer customerID, Integer repositoryID, int offset, int limit) throws PacketManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        List<PacketInDO> packetDOList;

        long total = 0;
        boolean isPagination = true;

        // validate
        if (offset < 0 || limit < 0)
            isPagination = false;
        if (repositoryID<0)
            repositoryID = null;

        // query
        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                packetDOList = packetInMapper.selectAll(null, repositoryID);
                if (packetDOList != null) {
                    PageInfo<PacketInDO> pageInfo = new PageInfo<>(packetDOList);
                    total = pageInfo.getTotal();
                } else
                    packetDOList = new ArrayList<>();
            } else {
                packetDOList = packetInMapper.selectAll(null, repositoryID);
                if (packetDOList != null)
                    total = packetDOList.size();
                else
                    packetDOList = new ArrayList<>();
            }
        } catch (PersistenceException e) {
            throw new PacketManageServiceException(e);
        }

        List<PacketDTO> packetDTOS = new ArrayList<>();
        if (!packetDOList.isEmpty())
            packetDOList.forEach(packet -> packetDTOS.add(packetConvertToPacketDTO(packet)));

        resultSet.put("data", packetDTOS);
        resultSet.put("total", total);
        return resultSet;
    }

    /**
     * 选择指定包裹ID的信息
     * @param packetID 包裹ID
     * @return
     * @throws PacketManageServiceException
     */
    @Override
    public Map<String, Object> selectByPacketID(Integer packetID) throws PacketManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
//        List<Packet> packetList = new ArrayList<>();
        List<PacketDTO> packetDTOS = new ArrayList<>();
        long total = 0;

        // 查询
        PacketInDO packetDO;
        try {
            packetDO = packetInMapper.selectByPacketID(packetID);
        } catch (PersistenceException e) {
            throw new PacketManageServiceException(e);
        }

        if (packetDO != null) {
            packetDTOS.add(packetConvertToPacketDTO(packetDO));
//            packetList.add(packet);
            total = 1;
        }

        resultSet.put("data", packetDTOS);
        resultSet.put("total", total);
        return resultSet;
    }

    /**
     * 模糊查询 返回指定运单号的信息
     * @param trace
     * @param status
     * @param customerID
     * @param repositoryID
     * @return
     * @throws PacketManageServiceException
     */
    @Override
    public Map<String, Object> selectByTraceApproximate(String trace, String status, Integer customerID, Integer repositoryID) throws PacketManageServiceException {
        return  selectByTraceApproximate(trace, status, customerID, repositoryID, -1, -1);
    }

    /**
     * 模糊查询 分页 返回指定运单号的信息
     * @param trace
     * @param status
     * @param customerID
     * @param repositoryID
     * @param offset
     * @param limit
     * @return
     * @throws PacketManageServiceException
     */
    @Override
    public Map<String, Object> selectByTraceApproximate(String trace, String status, Integer customerID, Integer repositoryID, int offset, int limit) throws PacketManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        List<PacketInDO> packetDOList;

        long total = 0;
        boolean isPagination = true;

        // validate
        if (offset < 0 || limit < 0)
            isPagination = false;
        if (status.equals(""))
            status = null;
        if (repositoryID <0)
            repositoryID = null;

        // query
        try {
            if (isPagination) {
                PageHelper.offsetPage(offset, limit);
                packetDOList = packetInMapper.selectByTraceApproximate(trace, status, customerID, repositoryID);
                if (packetDOList != null) {
                    PageInfo<PacketInDO> pageInfo = new PageInfo<>(packetDOList);
                    total = pageInfo.getTotal();
                } else
                    packetDOList = new ArrayList<>();
            } else {
                packetDOList = packetInMapper.selectByTraceApproximate(trace, status, customerID, repositoryID);
                if (packetDOList != null)
                    total = packetDOList.size();
                else
                    packetDOList = new ArrayList<>();
            }
        } catch (PersistenceException e) {
            throw new PacketManageServiceException(e);
        }

        List<PacketDTO> packetDTOS = new ArrayList<>();
        packetDOList.forEach(packet -> packetDTOS.add(packetConvertToPacketDTO(packet)));

        resultSet.put("data", packetDTOS);
        resultSet.put("total", total);
        return resultSet;
    }

    /**
     * 添加包裹信息
     * @param packetDO
     * @return
     * @throws PacketManageServiceException
     */
    @Override
    public boolean addPacket(PacketInDO packetDO) throws PacketManageServiceException {
        try {
            // 插入新的记录
            if (packetDO != null) {
                // 验证
                if (packetCheck(packetDO)) {
                    // 通过单号验证包裹信息是否存在
                    List<PacketInDO> p = packetInMapper.selectByTraceApproximate(packetDO.getTrace(), null, packetDO.getCustomerID(), packetDO.getRepositoryID());
                    if (p != null){ //如果已经存在该单号
                        updatePacket(packetDO);
                        return true;
                    }
                    packetDO.setTime(new Date());
                    packetInMapper.insert(packetDO);
                    PacketInDO packetDOID = packetInMapper.selectByTraceApproximate(packetDO.getTrace(), null, packetDO.getCustomerID(), packetDO.getRepositoryID()).get(0);
                    //添加附加包裹信息
                    packetRefMangeService.addPacketRef(packetDOID);
                    return true;
                }
            }
            return false;
        } catch (PersistenceException e) {
            throw new PacketManageServiceException(e);
        }
    }

    /**
     * 更新包裹信息
     * @param packetDO
     * @return
     */
    @Override
    public boolean updatePacket(PacketInDO packetDO) throws PacketManageServiceException {
        try {
            // 更新记录
            if (packetDO != null && packetCheck(packetDO)) {
                packetRefMangeService.updatePacketRef(packetDO); //先更新依赖包裹
//                packetDO.setTime(new Date());
                packetInMapper.update(packetDO);
                return true;
            }
            return false;
        } catch (PersistenceException e) {
            throw new PacketManageServiceException(e);
        }
    }

    /**
     * 删除包裹信息
     * @param packetID
     * @return
     */
    @Override
    public boolean deletePacket(Integer packetID) throws PacketManageServiceException {
        try {
            //查看是否有预报信息
            List<PacketStorage> packetStorageList = packetStorageMapper.selectAll(packetID, "",null);
            if (!packetStorageList.isEmpty())
                return false;
            //检查该包裹是否已签收
            PacketInDO packetDO = packetInMapper.selectByPacketID(packetID);
            String status = packetDO.getStatus();
            if(status.equals(StatusUtil.PACKET_STATUS_RECEIVE)){
                //先删除相关依赖的PacketRef
                packetRefMangeService.deletePacketRef(packetID);
                packetInMapper.deleteByPacketID(packetID);
                return true;
            }
            return false;
        } catch (PersistenceException e) {
            throw new PacketManageServiceException(e);
        }
    }

    /**
     *
     * @param file
     * @return
     * @throws PacketManageServiceException
     */
    @UserOperation(value = "导入包裹信息")
    @Override
    public Map<String, Object> importPacket(MultipartFile file) throws PacketManageServiceException {
        // 初始化结果集
        Map<String, Object> resultSet = new HashMap<>();
        int total = 0;
        int available = 0;

        // 从 Excel 文件中读取
        List<Object> packetUpList = excelUtil.excelReader(PacketUp.class, file);
        if (packetUpList != null) {
            total = packetUpList.size();
            // 验证每一条记录
            PacketUp packetUp;
            PacketInDO packetDO;
            List<PacketInDO> availableList = new ArrayList<>();
            for (Object object : packetUpList) {
                packetUp = (PacketUp) object;
                packetDO = packetConvertToPacketDO(packetUp);
                if (packetCheck(packetDO)) {
                    availableList.add(packetDO);
                }
            }
            // 保存到数据库
            try {
                available = availableList.size();
                if (available > 0) {
                    packetInMapper.insertBatch(availableList);
                }
            } catch (PersistenceException e) {
                throw new PacketManageServiceException(e);
            }
        }

        resultSet.put("total", total);
        resultSet.put("available", available);
        return resultSet;
    }

    /**
     * 导出包裹信息到文件中
     *
     * @param packetDOS 包含若干条 Supplier 信息的 List
     * @return excel 文件
     */
    @UserOperation(value = "导出包裹信息")
    @Override
    public File exportPacket(List<PacketInDO> packetDOS) {
        if (packetDOS == null)
            return null;

        return excelUtil.excelWriter(PacketInDO.class, packetDOS);
    }


    /**
     * 检查包裹信息是否满足要求
     *
     * @param packetDO 货物信息
     * @return 若货物信息满足要求则返回true，否则返回false
     */
    private boolean packetCheck(PacketInDO packetDO) {
        if (packetDO != null) {
            if ( (packetDO.getTrace()!=null) && (packetDO.getStatus()!=null) && (packetDO.getRepositoryID()!=null)) {
                return true;
            }
        }
        return false;
    }

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm");

    private PacketInDO packetConvertToPacketDO(PacketUp packetUp){
        PacketInDO packetDO = new PacketInDO();
        packetDO.setTrace(packetUp.getTrace());
        packetDO.setStatus(StatusUtil.PACKET_STATUS_SEND);
        packetDO.setTime(new Date());
        packetDO.setRepositoryID(StatusUtil.DEFAULT_REPOSITORY_ID);
        packetDO.setDesc(packetUp.getDesc());
        return packetDO;
    }

    /**
     *
     * @param packetDO
     * @return
     */
    private PacketDTO packetConvertToPacketDTO(PacketInDO packetDO){
        PacketDTO packetDTO = new PacketDTO();
        packetDTO.setId(packetDO.getId());
        packetDTO.setTrace(packetDO.getTrace());
        packetDTO.setTime(dateFormat.format(packetDO.getTime()));
        packetDTO.setStatus(packetDO.getStatus());
        packetDTO.setDesc(packetDO.getDesc());
        packetDTO.setRepositoryID(packetDO.getRepositoryID());
        return packetDTO;
    }

    /**
     * 客户操作包裹预入库操作
     *
     * @param packetID     包裹ID
     * @param goodsID      货物ID
     * @param repositoryID 入库仓库ID
     * @param number       入库数量
     * @return 返回一个boolean 值，若值为true表示入库成功，否则表示入库失败
     */
    @UserOperation(value = "客户预报")
    @Override
    public boolean packetStockInOperation(Integer packetID, Integer goodsID, Integer repositoryID, long number) throws PacketManageServiceException {

        // 验证对应ID的记录是否存在
        if (!(packetValidate(packetID) && goodsValidate(goodsID) && repositoryValidate(repositoryID)))
            return false;

        // 检查入库数量有效性
        if (number < 0)
            return false;

        try {
            //查看当前PacketID和GoodsID是否有预报
            List<PacketStorage> packetStorageList = packetStorageMapper.selectByGoodsID(goodsID, packetID, repositoryID);
            PacketStorage packetStorage;
            if ( !packetStorageList.isEmpty()){
                //增加预报数量
                packetStorage = packetStorageList.get(0);
                Long newNum = packetStorage.getNumber() + number;
                Long newSto = packetStorage.getStorage();
                packetStorage.setNumber(newNum);
                packetStorage.setStorage(newSto);
                packetStorageMapper.update(packetStorage);
                return true;
            }else {
                //初次预报
                packetStorage = new PacketStorage();
                packetStorage.setPacketID(packetID);
                packetStorage.setGoodsID(goodsID);
                packetStorage.setRepositoryID(repositoryID);
                packetStorage.setNumber(number);
                packetStorage.setStorage((long) 0);
                packetStorageMapper.insert(packetStorage);
                return true;
            }
        } catch (PersistenceException e) {
            throw new PacketManageServiceException(e);
        }
    }

    /**
     * 检查包裹ID对应的记录是否存在
     *
     *
     * @return 若存在则返回true，否则返回false
     */
    private boolean packetValidate(Integer packetID) throws PacketManageServiceException {
        try {
            PacketInDO packetDO = packetInMapper.selectByPacketID(packetID);
            return packetDO != null;
        } catch (PersistenceException e) {
            throw new PacketManageServiceException(e);
        }
    }

    /**
     * 检查仓库ID对应的记录是否存在
     *
     * @param repositoryID 仓库ID
     * @return 若存在则返回true，否则返回false
     */
    private boolean repositoryValidate(Integer repositoryID) throws PacketManageServiceException {
        try {
            Repository repository = repositoryMapper.selectByID(repositoryID);
            return repository != null;
        } catch (PersistenceException e) {
            throw new PacketManageServiceException(e);
        }
    }

    /**
     * 检查货物ID对应的记录是否存在
     *
     * @param goodsID 货物ID
     * @return 若存在则返回true，否则返回false
     */
    private boolean goodsValidate(Integer goodsID) throws PacketManageServiceException {
        try {
            Goods goods = goodsMapper.selectById(goodsID);
            return goods != null;
        } catch (PersistenceException e) {
            throw new PacketManageServiceException(e);
        }
    }

}
