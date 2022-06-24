package com.epam.esm.core.model.domain;

import com.epam.esm.core.validation.CreateValidation;
import com.epam.esm.core.validation.NullOrNotBlank;
import com.epam.esm.core.validation.UpdateValidation;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


/**
 * The type Gift certificate.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
@Table(name = "gift_certificates")
public class GiftCertificate extends AbstractDaoEntity {
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime lastUpdateDate;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "gift_certificate_has_tag",
            joinColumns = @JoinColumn(name = "gift_certificate_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<@Valid Tag> tags = new HashSet<>();

    /**
     * Instantiates a new Gift certificate.
     */
    public GiftCertificate() {}

    private GiftCertificate(GiftCertificateBuilder builder) {
        super(builder.id);
        this.name = builder.name;
        this.description = builder.description;
        this.price = builder.price;
        this.duration = builder.duration;
        this.createDate = builder.createDate;
        this.lastUpdateDate = builder.lastUpdateDate;
        this.tags = builder.tags;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets price.
     *
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Sets price.
     *
     * @param price the price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public Integer getDuration() {
        return duration;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    /**
     * Gets create date.
     *
     * @return the create date
     */
    public LocalDateTime getCreateDate() {
        return createDate;
    }

    /**
     * Sets create date.
     *
     * @param createDate the create date
     */
    public void setCreateDate(LocalDateTime createDate) {
        this.createDate = createDate;
    }

    /**
     * Gets last update date.
     *
     * @return the last update date
     */
    public LocalDateTime getLastUpdateDate() {
        return lastUpdateDate;
    }

    /**
     * Sets last update date.
     *
     * @param lastUpdateDate the last update date
     */
    public void setLastUpdateDate(LocalDateTime lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    /**
     * Gets tags.
     *
     * @return the tags
     */
    public Set<Tag> getTags() {
        return this.tags;
    }

    /**
     * Sets tags.
     *
     * @param tags the tags
     */
    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GiftCertificate)) return false;
        if (!super.equals(o)) return false;

        GiftCertificate that = (GiftCertificate) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (price != null ? !price.equals(that.price) : that.price != null) return false;
        if (duration != null ? !duration.equals(that.duration) : that.duration != null) return false;
        if (createDate != null ? !createDate.equals(that.createDate) : that.createDate != null) return false;
        if (lastUpdateDate != null ? !lastUpdateDate.equals(that.lastUpdateDate) : that.lastUpdateDate != null)
            return false;
        return tags != null ? tags.equals(that.tags) : that.tags == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (createDate != null ? createDate.hashCode() : 0);
        result = 31 * result + (lastUpdateDate != null ? lastUpdateDate.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GiftCertificate{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", price=").append(price);
        sb.append(", duration=").append(duration);
        sb.append(", createDate=").append(createDate);
        sb.append(", lastUpdateDate=").append(lastUpdateDate);
        sb.append(", tags=").append(tags);
        sb.append('}');
        return sb.toString();
    }

    /**
     * The type Gift certificate builder.
     */
    public static class GiftCertificateBuilder {
        private long id;
        private String name;
        private String description;
        private BigDecimal price;
        private int duration;
        private LocalDateTime createDate;
        private LocalDateTime lastUpdateDate;
        private Set<Tag> tags = new HashSet<>();

        /**
         * Id gift certificate builder.
         *
         * @param id the id
         * @return the gift certificate builder
         */
        public GiftCertificateBuilder id(long id) {
            this.id = id;
            return this;
        }


        /**
         * Name gift certificate builder.
         *
         * @param name the name
         * @return the gift certificate builder
         */
        public GiftCertificateBuilder name(String name) {
            this.name = name;
            return this;
        }


        /**
         * Description gift certificate builder.
         *
         * @param description the description
         * @return the gift certificate builder
         */
        public GiftCertificateBuilder description(String description) {
            this.description = description;
            return this;
        }


        /**
         * Price gift certificate builder.
         *
         * @param price the price
         * @return the gift certificate builder
         */
        public GiftCertificateBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        /**
         * Duration gift certificate builder.
         *
         * @param duration the duration
         * @return the gift certificate builder
         */
        public GiftCertificateBuilder duration(int duration) {
            this.duration = duration;
            return this;
        }

        /**
         * Create date gift certificate builder.
         *
         * @param createDate the create date
         * @return the gift certificate builder
         */
        public GiftCertificateBuilder createDate(LocalDateTime createDate) {
            this.createDate = createDate;
            return this;
        }

        /**
         * Last update date gift certificate builder.
         *
         * @param lastUpdateDate the last update date
         * @return the gift certificate builder
         */
        public GiftCertificateBuilder lastUpdateDate(LocalDateTime lastUpdateDate) {
            this.lastUpdateDate = lastUpdateDate;
            return this;
        }


        /**
         * Tags gift certificate builder.
         *
         * @param tags the tags
         * @return the gift certificate builder
         */
        public GiftCertificateBuilder tags(Set<Tag> tags) {
            this.tags = tags;
            return this;
        }

        /**
         * Build gift certificate.
         *
         * @return the gift certificate
         */
        public GiftCertificate build() {
            return new GiftCertificate(this);
        }
    }
}
