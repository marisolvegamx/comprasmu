package com.example.comprasmu.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.dao.ReactivoDao;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.Reactivo;

import java.util.List;

public  class ReactivoRepositoryImpl  extends BaseRepository<Reactivo> {

    private ReactivoDao dao;

    private LiveData<List<Reactivo>> allReactivo;

    public ReactivoRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=comprasDataBase.getReactivoDao();
    }

    @Override
    public LiveData<List<Reactivo>> getAll() {
        return dao.findAll();
    }

    @Override
    public List<Reactivo> getAllsimple() {
        return dao.findAllsimple();
    }

    @Override
    public LiveData<Reactivo> find(int id) {
        return dao.find(id);
    }
    public LiveData<Reactivo> findAnterior(int id) {
        return dao.find(id);
    }
    @Override
    public Reactivo findsimple(int id) {
        return dao.findsimple(id);
    }
    public Reactivo findByNombre(String nombre, int cliente) {
        return dao.findByNombre(nombre, cliente);
    }
    public List<Reactivo> getEmp(int cliente) {
        Log.e("si es auqi","eee");
        return dao.getEmp(cliente);
    }
    @Override
    public long insert(Reactivo object) {
        return dao.insert(object);
    }

    @Override
    public void delete(Reactivo object) {
         dao.delete(object);
    }

    @Override
    public void insertAll(List<Reactivo> words) {
        dao.insertAll(words);
    }


}
