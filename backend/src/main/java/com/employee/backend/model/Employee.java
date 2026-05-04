package com.employee.backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Employee {

    private Long id;

    @NotNull
    @JsonProperty("employee_id")
    @JsonAlias("employeeId")
    private Long employeeId;

    @NotNull
    private String name;

    @Email
    @NotNull
    private String email;

    @JsonProperty("profile_picture")
    @JsonAlias("profilePicture")
    private String profilePicture;

    @JsonProperty("mobile")
    @JsonAlias("mobile")
    private String mobile;

    @JsonProperty("dob")
    @JsonAlias("dob")
    private LocalDate dob;

    @JsonProperty("gender")
    @JsonAlias("gender")
    private String gender;

    private String department;
    private String role;
    private Status status;

    @JsonProperty("hire_date")
    @JsonAlias("hireDate")
    private LocalDate hireDate;

    @JsonProperty("last_update")
    @JsonAlias("lastUpdate")
    private LocalDateTime lastUpdate;

    // Standard Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getProfilePicture() { return profilePicture; }
    public void setProfilePicture(String profilePicture) { this.profilePicture = profilePicture; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public LocalDate getDob() { return dob; }
    public void setDob(LocalDate dob) { this.dob = dob; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public LocalDate getHireDate() { return hireDate; }
    public void setHireDate(LocalDate hireDate) { this.hireDate = hireDate; }

    public LocalDateTime getLastUpdate() { return lastUpdate; }
    public void setLastUpdate(LocalDateTime lastUpdate) { this.lastUpdate = lastUpdate; }
}
