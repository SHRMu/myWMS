package de.demarks.wms.dao;

import de.demarks.wms.domain.PacketRef;
import org.apache.ibatis.annotations.Param;
import org.apache.tools.ant.taskdefs.Pack;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 包裹依赖信息 PacketRef 映射器
 *
 * @author huanyingcool
 *
 */

@Repository
public interface PacketRefMapper {

    /**
     * 返回全部信息
     * @param repositoryID
     * @return
     */
    List<PacketRef> selectAll(@Param("repositoryID") Integer repositoryID);

    /**
     * 返回指定packetID的信息
     * @param packetID
     */
    PacketRef selectByPacketID(@Param("packetID") Integer packetID);

    /**
     * 返回指定RefID的信息
     * @param refID
     * @return
     */
    List<PacketRef> selectByRefID(@Param("refID") Integer refID,
                                  @Param("repositoryID") Integer repositoryID);

    /**
     * 精确查询 返回指定运单号的信息
     * @param trace
     * @param refID
     * @return
     */
    PacketRef selectByTrace(@Param("trace") String trace,
                            @Param("refID") Integer refID,
                            @Param("repositoryID") Integer repositoryID);

    /**
     * 模糊查询 返回指定运单号的信息
     * @param trace
     * @param status
     * @param repositoryID
     * @return
     */
    List<PacketRef> selectByTraceApproximate(@Param("trace") String trace,
                                              @Param("status") String status,
                                              @Param("repositoryID") Integer repositoryID);

    /**
     * 添加附加包裹
     * @param trace
     * @param refID
     */
    void insert(@Param("trace") String trace,
                @Param("refID") Integer refID);

    /**
     * 删除指定ID号的包裹
     * @param packetID
     */
    void deleteByPacketID(@Param("packetID") Integer packetID);

    /**
     * 删除指定refID的包裹
     * @param refID
     */
    void deleteByRefID(@Param("refID") Integer refID);
}
