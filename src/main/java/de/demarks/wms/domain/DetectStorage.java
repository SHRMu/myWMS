package de.demarks.wms.domain;

public class DetectStorage {

    private Integer goodsID;// 货物ID
    private String goodsName; //货物名称
    private Integer batchID; // 货物批次
    private String batchCode; // 批次编号
    private Integer repositoryID;// 仓库ID
    private long number; // 检测总数
    private long passed; // 待出库良品数
    private long scratch; // 划痕数
    private long damage; // 故障数

    public Integer getGoodsID() {
        return goodsID;
    }

    public void setGoodsID(Integer goodsID) {
        this.goodsID = goodsID;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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

    public Integer getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(Integer repositoryID) {
        this.repositoryID = repositoryID;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public long getPassed() {
        return passed;
    }

    public void setPassed(long passed) {
        this.passed = passed;
    }

    public long getScratch() {
        return scratch;
    }

    public void setScratch(long scratch) {
        this.scratch = scratch;
    }

    public long getDamage() {
        return damage;
    }

    public void setDamage(long damage) {
        this.damage = damage;
    }

    @Override
    public String toString() {
        return "DetectStorage{" +
                "goodsID=" + goodsID +
                ", goodsName='" + goodsName + '\'' +
                ", batchID=" + batchID +
                ", batchCode='" + batchCode + '\'' +
                ", repositoryID=" + repositoryID +
                ", number=" + number +
                ", passed=" + passed +
                ", scratch=" + scratch +
                ", damage=" + damage +
                '}';
    }
}
