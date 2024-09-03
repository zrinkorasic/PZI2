package org.example.Models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Orders {

    @Id
    @GeneratedValue
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    private User user;

    @NotBlank(message = "Polje je obvezno.")
    private String address;

    @NotBlank(message = "Polje je obvezno.")
    private String number;
    private float totalPrice;

    public Orders(){

    }
    public Orders(Long id, User user, String address, String number, float totalPrice) {
        this.id = id;
        this.user = user;
        this.address = address;
        this.number = number;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }
}
