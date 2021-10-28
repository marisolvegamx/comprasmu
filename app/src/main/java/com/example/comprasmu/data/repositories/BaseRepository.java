package com.example.comprasmu.data.repositories;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;

import com.example.comprasmu.data.dao.BaseDao;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;

import java.util.List;

public abstract class BaseRepository<T> {



    public abstract LiveData<List<T>> getAll() ;
    public abstract List<T> getAllsimple() ;
    public abstract LiveData<T> find(int id);
    public abstract T findsimple(int id);

//    public LiveData<List<T>> getAll() {
//        return dao.findAll();
//    }
//
//
//    public LiveData<T> find(int id) {
//        return dao.find(id);
//    }
    public abstract   long insert(T object);

    public abstract void delete(T object);

    public abstract void insertAll(List<T> words);


}
