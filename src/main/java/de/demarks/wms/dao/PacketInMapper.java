package de.demarks.wms.dao;

import de.demarks.wms.domain.PacketInDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 接收包裹运单信息 PacketIn 映射器
 *
 * @author huanyingcool
 *
 */

@Repository
public interface PacketInMapper {

    /**
     * 返回全部
     * @param customerID
     * @param repositoryID
     * @return
     */
    List<PacketInDO> selectAll(@Param("customerID") Integer customerID,
                               @Param("repositoryID") Integer repositoryID);

    /**
     * 精确查询 返回指定PacketID
     * @param packetID
     * @return
     */
    PacketInDO selectByPacketID(@Param("packetID") Integer packetID);

    /**
     * 模糊查询 返回指定运单号
     * @param trace
     * @param status
     * @param customerID
     * @param repositoryID
     * @return
     */
    List<PacketInDO> selectByTraceApproximate(@Param("trace") String trace,
                                              @Param("status") String status,
                                              @Param("customerID") Integer customerID,
                                              @Param("repositoryID") Integer repositoryID);


    /**
     * 模糊查询 返回指定日期范围的运单信息
     * @param customerID
     * @param repositoryID
     * @param startDate
     * @param endDate
     * @return
     */
    List<PacketInDO> selectByDate(@Param("customerID") Integer customerID,
                                  @Param("repositoryID") Integer repositoryID,
                                  @Param("startDate") Date startDate,
                                  @Param("endDate") Date endDate);

    /**
     * 添加一条包裹信息
     * @param packetInDO
     * @return
     */
    void insert(PacketInDO packetInDO);

    /**
     * 批量添加包裹信息
     * @param packetInDO
     */
    void insertBatch(List<PacketInDO> packetInDO);

    /**
     * 更新一条包裹信息
     * @param packetDO
     */
    void update(PacketInDO packetDO);

    /**
     * 删除指定packetID
     * @param packetID
     */
    void deleteByPacketID(@Param("packetID") Integer packetID);
}
