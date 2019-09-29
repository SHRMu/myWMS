package de.demarks.wms.dao;

import de.demarks.wms.domain.StockInDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 入库记录映射器
 *
 * @author huanyingcool
 */
@Repository
public interface StockInMapper {

    /**
     *
     * @param batchID
     * @param repositoryID
     * @return
     */
    List<StockInDO> selectAll(@Param("batchID") Integer batchID,
                              @Param("repositoryID") Integer repositoryID);

    /**
     *
     * @param recordID
     * @return
     */
    StockInDO selectByRecordID(@Param("recordID") Integer recordID);

    /**
     *
     * @param goodsID
     * @param batchID
     * @param repositoryID
     * @return
     */
    List<StockInDO> selectByGoodsID(@Param("goodsID") Integer goodsID,
                                    @Param("batchID") Integer batchID,
                                    @Param("repositoryID") Integer repositoryID);

    /**
     *
     * @param goodsName
     * @param batchID
     * @param repositoryID
     * @return
     */
    List<StockInDO> selectByGoodsName(@Param("goodsName") String goodsName,
                                      @Param("batchID") Integer batchID,
                                      @Param("repositoryID") Integer repositoryID);

    List<StockInDO> selectByCustomerID(@Param("customerID") Integer customerID,
                                       @Param("batchID") Integer batchID,
                                       @Param("repositoryID") Integer repositoryID);

    List<StockInDO> selectByDate(@Param("batchID") Integer batchID,
                                 @Param("repositoryID") Integer repositoryID,
                                 @Param("startDate") Date startDate,
                                 @Param("endDate") Date endDate);

    /**
     * 添加一条新的入库记录
     *
     * @param stockInDO 入库记录
     */
    void insert(StockInDO stockInDO);

    /**
     * 更新入库记录
     *
     * @param stockInDO 入库记录
     */
    void update(StockInDO stockInDO);

    /**
     * 删除 指定ID 的记录
     * @param recordID
     */
    void deleteByRecordID(Integer recordID);

}
