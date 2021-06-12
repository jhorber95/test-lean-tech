package com.leantech.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDto {

    private Integer id;
    private String name;
    private String lastName;
    private String address;
    private String cellphone;
    private String cityName;
}
