package com.epam.esm.core.model.dto;

import javax.validation.constraints.Positive;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public class OrderDto implements Serializable {
    @Positive
    private Long id;
    @Positive
    private BigDecimal cost;
    private LocalDateTime purchaseDate;

    public OrderDto() {
    }

    public OrderDto(Long id, BigDecimal cost, LocalDateTime purchaseDate) {
        this.id = id;
        this.cost = cost;
        this.purchaseDate = purchaseDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public LocalDateTime getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDateTime purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDto entity = (OrderDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.cost, entity.cost) &&
                Objects.equals(this.purchaseDate, entity.purchaseDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cost, purchaseDate);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "cost = " + cost + ", " +
                "purchaseDate = " + purchaseDate + ")";
    }
}
