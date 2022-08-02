package com.epam.esm.core.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * It's a DTO class for the Order entity
 */
@Relation(collectionRelation = "orders", itemRelation = "order")
public class OrderDto extends RepresentationModel<OrderDto> {
    @Positive
    private Long id;
    @Positive
    private BigDecimal cost;
    private LocalDateTime purchaseDate;

    @JsonIgnore
    private Long userId;


    public OrderDto() {
    }

    public OrderDto(Long id, BigDecimal cost, LocalDateTime purchaseDate) {
        this(id, cost, purchaseDate, null);
    }

    public OrderDto(Long id, BigDecimal cost, LocalDateTime purchaseDate, Long userId) {
        this.id = id;
        this.cost = cost;
        this.purchaseDate = purchaseDate;
        this.userId = userId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
