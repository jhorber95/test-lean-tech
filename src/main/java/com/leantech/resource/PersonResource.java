package com.leantech.resource;

import com.leantech.resource.error.BadRequestAlertException;
import com.leantech.service.PersonService;
import com.leantech.service.dto.PersonDto;
import com.leantech.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PersonResource {

    private final PersonService service;

    @GetMapping("/persons")
    public ResponseEntity<List<PersonDto>> getAll(Pageable pageable) {
        Page<PersonDto> page = service.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/persons")
    public ResponseEntity<PersonDto> save(@RequestBody PersonDto personDto) {
        if (personDto.getId() != null) {
            throw new BadRequestAlertException("Person already has id");
        }
        PersonDto page = service.save(personDto);
        return new ResponseEntity<>(page, HttpStatus.CREATED);
    }
}
