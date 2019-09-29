package de.demarks.wms.exception;

public class DetectStorageServiceException extends BusinessException{

    DetectStorageServiceException(){
        super();
    }

    public DetectStorageServiceException(Exception e){
        super(e);
    }

    DetectStorageServiceException(Exception e, String exceptionDesc){
        super(e, exceptionDesc);
    }
}
