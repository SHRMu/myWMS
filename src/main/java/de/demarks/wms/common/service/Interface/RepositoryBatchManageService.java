package de.demarks.wms.common.service.Interface;

import de.demarks.wms.domain.RepositoryBatch;
import de.demarks.wms.exception.RepositoryBatchManageServiceException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 仓库批次管理 service
 *
 * @author Shouran
 */
public interface RepositoryBatchManageService {

    /**
     * 返回指定 Batch ID 的批次记录
     *
     * @param batchID 批次ID
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    Map<String, Object> selectByBatchID(Integer batchID) throws RepositoryBatchManageServiceException;

    /**
     * 返回指定 Batch code 的批次记录
     * 支持模糊查询
     *
     * @param code 仓库名称
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    Map<String, Object> selectByCode(String code) throws RepositoryBatchManageServiceException;

    /**
     * 返回指定 Batch code 的批次记录
     * 支持查询分页以及模糊查询
     *
     * @param code 批次
     * @param offset  分页的偏移值
     * @param limit   分页的大小
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    Map<String, Object> selectByCode( String code, int offset, int limit) throws RepositoryBatchManageServiceException;

    /**
     * 返回指定 Batch status 的批次记录
     * 支持模糊查询
     *
     * @param status 批次状态
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    Map<String, Object> selectByStatus(String status) throws RepositoryBatchManageServiceException;

    /**
     * 返回指定 Batch status 的批次记录
     * 支持查询分页以及模糊查询
     *
     * @param status 批次状态
     * @param offset  分页的偏移值
     * @param limit   分页的大小
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    Map<String, Object> selectByStatus( String status, int offset, int limit) throws RepositoryBatchManageServiceException;

    /**
     * 查询所有的批次记录
     *
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    Map<String, Object> selectAll() throws RepositoryBatchManageServiceException;

    /**
     * 分页查询批次记录
     *
     * @param offset 分页的偏移值
     * @param limit  分页的大小
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    Map<String, Object> selectAll(int offset, int limit) throws RepositoryBatchManageServiceException;

    /**
     * 添加仓库批次信息
     *
     * @param repositoryBatch 仓库批次信息
     * @return 返回一个boolean值，值为true代表添加成功，否则代表失败
     */
    boolean addRepositoryBatch(RepositoryBatch repositoryBatch) throws RepositoryBatchManageServiceException;

    /**
     * 更新仓库批次记录
     *
     * @param repositoryBatch 仓库批次信息
     * @return 返回一个boolean值，值为true代表更新成功，否则代表失败
     */
    boolean updateRepositoryBatch(RepositoryBatch repositoryBatch) throws RepositoryBatchManageServiceException;

    /**
     * 删除指定 batchID 和 repositoryID 的批次记录
     *
     * @param batchID 批次ID
     * @param repositoryID 仓库ID
     * @return 返回一个boolean值，值为true代表更新成功，否则代表失败
     */
    boolean deleteRepositoryBatch(Integer batchID, Integer repositoryID) throws RepositoryBatchManageServiceException;


    Map<String, Object> importRepositoryBatch(MultipartFile file) throws RepositoryBatchManageServiceException;


    File exportRepositoryBatch(List<RepositoryBatch> repositoryBatchList);

}
