package com.advertising.analytics.entities;

import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "product_data")
public class ProductData extends AnalyticEntity {

    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne
    @JoinColumn(name = "provider_id")
    private Provider provider;

    @Column(name = "clicks")
    private long clicks;

    @Column(name = "date_recorded")
    @Temporal(TemporalType.DATE)
    private Calendar dateRecorded;

    public ProductData() {

    }

    public ProductData(Provider provider, Product product, long clicks, Calendar dateRecorded) {
        this.provider = provider;
        this.product = product;
        this.clicks = clicks;
        this.dateRecorded = dateRecorded;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Provider getProvider() {
        return provider;
    }

    public void setProvider(Provider provider) {
        this.provider = provider;
    }

    public long getClicks() {
        return clicks;
    }

    public void setClicks(long clicks) {
        this.clicks = clicks;
    }

    public Calendar getDateRecorded() {
        return dateRecorded;
    }

    public void setDateRecorded(Calendar dateRecorded) {
        this.dateRecorded = dateRecorded;
    }
}
