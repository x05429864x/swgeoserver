package com.siweidg.swgeoserver.comm.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页请求
 */
public class PageRequest {
    /**
     * 当前页码
     */
    private static int  pageNum = 1;
    /**
     * 每页数量
     */
    private static int  pageSize = 20;

    private String order = "desc";

    private String sort = "id";

    private Map<String ,Object> params = new HashMap<>();

    public int getPageNum() {
        return pageNum;
    }
    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Map<String, Object> getParams() {
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}