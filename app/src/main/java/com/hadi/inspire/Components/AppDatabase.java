package com.hadi.inspire.Components;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Quote.class}, version = 1 , exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract QuoteDao quoteDao();
}