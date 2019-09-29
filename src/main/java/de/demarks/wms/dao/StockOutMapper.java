package de.demarks.wms.dao;

import de.demarks.wms.domain.StockOutDO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * 出库记录 映射器
 *
 * @author Ken
 */
@Repository
public interface StockOutMapper {


    List<StockOutDO> selectAll(@Param("batchID") Integer batchID,
                               @Param("repositoryID") Integer repositoryID);

    StockOutDO selectByRecordID(@Param("recordID") Integer recordID);

    List<StockOutDO> selectByGoodsID(@Param("goodsID") Integer goodsID,
                                     @Param("batchID") Integer batchID,
                                     @Param("repositoryID") Integer repositoryID);

    List<StockOutDO> selectByGoodsName(@Param("goodsName") String goodsName,
                                       @Param("batchID") Integer batchID,
                                       @Param("repositoryID") Integer repositoryID);

    List<StockOutDO> selectByCustomerID(@Param("customerID") Integer customerID,
                                        @Param("batchID") Integer batchID,
                                        @Param("repositoryID") Integer repositoryID);

    List<StockOutDO> selectByDate(@Param("batchID") Integer batchID,
                                  @Param("repositoryID") Integer repositoryID,
                                 @Param("startDate") Date startDate,
                                 @Param("endDate") Date endDate);

    void insert(StockOutDO stockOutDO);

    void update(StockOutDO stockOutDO);

    void deleteByRecordID(Integer recordID);
}
