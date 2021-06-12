package com.leantech.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    private Integer id;
    private Integer personId;
    private Integer personName;
    private Integer personLastName;
    private Integer positionId;
    private Integer positionName;
    private Double salary;
}
