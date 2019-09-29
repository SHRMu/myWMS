package de.demarks.wms.domain;

import java.util.Date;

/**
 * 主单和附属单都会添加进ref中
 *
 * @author huanyingcool
 */
public class PacketRef {
    private Integer id;
    private String trace;
    private Integer refid;
    private String reftrace;
    private Date time; //包裹发货时间
    private String status;
    private String desc;
    private Integer repositoryID; //包裹所属仓库

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public Integer getRefid() {
        return refid;
    }

    public void setRefid(Integer refid) {
        this.refid = refid;
    }

    public String getReftrace() {
        return reftrace;
    }

    public void setReftrace(String reftrace) {
        this.reftrace = reftrace;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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

    public Integer getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(Integer repositoryID) {
        this.repositoryID = repositoryID;
    }

    @Override
    public String toString() {
        return "PacketRef{" +
                "id=" + id +
                ", trace='" + trace + '\'' +
                ", refid=" + refid +
                ", reftrace='" + reftrace + '\'' +
                ", time=" + time +
                ", status='" + status + '\'' +
                ", desc='" + desc + '\'' +
                ", repositoryID=" + repositoryID +
                '}';
    }
}
