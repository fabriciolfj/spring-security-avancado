package com.github.fabriciolfj.appexemplo1.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "authority")
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @JoinColumn(name = "user")
    @ManyToOne
    private User user;
}
