package com.example.comprasmu.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.ComprasDataBase;

import com.example.comprasmu.data.dao.TablaVersionesDao;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.TablaVersiones;


import java.util.List;

public class TablaVersionesRepImpl extends BaseRepository<TablaVersiones> {

    private TablaVersionesDao dao;

    private LiveData<List<InformeCompra>> allInformeCompra;

    public TablaVersionesRepImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=comprasDataBase.getTableVersionesDao();
    }

    @Override
    public LiveData<List<TablaVersiones>> getAll() {
      return dao.findAll();
    }

    @Override
    public LiveData<TablaVersiones> find(int id) {
        return dao.find(id);
    }



    @Override
    public void delete(TablaVersiones object) {
        dao.delete(object);
    }

    @Override
    public void insertAll(List<TablaVersiones> objects) {
        dao.insertAll(objects);
    }


    public LiveData<TablaVersiones> getVersionByNombreTabla(String nombre) {
        return dao.getVersionByNombreTabla(nombre);
    }
    @Override
    public long insert(TablaVersiones object) {
        return dao.insert(object);

    }



}
