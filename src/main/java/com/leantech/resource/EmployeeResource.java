package com.leantech.resource;

import com.leantech.resource.error.BadRequestAlertException;
import com.leantech.service.EmployeeService;
import com.leantech.service.dto.EmployeeDto;
import com.leantech.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmployeeResource {

    private static final String EMPLOYEE_PATH = "/employees";
    private final EmployeeService service;


    @GetMapping(EMPLOYEE_PATH)
    public ResponseEntity<List<EmployeeDto>> getAll(Pageable pageable, @RequestParam(value = "filter", required = false, defaultValue = "") String filter) {
        Page<EmployeeDto> page = service.findByCriteria(pageable, filter);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }


    @PostMapping(EMPLOYEE_PATH)
    public ResponseEntity<EmployeeDto> create(@RequestBody EmployeeDto dto) {
        if (dto.getId() != null) {
            throw new BadRequestAlertException("Employee already has id");
        }
        if (dto.getPerson() != null && dto.getPersonId() == null) {
            throw new BadRequestAlertException("Employee with person data");
        }
        return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED);
    }

    @PutMapping(EMPLOYEE_PATH)
    public ResponseEntity<EmployeeDto> update(@RequestBody EmployeeDto dto) {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Employee doesn't has id");
        }
        return new ResponseEntity<>(service.save(dto), HttpStatus.OK);
    }

    @DeleteMapping(EMPLOYEE_PATH + "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

}
