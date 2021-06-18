package com.leantech.service;

import com.leantech.domain.Employee;
import com.leantech.domain.Person;
import com.leantech.domain.Position;
import com.leantech.repository.EmployeeRepository;
import com.leantech.service.dto.EmployeeDto;
import com.leantech.service.dto.PersonDto;
import com.leantech.service.mapper.EmployeeMapper;
import io.github.perplexhub.rsql.RSQLJPASupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository repository;
    private final EmployeeMapper mapper;

    private final PersonService personService;

    public Page<EmployeeDto> findByCriteria(Pageable pageable, String filter) {
        Specification<Employee> specification = Specification.where(null);
        if (filter != null && !filter.isEmpty()) {
            specification = RSQLJPASupport.toSpecification(filter, buildPropertyPathMapper());
        }

        return repository.findAll(specification, pageable)
                .map(mapper::toDto);
    }

    public EmployeeDto save(EmployeeDto dto) {
        if (dto.getPerson() != null) {
            PersonDto person = personService.save(dto.getPerson());
            dto.setPersonId(person.getId());
        }
        Employee entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public Optional<EmployeeDto> findOne(Integer id) {
        return repository.findById(id)
                .map(mapper::toDto);
    }

    public List<EmployeeDto> getByPosition(Integer positionId) {
        return repository.findByPosition(Position.builder().id(positionId).build())
                .stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        repository.deleteById(id);
    }

    private Map<String, String> buildPropertyPathMapper() {
        Map<String, String> propertyPathMapper = new HashMap<>();
        propertyPathMapper.put("positionName", "position.name");
        propertyPathMapper.put("positionId", "position.id");
        propertyPathMapper.put("personId", "person.id");
        propertyPathMapper.put("personName", "person.name");
        propertyPathMapper.put("personLastName", "person.lastName");
        return propertyPathMapper;
    }
}
