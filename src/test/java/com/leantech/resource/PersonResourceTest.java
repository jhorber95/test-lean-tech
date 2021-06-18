package com.leantech.resource;


import com.leantech.LeanTechGradleApplication;
import com.leantech.domain.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;

@SpringBootTest(classes = LeanTechGradleApplication.class)
@AutoConfigureMockMvc
public class PersonResourceTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LASTNAME = "XXXX";
    private static final String UPDATED_LASTNAME = "RRRR";

    @Autowired
    private EntityManager em;

    private Person person;

    static Person createEntity(EntityManager em) {
        return Person.builder()
                .name(DEFAULT_NAME)
                .lastname(DEFAULT_LASTNAME)
                .build();
    }

    static Person createUpdateEntity(EntityManager em) {
        return Person.builder()
                .name(UPDATED_NAME)
                .lastname(UPDATED_LASTNAME)
                .build();
    }
}
