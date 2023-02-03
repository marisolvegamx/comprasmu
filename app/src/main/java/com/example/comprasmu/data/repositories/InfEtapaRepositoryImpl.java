package com.example.comprasmu.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.InformeEtapaDao;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;

import java.util.ArrayList;
import java.util.List;

public class InfEtapaRepositoryImpl extends BaseRepository<InformeEtapa> {


    private final InformeEtapaDao icDao;
    private LiveData<List<InformeEtapa>> allInformeEtapa;

    public InfEtapaRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        icDao = comprasDataBase.getInformeEtapaDao();

    }
    public InformeEtapaDao getDao(){
        return icDao;
    }

    @Override
    public LiveData<List<InformeEtapa>> getAll() {
        return null;
    }

    @Override
    public List<InformeEtapa> getAllsimple() {
        return null;
    }

    public LiveData<List<InformeEtapa>> getAll(int etapa, String indice, int plantaid) {
        return icDao.getInformes(etapa, indice,plantaid);
    }
    public LiveData<List<InformeEtapa>> getAllsp(int etapa, String indice) {
        return icDao.getInformesAll(etapa, indice);
    }


    public List<InformeEtapa> getAllsimple(int etapa) {
        return icDao.getInformesSimple(etapa);
    }

    @Override
    public LiveData<InformeEtapa> find(int id) {
        return icDao.find(id);
    }

    @Override
    public InformeEtapa findsimple(int id) {
        return icDao.findSimple(id);
    }

    @Override
    public long insert(InformeEtapa newInformeEtapa) {
        return icDao.insert(newInformeEtapa);
    }

    @Override
    public void delete(InformeEtapa object) {
        icDao.delete(object);
    }
    public void deleteByIndice(String indice) {
        icDao.deleteByIndice(indice);
    }

    public void  insertAll(List<InformeEtapa> newInformeEtapa) {
         icDao.insertAll(newInformeEtapa);
    }

    public long getUltimo(String indice) {
        return icDao.getUltimoId( indice);
    }


    public void actualizarEstatus(int id, int estatus) {

         icDao.actualizarEstatus(id, estatus);
    }
    public void actualizarComentariosPrep(int id, String comentarios) {

        icDao.actualizarComentariosPrep(id, comentarios);
    }
    public void actualizarcomentariosEtiq(int id, String comentarios) {

        icDao.actualizarComentariosEtiq(id, comentarios);
    }
    public void actualizarComentariosEmp(int id, String comentarios) {

        icDao.actualizarComentariosEmp(id, comentarios);
    }
    public void actualizarMuestrasEtiq(int id, int totmuestras) {

        icDao.actualizarMuestrasEtiq(id, totmuestras);
    }
    public void actualizarEstatusSync(int id, int estatus) {

        icDao.actualizarEstatusSync(id, estatus);
    }

    public List<InformeEtapa> getInformesPendSubir(String indice) {

        return icDao.getxEstatusSync(indice, 0);
    }



    public void deleteInformeEtapa(int id) {
       icDao.deleteInforme( id);
    }

    public LiveData<InformeEtapa> getInformeEtapa(int id) {
        return icDao.getInforme(id);
    }



    public InformeEtapa getInformePend(String indice,int etapa) {
        List<InformeEtapa> lista=icDao.getInformePendSimp(indice,etapa);
        if(lista!=null&&lista.size()>0){
            return lista.get(0);
        }
        return null;
    }

    public LiveData<List<InformeEtapa>> getInformesxEstatus(String indice,int etapa, int estatus) {
        return icDao.getInformesxEstatus(indice,etapa,estatus);


    }
    public LiveData<List<InformeEtapa>> getInformesPend(String indice,int etapa) {
       return icDao.getInformePend(indice,etapa);


    }
    public InformeEtapa getInformexPlan(String indice,int etapa, int planta) {
        return icDao.getInformexPlant(etapa,indice, planta);


    }
    public List<InformeEtapa> getPlantasconInf(String indice) {
        return icDao.getPlantasconInf(indice);
    }


}
