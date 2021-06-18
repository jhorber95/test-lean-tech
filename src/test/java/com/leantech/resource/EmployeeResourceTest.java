package com.leantech.resource;

import com.leantech.LeanTechGradleApplication;
import com.leantech.domain.Employee;
import com.leantech.domain.Person;
import com.leantech.domain.Position;
import com.leantech.repository.EmployeeRepository;
import com.leantech.service.dto.EmployeeDto;
import com.leantech.service.dto.PositionDto;
import com.leantech.service.mapper.EmployeeMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = LeanTechGradleApplication.class)
@AutoConfigureMockMvc
public class EmployeeResourceTest {


    private static final Double DEFAULT_SALARY = 1D;
    private static final Double UPDATE_SALARY = 2D;

    @Autowired
    private EmployeeRepository repository;

    @Autowired
    private EmployeeMapper mapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMockMvc;

    private Employee employee;

    static Employee createEntity(EntityManager em) {
        Employee employee = Employee.builder()
                .salary(DEFAULT_SALARY)
                .build();

        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceTest.createEntity(em);
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }

        employee.setPerson(person);

        Position position;

        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            position = PositionResourceTest.createEntity(em);
            em.persist(position);
            em.flush();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }

        employee.setPosition(position);
        return employee;
    }

    static Employee createUpdateEntity(EntityManager em) {
        Employee employee = Employee.builder()
                .salary(UPDATE_SALARY)
                .build();

        Person person;
        if (TestUtil.findAll(em, Person.class).isEmpty()) {
            person = PersonResourceTest.createUpdateEntity(em);
            em.persist(person);
            em.flush();
        } else {
            person = TestUtil.findAll(em, Person.class).get(0);
        }

        employee.setPerson(person);

        Position position;
        if (TestUtil.findAll(em, Position.class).isEmpty()) {
            position = PositionResourceTest.createUpdateEntity(em);
            em.persist(position);
            em.flush();
        } else {
            position = TestUtil.findAll(em, Position.class).get(0);
        }

        employee.setPosition(position);

        return employee;
    }

    @BeforeEach
    void initTest() {
        employee = createEntity(em);
    }

    @Test
    @Transactional
    void createEmployee() throws Exception{

        int databaseSizeBeforeCreate = repository.findAll().size();

        EmployeeDto employeeDto = mapper.toDto(employee);

        restMockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(employeeDto)))
                .andExpect(status().isCreated());

        // Validate the Position in the database
        List<Employee> list = repository.findAll();
        assertThat(list).hasSize(databaseSizeBeforeCreate + 1);
        Employee position1 = list.get(list.size() - 1);
        assertThat(position1.getSalary()).isEqualTo(DEFAULT_SALARY);
    }

    @Test
    @Transactional
    void createEmployeeWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = repository.findAll().size();

        employee.setId(1);

        EmployeeDto employeeDto = mapper.toDto(employee);

        restMockMvc.perform(post("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(employeeDto)))
                .andExpect(status().isBadRequest());

        // Validate the Employee in the database
        List<Employee> list = repository.findAll();
        assertThat(list).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void updateEmployee() throws Exception{
        repository.saveAndFlush(employee);

        int databaseSizeBeforeUpdate = repository.findAll().size();

        Employee updateEmployee = repository.getById(employee.getId());

        em.detach(updateEmployee);

        updateEmployee.setSalary(UPDATE_SALARY);

        EmployeeDto dto = mapper.toDto(updateEmployee);

        restMockMvc.perform(put("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(dto)))
                .andExpect(status().isOk());

        List<Employee> list = repository.findAll();
        assertThat(list).hasSize(databaseSizeBeforeUpdate);
        Employee position = list.get(list.size() - 1);
        assertThat(position.getSalary()).isEqualTo(UPDATE_SALARY);
    }

    @Test
    @Transactional
    void updateNonExistingEmployee() throws Exception {
        int databaseSizeBeforeUpdate = repository.findAll().size();

        // Create the EmployeeDto
        EmployeeDto positionDto = mapper.toDto(employee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMockMvc.perform(put("/api/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(positionDto)))
                .andExpect(status().isBadRequest());

        // Validate the PositionDto in the database
        List<Employee> list = repository.findAll();
        assertThat(list).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmployee() throws Exception {
        // Initialize the database
        repository.saveAndFlush(employee);

        int databaseSizeBeforeDelete = repository.findAll().size();

        // Delete the Employee
        restMockMvc.perform(delete("/api/employees/{id}", employee.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Employee> list = repository.findAll();
        assertThat(list).hasSize(databaseSizeBeforeDelete - 1);
    }


}
