package com.model;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

/**
 * Model for staff resource
 */
public class StaffResource {
    private int page;
    private int totalPage;
    private int perPage;
    private int total;
    private List<StaffModel> data;

    public List<StaffModel> getData() {
        return data;
    }

    public void setData(List<StaffModel> data) {
        this.data = data;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPerPage() {
        return perPage;
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
