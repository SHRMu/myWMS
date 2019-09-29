package de.demarks.wms.domain;

/**
 * 出库/入库记录DO
 *
 * @author Shouran
 *
 */
public class StockRecordDTO {

    private Integer recordID;
    private String type;
    private String supplierOrCustomerName;
    private String goodsName;
    private String batchCode;
    private Integer repositoryID;
    private long number;
    private String time;
    private String personInCharge;

    public Integer getRecordID() {
        return recordID;
    }

    public String getType() {
        return type;
    }

    public String getSupplierOrCustomerName() {
        return supplierOrCustomerName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public Integer getRepositoryID() {
        return repositoryID;
    }

    public long getNumber() {
        return number;
    }

    public String getTime() {
        return time;
    }

    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setRecordID(Integer recordID) {
        this.recordID = recordID;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSupplierOrCustomerName(String supplierOrCustomerName) {
        this.supplierOrCustomerName = supplierOrCustomerName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public void setRepositoryID(Integer repositoryID) {
        this.repositoryID = repositoryID;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    @Override
    public String toString() {
        return "StockRecordDTO{" +
                "recordID=" + recordID +
                ", type='" + type + '\'' +
                ", supplierOrCustomerName='" + supplierOrCustomerName + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", batchCode='" + batchCode + '\'' +
                ", repositoryID=" + repositoryID +
                ", number=" + number +
                ", time='" + time + '\'' +
                ", personInCharge='" + personInCharge + '\'' +
                '}';
    }
}
