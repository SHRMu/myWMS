package de.demarks.wms.common.service.Interface;

import com.sun.xml.internal.ws.api.message.Packet;
import de.demarks.wms.domain.PacketInDO;
import de.demarks.wms.exception.PacketManageServiceException;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 包裹运单管理 Service
 *
 * @author huanyingcool
 */
public interface PacketManageService {


    /**
     * 选择全部信息
     * @param customerID
     * @param repositoryID
     * @return
     * @throws PacketManageServiceException
     */
    Map<String, Object> selectAll(@Param("customerID")Integer customerID,
                                  @Param("repositoryID") Integer repositoryID) throws PacketManageServiceException;

    /**
     * 分页 选择全部信息
     * @param customerID
     * @param repositoryID
     * @param offset 分页的偏移值
     * @param limit  分页的大小
     * @return
     * @throws PacketManageServiceException
     */
    Map<String, Object> selectAll(@Param("customerID") Integer customerID,
                                  @Param("repositoryID") Integer repositoryID,
                                  @Param("offset")int offset,
                                  @Param("limit") int limit) throws PacketManageServiceException;

    /**
     * 选择指定包裹ID的记录
     * @param packetID
     * @return
     * @throws PacketManageServiceException
     */
    Map<String, Object> selectByPacketID(@Param("packetID") Integer packetID) throws PacketManageServiceException;

    /**
     * 模糊搜索 返回指定运单号的信息
     * @param trace
     * @param status
     * @param customerID
     * @param repositoryID
     * @return
     * @throws PacketManageServiceException
     */
    Map<String, Object> selectByTraceApproximate(@Param("trace") String trace,
                                              @Param("status") String status,
                                              @Param("customerID") Integer customerID,
                                              @Param("repositoryID") Integer repositoryID) throws PacketManageServiceException;

    /**
     * 模糊搜索 分页 返回指定运单号的信息
     * @param trace
     * @param status
     * @param customerID
     * @param repositoryID
     * @param offset
     * @param limit
     * @return
     * @throws PacketManageServiceException
     */
    Map<String, Object> selectByTraceApproximate(@Param("trace") String trace,
                                              @Param("status") String status,
                                              @Param("customerID") Integer customerID,
                                              @Param("repositoryID") Integer repositoryID,
                                              @Param("offset")int offset,
                                              @Param("limit") int limit) throws PacketManageServiceException;

    /**
     * 添加包裹信息
     * @param packetDO
     * @return
     * @throws PacketManageServiceException
     */
    boolean addPacket(PacketInDO packetDO) throws PacketManageServiceException;

    /**
     * 更新包裹信息
     * @param packetDO
     * @return
     */
    boolean updatePacket(PacketInDO packetDO) throws PacketManageServiceException;

    /**
     * 删除 指定ID 的包裹
     * @param packetID
     * @return
     * @throws PacketManageServiceException
     */
    boolean deletePacket(Integer packetID) throws PacketManageServiceException;

    /**
     * 导入
     * @param file
     * @return
     * @throws PacketManageServiceException
     */
    Map<String, Object> importPacket(MultipartFile file) throws PacketManageServiceException;

    /**
     * 导出
     * @param packetDOS
     * @return
     */
    File exportPacket(List<PacketInDO> packetDOS);

    /**
     * 客户预报操作
     * @param packetID     包裹ID
     * @param goodsID      货物ID
     * @param repositoryID 入库仓库ID
     * @param number       入库数量
     * @return 返回一个boolean 值，若值为true表示入库成功，否则表示入库失败
     */
    boolean packetStockInOperation(Integer packetID, Integer goodsID, Integer repositoryID, long number) throws PacketManageServiceException;




}
