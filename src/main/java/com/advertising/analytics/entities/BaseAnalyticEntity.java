package com.advertising.analytics.entities;

import java.io.Serializable;
import java.util.Calendar;

public interface BaseAnalyticEntity extends Serializable {

    /**
     * @return The primary key.
     */
    long getId();

    /**
     * @return The date the Entity was first created.
     */
    Calendar getDateCreated();

    /**
     * @return The date the Entity was more recently updated.
     */
    Calendar getDateUpdated();
}
