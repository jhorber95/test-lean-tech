package com.leantech.service;

import com.leantech.domain.Position;
import com.leantech.repository.PositionRepository;
import com.leantech.service.dto.PositionDto;
import com.leantech.service.mapper.PositionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PositionService {

    private final PositionRepository repository;
    private final PositionMapper mapper;

    public PositionDto save(PositionDto dto){
        Position entity = mapper.toEntity(dto);
        return mapper.toDto(repository.save(entity));
    }

    public Page<PositionDto> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::toDto);
    }

    public Optional<PositionDto> findOne(Integer id) {
        return repository.findById(id)
                .map(mapper::toDto);
    }

}
