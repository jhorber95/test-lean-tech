package com.leantech.service.mapper;

import com.leantech.domain.Employee;
import com.leantech.service.dto.EmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {PersonMapper.class, PositionMapper.class})
public interface EmployeeMapper extends EntityMapper<EmployeeDto, Employee> {

    @Mapping(target = "personId", source = "person.id")
    @Mapping(target = "personName", source = "person.name")
    @Mapping(target = "positionId", source = "position.id")
    @Mapping(target = "positionName", source = "position.name")
    EmployeeDto toDto(Employee entity);

    @Mapping(target = "person", source = "personId")
    @Mapping(target = "position", source = "positionId")
    Employee toEntity(EmployeeDto dto);

    default Employee fromId(Integer id) {
        if (id == null) {
            return null;
        }
        return Employee.builder().id(id).build();
    }
}
