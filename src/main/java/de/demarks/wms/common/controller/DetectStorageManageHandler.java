package de.demarks.wms.common.controller;

import de.demarks.wms.common.service.Interface.DetectStorageService;
import de.demarks.wms.common.util.Response;
import de.demarks.wms.common.util.ResponseUtil;
import de.demarks.wms.domain.DetectStorage;
import de.demarks.wms.exception.DetectStorageServiceException;
import org.apache.commons.lang3.StringUtils;
import org.omg.PortableInterceptor.INACTIVE;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author huanyingcool
 */
@Controller
@RequestMapping(value = "/**/detectStorageManage")
public class DetectStorageManageHandler {

    @Autowired
    private ResponseUtil responseUtil;

    @Autowired
    private DetectStorageService detectStorageManageService;

    private static final String SEARCH_ALL = "searchAll";
    private static final String SEARCH_BY_GOODS_ID = "searchByGoodsID";
    private static final String SEARCH_BY_GOODS_NAME = "searchByGoodsName";

    /**
     * 查询库存信息
     *
     * @param searchType       查询类型
     * @param keyword          查询关键字
     * @param batchBelong      查询批次
     * @param repositoryID     查询仓库
     * @param offset           分页偏移值
     * @param limit            分页大小
     * @return 结果的一个Map，其中： key为 data 的代表记录数据；key 为 total 代表结果记录的数量
     */
    private Map<String, Object> query(String searchType, String keyword, String batchBelong, Integer repositoryID, int offset,
                                      int limit) throws DetectStorageServiceException {
        Map<String, Object> queryResult = null;

        switch (searchType) {
            case SEARCH_ALL:
                if (StringUtils.isNumeric(batchBelong)){
                    Integer batchID = Integer.valueOf(batchBelong);
                    queryResult = detectStorageManageService.selectAll(batchID, repositoryID, offset, limit);
                }else
                    queryResult = detectStorageManageService.selectAll(-1, repositoryID, offset, limit);
                break;
            case SEARCH_BY_GOODS_ID:
                if (StringUtils.isNumeric(keyword)){
                    Integer goodsID = Integer.valueOf(keyword);
                    if (StringUtils.isNumeric(batchBelong)) {
                        Integer batchID = Integer.valueOf(batchBelong);
                        queryResult = detectStorageManageService.selectByGoodsID(goodsID, batchID, repositoryID, offset, limit);
                    }else
                        queryResult = detectStorageManageService.selectByGoodsID(goodsID, -1, repositoryID, offset, limit);
                }
                break;
            case SEARCH_BY_GOODS_NAME:
                if (StringUtils.isNumeric(batchBelong)) {
                    Integer batchID = Integer.valueOf(batchBelong);
                    queryResult = detectStorageManageService.selectByGoodsName(keyword, batchID, repositoryID, offset, limit);
                }else
                    queryResult = detectStorageManageService.selectByGoodsName(keyword, -1, repositoryID, offset, limit);
                break;
            default:
                // do other thing
                break;
        }
        return queryResult;
    }


    /**
     * 查询指定批次和仓库的检测库存
     *
     * @param keyword          查询关键字
     * @param searchType       查询类型
     * @param batchBelong      查询所属的批次
     * @param repositoryID 查询所属的仓库
     * @param offset           分页偏移值
     * @param limit            分页大小
     * @return 结果的一个Map，其中： key为 rows 的代表记录数据；key 为 total 代表结果记录的数量
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "getDetectStorageList", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getDetectStorageList(@RequestParam("keyword") String keyword,@RequestParam("searchType") String searchType,
                                                 @RequestParam("batchBelong") String batchBelong, @RequestParam("repositoryBelong") Integer repositoryID,
                                                 @RequestParam("offset") int offset, @RequestParam("limit") int limit) throws DetectStorageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();

        List<DetectStorage> rows;
        long total = 0;

        // query
        Map<String, Object> queryResult = query(searchType, keyword, batchBelong, repositoryID, offset, limit);
        if (queryResult != null) {
            rows = (List<DetectStorage>) queryResult.get("data");
            total = (long) queryResult.get("total");
        } else
            rows = new ArrayList<>();

        // 设置 Response
        responseContent.setCustomerInfo("rows", rows);
        responseContent.setResponseTotal(total);
        return responseContent.generateResponse();
    }


    /**
     * 添加一条库存信息
     *
     * @return 返回一个map，其中：key 为 result表示操作的结果，包括：success 与 error
     */
    @RequestMapping(value = "addDetectStorage", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> addDetectStorage(@RequestBody Map<String, Object> params) throws DetectStorageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();
        String isSuccess = Response.RESPONSE_RESULT_ERROR;
        boolean isAvailable = true;

        String goodsID = (String) params.get("goodsID");
        String batchID = (String) params.get("batchID");
        String repositoryID = (String) params.get("repositoryID");
        String passed = (String) params.get("passed");
        String scratch = (String) params.get("scratch");
        String damage = (String) params.get("damage");

        if (StringUtils.isBlank(goodsID) || !StringUtils.isNumeric(goodsID))
            isAvailable = false;
        if (StringUtils.isBlank(batchID) || !StringUtils.isNumeric(batchID))
            isAvailable = false;
        if (StringUtils.isBlank(repositoryID) || !StringUtils.isNumeric(repositoryID))
            isAvailable = false;
        if (StringUtils.isBlank(passed) || !StringUtils.isNumeric(passed))
            isAvailable = false;
        if (StringUtils.isBlank(scratch) || !StringUtils.isNumeric(scratch))
            isAvailable = false;
        if (StringUtils.isBlank(damage) || !StringUtils.isNumeric(damage))
            isAvailable = false;

        if (isAvailable) {
            isSuccess = detectStorageManageService.addDetectStorage(Integer.valueOf(goodsID), Integer.valueOf(batchID), Integer.valueOf(repositoryID),
                    Integer.valueOf(passed), Integer.valueOf(scratch), Integer.valueOf(damage)) ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;
        }

        // 设置 Response
        responseContent.setResponseResult(isSuccess);
        return responseContent.generateResponse();
    }


    /**
     * 更新库存信息
     *
     * @return 返回一个map，其中：key 为 result表示操作的结果，包括：success 与 error
     */
    @RequestMapping(value = "updateDetectStorage", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> updateDetectStorage(@RequestBody Map<String, Object> params) throws DetectStorageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();
        boolean isAvailable = true;
        String result = Response.RESPONSE_RESULT_ERROR;

        String goodsID = (String) params.get("goodsID");
        String batchID = (String) params.get("batchID");
        String repositoryID = (String) params.get("repositoryID");
        String passed = (String) params.get("passed");
        String scratch = (String) params.get("scratch");
        String damage = (String) params.get("damage");


        if (StringUtils.isBlank(goodsID) || !StringUtils.isNumeric(goodsID))
            isAvailable = false;
        if (StringUtils.isBlank(batchID) || !StringUtils.isNumeric(batchID))
            isAvailable = false;
        if (StringUtils.isBlank(repositoryID) || !StringUtils.isNumeric(repositoryID))
            isAvailable = false;
        if (StringUtils.isBlank(passed) || !StringUtils.isNumeric(passed))
            isAvailable = false;
        if (StringUtils.isBlank(scratch) || !StringUtils.isNumeric(scratch))
            isAvailable = false;
        if (StringUtils.isBlank(damage) || !StringUtils.isNumeric(damage))
            isAvailable = false;


        if (isAvailable) {
            result = detectStorageManageService.updateDetectStorage(Integer.valueOf(goodsID), Integer.valueOf(batchID), Integer.valueOf(repositoryID), 0,
                    Integer.valueOf(passed),Integer.valueOf(scratch),Integer.valueOf(damage)) ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;
        }

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

    /**
     * 删除一条检测库存信息
     *
     * @param goodsID      货物ID
     * @param batchID      批次ID
     * @param repositoryID 仓库ID
     * @return 返回一个map，其中：key 为 result表示操作的结果，包括：success 与 error
     */
    @RequestMapping(value = "deleteDetectStorage", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> deleteDetectStorage(@RequestParam("goodsID") String goodsID,
                                            @RequestParam("batchID") String batchID,
                                            @RequestParam("repositoryID") String repositoryID) throws DetectStorageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();

        String result = Response.RESPONSE_RESULT_ERROR;
        boolean isAvailable = true;

        if (StringUtils.isBlank(goodsID) || !StringUtils.isNumeric(goodsID))
            isAvailable = false;
        if (StringUtils.isBlank(batchID) || !StringUtils.isNumeric(batchID))
            isAvailable = false;
        if (StringUtils.isBlank(repositoryID) || !StringUtils.isNumeric(repositoryID))
            isAvailable = false;

        if (isAvailable) {
            result = detectStorageManageService.deleteDetectStorage(Integer.valueOf(goodsID), Integer.valueOf(batchID), Integer.valueOf(repositoryID))
                    ? Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;
        }

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }


    /**
     * 查询库存信息，查询所属的仓库为session保存的信息
     *
     * @param keyword    查询关键字
     * @param searchType 查询类型
     * @param offset     分页偏移值
     * @param limit      分页大小
     * @param request    请求
     * @return 结果的一个Map，其中： key为 rows 的代表记录数据；key 为 total 代表结果记录的数量
     */
    @SuppressWarnings("unchecked")
//    @RequestMapping(value = "getSessDetectStorageList", method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, Object> getDetectStorageList(@RequestParam("keyword") String keyword,
                                             @RequestParam("searchType") String searchType, @RequestParam("offset") int offset,
                                             @RequestParam("limit") int limit, HttpServletRequest request) throws DetectStorageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();

        List<DetectStorage> rows = null;
        long total = 0;

        HttpSession session = request.getSession();
        Integer batchID = (Integer)session.getAttribute("batchBelong");
        Integer repositoryID = (Integer) session.getAttribute("repositoryBelong");
        if (batchID != null && repositoryID != null) {
            Map<String, Object> queryResult = query(searchType, keyword, batchID.toString(), repositoryID, offset, limit);
            if (queryResult != null) {
                rows = (List<DetectStorage>) queryResult.get("data");
                total = (long) queryResult.get("total");
            }
        }

        // 设置 Response
        responseContent.setCustomerInfo("rows", rows);
        responseContent.setResponseTotal(total);
        return responseContent.generateResponse();
    }


}
