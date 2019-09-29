package de.demarks.wms.dao;

import de.demarks.wms.domain.RepositoryAdmin;
import de.demarks.wms.domain.RepositoryBatch;
import de.demarks.wms.exception.RepositoryManageServiceException;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * RepositoryBatch 映射器
 *
 * @author huanyingcool
 */
@Repository
public interface RepositoryBatchMapper {

    /**
     * 选择指定仓库的所有批次信息
     * @param repositoryID 仓库ID
     * @return 返回所有的仓库批次信息
     */
    List<RepositoryBatch> selectAll(@Param("repositoryID") Integer repositoryID);

    /**
     * 选择指定 Batch ID 的批次信息
     * @param batchID      自增批次ID
     * @param repositoryID 仓库ID
     * @return 返回指定 batchID 的批次信息
     */
    RepositoryBatch selectByID(@Param("batchID") Integer batchID,
                               @Param("repositoryID") Integer repositoryID);

    /**
     * 选择指定 Batch Code 的批次信息
     * @param batchCode 各仓库自定的 batchCode
     * @param repositoryID 仓库ID
     * @return 返回指定 batchCode 的批次信息
     */
    List<RepositoryBatch> selectByCode(@Param("batchCode") String batchCode,
                                       @Param("repositoryID") Integer repositoryID);

    /**
     * 选择指定 Batch Status 的批次信息
     * @param status 各仓库自定的 BATCH_STATUS
     * @param repositoryID 仓库ID
     * @return 返回指定 BATCH_STATUS 的批次信息
     */
    List<RepositoryBatch> selectByStatus(@Param("status") String status,
                                         @Param("repositoryID") Integer repositoryID);

    /**
     * 插入一条仓库批次信息
     * @param repositoryBatch 仓库批次信息
     */
    void insert(RepositoryBatch repositoryBatch);


    void insertBatch(List<RepositoryBatch> repositoryBatch);

    /**
     * 更新 repositoryBatch 记录
     * @param repositoryBatch 批次信息
     */
    void update(RepositoryBatch repositoryBatch);

    /**
     * 删除指定 batchID 的 RepositoryBatch 记录
     * @param batchID 批次ID
     */
    void deleteByID(Integer batchID);


}
