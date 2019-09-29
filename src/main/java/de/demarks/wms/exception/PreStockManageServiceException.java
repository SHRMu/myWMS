package de.demarks.wms.exception;

/**
 * StockRecordManageService异常
 *
 * @author Ken
 * @since 2017/3/8.
 */
public class PreStockManageServiceException extends BusinessException {

    public PreStockManageServiceException(){
        super();
    }

    public PreStockManageServiceException(Exception e){
        super(e);
    }

    public PreStockManageServiceException(Exception e, String exceptionDesc){
        super(e, exceptionDesc);
    }

    public PreStockManageServiceException(String exceptionDesc){
        super(exceptionDesc);
    }

}
