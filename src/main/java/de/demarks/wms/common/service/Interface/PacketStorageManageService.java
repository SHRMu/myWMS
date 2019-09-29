package de.demarks.wms.common.service.Interface;

import de.demarks.wms.domain.PacketStorage;
import de.demarks.wms.exception.PacketStorageManageServiceException;
import de.demarks.wms.exception.PreStockManageServiceException;
import de.demarks.wms.exception.StockRecordManageServiceException;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 预报库存管理 Service
 *
 * @author huanyingcool
 */
public interface PacketStorageManageService {

   Map<String, Object> selectAll(@Param("packetID") Integer packetID, @Param("packetStatus")String packetStatus, @Param("repositoryID") Integer repositoryID) throws PacketStorageManageServiceException;

   Map<String, Object> selectAll(@Param("packetID") Integer packetID,@Param("packetStatus")String packetStatus, @Param("repositoryID") Integer repositoryID,
                                 @Param("offset") Integer offset, @Param("limit") Integer limit) throws PacketStorageManageServiceException;

   Map<String, Object> selectByTrace(@Param("trace") String trace, @Param("status") String status, @Param("repositoryID") Integer repositoryID) throws PacketStorageManageServiceException;

   Map<String, Object> selectByTrace(@Param("trace") String trace, @Param("status") String status, @Param("repositoryID") Integer repositoryID,
                                     @Param("offset") Integer offset, @Param("limit") Integer limit) throws PacketStorageManageServiceException;

   Map<String, Object> selectByGoodsID(@Param("goodsID") Integer goodsID, @Param("packetID") Integer packetID,
                                       @Param("repositoryID") Integer repositoryID) throws PacketStorageManageServiceException;

   Map<String, Object> selectByGoodsID(@Param("goodsID") Integer goodsID, @Param("packetID") Integer packetID, @Param("repositoryID") Integer repositoryID,
                                       @Param("offset") Integer offset, @Param("limit") Integer limit) throws PacketStorageManageServiceException;

   Map<String, Object> selectByGoodsIDandStatus(@Param("goodsID") Integer goodsID, @Param("packetStatus") String packetStatus,
                                       @Param("repositoryID") Integer repositoryID) throws PacketStorageManageServiceException;

   Map<String, Object> selectByGoodsIDandStatus(@Param("goodsID") Integer goodsID, @Param("packetStatus") String packetStatus, @Param("repositoryID") Integer repositoryID,
                                       @Param("offset") Integer offset, @Param("limit") Integer limit) throws PacketStorageManageServiceException;

   Map<String, Object> selectByGoodsNameAndStatus(@Param("goodsName") String goodsName, @Param("packetStatus") String packetStatus,
                                         @Param("repositoryID") Integer repositoryID) throws PacketStorageManageServiceException;

   Map<String, Object> selectByGoodsNameAndStatus (@Param("goodsName") String goodsName,  @Param("packetStatus") String packetStatus,  @Param("repositoryID") Integer repositoryID,
                                         @Param("offset") Integer offset, @Param("limit") Integer limit) throws PacketStorageManageServiceException;

   boolean addPacketStorage(@Param("goodsID") Integer goodsID, @Param("packetID") Integer packetID, @Param("repositoryID") Integer repositoryID,
                      @Param("number") long number, @Param("storage") long storage) throws PacketStorageManageServiceException;

   boolean updatePacketStorage(@Param("goodsID") Integer goodsID,  @Param("packetID") Integer packetID, @Param("repositoryID") Integer repositoryID,
                               @Param("number") long number, @Param("storage") long storage) throws PacketStorageManageServiceException;

   boolean deletePacketStorage(@Param("goodsID") Integer goodsID,  @Param("packetID") Integer packetID, @Param("repositoryID") Integer repositoryID)throws PacketStorageManageServiceException;

   boolean packetStorageIncrease(@Param("goodsID") Integer goodsID, @Param("packetID") Integer packetID, @Param("repositoryID") Integer repositoryID,
                                 @Param("number") long number) throws PacketStorageManageServiceException;

   Map<String, Object> importPacketStorage(MultipartFile file) throws PacketStorageManageServiceException;

   File exportPacketStorage(List<PacketStorage> packetStorageList);

   Map<String, Object> selectPacketRecord(@Param("packetID") Integer packetID, @Param("repositoryID") Integer repositoryID,
                                          @Param("startDateStr") String startDateStr, @Param("endDateStr") String endDateStr) throws PacketStorageManageServiceException;


   Map<String, Object> selectPacketRecord(@Param("packetID") Integer packetID, @Param("repositoryID") Integer repositoryID,
                                          @Param("startDateStr") String startDateStr, @Param("endDateStr") String endDateStr,
                                          @Param("offset") int offset, @Param("limit") int limit) throws PacketStorageManageServiceException;

}