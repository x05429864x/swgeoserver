package it.geosolutions.swgeoserver.entry;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(value="模型实体",description="模型")
public class Entity {

    @ApiModelProperty(value = "工作区名称",dataType = "String")
    private String workSpace;

    @ApiModelProperty(value = "数据存储名称",dataType = "String")
    private String dataStore;

    @ApiModelProperty(value = "图层名称",dataType = "String")
    private String layer;

    @ApiModelProperty(value = "图层中文名称",dataType = "String")
    private String layerCn;

    public String getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(String workSpace) {
        this.workSpace = workSpace;
    }

    public String getDataStore() {
        return dataStore;
    }

    public void setDataStore(String dataStore) {
        this.dataStore = dataStore;
    }

    public String getLayer() {
        return layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public String getLayerCn() {
        return layerCn;
    }

    public void setLayerCn(String layerCn) {
        this.layerCn = layerCn;
    }
}
