package com.example.comprasmu.data.dao;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.modelos.Configuracion;
import com.example.comprasmu.data.modelos.Sustitucion;
import com.example.comprasmu.data.repositories.BaseRepository;

import java.util.ArrayList;
import java.util.List;

public class ConfiguracionRepositoryImpl extends BaseRepository<Configuracion> {

    private final ConfiguracionDao dao;



    public ConfiguracionRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=comprasDataBase.getConfiguracionDao();
    }



    public LiveData<List<Configuracion>> getAll() {
      return dao.findAll();
    }

    @Override
    public List<Configuracion> getAllsimple() {
        return dao.getAllSencillo();
    }

    //todo no tengo
    @Override
    public LiveData<Configuracion> find(int id) {
        return null;
    }
    //todo no tengo
    @Override
    public Configuracion findsimple(int id) {
        return null;
    }


    public Configuracion findsimple(String clave) {
        return dao.findsimple(clave);
    }


    public LiveData<Configuracion> findByClave(String clave) {
        return dao.findByClave(clave);
    }



    public void deleteAll(){
     dao.deleteAll();
    }


    public void insertAll(List<Configuracion> objects) {
        dao.insertAll(objects);
    }


    public long insert(Configuracion object) {
        return dao.insert(object);
    }

    @Override
    public void delete(Configuracion object) {

    }

}
