package com.advertising.analytics.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"date_created", "date_updated"}, allowGetters = true)
public abstract class AnalyticEntity implements BaseAnalyticEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_created", nullable = false, updatable = false)
    @CreatedDate
    public Calendar dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_updated", nullable = false)
    @LastModifiedDate
    public Calendar dateUpdated;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public Calendar getDateCreated() {
        return dateCreated;
    }

    @Override
    public Calendar getDateUpdated() {
        return dateUpdated;
    }
}
