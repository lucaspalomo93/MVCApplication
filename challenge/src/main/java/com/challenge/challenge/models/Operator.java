package com.challenge.challenge.models;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "operators")
public class Operator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name cannot be Empty")
    @Pattern(regexp = "^[A-Za-z]*$", message = "Cannot contain numbers")
    private String name;

    @NotEmpty(message = "Surname cannot be empty")
    @Pattern(regexp = "^[A-Za-z]*$", message = "Cannot contain numbers")
    private String surname;

    @NotEmpty(message = "Username cannot be empty")
    @Column(unique = true)
    @Pattern(regexp = "^[A-Za-z]\\w{1,29}$", message = "Must start with a letter and have at least 2 character")
    private String userName;

    @NotEmpty(message = "Password cannot be empty")
    private String password;

    private int status;

    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date creationDate = new Date(System.currentTimeMillis());

    @Column(name = "last_login_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLoginDate;

    @OneToMany(cascade = CascadeType.MERGE)
    @JoinColumn(name = "id_usuario")
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private List<Role> roles;

}
