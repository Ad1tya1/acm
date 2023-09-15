package com.span.acm.dto;

import lombok.Data;

import java.util.Set;

@Data
public class EmployeeDTO {
    private String name;
    private Set<Long> moduleIds;
}
