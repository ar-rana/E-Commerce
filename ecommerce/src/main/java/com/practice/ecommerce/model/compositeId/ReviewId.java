package com.practice.ecommerce.model.compositeId;

public class ReviewId {
    private String identifier;
    private Integer productId;

    public ReviewId() { }

    public ReviewId(Integer productId, String identifier) {
        this.productId = productId;
        this.identifier = identifier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }
}
