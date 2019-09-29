package de.demarks.wms.common.service.Interface;

import de.demarks.wms.exception.StockRecordManageServiceException;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * 出入库管理
 *
 * @author huanyingcool
 */
public interface StockRecordManageService {

    /**
     *
     * @param packetID
     * @param goodsID
     * @param batchID
     * @param repositoryID
     * @param number
     * @param personInCharge
     * @return
     * @throws StockRecordManageServiceException
     */
    boolean stockInOperation(@Param("packetID") Integer packetID, @Param("customerID") Integer customerID, @Param("goodsID") Integer goodsID,
                             @Param("batchID") Integer batchID, @Param("repositoryID") Integer repositoryID,
                             @Param("number") long number, @Param("personInCharge") String personInCharge) throws StockRecordManageServiceException;

    /**
     * 货物出库
     * @param packetID
     * @param customerID
     * @param goodsID
     * @param batchID
     * @param repositoryID
     * @param number
     * @param personInCharge
     * @return
     * @throws StockRecordManageServiceException
     */
    boolean stockOutOperation(@Param("packetID") Integer packetID, @Param("customerID") Integer customerID, @Param("goodsID") Integer goodsID,
                              @Param("batchID") Integer batchID, @Param("repositoryID") Integer repositoryID,
                              @Param("number") long number, @Param("personInCharge") String personInCharge) throws StockRecordManageServiceException;

    /**
     * 查询出入库记录
     *
     * @param batchID      批次ID
     * @param repositoryID 仓库ID
     * @param endDateStr   查询记录起始日期
     * @param startDateStr 查询记录结束日期
     * @param searchType   记录查询方式
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    Map<String, Object> selectStockRecord(Integer batchID, Integer repositoryID, String startDateStr, String endDateStr, String searchType) throws StockRecordManageServiceException;

    /**
     * 分页查询出入库记录
     *
     * @param batchID      批次ID
     * @param repositoryID 仓库ID
     * @param endDateStr   查询记录起始日期
     * @param startDateStr 查询记录结束日期
     * @param searchType   记录查询方式
     * @param offset       分页偏移值
     * @param limit        分页大小
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    Map<String, Object> selectStockRecord(Integer batchID, Integer repositoryID, String startDateStr, String endDateStr, String searchType, int offset, int limit) throws StockRecordManageServiceException;
}
