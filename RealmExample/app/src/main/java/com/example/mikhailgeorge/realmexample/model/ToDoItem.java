package com.example.mikhailgeorge.realmexample.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by mikhailgeorge on 5/20/16.
 */
public class ToDoItem extends RealmObject {
    @PrimaryKey
    private long id;

    private String description;
    private String url;

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

    public String getURL() {
        return this.url;
    }

    public void setURL(String url) {
        this.url = url;
    }

}
