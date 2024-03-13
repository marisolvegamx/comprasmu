package com.example.comprasmu.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.CorEtiquetadoCajaDao;
import com.example.comprasmu.data.modelos.CorEtiquetadoCaja;

import java.util.List;

public class CorEtiqCajaRepoImpl extends BaseRepository<CorEtiquetadoCaja> {


    private final CorEtiquetadoCajaDao icDao;

    public CorEtiqCajaRepoImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        icDao = comprasDataBase.getCorEtiquetadoCajaDao();

    }
    public CorEtiquetadoCajaDao getDao(){
        return icDao;
    }

    @Override
    public LiveData<List<CorEtiquetadoCaja>> getAll() {
        return null;
    }

    @Override
    public List<CorEtiquetadoCaja> getAllsimple() {
        return null;
    }

  /*  public LiveData<List<SolicitudWithCor>> getAllxEtaPlan(int plantaSel, String indice, int etapa) {
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

        for(int i=0;i<params.length;i++)
           Log.d("CorEtiquetadoCajaRepoImpl","***"+params[i]);
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
        //      Log.d("CorEtiquetadoCajaRepoImpl","***"+params[i]);
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
        Log.d("CorEtiquetadoCajaRepoImpl",query);
        return icDao.getCorreSolByFiltro( sqlquery);
    }*/
    public List<CorEtiquetadoCaja> getCorrecxSolSim(int solid,String indice, int numfoto) {
        return icDao.getCorEtiquetadoCajasSimp(solid, indice, numfoto);
    }

    @Override
    public LiveData<CorEtiquetadoCaja> find(int id) {
        return icDao.find(id);
    }

    @Override
    public CorEtiquetadoCaja findsimple(int id) {
        return icDao.findSimple(id);
    }


    @Override
    public long insert(CorEtiquetadoCaja newCorEtiquetadoCaja) {
        return icDao.insert(newCorEtiquetadoCaja);
    }

    @Override
    public void delete(CorEtiquetadoCaja object) {
        icDao.delete(object);
    }

    public void  insertAll(List<CorEtiquetadoCaja> newCorEtiquetadoCaja) {
         icDao.insertAll(newCorEtiquetadoCaja);
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

    public void deleteByIndice(String indice) {
        icDao.deleteByIndice(indice);
    }
    public LiveData<List<CorEtiquetadoCaja>> getByIndice(String indice){
        return icDao.getByIndice(indice);
    }
}
