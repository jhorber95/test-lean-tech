package com.leantech.service.mapper;

import com.leantech.domain.Person;
import com.leantech.service.dto.PersonDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PersonMapper extends EntityMapper<PersonDto, Person>{

    default Person fromId(Integer id) {
        if (id == null) {
            return null;
        }
        return Person.builder().id(id).build();
    }
}
