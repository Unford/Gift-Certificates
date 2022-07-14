package com.epam.esm.core.model.dto;


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

// todo   public Optional<Sort> toOrderList() {
//        Optional<Sort> result = Optional.empty();
//        if (sort != null && !sort.isEmpty()) {
//            List<Sort.Order> orders = new ArrayList<>();
//            Arrays.stream(sort.split(",\\s*")).forEach((s -> {
//                Sort.Direction direction = Sort.Direction.ASC;
//                s.startsWith("-");
//                String property = "";
//                orders.add(new Sort.Order(direction, property));
//            }));
//            result = Optional.of(Sort.by(orders));
//        }
//        return result;
//    }
}
