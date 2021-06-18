package com.leantech.resource;

import com.leantech.resource.error.BadRequestAlertException;
import com.leantech.service.PositionService;
import com.leantech.service.dto.PositionDto;
import com.leantech.service.dto.PositionEmployees;
import com.leantech.util.HeaderUtil;
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
public class PositionResource {

    private final PositionService service;

    @GetMapping("/positions")
    public ResponseEntity<List<PositionDto>> getAll(Pageable pageable) {
        Page<PositionDto> page = service.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @PostMapping("/positions")
    public ResponseEntity<PositionDto> save(@RequestBody PositionDto dto) {
        if (dto.getId() != null) {
            throw new BadRequestAlertException("Position already has id");
        }
        return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED);
    }

    @PutMapping("/positions")
    public ResponseEntity<PositionDto> update(@RequestBody PositionDto dto) {
        if (dto.getId() == null) {
            throw new BadRequestAlertException("Invalid id");
        }
        return new ResponseEntity<>(service.save(dto), HttpStatus.OK);
    }

    @GetMapping("/positions-employees")
    public ResponseEntity<List<PositionEmployees>> getPositionWithEmployees() {
        List<PositionEmployees> page = service.getWithEmployees();
        return ResponseEntity.ok().body(page);
    }

    @DeleteMapping("/positions/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert("Lean", true, "Position", id.toString())).build();

    }

}
