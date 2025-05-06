package com.example.comprasmu.data.repositories;

import android.content.Context;
import androidx.lifecycle.LiveData;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.CorEtiquetadoCajaDao;
import com.example.comprasmu.data.dao.CorEtiquetadoCajaDetDao;
import com.example.comprasmu.data.modelos.CorEtiquetadoCajaDet;
import java.util.List;

public class CorEtiqCajaDetRepoImpl extends BaseRepository<CorEtiquetadoCajaDet> {

    private static CorEtiquetadoCajaDetDao corEtiquetadoCajaDetDao;
    private static CorEtiqCajaDetRepoImpl INSTANCE;

    public static CorEtiqCajaDetRepoImpl getInstance(CorEtiquetadoCajaDetDao corEtiquetadoDao) {
        if (INSTANCE == null) {
            corEtiquetadoCajaDetDao=corEtiquetadoDao;
            synchronized (CorEtiqCajaRepoImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE=new CorEtiqCajaDetRepoImpl();
                }
            }
        }
        return INSTANCE;

    }
    public CorEtiquetadoCajaDetDao getDao(){
        return corEtiquetadoCajaDetDao;
    }

    @Override
    public LiveData<List<CorEtiquetadoCajaDet>> getAll() {
        return null;
    }

    @Override
    public List<CorEtiquetadoCajaDet> getAllsimple() {
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
           Log.d("CorEtiquetadoCajaDetRepoImpl","***"+params[i]);
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
        //      Log.d("CorEtiquetadoCajaDetRepoImpl","***"+params[i]);
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
        Log.d("CorEtiquetadoCajaDetRepoImpl",query);
        return icDao.getCorreSolByFiltro( sqlquery);
    }*/

    public List<CorEtiquetadoCajaDet> getAllByCorId(int corid) {
        return corEtiquetadoCajaDetDao.getCorEtiquetadoCajaDetSimp(corid);
    }

    public List<CorEtiquetadoCajaDet> getCorrecxdescSimple(int corId, int descripcionId, int numcaja) {
        return corEtiquetadoCajaDetDao.getCorrecxdescSimple(corId,descripcionId, numcaja);
    }
    public List<CorEtiquetadoCajaDet> getCorrecxdescSimple2(int corId, int descripcionId, int numcaja) {
        return corEtiquetadoCajaDetDao.getCorrecxdescSimple2(corId,descripcionId, numcaja);
    }
    @Override
    public LiveData<CorEtiquetadoCajaDet> find(int id) {
        return corEtiquetadoCajaDetDao.find(id);
    }

    @Override
    public CorEtiquetadoCajaDet findsimple(int id) {
        return corEtiquetadoCajaDetDao.findSimple(id);
    }


    @Override
    public long insert(CorEtiquetadoCajaDet newCorEtiquetadoCajaDet) {
        return corEtiquetadoCajaDetDao.insert(newCorEtiquetadoCajaDet);
    }

    @Override
    public void delete(CorEtiquetadoCajaDet object) {
        corEtiquetadoCajaDetDao.delete(object);
    }

    public void  insertAll(List<CorEtiquetadoCajaDet> newCorEtiquetadoCajaDet) {
        corEtiquetadoCajaDetDao.insertAll(newCorEtiquetadoCajaDet);
    }



    public void actualizarEstatus(int id, int estatus) {

        corEtiquetadoCajaDetDao.actualizarEstatus(id, estatus);
    }

    public void actualizarEstatusSync(int id, int estatus) {

        corEtiquetadoCajaDetDao.actualizarEstatusSync(id, estatus);
    }

    public void deleteAll() {
        corEtiquetadoCajaDetDao.deleteAll();
    }

}
