package de.demarks.wms.common.service.Interface;

import de.demarks.wms.domain.DetectStorage;
import de.demarks.wms.exception.DetectStorageServiceException;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 已检测库存信息管理
 *
 * @author huanyingcool
 */

public interface DetectStorageService {

    /**
     * 选择所有记录
     * @param repositoryID
     * @return
     * @throws DetectStorageServiceException
     */
    Map<String, Object> selectAll(@Param("batchID") Integer batchID,
                                  @Param("repositoryID") Integer repositoryID) throws DetectStorageServiceException;

    /**
     * 分页 选择所有记录
     * @param repositoryID
     * @param offset
     * @param limit
     * @return
     * @throws DetectStorageServiceException
     */
    Map<String, Object> selectAll(@Param("batchID") Integer batchID, @Param("repositoryID") Integer repositoryID,
                                  @Param("offset") int offset, @Param("limit") int limit) throws DetectStorageServiceException;

    /**
     * 选择指定ID的记录
     * @param goodsID
     * @param batchID
     * @param repositoryID
     * @return
     * @throws DetectStorageServiceException
     */
    Map<String, Object> selectByGoodsID(@Param("goodsID") Integer goodsID, @Param("batchID") Integer batchID,
                                        @Param("repositoryID") Integer repositoryID) throws DetectStorageServiceException;

    /**
     * 分页 选择指定ID的记录
     * @param goodsID
     * @param batchID
     * @param repositoryID
     * @param offset
     * @param limit
     * @return
     * @throws DetectStorageServiceException
     */
    Map<String, Object> selectByGoodsID(@Param("goodsID") Integer goodsID, @Param("batchID") Integer batchID,
                                        @Param("repositoryID") Integer repositoryID,
                                        @Param("offset") int offset, @Param("limit") int limit) throws DetectStorageServiceException;

    /**
     * 模糊查询 返回指定名称的记录
     * @param goodsName
     * @param batchID
     * @param repositoryID
     * @return
     * @throws DetectStorageServiceException
     */
    Map<String, Object> selectByGoodsName(@Param("goodsName") String goodsName, @Param("batchID") Integer batchID,
                                          @Param("repositoryID") Integer repositoryID) throws DetectStorageServiceException;


    Map<String, Object> selectByGoodsName(@Param("goodsName") String goodsName, @Param("batchID") Integer batchID,
                                          @Param("repositoryID") Integer repositoryID,
                                          @Param("offset") int offset, @Param("limit") int limit) throws DetectStorageServiceException;

    boolean addDetectStorage(@Param("goodsID") Integer goodsID,
                             @Param("batchID") Integer batchID, @Param("repositoryID") Integer repositoryID,
                             @Param("passed") long passed, @Param("scratch") long scratch,@Param("damage") long damage) throws DetectStorageServiceException;


    boolean updateDetectStorage(@Param("goodsID") Integer goodsID, @Param("batchID") Integer batchID, @Param("repositoryID") Integer repositoryID,
                                @Param("number") long number, @Param("passed") long passed, @Param("scratch") long scratch,@Param("damage") long damage) throws DetectStorageServiceException;

    boolean deleteDetectStorage(Integer goodsID, Integer batchID, Integer repositoryID) throws DetectStorageServiceException;


    boolean detectStorageIncrease(Integer goodsID, Integer batchID, Integer repositoryID, long passed, long scratch, long damage) throws DetectStorageServiceException;


    boolean detectStoragePassedDecrease(Integer goodsID, Integer batchID, Integer repositoryID, long number) throws DetectStorageServiceException;

}
