package com.example.comprasmu.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.comprasmu.R;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.InformeCompraDao;
import com.example.comprasmu.data.dao.InformeCompraDetDao;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public  class InformeComDetRepositoryImpl extends BaseRepository<InformeCompraDetalle> {

    private final InformeCompraDetDao dao;

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
    public List<InformeCompraDetalle> getByProducto(String indice, int planta,int producto, int presentacion, String tamanio ) {
        return dao.getByProducto(indice, planta,producto,presentacion,tamanio);
    }
    public List<InformeCompraDetalle> getByProductoAnaPen(String indice, int planta,int producto, int analisis, int presentacion, String tamanio, String siglas ) {

        return dao.getByProductoAnaSig(indice, planta,producto,analisis,presentacion,tamanio, siglas);
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


    public List<InformeCompraDetalle> findByCompra(int idcompra, int iddet) {
        return dao.findByCompra(idcompra,iddet);
    }

    public List<InformeCompraDetalle> findByCompra(int idcompra) {
        return dao.findByCompra(idcompra);
    }
    public InformeCompraDetalle findByInformeFoto(int informeid, int fotoatra) {
        return dao.findByInformeAtra(informeid,fotoatra);
    }

    public List<InformeCompraDetalle> findByCompraBu(int idcompra, int iddet) {
        return dao.findByCompraBu(idcompra,iddet);
    }
    public InformeCompraDetalle getInformeByFoto(int informeid, int numfoto, int descripcionId ) {

        String query="select * from informe_detalle where informesId=? ";
        switch (descripcionId) {
            // case 2: query="and foto_atributoa=?";
            //     break;

            case 7:
                query =query+ "and etiqueta_evaluacion=?";
                break;
            case 9:
                query =query+ "and foto_codigo_produccion=?";
                break;
        }
        ArrayList<Integer> filtros=new ArrayList<Integer>();
        filtros.add(informeid);
        filtros.add(numfoto);

        Object[] params=filtros.toArray();

        for(int i=0;i<params.length;i++)
            Log.d("InformeComDetRepoImpl","***"+params[i]);
        Log.d("InformeComDetRepoImpl","****"+query);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,filtros.toArray()
        );

        return dao.getInfCompraDetByFiltros(sqlquery);
    }


    public InformeCompraDetalle getByqr(String qr) {
        return dao.getByqr(qr);
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
    public void deleteAll() {
        dao.deleteAll();
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
    public void actualizarEstatus(int idinfo, int estatus) {

        dao.actualizarEstatus(idinfo, estatus);
    }
    public LiveData<List<InformeCompraDetalle>> getCancelados(String indice) {

        return dao.getByEstatus2(indice,2);
    }

    public List<InformeCompraDetalle> getCanceladosSim(String indice) {

        return dao.getByEstatussimpl(indice,2);
    }
    public LiveData<List<InformeCompraDao.InformeCompravisita>> getCanceladosVis(String indice) {
        return dao.getCancel(indice,2);
    }
    public MutableLiveData<Integer> gettotCancelados(String indice) {
        MutableLiveData<Integer> valor=new MutableLiveData<>();
        List<InformeCompraDetalle> cancel= dao.getByEstatussimpl(indice,2);
        if(cancel!=null)
            valor.setValue( cancel.size());
       return valor;
    }
    public List<InformeCompraDetalle> getDetallePendSubir(String indice) {
        return dao.getByEstatus(indice,0);
    }
    public InformeCompraDetalle findByQr(String qr, String indice) {
        return dao.findByQr(qr,indice);
    }

    public InformeCompraDetalle getCancelda(int comprasId, int comprasDetId, int estatus) {
        return dao.findByCompraEst( comprasId, comprasDetId);
    }

    public int getTotalMuesxPlan(int plantaSel, String indiceactual) {
        List<InformeCompraDetalle> muestras= dao.getInformesxPlanta(plantaSel,indiceactual);
        if(muestras!=null)
            return muestras.size();
        return 0;
    }

    public int getTotalMuesxCliCd(int clienteSel, String indiceactual, String cd) {
        List<InformeCompraDetalle> muestras= dao.getInformesxCliCd(cd,clienteSel,indiceactual);
        if(muestras!=null)
            return muestras.size();
        return 0;
    }


}

