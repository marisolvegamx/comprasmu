package com.example.comprasmu.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.GeocercaDao;
import com.example.comprasmu.data.dao.ReactivoDao;
import com.example.comprasmu.data.dao.TiendaDao;
import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.Reactivo;

import java.util.List;

public  class GeocercaRepositoryImpl extends BaseRepository<Geocerca> {

    private static GeocercaDao dao;
    private static GeocercaRepositoryImpl INSTANCE;


    public static GeocercaRepositoryImpl getInstance(GeocercaDao comprasdao) {
        if (INSTANCE == null) {
            dao=comprasdao;
            synchronized (GeocercaRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE=new GeocercaRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public LiveData<List<Geocerca>> getAll() {
        return dao.findAll();
    }

    @Override
    public List<Geocerca> getAllsimple() {
        return dao.findAllsimple();
    }

    @Override
    public LiveData<Geocerca> find(int id) {
        return dao.find(id);
    }

    @Override
    public Geocerca findsimple(int id) {
        return null;
    }


    public List<Geocerca> findsimplexCd(String ciudad) {
        return dao.findsimplexCd(ciudad);
    }

    @Override
    public long insert(Geocerca object) {
        return dao.insert(object);
    }

    @Override
    public void delete(Geocerca object) {
         dao.delete(object);
    }

    @Override
    public void insertAll(List<Geocerca> words) {
        dao.insertAll(words);
    }


}
