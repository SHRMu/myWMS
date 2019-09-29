package de.demarks.wms.common.controller;

import de.demarks.wms.common.service.Interface.DetectManageService;
import de.demarks.wms.common.util.Response;
import de.demarks.wms.common.util.ResponseUtil;
import de.demarks.wms.domain.DetectDO;
import de.demarks.wms.exception.DetectManageServiceException;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "detectManage")
public class DetectManageHandler {

    @Autowired
    private ResponseUtil responseUtil;

    @Autowired
    private DetectManageService detectManageService;

    /**
     * 货物检测操作
     * @param goodsID
     * @param batchID
     * @param repositoryID
     * @param passed
     * @param scratch
     * @param damage
     * @param personInCharge
     * @param desc
     * @param request
     * @return
     * @throws DetectManageServiceException
     */
    @RequestMapping(value = "detect", method = RequestMethod.POST)
    public
    @ResponseBody
    Map<String, Object> detect(@RequestParam("goodsID") Integer goodsID, @RequestParam("batchID") Integer batchID, @RequestParam("repositoryID") Integer repositoryID,
                               @RequestParam("passed") long passed, @RequestParam("scratch") long scratch, @RequestParam("damage") long damage,
                               @RequestParam("personInCharge") String personInCharge, @Param("desc") String desc,
                               HttpServletRequest request) throws DetectManageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();

        HttpSession session = request.getSession();
        if (personInCharge.equals(""))
            personInCharge = (String) session.getAttribute("userName");

        String result = detectManageService.detectOperation(goodsID, batchID, repositoryID, passed, scratch, damage, personInCharge,desc) ?
                Response.RESPONSE_RESULT_SUCCESS : Response.RESPONSE_RESULT_ERROR;

        // 设置 Response
        responseContent.setResponseResult(result);
        return responseContent.generateResponse();
    }

    /**
     * 查询检测记录
     *
     * @param batchIDStr        查询批次ID
     * @param repositoryIDStr 查询记录所对应的仓库ID
     * @param endDateStr      查询的记录起始日期
     * @param startDateStr    查询的记录结束日期
     * @param limit           分页大小
     * @param offset          分页偏移值
     * @return 返回一个Map，其中：Key为rows的值代表所有记录数据，Key为total的值代表记录的总条数
     */
    @SuppressWarnings({"SingleStatementInBlock", "unchecked"})
    @RequestMapping(value = "searchDetectRecord", method = RequestMethod.GET)
    public @ResponseBody
    Map<String, Object> searchDetectRecord(@RequestParam("batchID") String batchIDStr, @RequestParam("repositoryID") String repositoryIDStr,
                                           @RequestParam("startDate") String startDateStr, @RequestParam("endDate") String endDateStr,
                                           @RequestParam("limit") int limit, @RequestParam("offset") int offset) throws ParseException, DetectManageServiceException {
        // 初始化 Response
        Response responseContent = responseUtil.newResponseInstance();
        List<DetectDO> rows = null;
        long total = 0;

        // 参数检查
        String regex = "([0-9]{4})-([0-9]{2})-([0-9]{2})";
        boolean startDateFormatCheck = (StringUtils.isEmpty(startDateStr) || startDateStr.matches(regex));
        boolean endDateFormatCheck = (StringUtils.isEmpty(endDateStr) || endDateStr.matches(regex));
        boolean batchIDCheck = (StringUtils.isEmpty(batchIDStr) || StringUtils.isNumeric(batchIDStr));
        boolean repositoryIDCheck = (StringUtils.isEmpty(repositoryIDStr) || StringUtils.isNumeric(repositoryIDStr));

        if (startDateFormatCheck && endDateFormatCheck && batchIDCheck && repositoryIDCheck) {
            Integer batchID = -1;
            Integer repositoryID = -1;
            if (StringUtils.isNumeric(batchIDStr)) {
                batchID = Integer.valueOf(batchIDStr);
            }
            if (StringUtils.isNumeric(repositoryIDStr)) {
                repositoryID = Integer.valueOf(repositoryIDStr);
            }

            // 转到 Service 执行查询
            Map<String, Object> queryResult = detectManageService.selectDetectRecord(batchID, repositoryID, startDateStr, endDateStr, offset, limit);
            if (queryResult != null) {
                rows = (List<DetectDO>) queryResult.get("data");
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
