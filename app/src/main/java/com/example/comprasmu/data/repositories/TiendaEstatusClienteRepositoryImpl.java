package com.example.comprasmu.data.repositories;

import androidx.lifecycle.LiveData;
import com.example.comprasmu.data.dao.TiendaEstatusClienteDao;
import com.example.comprasmu.data.modelos.TiendaEstatusCliente;
import java.util.List;

public class TiendaEstatusClienteRepositoryImpl extends BaseRepository<TiendaEstatusCliente> {

    private static TiendaEstatusClienteDao dao;
    private static TiendaEstatusClienteRepositoryImpl INSTANCE;

    public static TiendaEstatusClienteRepositoryImpl getInstance(TiendaEstatusClienteDao comprasdao) {
        if (INSTANCE == null) {
            dao=comprasdao;
            synchronized (TiendaEstatusClienteRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE=new TiendaEstatusClienteRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }


    @Override
    public LiveData<List<TiendaEstatusCliente>> getAll() {
      return dao.findAll();
    }

    //no está implementada
    @Override
    public List<TiendaEstatusCliente> getAllsimple() {
        return null;
    }

    //no está implementada
    @Override
    public LiveData<TiendaEstatusCliente> find(int id) {
        return null;
    }
    public LiveData<TiendaEstatusCliente> find(int id, int clienteId) {
        return dao.find(id, clienteId);
    }

    @Override
    public TiendaEstatusCliente findsimple(int id) {
        return null;
    }

    @Override
    public void delete(TiendaEstatusCliente object) {
        dao.delete(object);
    }

    @Override
    public void insertAll(List<TiendaEstatusCliente> objects) {
        dao.insertAll(objects);
    }

    @Override
    public long insert(TiendaEstatusCliente object) {
        return dao.insert(object);
    }

    public void deleteAll(){
        dao.deleteAll();
    }

    public List<TiendaEstatusCliente> findByTienda(int idTienda) {
        return dao.findByTienda(idTienda);
    }
    public List<TiendaEstatusCliente> getTiendaAmarillaxCliente(int idTienda, int idCliente) {
        return dao.getTiendaAmarillaxCliente(idTienda,idCliente);
    }


}
