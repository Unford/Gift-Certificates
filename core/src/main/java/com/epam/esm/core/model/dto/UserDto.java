package com.epam.esm.core.model.dto;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * It's a DTO class for the User entity
 */
@Relation(collectionRelation = "users", itemRelation = "user")
public class UserDto extends RepresentationModel<UserDto> {
    @Positive
    private Long id;
    @NotBlank
    @Size(max = 255, min = 5)
    private String name;
    @NotBlank
    @Size(max = 255, min = 5)
    private String login;

    public UserDto() {
    }

    public UserDto(Long id, String name, String login) {
        this.id = id;
        this.name = name;
        this.login = login;
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

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto entity = (UserDto) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.login, entity.login);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, login);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "login = " + login + ")";
    }
}
