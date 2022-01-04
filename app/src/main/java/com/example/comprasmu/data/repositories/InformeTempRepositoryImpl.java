package com.example.comprasmu.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.comprasmu.data.ComprasDataBase;

import com.example.comprasmu.data.dao.InformeTempDao;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.InformeWithDetalle;

import java.util.ArrayList;
import java.util.List;

public class InformeTempRepositoryImpl  extends BaseRepository<InformeTemp> {


    private InformeTempDao icDao;
    private LiveData<List<InformeTemp>> allInformeTemp;

    public InformeTempRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        icDao = comprasDataBase.getInformeTempDao();

    }
    public InformeTempDao getDao(){
        return icDao;
    }





    @Override
    public LiveData<List<InformeTemp>> getAll() {
        return icDao.getInformes();
    }

    public List<InformeTemp> getAllByTabla(String tabla) {
        return icDao.getInformesByTabla(tabla);
    }

    @Override
    public List<InformeTemp> getAllsimple() {
        return icDao.getInformessimple();
    }

    public List<InformeTemp> getProductoSel() {
        return icDao.getProductoSel();
    }

    @Override
    public LiveData<InformeTemp> find(int id) {
        return icDao.getInforme(id);
    }

    @Override
    public InformeTemp findsimple(int id) {
        return icDao.getInformesimple(id);
    }

    public InformeTemp findByNombre(String campo) {
        return icDao.getInformesByNombre(campo);
    }
    public InformeTemp getUltimo(boolean pregunta) {
        return icDao.getUltimo(pregunta);
    }

    @Override
    public long insert(InformeTemp object) {
        return icDao.insert(object);
    }

    @Override
    public void delete(InformeTemp object) {
        icDao.delete(object);
    }

    public void deleteAll() {
        icDao.deleteAll();
    }
    public void deleteMenosCliente() {
        icDao.deleteMenosCliente();
    }
    @Override
    public void insertAll(List<InformeTemp> words) {
        icDao.insertAll(words);
    }



}
