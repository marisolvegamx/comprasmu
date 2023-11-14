package com.example.comprasmu.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.BaseDao;
import com.example.comprasmu.data.dao.InformeCompraDao;
import com.example.comprasmu.data.dao.VisitaDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.modelos.VisitaWithInformes;
import com.example.comprasmu.utils.Constantes;


import java.util.ArrayList;
import java.util.List;

public class VisitaRepositoryImpl  extends BaseRepository<Visita>{
    private static VisitaRepositoryImpl sInstance;
    private final VisitaDao icDao;
    private LiveData<List<Visita>> allVisita;

    public VisitaRepositoryImpl(Context context) {

        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        icDao = comprasDataBase.getVisitaDao();
     }
    /*public static VisitaRepositoryImpl getsInstance() {
        if (sInstance == null) {
            sInstance = new VisitaRepositoryImpl();
        }
        return sInstance;
    }*/

        //traigo solo los abiertos
    public LiveData<List<Visita>> getSearchResults (String indice, String nombretienda, String ciudad) {
        String query="Select * from visitas " +
                "where (estatus=1 or estatus=3)";
        ArrayList<String> filtros=new ArrayList<String>();
        if(indice!=null&&!indice.equals("")) {
            query += " and indice=?";
            filtros.add(indice);
        }
        if(nombretienda!=null&&!nombretienda.equals("")) {
            query = query + " and nombretienda like '%?%'";
            filtros.add(nombretienda);
        }
        if(ciudad!=null&&!ciudad.equals("")) {
            query = query + " and ciudad like '%?%'";
            filtros.add(ciudad);
        }

        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                filtros.toArray());

        return icDao.getVisitaByFiltros(sqlquery);
    }

    public LiveData<List<VisitaWithInformes>> getInformesVisitas (String indice, String nombretienda, String ciudad, int planta, int cliente) {
        String query="SELECT visista.*" +
                "FROM informe_compras, visitas " +
                "            WHERE informe_compras.visitasId = visitas.id ";
        ArrayList<String> filtros=new ArrayList<String>();
        if(indice!=null&&!indice.equals("")) {
            query += " and visitas.indice=?";
            filtros.add(indice);
        }
        if(nombretienda!=null&&!nombretienda.equals("")) {
            query = query + " and visita.tiendaNombre like '%?%'";
            filtros.add(nombretienda);
        }
        if(ciudad!=null&&!ciudad.equals("")) {
            query = query + " and visita.ciudad like '%?%'";
            filtros.add(ciudad);
        }
        if(planta>0) {
            query = query + " and plantasId = ?";
            filtros.add(planta+"");
        }
        if(cliente>0) {
            query = query + " and clientesId = ?";
            filtros.add(cliente+"");
        }
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                filtros.toArray());

        return icDao.getVisitaWithInformesByFiltros(sqlquery);
    }

    public void actualizarEstatus(int id, int estatus) {

        icDao.actualizarEstatus(id,estatus);
    }

    public void deleteVisitaByIndice(String indice) {
        icDao.deleteVisitaByIndice(indice);
    }

    public LiveData<VisitaWithInformes> getVisitaWithInformeById(int id) {
        return icDao.getVisitaWithInformesById(id);
    }


    public List<VisitaWithInformes> getVisitaWithInformesByIndice(String indice) {
        return icDao.getVisitasWithInformesByIndice(indice);
    }
    public List<VisitaWithInformes> getVisitaWithInformesByIndice2(String indice) {
        return icDao.getVisitasWithInformesByIndice2(indice);
    }
    public List<VisitaWithInformes> getVisitaWithInformesByIndicePend(String indice) {
        return icDao.getVisitasWithInformesByIndicePend(indice);
    }
    public List<Visita> getVisitaPendSubir(String indice) {
        return icDao.getVisitasxEst( indice,0);
    }
    public List<Visita> getVisitasxIndice(String indice) {
        return icDao.getVisitasxIndice( indice);
    }
    public LiveData<List<Visita>> getInformesByIndice(String indice) {
        return icDao.findAllByIndice(indice);
    }

   public int getUltimo(String indice){
       int ultimo=  icDao.getUltimoId(indice);
       return ultimo;
   }
    public int getSiguienteId(String indice) {
        int siguiente=1;

        int ultimo=  icDao.getUltimoId(indice);
        if(ultimo>0){
            siguiente=ultimo+1;
        }
        return  siguiente;
    }

    @Override
    public LiveData<List<Visita>> getAll() {
        return  icDao.findAll();
    }

    @Override
    public List<Visita> getAllsimple() {
        return icDao.findAllsimple();
    }

    @Override
    public LiveData<Visita> find(int id) {
        return icDao.find(id);
    }

    @Override
    public Visita findsimple(int id) {
        return icDao.findsimple(id);
    }

  /*  public Visita findsimpleest(int id, int estatus) {
        return icDao.findsimpleest(id,estatus);
    }*/

    @Override
    public long insert(Visita object)
    {
        //busco el consecutivo x ciudad
        if(object.getConsecutivocd()==0) {
            int consecutivocd = icDao.getUltimoConsCd(object.getIndice(), object.getCiudad());
            consecutivocd = consecutivocd + 1;
            object.setConsecutivocd(consecutivocd);
        }
            return icDao.insert(object);
    }

    @Override
    public void delete(Visita object) {
            icDao.delete(object);
        }



    @Override
    public void insertAll(List<Visita> objects) {
        icDao.insertAll(objects);
    }

    public void actualizarEstatusSync(int id, int estatus) {

        icDao.actualizarEstatusSync(id, estatus);
    }

}
