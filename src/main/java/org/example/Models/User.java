package org.example.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.HashSet;
import java.util.Set;

@Entity
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank(message = "Polje ime je obvezno.")
    @Size(min=3,message = "Polje ime mora imati minimalno 3 znaka.")
    private String firstName;

    @NotBlank(message = "Polje prezime je obvezno.")
    @Size(min=3,message = "Polje prezime mora imati minimalno 3 znaka.")
    private String lastName;

    @NotBlank(message = "Polje email je obvezno.")
    @Email(message = "Email mora biti ispravnog formata.")
    private String email;

    @NotBlank(message = "Polje lozinka je obvezno.")
    @Size(min=6,message = "Polje lozinka mora imati minimalno 6 znakova.")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    Set<Role> roles = new HashSet<>();

    public User(){
    }

    public User(Long id, String firstName, String lastName, String email, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.roles = new HashSet<>();
        this.roles.add(Role.USER);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public boolean isAdmin(){
        return roles.contains(Role.ADMIN);
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


}
