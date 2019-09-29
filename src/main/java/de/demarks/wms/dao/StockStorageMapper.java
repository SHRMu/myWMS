package de.demarks.wms.dao;

import de.demarks.wms.domain.StockStorage;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


import java.util.List;

/**
 * 入库库存信息映射器
 *
 * @author huanyingcool
 *
 */
@Repository
public interface StockStorageMapper {

	/**
	 * 选择全部
	 * @param batchID
	 * @param repositoryID
	 * @return
	 */
	List<StockStorage> selectAll(@Param("batchID") Integer batchID,
                                 @Param("repositoryID") Integer repositoryID);

	/**
	 * 精确搜索
	 * @param goodsID
	 * @param batchID
	 * @param repositoryID
	 * @return
	 */
	List<StockStorage> selectByGoodsID(@Param("goodsID") Integer goodsID,
                                       @Param("batchID") Integer batchID,
                                       @Param("repositoryID") Integer repositoryID);

	/**
	 * 模糊搜索
	 * @param goodsName
	 * @param batchID
	 * @param repositoryID
	 * @return
	 */
	List<StockStorage> selectByGoodsName(@Param("goodsName") String goodsName,
                                         @Param("batchID") Integer batchID,
                                         @Param("repositoryID") Integer repositoryID);

	/**
	 * 更新库存信息
	 * 该库存信息必需已经存在于数据库当中，否则更新无效
	 * @param stockStorage 库存信息
	 */
	void update(StockStorage stockStorage);
	
	/**
	 * 插入新的库存信息
	 * @param stockStorage 库存信息
	 */
	void insert(StockStorage stockStorage);
	
	/**
	 * 批量导入库存信息
	 * @param stockStorages 若干条库存信息
	 */
	void insertBatch(List<StockStorage> stockStorages);
	
	/**
	 * 删除指定仓库中的指定货物的库存信息
	 * @param goodsID 货物ID
	 * @param batchID 批次ID
	 * @param repositoryID 仓库ID
	 */
	void delete(@Param("goodsID") Integer goodsID,@Param("batchID") Integer batchID, @Param("repositoryID") Integer repositoryID);

}
