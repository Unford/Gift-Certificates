package com.epam.esm.core.model.dto;

import com.epam.esm.core.validation.CreateValidation;
import com.epam.esm.core.validation.NullOrNotBlank;
import com.epam.esm.core.validation.UpdateValidation;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;


public class GiftCertificateDto implements Serializable {
    @Positive
    private Long id;
    @NotBlank(groups = {CreateValidation.class})
    @NullOrNotBlank(groups = {UpdateValidation.class})
    @Size(max = 255, min = 5)
    private String name;
    @Size(max = 255, min = 5)
    private String description;
    @Positive
    private BigDecimal price;
    @Positive
    private Integer duration;
    private LocalDateTime createDate;
    private LocalDateTime lastUpdateDate;
    private Set<@Valid TagDto> tags;

    public GiftCertificateDto() {
    }

    public GiftCertificateDto(Long id, String name, String description, BigDecimal price,
                              Integer duration, LocalDateTime createDate,
                              LocalDateTime lastUpdateDate, Set<TagDto> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public LocalDateTime getCreateDate() {
        return createDate;
    }

    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Set<TagDto> getTags() {
        return tags;
    }

    public void setTags(Set<TagDto> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificateDto entity = (GiftCertificateDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.price, entity.price) &&
                Objects.equals(this.duration, entity.duration) &&
                Objects.equals(this.createDate, entity.createDate) &&
                Objects.equals(this.lastUpdateDate, entity.lastUpdateDate) &&
                Objects.equals(this.tags, entity.tags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, createDate, lastUpdateDate, tags);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "price = " + price + ", " +
                "duration = " + duration + ", " +
                "createDate = " + createDate + ", " +
                "lastUpdateDate = " + lastUpdateDate + ", " +
                "tags = " + tags + ")";
    }
}
