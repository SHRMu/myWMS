package de.demarks.wms.exception;

public class DetectManageServiceException extends BusinessException  {

    public DetectManageServiceException(){
        super();
    }

    public DetectManageServiceException(Exception e){
        super(e);
    }

    public DetectManageServiceException(Exception e, String exceptionDesc){
        super(e, exceptionDesc);
    }

    public DetectManageServiceException(String exceptionDesc){
        super(exceptionDesc);
    }

}
