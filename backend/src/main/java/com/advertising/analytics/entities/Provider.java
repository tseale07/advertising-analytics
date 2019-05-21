package com.advertising.analytics.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "provider")
public class Provider extends AnalyticEntity {

    @Column(name = "name")
    private String name;

    public Provider() {

    }

    public Provider(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
