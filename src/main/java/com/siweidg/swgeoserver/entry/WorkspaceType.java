package com.siweidg.swgeoserver.entry;

import java.io.Serializable;

public class WorkspaceType implements Serializable {

    private static final long serialVersionUID = 41440112347899159L;

    private Long id;

    private String enName;

    private String cnName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEnName() {
        return enName;
    }

    public void setEnName(String enName) {
        this.enName = enName == null ? null : enName.trim();
    }

    public String getCnName() {
        return cnName;
    }

    public void setCnName(String cnName) {
        this.cnName = cnName == null ? null : cnName.trim();
    }
}