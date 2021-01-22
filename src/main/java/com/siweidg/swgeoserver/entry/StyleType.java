package com.siweidg.swgeoserver.entry;

public class StyleType {
    private Long id;

    private Long dtype;

    private String linetype;

    private String lineopacity;

    private String linewidth;

    private String linecolor;

    private String fillopacity;

    private String fillcolor;

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
}