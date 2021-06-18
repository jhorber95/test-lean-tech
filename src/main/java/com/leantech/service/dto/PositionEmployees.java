package com.leantech.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PositionEmployees {
    private Integer id;
    private String name;
    private List<EmployeeDto> employees;
}
