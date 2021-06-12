package com.leantech.service;

import com.leantech.domain.Person;
import com.leantech.repository.PersonRepository;
import com.leantech.service.dto.PersonDto;
import com.leantech.service.mapper.PersonMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PersonService {

    private final PersonRepository repository;
    private final PersonMapper mapper;

    public PersonDto save(PersonDto dto) {
        Person entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public Optional<PersonDto> findOne(Integer id) {
        return repository.findById(id)
                .map(mapper::toDto);
    }

    public void delete(Integer id){
        repository.deleteById(id);
    }
}
