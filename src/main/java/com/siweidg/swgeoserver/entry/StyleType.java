package com.siweidg.swgeoserver.entry;

import io.swagger.annotations.ApiModel;

import java.io.Serializable;

@ApiModel(value="工作样式",description="工作样式类")
public class StyleType implements Serializable {
    private Long id;

    private Long dtype;

    private String linetype;

    private String lineopacity;

    private String linewidth;

    private String linecolor;

    private String fillopacity;

    private String fillcolor;

    private String styleTypeName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDtype() {
        return dtype;
    }

    public void setDtype(Long dtype) {
        this.dtype = dtype;
    }

    public String getLinetype() {
        return linetype;
    }

    public void setLinetype(String linetype) {
        this.linetype = linetype == null ? null : linetype.trim();
    }

    public String getLineopacity() {
        return lineopacity;
    }

    public void setLineopacity(String lineopacity) {
        this.lineopacity = lineopacity == null ? null : lineopacity.trim();
    }

    public String getLinewidth() {
        return linewidth;
    }

    public void setLinewidth(String linewidth) {
        this.linewidth = linewidth;
    }

    public String getLinecolor() {
        return linecolor;
    }

    public void setLinecolor(String linecolor) {
        this.linecolor = linecolor == null ? null : linecolor.trim();
    }

    public String getFillopacity() {
        return fillopacity;
    }

    public void setFillopacity(String fillopacity) {
        this.fillopacity = fillopacity == null ? null : fillopacity.trim();
    }

    public String getFillcolor() {
        return fillcolor;
    }

    public void setFillcolor(String fillcolor) {
        this.fillcolor = fillcolor == null ? null : fillcolor.trim();
    }

    public String getStyleTypeName() {
        return styleTypeName;
    }

    public void setStyleTypeName(String styleTypeName) {
        this.styleTypeName = styleTypeName;
    }
}