package com.epam.esm.core.model.dto;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;


public class GiftCertificateRequest extends PageRequest {
    private List<@Size(min = 5, max = 255) @NotBlank String> tag;
    private List<@Size(min = 5, max = 255) @NotBlank String> name;
    private List<@Size(min = 5, max = 255) @NotBlank String> description;
    @Pattern(regexp = "^(?:-?date|-?name|-?name, -?date|-?date, -?name)$")
    private String sort;


    public List<String> getTag() {
        return tag;
    }

    public void setTag(List<String> tag) {
        this.tag = tag;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
