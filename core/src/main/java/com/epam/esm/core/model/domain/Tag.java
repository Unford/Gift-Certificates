package com.epam.esm.core.model.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * It's an entity class that represents a tag
 */
@Entity
@Table(name = "tags")
public class Tag extends AbstractRepositoryEntity {

    @Column(unique = true)
    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<GiftCertificate> giftCertificates;

    /**
     * Instantiates a new Tag.
     */
    public Tag(){}

    /**
     * Instantiates a new Tag.
     *
     * @param id   the id
     * @param name the name
     */
    public Tag(Long id, String name){
        super(id);
        this.name = name;
    }

    public Tag(String name){
        this.name = name;
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

    public Set<GiftCertificate> getGiftCertificates() {
        return giftCertificates;
    }

    public void setGiftCertificates(Set<GiftCertificate> giftCertificates) {
        this.giftCertificates = giftCertificates;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tag{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;
        if (!super.equals(o)) return false;

        Tag tag = (Tag) o;

        return name != null ? name.equals(tag.name) : tag.name == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
