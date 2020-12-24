package com.github.fabriciolfj.appexemplo1.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String username;
    private String password;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Authority> authorities;
    @Enumerated(EnumType.STRING)
    private EncryptionAlgorithm algorithm;
}
