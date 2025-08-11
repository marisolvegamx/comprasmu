package com.example.comprasmu.data.repositories;

import androidx.lifecycle.LiveData;
import com.example.comprasmu.data.dao.HistoricoMuestrasDao;
import com.example.comprasmu.data.modelos.HistoricoMuestras;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HistoricoMuestrasRepositoryImpl extends BaseRepository<HistoricoMuestras> {

    private static HistoricoMuestrasDao dao;
    private static HistoricoMuestrasRepositoryImpl INSTANCE;
    public HistoricoMuestrasRepositoryImpl() {

    }

    public static HistoricoMuestrasRepositoryImpl getInstance(HistoricoMuestrasDao HistoricoMuestrasDao) {
        if (INSTANCE == null) {
            HistoricoMuestrasRepositoryImpl.dao=HistoricoMuestrasDao;
            synchronized (HistoricoMuestrasDao.class) {
                if (INSTANCE == null) {
                    INSTANCE=new HistoricoMuestrasRepositoryImpl();
                }
            }
        }
        return INSTANCE;

    }
    @Override
    public LiveData<List<HistoricoMuestras>> getAll() {
      return dao.findAll();
    }

    @Override
    public List<HistoricoMuestras> getAllsimple() {
        return dao.findAllsimple();
    }

  //no está implementado
    @Override
    public LiveData<HistoricoMuestras> find(int id) {
        return null;
    }
    //no está implementado
    @Override
    public HistoricoMuestras findsimple(int id) {
        return null;
    }


    public void deleteAll() {

        dao.deleteAll();
    }


    public List<HistoricoMuestras> getByIndice(String indice) {

        return dao.getByIndice(indice);
    }
    public List<HistoricoMuestras> getByPlanta(int plantaId) {

        return dao.getByPlanta(plantaId);
    }


    @Override
    public void insertAll(List<HistoricoMuestras> objects) {
        dao.insertAll(objects);
    }


    @Override
    public long insert(HistoricoMuestras object) {
        return dao.insert(object);
    }

    //no implementado
    @Override
    public void delete(HistoricoMuestras object) {

    }

    public void getByProducto(String indice, int planta, int producto, int analisis, int empaque, String tamanio) {

        dao.getByProducto( indice,  planta,  producto,  analisis,  empaque,  tamanio);
    }
}
