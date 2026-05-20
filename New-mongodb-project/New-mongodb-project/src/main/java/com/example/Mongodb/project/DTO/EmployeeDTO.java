package com.example.Mongodb.project.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class EmployeeDTO {


    private String Id;
    public String name;
    @Min(value = 18, message = "Age must be greater than 18")
    private Integer age;
    @NotBlank(message = "Address can not be empty")
    private String address;

    public Integer getAge() {
        return age;
    }


    public void setAge(Integer age) {
        this.age = age;
    }

    public @NotBlank() String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public EmployeeDTO() {
    }

    public EmployeeDTO(String address, String Id, Integer age) {
        this.address = address;
        this.age = age;
        this.Id = Id;
    }


}
