package com.practice.ecommerce.model.Enums;

public enum Keys {
    ORDER("order/%s"),
    PRODUCT("product/%s"),
    STOCK("stock/%s"),
    PRICE("price/%s"),
    SEARCH("search/%s"),
    CART("CART/%s"),
    WISHLIST("WISHLIST/%s"),
    USER("user/%s"),
    ETAG("etag/%s");

    private final String prefixKey;
    Keys(String key) {
        this.prefixKey =key;
    }

    public String getPrefixKey() {
        return this.prefixKey;
    }

    public static String key(Keys type, Object id) {
        return String.format(type.getPrefixKey(), id.toString());
    }
}
