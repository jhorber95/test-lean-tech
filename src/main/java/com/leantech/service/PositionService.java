package com.leantech.service;

import com.leantech.domain.Position;
import com.leantech.repository.PositionRepository;
import com.leantech.service.dto.EmployeeDto;
import com.leantech.service.dto.PositionDto;
import com.leantech.service.dto.PositionEmployees;
import com.leantech.service.mapper.PositionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository repository;
    private final EmployeeService employeeService;
    private final PositionMapper mapper;

    public PositionDto save(PositionDto dto){
        Position entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public Page<PositionDto> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDto);
    }

    public List<PositionEmployees> getWithEmployees() {
        return repository.findAll().stream()
                .map(position ->  {

                    List<EmployeeDto> employees = employeeService.getByPosition(position.getId())
                            .stream()
                            .sorted((o1, o2) -> o2.getSalary().compareTo(o1.getSalary()))
                            .collect(Collectors.toList());

                    PositionEmployees data = mapper.toDtoPositionEmployees(position);

                    data.setEmployees(employees);
                    return data;
                }).collect(Collectors.toList());
    }

    public Optional<PositionDto> findOne(Integer id) {
        return repository.findById(id)
                .map(mapper::toDto);
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
