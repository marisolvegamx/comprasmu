package com.example.comprasmu.data.repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.InformeEnvioDetDao;

import com.example.comprasmu.data.modelos.InformeEnvioDet;
import com.example.comprasmu.data.modelos.InformeEnvioPaq;

import java.util.ArrayList;
import java.util.List;

public  class InformeEnvioRepositoryImpl extends BaseRepository<InformeEnvioDet> {

    private final InformeEnvioDetDao dao;

    private LiveData<List<InformeEnvioPaq>> allInformeEnvio;

    public InformeEnvioRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=comprasDataBase.getInformeEnvioDetDao();
    }


    @Override
    public LiveData<List<InformeEnvioDet>> getAll() {
        return null;
    }

    @Override
    public List<InformeEnvioDet> getAllsimple() {
        return null;
    }

    @Override
    public LiveData<InformeEnvioDet> find(int id) {
        return dao.find(id);
    }

    @Override
    public InformeEnvioDet findsimple(int id) {
        return dao.findSimple(id);
    }
    public InformeEnvioPaq findInfsimple(int id) {
       return dao.getInformeEnviosimple(id);
      // return null;
    }

    @Override
    public long insert(InformeEnvioDet object) {
        return dao.insert(object);
    }


    @Override
    public void delete(InformeEnvioDet object) {
    dao.delete(object);
    }
    public void deleteById(int idInforme) {
        dao.deleteById(idInforme);
    }

    @Override
    public void insertAll(List<InformeEnvioDet> objects) {
    dao.insertAll(objects);
    }

    public void actualizarEstatusSync(int id, int estatus) {

        dao.actualizarEstatusSync(id, estatus);
    }

    public void actualizarEstatus(int idinfo, int estatus) {

        dao.actualizarEstatus(idinfo, estatus);
    }
  /*  public LiveData<List<InformeEnvioDet>> getCancelados(String indice) {

        return dao.getByEstatus(indice,2);
    }*/


}

