package de.demarks.wms.domain;

public class PacketUp {
    private String trace;
    private String desc;

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return "PacketUp{" +
                "trace='" + trace + '\'' +
                ", desc='" + desc + '\'' +
                '}';
    }
}
