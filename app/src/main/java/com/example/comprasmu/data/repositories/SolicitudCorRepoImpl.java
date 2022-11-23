package com.example.comprasmu.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.SolicitudCorDao;
import com.example.comprasmu.data.modelos.SolicitudCor;

import java.util.List;

public class SolicitudCorRepoImpl extends BaseRepository<SolicitudCor> {


    private final SolicitudCorDao icDao;
    private LiveData<List<SolicitudCor>> allSolicitudCor;

    public SolicitudCorRepoImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        icDao = comprasDataBase.getSolicitudCorDao();

    }
    public SolicitudCorDao getDao(){
        return icDao;
    }

    @Override
    public LiveData<List<SolicitudCor>> getAll() {
        return null;
    }

    @Override
    public List<SolicitudCor> getAllsimple() {
        return null;
    }

    public LiveData<List<SolicitudCor>> getAll(int etapa, String indice, int plantaid, int estatus) {
        return icDao.getSolicitudes(etapa, indice,plantaid, estatus);
    }

    public LiveData<Integer> totalSols(int etapa, String indice, int estatus) {
        return icDao.getTotSols(etapa, indice, estatus);
    }
    public int totalSolsxPlanta(int etapa, String indice, int estatus, int plantaid) {
        Log.d("SolicitudCorRepoImpl","pendientes"+etapa+"--"+indice+"--"+estatus+"--"+plantaid);
        return icDao.getTotSolsxPlanta(etapa, indice, estatus, plantaid);
    }

    @Override
    public LiveData<SolicitudCor> find(int id) {
        return icDao.find(id);
    }

    @Override
    public SolicitudCor findsimple(int id) {
        return icDao.findSimple(id);
    }

    @Override
    public long insert(SolicitudCor newSolicitudCor) {
        return icDao.insert(newSolicitudCor);
    }

    @Override
    public void delete(SolicitudCor object) {
        icDao.delete(object);
    }

    public void  insertAll(List<SolicitudCor> newSolicitudCor) {
         icDao.insertAll(newSolicitudCor);
    }

    public long getUltimo() {
        return icDao.getUltimoId();
    }


    public void actualizarEstatus(int id, int estatus) {
        Log.d("---","actalizando"+id+"--"+estatus);
         icDao.actualizarEstatus(id, estatus);
    }

    public void actualizarEstatusSync(int id, int estatus) {

        icDao.actualizarEstatusSync(id, estatus);
    }

    public List<SolicitudCor> getSolicitudesPendSubir(String indice) {

        return icDao.getxEstatusSync(indice, 0);
    }

    public void deleteSolicitudCor(int id) {
       icDao.deleteSolicitud( id);
    }

    public LiveData<SolicitudCor> getSolicitudCor(int id) {
        return icDao.getSolicitud(id);
    }


}
