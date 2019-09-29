package de.demarks.wms.domain;


import java.util.Date;

/**
 * 出库记录
 *
 * @author huanyingcool
 */
public class StockOutDO {

    private Integer id;
    private Integer goodsID;
    private String goodName;
    private Integer batchID;
    private String batchCode;
    private Integer customerID;
    private String customerName;
    private Integer repositoryID;
    private long number;
    private Date time;
    private String personInCharge;// 经手人

    public Integer getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(Integer repositoryID) {
        this.repositoryID = repositoryID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getBatchID() {
        return batchID;
    }

    public void setBatchID(Integer batchID) {
        this.batchID = batchID;
    }

    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(Integer goodsID) {
        this.goodsID = goodsID;
    }

    public String getGoodName() {
        return goodName;
    }

    public void setGoodName(String goodName) {
        this.goodName = goodName;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getPersonInCharge() {
        return personInCharge;
    }

    public void setPersonInCharge(String personInCharge) {
        this.personInCharge = personInCharge;
    }

    @Override
    public String toString() {
        return "StockOutDO{" +
                "id=" + id +
                ", goodsID=" + goodsID +
                ", goodName='" + goodName + '\'' +
                ", batchID=" + batchID +
                ", batchCode='" + batchCode + '\'' +
                ", customerID=" + customerID +
                ", customerName='" + customerName + '\'' +
                ", repositoryID=" + repositoryID +
                ", number=" + number +
                ", time=" + time +
                ", personInCharge='" + personInCharge + '\'' +
                '}';
    }
}
