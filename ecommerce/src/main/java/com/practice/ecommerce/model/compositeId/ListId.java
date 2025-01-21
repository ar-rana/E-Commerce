package com.practice.ecommerce.model.compositeId;

import com.practice.ecommerce.model.Enums.ListType;

public class ListId {

    private String identifier;
    private ListType listType;

    public ListId() { }

    public ListId(String identifier, ListType listType) {
        this.identifier = identifier;
        this.listType = listType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public ListType getListType() {
        return listType;
    }

    public void setListType(ListType listType) {
        this.listType = listType;
    }
}
