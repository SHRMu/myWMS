package de.demarks.wms.dao;

import de.demarks.wms.domain.DetectDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 检测记录映射器
 *
 * @author huanyingcool
 */

@Repository
public interface DetectMapper {

    /**
     * 返回全部结果
     * @param batchID
     * @param repositoryID
     * @return
     */
    List<DetectDO> selectAll(@Param("batchID") Integer batchID,
                             @Param("repositoryID") Integer repositoryID);

    /**
     * 返回指定 recordID 的结果
     * @param recordID
     * @return
     */
    DetectDO selectByRecordID(@Param("recordID") Integer recordID);

    /**
     * 返回指定 时间区域 的结果
     * @param batchID
     * @param repositoryID
     * @param startDate
     * @param endDate
     * @return
     */
    List<DetectDO> selectByDate(@Param("batchID") Integer batchID,
                                @Param("repositoryID") Integer repositoryID,
                                @Param("startDate") Date startDate,
                                @Param("endDate") Date endDate);

    /**
     * 返回 指定GoodsID 的结果
     * @param goodsID
     * @param batchID
     * @param repositoryID
     * @param startDate
     * @param endDate
     * @return
     */
    List<DetectDO> selectByGoodsID(@Param("goodsID") Integer goodsID,
                                    @Param("batchID") Integer batchID,
                                    @Param("repositoryID") Integer repositoryID,
                                    @Param("startDate") Date startDate,
                                    @Param("endDate") Date endDate);

    /**
     * 模糊搜索 指定GoodsName
     * @param goodsName
     * @param batchID
     * @param repositoryID
     * @param startDate
     * @param endDate
     * @return
     */
    List<DetectDO> selectByGoodsName(@Param("goodsName") String goodsName,
                                     @Param("batchID") Integer batchID,
                                     @Param("repositoryID") Integer repositoryID,
                                     @Param("startDate") Date startDate,
                                     @Param("endDate") Date endDate);


    /**
     * 添加一条记录
     * @param detectDO
     */
    void insert(DetectDO detectDO);

    /**
     * 更新一条记录
     * @param detectDO
     */
    void update(DetectDO detectDO);

    /**
     * 删除指定recordID的记录
     * @param recordID
     */
    void deleteByRecordID(Integer recordID);

}
