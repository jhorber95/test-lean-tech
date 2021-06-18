package com.leantech.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Builder
@Table(name = "employees")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Employee implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Integer id;

    @ManyToOne
    private Person person;

    @ManyToOne
    private Position position;
    private Double salary;
}
