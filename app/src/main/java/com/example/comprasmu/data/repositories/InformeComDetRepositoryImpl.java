package com.example.comprasmu.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.R;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.InformeCompraDetDao;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;


import java.util.HashMap;
import java.util.List;

public  class InformeComDetRepositoryImpl extends BaseRepository<InformeCompraDetalle> {

    private InformeCompraDetDao dao;

    private LiveData<List<InformeCompraDetalle>> allInformeCompraDetalle;

    public InformeComDetRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=comprasDataBase.getInformeCompraDetDao();
    }



    public LiveData<List<InformeCompraDetalle>> getAll(int idInforme) {
        return dao.findAll(idInforme);
    }
    public List<InformeCompraDetalle> getAllSencillo(int idInforme) {
        return dao.getAllSencillo(idInforme);
    }
    public List<Integer> getInformesWithImagen(int idInforme) {
        return dao.getInformesWithImagen(idInforme);
    }


    public void cancelAll(int idInforme) {
         dao.cancelAll(idInforme);
    }


    public List<InformeCompraDetalle> getByProductoAna(String indice, int planta,int producto, int analisis, int presentacion, String tamanio ) {
        return dao.getByProductoAna(indice, planta,producto,analisis,presentacion,tamanio);
    }

    @Override
    public LiveData<List<InformeCompraDetalle>> getAll() {
        return null;
    }

    @Override
    public List<InformeCompraDetalle> getAllsimple() {
        return null;
    }

    @Override
    public LiveData<InformeCompraDetalle> find(int id) {
        return dao.find(id);
    }

    @Override
    public InformeCompraDetalle findsimple(int id) {
        return dao.findsimple(id);
    }


    public InformeCompraDetalle findByCompra(int idcompra, int iddet) {
        return dao.findByCompra(idcompra,iddet);
    }
    public List<InformeCompraDetalle> findByCompraBu(int idcompra, int iddet) {
        return dao.findByCompraBu(idcompra,iddet);
    }
    @Override
    public long insert(InformeCompraDetalle object) {
        return dao.insert(object);
    }


    @Override
    public void delete(InformeCompraDetalle object) {
    dao.delete(object);
    }
    public void deleteByInforme(int idInforme) {
        dao.deleteByInforme(idInforme);
    }

    @Override
    public void insertAll(List<InformeCompraDetalle> objects) {
    dao.insertAll(objects);
    }

    public void actualizarEstatusSync(int id, int estatus) {

        dao.actualizarEstatusSync(id, estatus);
    }
    public void actualizarEstatusSyncxInfo(int idinfo, int estatus) {

        dao.actualizarEstatusSync(idinfo, estatus);
    }

}

