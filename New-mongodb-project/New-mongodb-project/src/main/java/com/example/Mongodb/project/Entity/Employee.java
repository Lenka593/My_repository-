package com.example.Mongodb.project.Entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document(collection = "employees")
public class Employee {
    @Id
    private String Id;
    private String name;
    private Integer age;
    private String address;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(Id, employee.Id) && Objects.equals(name, employee.name) && Objects.equals(age, employee.age) && Objects.equals(address, employee.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id, name, age, address);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "address='" + address + '\'' +
                ", Id='" + Id + '\'' +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public Integer getAge() {
        return age;
    }

    public String getId() {
        return Id;
    }
}
