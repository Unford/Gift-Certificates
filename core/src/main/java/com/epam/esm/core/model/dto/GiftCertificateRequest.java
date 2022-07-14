package com.epam.esm.core.model.dto;


import javax.validation.constraints.*;
import java.util.List;


public class GiftCertificateRequest extends PageRequestParameters {
    private List<@Size(min = 5, max = 255) @NotBlank String> tag;
    private List<@Size(min = 5, max = 255) @NotBlank String> name;
    private List<@Size(min = 5, max = 255) @NotBlank String> description;
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

    @Override
    @Pattern(regexp = "^(?:-?date|-?name|-?name, -?date|-?date, -?name)$")
    public String getSort() {
        return super.getSort();
    }

}
