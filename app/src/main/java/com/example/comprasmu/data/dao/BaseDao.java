package com.example.comprasmu.data.dao;


import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;

import java.util.List;


public abstract class BaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insert(T object);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<T> objects);

    @Delete
    public abstract void delete(T object);



}
