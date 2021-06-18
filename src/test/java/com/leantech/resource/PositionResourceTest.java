package com.leantech.resource;

import com.leantech.LeanTechGradleApplication;
import com.leantech.domain.Position;
import com.leantech.repository.PositionRepository;
import com.leantech.service.dto.PositionDto;
import com.leantech.service.mapper.PositionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LeanTechGradleApplication.class)
@AutoConfigureMockMvc
class PositionResourceTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private PositionMapper positionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMockMvc;

    private Position position;


    static Position createEntity(EntityManager em) {
        return Position.builder()
                .name(DEFAULT_NAME)
                .build();
    }

    static Position createUpdateEntity(EntityManager em) {
        return Position.builder()
                .name(UPDATED_NAME)
                .build();
    }

    @BeforeEach
    public void initTest() {
        position = createEntity(em);
    }


    @Test
    @Transactional
    void createPosition() throws Exception {
        int databaseSizeBeforeCreate = positionRepository.findAll().size();
        // Create the Position
        PositionDto positionDto = positionMapper.toDto(position);
        restMockMvc.perform(post("/api/positions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(positionDto)))
                .andExpect(status().isCreated());

        // Validate the Position in the database
        List<Position> lis = positionRepository.findAll();
        assertThat(lis).hasSize(databaseSizeBeforeCreate + 1);
        Position position1 = lis.get(lis.size() - 1);
        assertThat(position1.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createPositionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = positionRepository.findAll().size();

        // Create the Position with an existing ID
        position.setId(1);
        PositionDto dto = positionMapper.toDto(position);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMockMvc.perform(post("/api/positions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(dto)))
                .andExpect(status().isBadRequest());

        // Validate the Position in the database
        List<Position> list = positionRepository.findAll();
        assertThat(list).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void updatePosition() throws Exception {
        positionRepository.saveAndFlush(position);

        int databaseSizeBeforeUpdate = positionRepository.findAll().size();

        Position updatePosition = positionRepository.getById(position.getId());

        em.detach(updatePosition);

        updatePosition.setName(UPDATED_NAME);

        PositionDto dto = positionMapper.toDto(updatePosition);

        restMockMvc.perform(put("/api/positions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(dto)))
                .andExpect(status().isOk());

        List<Position> list = positionRepository.findAll();
        assertThat(list).hasSize(databaseSizeBeforeUpdate);
        Position position = list.get(list.size() - 1);
        assertThat(position.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void updateNonExistingPosition() throws Exception {
        int databaseSizeBeforeUpdate = positionRepository.findAll().size();

        // Create the PositionDto
        PositionDto positionDto = positionMapper.toDto(position);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMockMvc.perform(put("/api/positions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(positionDto)))
                .andExpect(status().isBadRequest());

        // Validate the PositionDto in the database
        List<Position> list = positionRepository.findAll();
        assertThat(list).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePosition() throws Exception {
        // Initialize the database
        positionRepository.saveAndFlush(position);

        int databaseSizeBeforeDelete = positionRepository.findAll().size();

        // Delete the Position
        restMockMvc.perform(delete("/api/positions/{id}", position.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Position> list = positionRepository.findAll();
        assertThat(list).hasSize(databaseSizeBeforeDelete - 1);
    }
}
