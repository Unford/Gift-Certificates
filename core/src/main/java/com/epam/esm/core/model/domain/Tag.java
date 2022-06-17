package com.epam.esm.core.model.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * The type Tag.
 */
public class Tag extends AbstractDaoEntity{
    @NotBlank
    @Size(max = 255, min = 5)
    private String name;

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
    public Tag(long id, String name){
        super(id);
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Tag{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
