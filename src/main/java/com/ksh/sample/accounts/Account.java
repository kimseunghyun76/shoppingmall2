package com.ksh.sample.accounts;

import java.util.Date;

import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Account {

    @Id @GeneratedValue
    private long id;

    @Column(unique=true)
    private String username;

    private String password;

    private String email;

    private String fullname;

    @Temporal(TemporalType.TIMESTAMP)
    private Date joined;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updated;

}
