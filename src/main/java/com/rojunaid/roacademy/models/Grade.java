package com.rojunaid.roacademy.models;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
public class Grade extends Auditable {

    @NotBlank
    @NotEmpty
    @Size(min = 3, max = 100, message = "Please give name between 3 to 100 characters.")
    private String name;

    public Grade() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
