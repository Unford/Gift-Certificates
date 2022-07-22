package com.epam.esm.core.model.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends AbstractRepositoryEntity {
    private BigDecimal cost;
    private LocalDateTime purchaseDate;
    @ManyToOne()//todo fetch type
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "order_has_gift_certificate",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "gift_certificate_id"))
    private List<GiftCertificate> giftCertificates;


    @PrePersist
    public void onPrePersist() {
        purchaseDate = LocalDateTime.now();
        cost = giftCertificates.stream().map(GiftCertificate::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<GiftCertificate> getGiftCertificates() {
        return giftCertificates;
    }

    public void setGiftCertificates(List<GiftCertificate> giftCertificates) {
        this.giftCertificates = giftCertificates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order)) return false;
        if (!super.equals(o)) return false;

        Order order = (Order) o;

        if (cost != null ? !cost.equals(order.cost) : order.cost != null) return false;
        return purchaseDate != null ? purchaseDate.equals(order.purchaseDate) : order.purchaseDate == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (cost != null ? cost.hashCode() : 0);
        result = 31 * result + (purchaseDate != null ? purchaseDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Order{");
        sb.append(", id=").append(id);
        sb.append("cost=").append(cost);
        sb.append(", purchaseDate=").append(purchaseDate);
        sb.append('}');
        return sb.toString();
    }
}
