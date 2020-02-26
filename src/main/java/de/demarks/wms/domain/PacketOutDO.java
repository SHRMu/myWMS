package de.demarks.wms.domain;

import java.util.Date;

/**
 * 发送包裹信息
 *
 * @author huanyingcool
 */
public class PacketOutDO {

    private Integer id; //包裹系统ID
    private String  trace; //包裹序列号
    private Date time; //包裹发货时间
    private String status; //包裹状态
    private String desc; //包裹描述
    private Integer customerID; //包裹收发客户
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

    public Integer getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Integer customerID) {
        this.customerID = customerID;
    }

    public Integer getRepositoryID() {
        return repositoryID;
    }

    public void setRepositoryID(Integer repositoryID) {
        this.repositoryID = repositoryID;
    }


    @Override
    public String toString() {
        return "PacketDO{" +
                "id=" + id +
                ", trace='" + trace + '\'' +
                ", time=" + time +
                ", status='" + status + '\'' +
                ", desc='" + desc + '\'' +
                ", customerID=" + customerID +
                ", repositoryID=" + repositoryID +
                '}';
    }

}
