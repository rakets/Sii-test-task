package com.test_task.sii.dto;

import jakarta.validation.constraints.NotBlank;

public class GymDTO {
    private Long id;
    @NotBlank(message = "Gym's name is required")
    private String name;
    @NotBlank(message = "Gym's address is required")
    private String address;
    @NotBlank(message = "Gym's phone number is required")
    private String phoneNumber;

    public GymDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
