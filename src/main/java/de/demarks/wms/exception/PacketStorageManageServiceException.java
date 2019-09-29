package de.demarks.wms.exception;

/**
 * GoodsManageService异常
 *
 * @author Ken
 * @since 2017/3/8.
 */
public class PacketStorageManageServiceException extends BusinessException {

    PacketStorageManageServiceException(){
        super();
    }

    public PacketStorageManageServiceException(Exception e){
        super(e);
    }

    PacketStorageManageServiceException(Exception e, String exceptionDesc){
        super(e, exceptionDesc);
    }

}
