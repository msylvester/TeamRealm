package com.eloquentgraffiti.mess323.realm;

import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * Created by michael on 5/15/16.
 */
public class TodoItem extends {

    @PrimaryKey
    private long id;

    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}