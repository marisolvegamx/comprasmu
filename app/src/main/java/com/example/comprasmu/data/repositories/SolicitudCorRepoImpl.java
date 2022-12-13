package com.example.comprasmu.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.SolicitudCorDao;
import com.example.comprasmu.data.modelos.SolicitudCor;

import java.util.Date;
import java.util.List;

public class SolicitudCorRepoImpl  {


    private final SolicitudCorDao icDao;
    private LiveData<List<SolicitudCor>> allSolicitudCor;

    public SolicitudCorRepoImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        icDao = comprasDataBase.getSolicitudCorDao();

    }
    public SolicitudCorDao getDao(){
        return icDao;
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


    public LiveData<SolicitudCor> find(int id,int numfoto) {
        return icDao.find(id,numfoto);
    }


    public SolicitudCor findsimple(int id,int numfoto) {
        return icDao.findSimple(id,numfoto);
    }


    public long insert(SolicitudCor newSolicitudCor) {
        return icDao.insert(newSolicitudCor);
    }


    public void delete(SolicitudCor object) {
        icDao.delete(object);
    }

    public void  insertAll(List<SolicitudCor> newSolicitudCor) {
         icDao.insertAll(newSolicitudCor);
    }

    public long getUltimo() {
        return icDao.getUltimoId();
    }


    public void actualizarEstatus(int id,int numfoto, int estatus) {
        Log.d("---","actalizando"+id+"--"+estatus);
         icDao.actualizarEstatus(id,numfoto, estatus);
    }

    public void actualizarEstatusSync(int id,int numfoto, int estatus) {

        icDao.actualizarEstatusSync(id,numfoto, estatus);
    }

    public List<SolicitudCor> getSolicitudesPendSubir(String indice) {

        return icDao.getxEstatusSync(indice, 0);
    }

    public void deleteSolicitudCor(int id,int numfoto) {
       icDao.deleteSolicitud( id,numfoto);
    }


    public void actualizarEst(String motivo, int contador, Date fecha,int estatus,int idsol, int numfoto) {
        icDao.actualizarEst(motivo,contador,fecha,  estatus,idsol,numfoto);
    }
    public void actualizar(String motivo, int contador, Date fecha,int idsol, int numfoto) {
        icDao.actualizar(motivo,contador,fecha,  idsol,numfoto);
    }
}
