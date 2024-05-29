package com.example.comprasmu.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.InformeGastoDetDao;
import com.example.comprasmu.data.dao.InformeGastoDetDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeGastoDet;
import com.example.comprasmu.data.modelos.InformeGastoDet;

import java.util.List;

public class InfGastoDetRepositoryImpl extends BaseRepository<InformeGastoDet> {

    private final InformeGastoDetDao dao;

    public InfGastoDetRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=comprasDataBase.getInformeGastoDetDao();
    }

    public LiveData<List<InformeGastoDet>> getAll(int idInforme) {
        return dao.findAll(idInforme);
    }

    public List<InformeGastoDet> getAllSencillo(int idInforme) {
        return dao.getAllSencillo(idInforme);
    }

    @Override
    public LiveData<List<InformeGastoDet>> getAll() {
        return null;
    }

    @Override
    public List<InformeGastoDet> getAllsimple() {
        return null;
    }

    @Override
    public LiveData<InformeGastoDet> find(int id) {
        return dao.find(id);
    }

    public LiveData<InformeGastoDet> getByConcepto( int idinf, int concepto) {
        return dao.getByConcepto(idinf,concepto);
    }


    public LiveData<List<ImagenDetalle>> getImagenxInf(int idinf) {
        return dao.getImagenxInf(idinf);
    }


    @Override
    public InformeGastoDet findsimple(int id) {
        return dao.findsimple(id);
    }


    @Override
    public long insert(InformeGastoDet object) {
        return dao.insert(object);
    }


    @Override
    public void delete(InformeGastoDet object) {
        dao.delete(object);
    }
    //elimina todos los de 1 informe
    public void deleteByInf( int id) {
        dao.deleteByInforme(id);
    }

    @Override
    public void insertAll(List<InformeGastoDet> objects) {
        dao.insertAll(objects);
    }

    public void actualizarEstatusSync(int id, int estatus) {

        dao.actualizarEstatusSync(id, estatus);
    }
    public InformeGastoDet getUltimo(int idinf) {
        List<InformeGastoDet> lista=dao.getUltimo(idinf);
        if(lista!=null&&lista.size()>0){
            return lista.get(0);
        }
        return null;
    }

    public void actEstatusSyncxInfo(int idinforme, int estatus) {
        dao.actEstatusSyncLis(idinforme, estatus);
    }

    public void deleteAll() {
        dao.deleteAll();
    }

    public void actEstatus(int id, int estatus) {
        dao.actualizarEstatus(id, estatus);
    }

}
