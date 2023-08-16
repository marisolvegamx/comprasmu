package com.example.comprasmu.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Query;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.CorreccionDao;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.SolicitudWithCor;

import java.util.ArrayList;
import java.util.List;

public class CorreccionRepoImpl extends BaseRepository<Correccion> {


    private final CorreccionDao icDao;

    public CorreccionRepoImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        icDao = comprasDataBase.getCorreccionDao();

    }
    public CorreccionDao getDao(){
        return icDao;
    }

    @Override
    public LiveData<List<Correccion>> getAll() {
        return null;
    }

    @Override
    public List<Correccion> getAllsimple() {
        return null;
    }

    public LiveData<List<SolicitudWithCor>> getAllxEtaPlan(int plantaSel, String indice, int etapa) {
        String query="Select * from solicitud_cor  " +
                "inner join correccion on correccion.solicitudId=solicitud_cor.id" +
                " and correccion.numfoto=solicitud_cor.numfoto" +
                " where solicitud_cor.indice=?";
        ArrayList<String> filtros=new ArrayList<String>();
        filtros.add(indice);
        query = query + " and plantasId=?";
        filtros.add(plantaSel+"");
        query = query + " and etapa = ?";
        filtros.add(etapa+"");

        Object[] params=filtros.toArray();

      //  for(int i=0;i<params.length;i++)
      //      Log.d("CorreccionRepoImpl","***"+params[i]);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                filtros.toArray());
        return icDao.getCorreSolByFiltros( sqlquery);
    }
    public LiveData<List<SolicitudWithCor>> getAllxEta( String indice, int etapa) {
        String query="Select * from solicitud_cor  " +
                "inner join correccion on correccion.solicitudId=solicitud_cor.id" +
                " and correccion.numfoto=solicitud_cor.numfoto" +
                " where solicitud_cor.indice=?";
        ArrayList<String> filtros=new ArrayList<String>();
        filtros.add(indice);
        query = query + " and etapa = ?";
        filtros.add(etapa+"");

        Object[] params=filtros.toArray();

        //  for(int i=0;i<params.length;i++)
        //      Log.d("CorreccionRepoImpl","***"+params[i]);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                filtros.toArray());
        return icDao.getCorreSolByFiltros( sqlquery);
    }




    public LiveData<SolicitudWithCor> findSolCor(int id) {
        String query="SELECT * FROM solicitud_cor inner join correccion on correccion.solicitudId=solicitud_cor.id" +
                "     and correccion.numfoto=solicitud_cor.numfoto  WHERE correccion.id =?";
        ArrayList<String> filtros=new ArrayList<String>();
        filtros.add(id+"");

        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                filtros.toArray());
        Log.d("CorreccionRepoImpl",query);
        return icDao.getCorreSolByFiltro( sqlquery);
    }
    public List<Correccion> getAllsimple(int etapa) {
        return icDao.getCorreccionesSimple(etapa);
    }

    @Override
    public LiveData<Correccion> find(int id) {
        return icDao.find(id);
    }

    @Override
    public Correccion findsimple(int id) {
        return icDao.findSimple(id);
    }


    public LiveData<List<Correccion>> getxsol(int idsol, String indice) {
        return icDao.tgetCorrecciones(idsol, indice );
    }

    @Override
    public long insert(Correccion newCorreccion) {
        return icDao.insert(newCorreccion);
    }

    @Override
    public void delete(Correccion object) {
        icDao.delete(object);
    }

    public void  insertAll(List<Correccion> newCorreccion) {
         icDao.insertAll(newCorreccion);
    }

    public long getUltimo(String indice) {
        return icDao.getUltimoId(indice);
    }


    public void actualizarEstatus(int id, int estatus) {

         icDao.actualizarEstatus(id, estatus);
    }


    public void actualizarEstatusSync(int id, int estatus) {

        icDao.actualizarEstatusSync(id, estatus);
    }



    public void deleteCorreccion(int id) {
       icDao.deleteCorreccion( id);
    }


    public void deleteByIndice(String indice) {
        icDao.deleteByIndice(indice);
    }
    public LiveData<List<Correccion>> getByIndice(String indice){
        return icDao.getByIndice(indice);
    }
}
