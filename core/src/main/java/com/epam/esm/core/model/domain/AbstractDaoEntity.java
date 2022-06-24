package com.epam.esm.core.model.domain;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Positive;

/**
 * The type Abstract dao entity.
 */
@MappedSuperclass
public abstract class AbstractDaoEntity {
    /**
     * The Id.
     */
    @Positive
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    /**
     * Instantiates a new Abstract dao entity.
     *
     * @param id the id
     */
    AbstractDaoEntity(long id) {
        this.id = id;
    }

    /**
     * Instantiates a new Abstract dao entity.
     */
    AbstractDaoEntity() {

    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractDaoEntity)) return false;

        AbstractDaoEntity that = (AbstractDaoEntity) o;

        return id != null ? id.equals(that.id) : that.id == null;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AbstractDaoEntity{");
        sb.append("id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}
