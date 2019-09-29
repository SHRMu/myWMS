package de.demarks.wms.exception;

/**
 * RepositoryAdminManageService异常
 *
 * @author Ken
 * @since 2017/3/8.
 */
public class RepositoryBatchManageServiceException extends BusinessException {

    public RepositoryBatchManageServiceException(){
        super();
    }

    public RepositoryBatchManageServiceException(Exception e){
        super(e);
    }

    public RepositoryBatchManageServiceException(Exception e, String exceptionDesc){
        super(e, exceptionDesc);
    }

    public RepositoryBatchManageServiceException(String exceptionDesc){
        super(exceptionDesc);
    }
}
