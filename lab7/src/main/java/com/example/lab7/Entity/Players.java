package com.example.lab7.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@Getter
@Setter

@JsonIgnoreProperties(value = {"position"})
@Table(name = "players")
public class Players {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private int PlayerId;
    @Column(name = "name")
    private String name;
    @Column(name = "mmr")
    private BigInteger mmr;
    @Column(name = "position")
    private Integer position;
    @Column(name = "region")
    private String region;

}

