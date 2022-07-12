package com.epam.esm.core.model.dto;

import javax.validation.constraints.Positive;

public class PageRequest {
    @Positive
    private int page = 1;
    @Positive
    private int size = 10;

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
}
