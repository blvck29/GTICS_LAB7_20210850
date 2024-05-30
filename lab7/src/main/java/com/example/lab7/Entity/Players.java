package com.example.lab7.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Entity
@Getter
@Setter

@Table(name = "players")
public class Players {

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
