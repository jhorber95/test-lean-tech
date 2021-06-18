package com.leantech.service.mapper;

import com.leantech.domain.Employee;
import com.leantech.domain.Position;
import com.leantech.service.dto.PositionDto;
import com.leantech.service.dto.PositionEmployees;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {EmployeeMapper.class, PersonMapper.class})
public interface PositionMapper extends EntityMapper<PositionDto, Position>{

    PositionEmployees toDtoPositionEmployees(Position position);

    default Position fromId(Integer id) {
        if (id == null) {
            return null;
        }
        return Position.builder().id(id).build();
    }
}
