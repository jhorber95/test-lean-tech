package com.leantech.service.mapper;

import com.leantech.domain.Position;
import com.leantech.service.dto.PositionDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PositionMapper extends EntityMapper<PositionDto, Position>{

    default Position fromId(Integer id) {
        if (id == null) {
            return null;
        }
        return Position.builder().id(id).build();
    }
}
