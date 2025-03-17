package com.springcloud.company.company.entity;

import lombok.Getter;

@Getter
public enum CompanyType {
    SUPPLIER("공급업체"),
    RECEIVINGCOMPANY("수령업체");

    private final String description;

    CompanyType(String description) {this.description = description; }

}
