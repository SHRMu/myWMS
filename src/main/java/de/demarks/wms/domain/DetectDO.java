package de.demarks.wms.domain;

import java.util.Date;

public class DetectDO {

    private Integer id;
    private Integer goodsID;
    private String goodsName;
    private Integer batchID;
    private String batchCode;
    private Integer repositoryID;
    private long number;
    private long passed;
    private long scratch;
    private long damage;
    private Date time;
    private String personInCharge;
    private String desc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "DetectDO{" +
                "id=" + id +
                ", goodsID=" + goodsID +
                ", goodsName='" + goodsName + '\'' +
                ", batchID=" + batchID +
                ", batchCode='" + batchCode + '\'' +
                ", repositoryID=" + repositoryID +
                ", number=" + number +
                ", passed=" + passed +
                ", scratch=" + scratch +
                ", damage=" + damage +
                ", time=" + time +
                ", personInCharge='" + personInCharge + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
