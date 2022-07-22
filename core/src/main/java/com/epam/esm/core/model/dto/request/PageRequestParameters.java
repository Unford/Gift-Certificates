package com.epam.esm.core.model.dto.request;


import javax.validation.constraints.Positive;


public class PageRequestParameters {
    @Positive
    private int page = 1;
    @Positive
    private int size = 10;

    private String sort;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

}
