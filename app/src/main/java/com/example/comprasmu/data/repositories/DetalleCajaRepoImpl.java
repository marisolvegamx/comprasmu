package com.example.comprasmu.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.DetalleCajaDao;
import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.SolicitudWithCor;

import java.util.ArrayList;
import java.util.List;

public class DetalleCajaRepoImpl extends BaseRepository<DetalleCaja> {


    private DetalleCajaDao icDao;

    public DetalleCajaRepoImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        icDao = comprasDataBase.getDetalleCajaDao();

    }
    public DetalleCajaDao getDao(){
        return icDao;
    }

    @Override
    public LiveData<List<DetalleCaja>> getAll() {
        return null;
    }

    @Override
    public List<DetalleCaja> getAllsimple() {
        return null;
    }


    @Override
    public LiveData<DetalleCaja> find(int id) {
        return icDao.find(id);
    }

    @Override
    public DetalleCaja findsimple(int id) {
        return icDao.findSimple(id);
    }

    @Override
    public long insert(DetalleCaja newDetalleCaja) {
        return icDao.insert(newDetalleCaja);
    }

    @Override
    public void delete(DetalleCaja object) {
        icDao.delete(object);
    }

    public void  insertAll(List<DetalleCaja> newDetalleCaja) {
         icDao.insertAll(newDetalleCaja);
    }

    public long getUltimo() {
        return icDao.getUltimoId();
    }


    public void actualizarEstatusSync(int id, int estatus) {

        icDao.actualizarEstatusSync(id, estatus);
    }


    public void deleteDetalleCaja(int id) {
       icDao.deleteDetalle( id);
    }



}
