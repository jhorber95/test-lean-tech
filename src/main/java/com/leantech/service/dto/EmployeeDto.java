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
    private String personName;
    private String personLastName;
    private Integer positionId;
    private String positionName;
    private Double salary;

    private PersonDto person;

}
