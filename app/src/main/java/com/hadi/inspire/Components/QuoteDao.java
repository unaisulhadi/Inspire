package com.hadi.inspire.Components;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface QuoteDao {

    @Query("SELECT * FROM quote")
    Flowable<List<Quote>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Quote quote);

    @Delete
    void delete(Quote quote);

    @Update
    void update(Quote quote);

}
