package de.demarks.wms.exception;

/**
 * GoodsManageService异常
 *
 * @author Ken
 * @since 2017/3/8.
 */
public class PacketManageServiceException extends BusinessException {

    PacketManageServiceException(){
        super();
    }

    public PacketManageServiceException(Exception e){
        super(e);
    }

    PacketManageServiceException(Exception e, String exceptionDesc){
        super(e, exceptionDesc);
    }

}
