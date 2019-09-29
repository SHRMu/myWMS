package de.demarks.wms.common.util;

public class StatusUtil {

    //客户发货包裹状态
    public static final String PACKET_STATUS_SEND = "发货中";
    public static final String PACKET_STATUS_RECEIVE = "已签收";

    //出库包裹状态
    public static final String STOCK_OUT_WAITING ="待箱单";
    public static final String STOCK_OUT_SEND = "已出库";

    //批次状态
    public static final String BATCH_STATUS_AVAILABLE = "可用";
    public static final String BATCH_STATUS_END = "完结";

    //仓库编号
    public static final Integer DEFAULT_REPOSITORY_ID = 3001;
}
