package com.example.comprasmu.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.InformeCompraDetDao;
import com.example.comprasmu.data.dao.InformeEtapaDetDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;

import java.util.List;

public  class InfEtapaDetRepoImpl extends BaseRepository<InformeEtapaDet> {

    private final InformeEtapaDetDao dao;



    public InfEtapaDetRepoImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=comprasDataBase.getInformeEtapaDetDao();
    }

    public LiveData<List<InformeEtapaDet>> getAll(int idInforme) {
        return dao.findAll(idInforme);
    }
    public LiveData<List<InformeEtapaDet>> getAllxEtapa(int idInforme, int etapa) {
        return dao.getAllxEtapa(idInforme, etapa);
    }
    public List<InformeEtapaDet> getAllSencillo(int idInforme) {
        return dao.getAllSencillo(idInforme);
    }

    @Override
    public LiveData<List<InformeEtapaDet>> getAll() {
        return null;
    }

    @Override
    public List<InformeEtapaDet> getAllsimple() {
        return null;
    }

    @Override
    public LiveData<InformeEtapaDet> find(int id) {
        return dao.find(id);
    }

    public LiveData<InformeEtapaDet> getByDescripcion(String descripcion, int idinf) {
        return dao.getByDescripcion(idinf,descripcion);
    }


    public LiveData<InformeEtapaDet> getByDescripcionCaja(String descripcion, int idinf, int caja) {
        return dao.getByDescripcionCaja(idinf,descripcion, caja);
    }
    public List<InformeEtapaDet> getByCaja( int idinf, int etapa, int numcaja) {
        return dao.getByCaja(idinf,etapa, numcaja);
    }
    public LiveData<InformeEtapaDet> getBynumfoto( int idinf, int etapa, int numfoto) {
        return dao.getBynumfoto(idinf,etapa, numfoto);
    }

    public LiveData<List<ImagenDetalle>> getImagenxInf(int idinf, int etapa) {
        return dao.getImagenxInf(idinf,etapa);
    }
    public void deleteByCaja( int idinf, int etapa, int numcaja) {
         dao.deleteByCaja(idinf,etapa, numcaja);
    }

    @Override
    public InformeEtapaDet findsimple(int id) {
        return dao.findsimple(id);
    }


    @Override
    public long insert(InformeEtapaDet object) {
        return dao.insert(object);
    }


    @Override
    public void delete(InformeEtapaDet object) {
    dao.delete(object);
    }
    //elimina todos los de 1 informe
    public void deleteByInf( int id) {
        dao.deleteByInforme(id);
    }

    @Override
    public void insertAll(List<InformeEtapaDet> objects) {
    dao.insertAll(objects);
    }

    public void actualizarEstatusSync(int id, int estatus) {

        dao.actualizarEstatusSync(id, estatus);
    }
    public InformeEtapaDet getUltimo(int idinf, int etapa) {
        List<InformeEtapaDet> lista=dao.getUltimo(idinf,etapa);
        if(lista!=null&&lista.size()>0){
            return lista.get(0);
        }
        return null;
    }
    public InformeEtapaDet getUltimoCaja(int idinf, int etapa, int caja) {
        List<InformeEtapaDet> lista=dao.getUltimoCaja(idinf,etapa,caja);
        if(lista!=null&&lista.size()>0){
            return lista.get(0);
        }
        return null;
    }

    public void actEstatusSyncxInfo(int idinforme, int estatus) {
        dao.actEstatusSyncLis(idinforme, estatus);
    }

    public int getTotMuesxCaja(int cajaAct, int informeEtiId) {
       return dao.totalMuesxCaja(cajaAct,informeEtiId);
    }
}

