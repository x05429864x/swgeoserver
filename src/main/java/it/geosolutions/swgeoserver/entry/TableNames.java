package it.geosolutions.swgeoserver.entry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel(value="表名对照类",description="表名")
public class TableNames implements Serializable {


    private static final long serialVersionUID = 42440112344509158L;

    @ApiModelProperty(value = "ID",hidden = true)
    private Long Id;

    @ApiModelProperty(value = "中文名称",dataType = "String",required = true,example = "中国")
    private String nameCn;

    @ApiModelProperty(value = "英文名称",dataType = "String",required = true,example = "china")
    private String nameEn;

    @ApiModelProperty(value = "工作区",dataType = "String")
    private String workspace;

    @ApiModelProperty(value = "存储",dataType = "String")
    private String datastore;

    @ApiModelProperty(value = "中心点",dataType = "String")
    private String center;

    @ApiModelProperty(value = "解压路径",dataType = "String")
    private String extractPath;

    @ApiModelProperty(value = "创建人",dataType = "long")
    private Long creater;

    @ApiModelProperty(value = "状态",dataType = "long",example = "0:生效 1:作废")
    private Long state;

    @ApiModelProperty(value = "创建时间",dataType = "Date")
    private Date createTime;

    @ApiModelProperty(value = "修改人",dataType = "long")
    private Long updater;

    @ApiModelProperty(value = "修改时间",dataType = "Date")
    private Date updateTime;

    @ApiModelProperty(value = "备注",dataType = "String")
    private String remark;

    @ApiModelProperty(value = "矢量/栅格",dataType = "Integer",example = "0:矢量,1:栅格")
    private Integer flag;

    @ApiModelProperty(value = "是否发布",dataType = "Integer",example = "0:未发布 1:已发布")
    private Long isPublish;

    @ApiModelProperty(value = "md5",dataType = "String")
    private String md5;

    @ApiModelProperty(value = "metadata",dataType = "String")
    private String metadata;

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

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getWorkspace() {
        return workspace;
    }

    public void setWorkspace(String workspace) {
        this.workspace = workspace;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getExtractPath() {
        return extractPath;
    }

    public void setExtractPath(String extractPath) {
        this.extractPath = extractPath;
    }

    public String getDatastore() {
        return datastore;
    }

    public void setDatastore(String datastore) {
        this.datastore = datastore;
    }

    public Long getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(Long isPublish) {
        this.isPublish = isPublish;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }
}
