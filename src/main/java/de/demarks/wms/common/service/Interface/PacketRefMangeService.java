package de.demarks.wms.common.service.Interface;

import de.demarks.wms.domain.PacketInDO;
import de.demarks.wms.exception.PacketManageServiceException;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

public interface PacketRefMangeService {


    /**
     * 模糊搜索 返回指定运单号的信息
     * @param trace
     * @param status
     * @param repositoryID
     * @return
     * @throws PacketManageServiceException
     */
    Map<String, Object> selectRefApproximate(@Param("trace") String trace,
                                             @Param("status") String status,
                                             @Param("repositoryID") Integer repositoryID) throws PacketManageServiceException;

    /**
     * 模糊搜索 分页 返回指定运单号的信息
     * @param trace
     * @param status
     * @param repositoryID
     * @param offset
     * @param limit
     * @return
     * @throws PacketManageServiceException
     */
    Map<String, Object> selectRefApproximate(@Param("trace") String trace,
                                             @Param("status") String status,
                                             @Param("repositoryID") Integer repositoryID,
                                             @Param("offset")int offset,
                                             @Param("limit") int limit) throws PacketManageServiceException;

    /**
     * 添加附加包裹信息
     * @return
     * @throws PacketManageServiceException
     */
    boolean addPacketRef(PacketInDO packetDO) throws PacketManageServiceException;

    /**
     * 更新包裹信息
     * @param packetDO
     * @return
     */
    boolean updatePacketRef(PacketInDO packetDO) throws PacketManageServiceException;

    /**
     * 删除附加包裹信息
     * @param refID
     * @return
     * @throws PacketManageServiceException
     */
    boolean deletePacketRef(Integer refID) throws PacketManageServiceException;

}
