package com.hadi.inspire.Components;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.io.Serializable;

@Entity
public class Quote implements Serializable {

    @PrimaryKey
    @NonNull
    private String id;


    @ColumnInfo(name = "quote")
    private String quote;


    @ColumnInfo(name = "author")
    private String author;

    @ColumnInfo(name = "position")
    private int position;

    public Quote() {
    }

    public Quote(@NonNull String id, String quote, String author) {
        this.id = id;
        this.quote = quote;
        this.author = author;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    private boolean isSelected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

//    @NonNull
//    @Override
//    public String toString() {
//        return quote.substring(0,5)+" "+author.substring(0,5);
//    }
}
