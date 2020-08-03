package it.geosolutions.swgeoserver.entry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value="表名对照类",description="表名")
public class TableNames {

    @ApiModelProperty(value = "ID",hidden = true)
    private Long Id;

    @ApiModelProperty(value = "中文名称",dataType = "String",required = true,example = "中国")
    private String nameCn;

    @ApiModelProperty(value = "英文名称",dataType = "String",required = true,example = "china")
    private String nameEn;

    @ApiModelProperty(value = "创建人",dataType = "long")
    private Long creater;

    @ApiModelProperty(value = "创建时间",dataType = "Date")
    private Date createTime;

    @ApiModelProperty(value = "修改人",dataType = "long")
    private Long updater;

    @ApiModelProperty(value = "修改时间",dataType = "Date")
    private Date updateTime;

    @ApiModelProperty(value = "状态",dataType = "long")
    private Long state;

    @ApiModelProperty(value = "备注",dataType = "String")
    private String remark;

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getNameCn() {
        return nameCn;
    }

    public void setNameCn(String nameCn) {
        this.nameCn = nameCn;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public Long getCreater() {
        return creater;
    }

    public void setCreater(Long creater) {
        this.creater = creater;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdater() {
        return updater;
    }

    public void setUpdater(Long updater) {
        this.updater = updater;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getState() {
        return state;
    }

    public void setState(Long state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
