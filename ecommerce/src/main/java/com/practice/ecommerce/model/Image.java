package com.practice.ecommerce.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "images")
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer imageId;
    @Column(nullable = false)
    public Integer productId;
    @Column(nullable = false)
    public String type;

    @Lob // large binary object
    @Column(nullable = false)
    public byte[] image;

    public Image(Integer productId, byte[] image, String type) {
        this.productId = productId;
        this.image = image;
        this.type = type;
    }

    public Image() { }

}
