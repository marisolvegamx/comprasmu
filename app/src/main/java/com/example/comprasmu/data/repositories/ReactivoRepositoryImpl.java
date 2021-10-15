package com.example.comprasmu.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.dao.ReactivoDao;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.Reactivo;

import java.util.List;

public abstract class ReactivoRepositoryImpl  extends BaseRepository<Reactivo> {

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
    public LiveData<Reactivo> find(int id) {
        return dao.find(id);
    }




}
