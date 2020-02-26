package de.demarks.wms.common.controller;

import de.demarks.wms.common.service.Interface.PacketManageService;
import de.demarks.wms.common.service.Interface.PacketRefMangeService;
import de.demarks.wms.common.service.Interface.PacketStorageManageService;
import de.demarks.wms.common.util.Response;
import de.demarks.wms.common.util.ResponseUtil;
import de.demarks.wms.common.util.StatusUtil;
import de.demarks.wms.dao.PacketInMapper;
import de.demarks.wms.domain.PacketInDO;
import de.demarks.wms.domain.PacketDTO;
import de.demarks.wms.exception.PacketManageServiceException;
import de.demarks.wms.exception.PacketStorageManageServiceException;
import de.demarks.wms.exception.PreStockManageServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 入库包裹操作请求 Handler
 *
 * @author huanyingcool
 */
@RequestMapping(value = "packetInManage")
@Controller
public class PacketInMangeHandler {

    @Autowired
    private ResponseUtil responseUtil;
    @Autowired
    private PacketInMapper packetInMapper;
    @Autowired
    private PacketManageService packetManageService;
    @Autowired
    private PacketStorageManageService packetStorageManageService;
    @Autowired
    private PacketRefMangeService packetRefMangeService;

    private static final String SEARCH_BY_PACKET_ID = "searchByPacketID";
    private static final String SEARCH_BY_TRACE = "searchByTrace";
    private static final String SEARCH_ACTIVE = "searchActive";
    private static final String SEARCH_ALL= "searchAll";

    private static final String SEARCH_REF_ACTIVE = "searchRefActive";

    /**
     * 通用的记录查询
     * @param searchType
     * @param keyWord
     * @param offset
     * @param limit
     * @return
     * @throws PacketManageServiceException
     */
    private Map<String, Object> query(@Param("searchType") String searchType, @Param("keyWord") String keyWord,
                                      @Param("customerID") Integer customerID, @Param("repositoryID") Integer repositoryID,
                                      @Param("offset") int offset, @Param("limit") int limit) throws PacketManageServiceException {

        Map<String, Object> queryResult = null;

        switch (searchType) {
            case SEARCH_BY_PACKET_ID:
                if (StringUtils.isNumeric(keyWord))
                    queryResult = packetManageService.selectByPacketID(Integer.valueOf(keyWord));
                break;
            case SEARCH_BY_TRACE:
                queryResult = packetManageService.selectByTraceApproximate(keyWord, "", customerID, repositoryID, offset, limit);
                break;
            case SEARCH_ACTIVE:
                queryResult = packetManageService.selectByTraceApproximate("",StatusUtil.PACKET_STATUS_SEND, customerID, repositoryID);
                break;
            case SEARCH_ALL:
                queryResult = packetManageService.selectAll(customerID, repositoryID, offset, limit);
                break;
            case SEARCH_REF_ACTIVE:
                queryResult = packetRefMangeService.selectRefApproximate(keyWord,StatusUtil.PACKET_STATUS_SEND,repositoryID);
                break;
            default:
                // do other thing
                break;
        }
        return queryResult;
    }

    /**
     * 获取指定包裹列表
     * @param searchType
     * @param keyWord
     * @param customerID
     * @param repositoryID
     * @param offset
     * @param limit
     * @return
     * @throws PacketManageServiceException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getPacketList", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getPacketList(@RequestParam("searchType") String searchType, @RequestParam("keyWord") String keyWord,
                                      @Param("customerID") Integer customerID, @Param("repositoryID") Integer repositoryID,
                                      @RequestParam("offset") int offset, @RequestParam("limit") int limit) throws PacketManageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();
        List<PacketInDO> rows = null;
        long total = 0;

        // 查询
        Map<String, Object> queryResult = query(searchType, keyWord, customerID, repositoryID, offset, limit);

        if (queryResult != null) {
            rows = (List<PacketInDO>) queryResult.get("data");
            total = (long) queryResult.get("total");
        }

        // 设置 Response
        responseContent.setCustomerInfo("rows", rows);
        responseContent.setResponseTotal(total);
        return responseContent.generateResponse();
    }

    /**
     * 获取未签收包裹列表
     * @param searchType
     * @param keyWord
     * @param customerID
     * @param repositoryID
     * @param offset
     * @param limit
     * @return
     * @throws PacketManageServiceException
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getActivePacketList", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getActivePacketList(@RequestParam("searchType") String searchType, @RequestParam("keyWord") String keyWord,
                                            @Param("customerID") Integer customerID, @Param("repositoryID") Integer repositoryID,
                                            @RequestParam("offset") int offset, @RequestParam("limit") int limit) throws PacketManageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();
        List<PacketInDO> rows = null;
        long total = 0;

        // 查询
        Map<String, Object> queryResult = query(searchType, keyWord, customerID, repositoryID, offset, limit);

        if (queryResult != null) {
            rows = (List<PacketInDO>) queryResult.get("data");
            total = (long) queryResult.get("total");
        }

        // 设置 Response
        responseContent.setCustomerInfo("rows", rows);
        responseContent.setResponseTotal(total);
        return responseContent.generateResponse();
    }


    /**
     * 查询指定packetID的包裹信息
     * @param packetID
     * @return
     * @throws PacketManageServiceException
     */
    @RequestMapping(value = "getPacketInfo", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getPacketInfo(@RequestParam("packetID") Integer packetID) throws PacketManageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();
        String result = Response.RESPONSE_RESULT_ERROR;

        // 获取货物信息
        PacketDTO packetDTO = null;
        Map<String, Object> queryResult = packetManageService.selectByPacketID(packetID);
        if (queryResult != null) {
            packetDTO = (PacketDTO) queryResult.get("data");
            if (packetDTO != null) {
                result = Response.RESPONSE_RESULT_SUCCESS;
            }
        }

        // 设置 Response
        responseContent.setResponseResult(result);
        responseContent.setResponseData(packetDTO);
        return responseContent.generateResponse();
    }

    /**
     * 添加包裹信息
     * @param packetDO
     * @return
     * @throws PacketManageServiceException
     */
    @RequestMapping(value = "addPacket", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addPacket(@RequestBody PacketInDO packetDO) throws PacketManageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();

        // 设置包裹已发货
        packetDO.setStatus(StatusUtil.PACKET_STATUS_SEND);
        String result = packetManageService.addPacket(packetDO) ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

    /**
     * 更新包裹信息
     * @param packetDO
     * @return
     * @throws PacketManageServiceException
     */
    @RequestMapping(value = "updatePacket", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> updatePacket(@RequestBody PacketInDO packetDO) throws PacketManageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();

        String result = packetManageService.updatePacket(packetDO) ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

    /**
     * 删除包裹信息
     * @param packetID
     * @return
     * @throws PacketManageServiceException
     */
    @RequestMapping(value = "deletePacket", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> deletePacket(@RequestParam("packetID") Integer packetID) throws PacketManageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();

        String result = packetManageService.deletePacket(packetID) ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }


    //todo: 导入包裹是添加packetRef信息
    @RequestMapping(value = "importPacket", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> importPacket(@RequestParam("file") MultipartFile file) throws PacketManageServiceException {
        //  初始化 Response
        Response responseContent = responseUtil.newResponseInstance();
        String result = Response.RESPONSE_RESULT_ERROR;

        // 读取文件内容
        int total = 0;
        int available = 0;
        if (file != null) {
            Map<String, Object> importInfo = packetManageService.importPacket(file);
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

    /**
     * 导出包裹信息
     *
     * @param searchType 查找类型
     * @param keyWord    查找关键字
     * @param response   HttpServletResponse
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "exportPacket", method = RequestMethod.GET)
    public void exportPacket(@RequestParam("searchType") String searchType, @RequestParam("keyWord") String keyWord,
                            HttpServletResponse response) throws PacketManageServiceException, IOException {

        String fileName = "packetInfo.xlsx";

        List<PacketInDO> packetDOS = null;
        Map<String, Object> queryResult = query(searchType, keyWord, -1, -1, -1, -1);

        if (queryResult != null) {
            packetDOS = (List<PacketInDO>) queryResult.get("data");
        }

        // 获取生成的文件
        File file = packetManageService.exportPacket(packetDOS);

        // 写出文件
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

    /**
     *  客户预报操作
     * @param trace
     * @param repositoryID
     * @param goodsID
     * @param number
     * @param request
     * @return
     * @throws PacketManageServiceException
     * @throws PreStockManageServiceException
     */
    @RequestMapping(value = "packetStockIn", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> preStockIn(@RequestParam("trace") String trace, @RequestParam("goodsID") Integer goodsID, @RequestParam("repositoryID") Integer repositoryID,
                                   @RequestParam("number") long number, HttpServletRequest request) throws PacketManageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();
        HttpSession session = request.getSession();
//        String personInCharge = (String) session.getAttribute("userName"); //默认获取的userName取锁定客户ID

        PacketInDO packetDO = packetInMapper.selectByTraceApproximate(trace,null , null, repositoryID).get(0); //验证是否有过该包裹预报
        Integer packetID;
        String result = Response.RESPONSE_RESULT_ERROR;
        if (packetDO != null){
            //如果包裹已有预报信息
            packetID = packetDO.getId();
            //执行包裹预报操作
            result = packetManageService.packetStockInOperation(packetID, goodsID, repositoryID, number) ?
                    Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;
        }

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();

    }

    /**
     *
     * @param packetIDStr
     * @param repositoryID
     * @param startDateStr
     * @param endDateStr
     * @param limit
     * @param offset
     * @return
     * @throws ParseException
     * @throws PacketManageServiceException
     */
    @SuppressWarnings({"SingleStatementInBlock", "unchecked"})
    @RequestMapping(value = "searchPacketRecord", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> searchDetectRecord(@RequestParam("packetID") String packetIDStr, @RequestParam("repositoryID") Integer repositoryID,
                                           @RequestParam("startDate") String startDateStr, @RequestParam("endDate") String endDateStr,
                                           @RequestParam("limit") int limit, @RequestParam("offset") int offset) throws ParseException, PacketStorageManageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();
        List<PacketDTO> rows = null;
        long total = 0;

        // 参数检查
        String regex = "([0-9]{4})-([0-9]{2})-([0-9]{2})";
        boolean startDateFormatCheck = (StringUtils.isEmpty(startDateStr) || startDateStr.matches(regex));
        boolean endDateFormatCheck = (StringUtils.isEmpty(endDateStr) || endDateStr.matches(regex));
        boolean packetIDCheck = (StringUtils.isEmpty(packetIDStr) || StringUtils.isNumeric(packetIDStr));

        if (startDateFormatCheck && endDateFormatCheck && packetIDCheck) {
            Integer packetID = -1;
            if (StringUtils.isNumeric(packetIDStr)) {
                packetID = Integer.valueOf(packetIDStr);
            }

            // 转到 Service 执行查询
            Map<String, Object> queryResult = packetStorageManageService.selectPacketRecord(packetID, repositoryID, startDateStr, endDateStr, offset, limit);
            if (queryResult != null) {
                rows = (List<PacketDTO>) queryResult.get("data");
                total = (long) queryResult.get("total");
            }
        } else
            responseContent.setResponseMsg("Request argument error");

        if (rows == null)
            rows = new ArrayList<>(0);

        responseContent.setCustomerInfo("rows", rows);
        responseContent.setResponseTotal(total);
        return responseContent.generateResponse();
    }

}
