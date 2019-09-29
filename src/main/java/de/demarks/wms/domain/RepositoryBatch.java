package de.demarks.wms.domain;

import java.util.Date;

/**
 * 批次信息
 *
 * @author Shouran
 *
 */
public class RepositoryBatch {

    private Integer id; // 批次ID 自增
    private String code; // 批次编号 Anker 10
    private String status; // 批次状态 可用/完结
    private String desc;// 批次描述
    private Date time; //批次开始时间 创建时间
    private Integer repositoryID; // 批次所属仓库

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Integer getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(Integer repositoryID) {
        this.repositoryID = repositoryID;
    }

    @Override
    public String toString() {
        return "RepositoryBatch{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", status='" + status + '\'' +
                ", desc='" + desc + '\'' +
                ", time=" + time +
                ", repositoryID=" + repositoryID +
                '}';
    }
}
