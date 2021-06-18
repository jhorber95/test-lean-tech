package com.leantech.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@Table(name = "position")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Position {

    @Id
    @GeneratedValue
    private Integer id;
    private String name;
}
