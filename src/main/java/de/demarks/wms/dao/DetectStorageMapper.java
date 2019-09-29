package de.demarks.wms.dao;

import de.demarks.wms.domain.DetectStorage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  已检测 库存映射器
 *
 * @author huanyingcool
 *
 */
@Repository
public interface DetectStorageMapper {

    /**
     * 返回全部
     * @param batchID
     * @param repositoryID
     * @return
     */
    List<DetectStorage> selectAll(@Param("batchID") Integer batchID,
                                  @Param("repositoryID") Integer repositoryID);

    /**
     * 返回 指定goodsID 的记录
     * @param goodsID
     * @param batchID
     * @param repositoryID
     * @return
     */
    List<DetectStorage> selectByGoodsID(@Param("goodsID") Integer goodsID,
                                        @Param("batchID") Integer batchID,
                                        @Param("repositoryID") Integer repositoryID);

    /**
     * 模糊搜索 返回 指定goodsName 的记录
     * @param goodsName
     * @param batchID
     * @param repositoryID
     * @return
     */
    List<DetectStorage> selectByGoodsName(@Param("goodsName") String goodsName,
                                          @Param("batchID") Integer batchID,
                                          @Param("repositoryID") Integer repositoryID);

    /**
     * 添加一条记录
     * @param detectStorage
     */
    void insert(DetectStorage detectStorage);

    /**
     * 批量导入记录
     * @param storages
     */
    void insertBatch(List<DetectStorage> storages);

    /**
     * 更新一条记录
     * @param storage
     */
    void update(DetectStorage storage);

    /**
     * 删除一条记录
     * @param storage
     */
    void delete(DetectStorage storage);

    /**
     * 删除 指定ID 的记录
     * @param goodsID
     * @param batchID
     * @param repositoryID
     */
    void deleteByID(@Param("goodsID") Integer goodsID,
                    @Param("batchID") Integer batchID,
                    @Param("repositoryID") Integer repositoryID);

}
