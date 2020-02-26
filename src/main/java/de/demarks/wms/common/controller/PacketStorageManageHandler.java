package de.demarks.wms.common.controller;
import de.demarks.wms.common.service.Interface.PacketManageService;
import de.demarks.wms.common.service.Interface.PacketStorageManageService;
import de.demarks.wms.common.util.Response;
import de.demarks.wms.common.util.ResponseUtil;
import de.demarks.wms.common.util.StatusUtil;
import de.demarks.wms.dao.PacketInMapper;
import de.demarks.wms.domain.PacketInDO;
import de.demarks.wms.domain.PacketStorage;
import de.demarks.wms.exception.PacketStorageManageServiceException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 *
 *  客户预报库存管理
 *
 * @author huanyingcool
 */

@Controller
@RequestMapping("**/packetStorageManage")
public class PacketStorageManageHandler {

    @Autowired
    private ResponseUtil responseUtil;
    @Autowired
    private PacketInMapper packetInMapper;
    @Autowired
    private PacketManageService packetManageService;
    @Autowired
    private PacketStorageManageService packetStorageManageService;

    private static final String SEARCH_BY_GOODID = "searchByGoodsID";
    private static final String SEARCH_BY_GOODNAME = "searchByGoodsName";
    private static final String SEARCH_BY_PACKETID = "searchByPacketID";
    private static final String SEARCH_BY_PACKETTRACE = "searchByPacketTrace";
    private static final String SEARCH_ALL = "searchAll";

    /**
     *  查询预报包裹库存信息
     *
     * @param searchType       查询类型
     * @param keyword          查询关键字
     * @param repositoryID     查询仓库
     * @param offset           分页偏移值
     * @param limit            分页大小
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    private Map<String, Object> query(String searchType, String keyword, String packetInfo, Integer repositoryID,
                                      int offset, int limit) throws PacketStorageManageServiceException {

        Map<String, Object> queryResult = null;

        switch (searchType) {
            case SEARCH_BY_GOODID:
                if (StringUtils.isNumeric(keyword)){
                    Integer goodsID = Integer.valueOf(keyword);
                    queryResult = packetStorageManageService.selectByGoodsIDandStatus(goodsID,packetInfo,repositoryID);
                }
                break;
            case SEARCH_BY_GOODNAME:
                queryResult = packetStorageManageService.selectByGoodsNameAndStatus(keyword, packetInfo, repositoryID);
                break;
            case SEARCH_BY_PACKETID:
                if (StringUtils.isNumeric(keyword)){
                    Integer packetID = Integer.valueOf(keyword);
                    queryResult = packetStorageManageService.selectAll(packetID,packetInfo,repositoryID);
                }
                break;
            case SEARCH_BY_PACKETTRACE:
                queryResult = packetStorageManageService.selectByTrace(keyword, packetInfo, repositoryID);
                break;
            case SEARCH_ALL:
                queryResult = packetStorageManageService.selectAll(-1,packetInfo, repositoryID);
            default:
                break;
        }
        return queryResult;
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getStorageList", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getStorageListWithPacket(@RequestParam("searchType") String searchType, @RequestParam("keyword") String keyword,
                                                 @RequestParam("packetInfo") String packetInfo, @RequestParam("repositoryID") Integer repositoryID,
                                                 @RequestParam("offset") int offset, @RequestParam("limit") int limit) throws PacketStorageManageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();
        List<PacketStorage> rows;
        long total = 0;

        // query
        Map<String, Object> queryResult = query(searchType, keyword, packetInfo, repositoryID, offset, limit);
        if (queryResult != null) {
            rows = (List<PacketStorage>) queryResult.get("data");
            total = (long) queryResult.get("total");
        } else
            rows = new ArrayList<>();

        // 设置 Response
        responseContent.setCustomerInfo("rows", rows);
        responseContent.setResponseTotal(total);
        return responseContent.generateResponse();
    }


    @RequestMapping(value = "addPacketStorage", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addStorageRecord(@RequestBody Map<String, Object> params) throws PacketStorageManageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();
        String isSuccess = Response.RESPONSE_RESULT_ERROR;
        boolean isAvailable = true;

        String packetID = (String) params.get("packetID");
        String goodsID = (String) params.get("goodsID");
        String repositoryID = (String) params.get("repositoryID");
        String number = (String) params.get("number");
        String storage = (String) params.get("storage");

        if (StringUtils.isBlank(packetID)||!StringUtils.isNumeric(packetID))
            isAvailable = false;
        if (StringUtils.isBlank(goodsID) || !StringUtils.isNumeric(goodsID))
            isAvailable = false;
        if (StringUtils.isBlank(repositoryID) || !StringUtils.isNumeric(repositoryID))
            isAvailable = false;
        if (StringUtils.isBlank(number) || !StringUtils.isNumeric(number))
            isAvailable = false;
        if (StringUtils.isBlank(storage) || !StringUtils.isNumeric(storage))
            isAvailable = false;
        if (isAvailable) {
            isSuccess = packetStorageManageService.addPacketStorage( Integer.valueOf(goodsID), Integer.valueOf(packetID),Integer.valueOf(repositoryID),
                    Integer.valueOf(number), Integer.valueOf(storage)) ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;
        }

        // 设置 Response
        responseContent.setResponseResult(isSuccess);
        return responseContent.generateResponse();
    }


    @RequestMapping(value = "updatePacketStorage", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> updateStorageRecord(@RequestBody Map<String, Object> params) throws PacketStorageManageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();
        boolean isAvailable = true;
        String result = Response.RESPONSE_RESULT_ERROR;

        String goodsID = (String) params.get("goodsID");
        String packetID = (String) params.get("packetID");
        String repositoryID = (String) params.get("repositoryID");
        String number = (String) params.get("number");
        String storage = (String) params.get("storage");
        String status = (String) params.get("status");

        if (StringUtils.isBlank(goodsID) || !StringUtils.isNumeric(goodsID))
            isAvailable = false;
        if (StringUtils.isBlank(packetID) || !StringUtils.isNumeric(packetID))
            isAvailable = false;
        if (StringUtils.isBlank(repositoryID) || !StringUtils.isNumeric(repositoryID))
            isAvailable = false;
        if (StringUtils.isBlank(number) || !StringUtils.isNumeric(number))
            isAvailable = false;
        if (StringUtils.isBlank(storage) || !StringUtils.isNumeric(storage))
            isAvailable = false;
        if (StringUtils.isBlank(status))
            isAvailable = false;

        PacketInDO packetDO = packetInMapper.selectByPacketID(Integer.valueOf(packetID));
        if (packetDO != null && !packetDO.getStatus().equals(status)){
            packetDO.setStatus(status);
            packetInMapper.update(packetDO);
        }

        if (isAvailable) {
            result = packetStorageManageService.updatePacketStorage(Integer.valueOf(goodsID), Integer.valueOf(packetID), Integer.valueOf(repositoryID),
                    Integer.valueOf(number), Integer.valueOf(storage)) ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;
        }

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

    @RequestMapping(value = "deletePacketStorage", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> deleteStorageRecord(@RequestParam("goodsID") String goodsID, @RequestParam("packetID") String packetID,
                                            @RequestParam("repositoryID") String repositoryID) throws PacketStorageManageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();

        String result = Response.RESPONSE_RESULT_ERROR;
        boolean isAvailable = true;

        if (StringUtils.isBlank(goodsID) || !StringUtils.isNumeric(goodsID))
            isAvailable = false;
        if (StringUtils.isBlank(packetID) || !StringUtils.isNumeric(packetID))
            isAvailable = false;
        if (StringUtils.isBlank(repositoryID) || !StringUtils.isNumeric(repositoryID))
            isAvailable = false;

        if (isAvailable) {
            result = packetStorageManageService.deletePacketStorage(Integer.valueOf(goodsID), Integer.valueOf(packetID), Integer.valueOf(repositoryID))
                    ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;
        }

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }


    @RequestMapping(value = "importPacketStorage", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> importStorageRecord(@RequestParam("file") MultipartFile file) throws PacketStorageManageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();
        String result = Response.RESPONSE_RESULT_ERROR;

        int total = 0;
        int available = 0;

        if (file != null) {
            Map<String, Object> importInfo = packetStorageManageService.importPacketStorage(file);
            if (importInfo != null) {
                total = (int) importInfo.get("total");
                available = (int) importInfo.get("available");
                result = Response.RESPONSE_RESULT_SUCCESS;
            }
        }

        // 设置 Response
        responseContent.setResponseResult(result);
        responseContent.setResponseTotal(total);
        responseContent.setCustomerInfo("available", available);
        return responseContent.generateResponse();
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "exportPacketStorage", method = RequestMethod.GET)
    public void exportStorageRecord(@RequestParam("searchType") String searchType, @RequestParam("keyword") String keyword,
                                    @RequestParam(value = "packetTrace") String packetTrace, @RequestParam(value = "repositoryBelong", required = false) String repositoryBelong,
                                    HttpServletRequest request, HttpServletResponse response) throws PacketStorageManageServiceException, IOException {

        String fileName = "packetStorageInfo.xlsx";

        HttpSession session = request.getSession();
        Integer sessionRepositoryBelong = (Integer) session.getAttribute("repositoryBelong");
//        if (sessionRepositoryBelong != null && !sessionRepositoryBelong.equals("none"))
//            repositoryBelong = sessionRepositoryBelong.toString();

        List<PacketStorage> packetStorageList = null;
        Map<String, Object> queryResult = query(searchType, keyword, packetTrace, StatusUtil.DEFAULT_REPOSITORY_ID, -1, -1);
        if (queryResult != null)
            packetStorageList = (List<PacketStorage>) queryResult.get("data");

        File file = packetStorageManageService.exportPacketStorage(packetStorageList);
        if (file != null) {
            // 设置响应头
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            FileInputStream inputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();
            byte[] buffer = new byte[8192];

            int len;
            while ((len = inputStream.read(buffer, 0, buffer.length)) > 0) {
                outputStream.write(buffer, 0, len);
                outputStream.flush();
            }

            inputStream.close();
            outputStream.close();

        }
    }


}
