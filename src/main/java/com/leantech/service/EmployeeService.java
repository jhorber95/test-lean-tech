package com.leantech.service;

import com.leantech.domain.Employee;
import com.leantech.repository.EmployeeRepository;
import com.leantech.service.dto.EmployeeDto;
import com.leantech.service.mapper.EmployeeMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    public EmployeeDto save(EmployeeDto dto) {
        Employee entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public Optional<EmployeeDto> findOne(Integer id) {
        return repository.findById(id)
                .map(mapper::toDto);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
