package com.model;

import org.springframework.boot.context.properties.bind.DefaultValue;

import javax.validation.constraints.Min;

public class PaginationModel {

    private Integer page;
    private Integer perPage;
    private String sortBy;
    private String sortType;

    public PaginationModel() {
    }

    public PaginationModel(Integer page, Integer perPage, String sortBy, String sortType) {
        this.page = page;
        this.perPage = perPage;
        this.sortBy = sortBy;
        this.sortType = sortType;
    }


    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }
}
