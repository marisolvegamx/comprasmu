package com.example.comprasmu.data.repositories;

import androidx.lifecycle.LiveData;
import com.example.comprasmu.data.dao.CorEtiquetadoCajaDao;
import com.example.comprasmu.data.modelos.CorEtiquetadoCaja;

import java.util.List;

public class CorEtiqCajaRepoImpl extends BaseRepository<CorEtiquetadoCaja> {


    private static CorEtiquetadoCajaDao icDao;
    private static CorEtiqCajaRepoImpl INSTANCE;

    public static CorEtiqCajaRepoImpl getInstance(CorEtiquetadoCajaDao corEtiquetadoCajaDao) {
        if (INSTANCE == null) {
            icDao=corEtiquetadoCajaDao;
            synchronized (CorEtiqCajaRepoImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE=new CorEtiqCajaRepoImpl();
                }
            }
        }
        return INSTANCE;

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

    public void deleteAll() {
        icDao.deleteAll();
    }
    public LiveData<List<CorEtiquetadoCaja>> getByIndice(String indice){
        return icDao.getByIndice(indice);
    }

    public List<CorEtiquetadoCaja> getAllSim(String indice) {
        return icDao.getAllSim(indice);
    }
}
