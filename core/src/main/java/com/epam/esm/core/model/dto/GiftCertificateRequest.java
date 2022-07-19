package com.epam.esm.core.model.dto;


import com.epam.esm.core.dao.specification.SearchOperation;
import com.epam.esm.core.validation.NullOrNotBlank;

import javax.validation.constraints.*;
import java.util.List;


public class GiftCertificateRequest extends PageRequestParameters {
    private @Pattern(regexp = "^((!?in)|(eq)|(lk)):[^:]+") @Size(min = 5, max = 255)
    @NullOrNotBlank String tag;
    private List<@Pattern(regexp = "^!?((in)|(eq)|(lk)):[^:,]+") @Size(min = 5, max = 255)
    @NotBlank String> name;
    private List<@Pattern(regexp = "^!?((in)|(eq)|(lk)):[^:,]+") @Size(min = 5, max = 255)
    @NotBlank String> description;

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
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
