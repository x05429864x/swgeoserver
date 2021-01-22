package com.siweidg.swgeoserver.entry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value="样式",description="样式类")
public class Style implements Serializable {

    private static final long serialVersionUID = 42440112347899158L;

    @ApiModelProperty(value = "ID",hidden = true)
    private Long styleId;

    @ApiModelProperty(value = "样式中文名称",dataType = "String",required = true)
    private String styleNameCn;

    @ApiModelProperty(value = "样式英文名称",dataType = "String",hidden = true)
    private String styleNameEn;

    @ApiModelProperty(value = "styleWorkspace",hidden = true)
    private String styleWorkspace;

    @ApiModelProperty(value = "styleCreater",hidden = true)
    private Long styleCreater;

    @ApiModelProperty(value = "styleCreateTime",hidden = true)
    private Date styleCreateTime;

    @ApiModelProperty(value = "styleRemark",hidden = true)
    private String styleRemark;

    @ApiModelProperty(value = "styleFlag",hidden = true)
    private Integer styleFlag;

    public Long getStyleId() {
        return styleId;
    }

    public void setStyleId(Long styleId) {
        this.styleId = styleId;
    }

    public String getStyleNameCn() {
        return styleNameCn;
    }

    public void setStyleNameCn(String styleNameCn) {
        this.styleNameCn = styleNameCn;
    }

    public String getStyleNameEn() {
        return styleNameEn;
    }

    public void setStyleNameEn(String styleNameEn) {
        this.styleNameEn = styleNameEn;
    }

    public String getStyleWorkspace() {
        return styleWorkspace;
    }

    public void setStyleWorkspace(String styleWorkspace) {
        this.styleWorkspace = styleWorkspace;
    }

    public Long getStyleCreater() {
        return styleCreater;
    }

    public void setStyleCreater(Long styleCreater) {
        this.styleCreater = styleCreater;
    }

    public Date getStyleCreateTime() {
        return styleCreateTime;
    }

    public void setStyleCreateTime(Date styleCreateTime) {
        this.styleCreateTime = styleCreateTime;
    }

    public String getStyleRemark() {
        return styleRemark;
    }

    public void setStyleRemark(String styleRemark) {
        this.styleRemark = styleRemark;
    }

    public Integer getStyleFlag() {
        return styleFlag;
    }

    public void setStyleFlag(Integer styleFlag) {
        this.styleFlag = styleFlag;
    }
}