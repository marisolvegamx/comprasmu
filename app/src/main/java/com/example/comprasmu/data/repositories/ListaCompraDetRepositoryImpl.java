package com.example.comprasmu.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.dao.ListaCompraDetalleDao;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;

import java.util.List;

public class ListaCompraDetRepositoryImpl extends BaseRepository<ListaCompraDetalle> {

    private ListaCompraDetalleDao dao;

    private LiveData<List<InformeCompra>> allInformeCompra;

    public ListaCompraDetRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=comprasDataBase.getListaCompraDetalleDao();
    }

    @Override
    public LiveData<List<ListaCompraDetalle>> getAll() {
      return dao.findAll();
    }

    public LiveData<List<ListaCompraDetalle>> getAllByLista(int listasId) {
        return dao.getListaDetallesByLista(listasId);
    }
    public List<ListaCompraDetalle> getAllByListasimple(int listasId) {
        return dao.getListaDetallesByListasimple(listasId);
    }

    @Override
    public LiveData<ListaCompraDetalle> find(int id) {
        return dao.find(id);
    }

    public ListaCompraDetalle findsimple(int id) {
        return dao.findsimple(id);
    }


    @Override
    public void delete(ListaCompraDetalle object) {
        dao.delete(object);
    }

    @Override
    public void insertAll(List<ListaCompraDetalle> objects) {
        dao.insertAll(objects);
    }


    @Override
    public long insert(ListaCompraDetalle object) {
        return dao.insert(object);
    }
    public void actualizarComprados(int id, int cantidad){
        dao.actualizarCompra(id, cantidad);
    }

    public void deleteByLista(int id) {
        dao.deleteByLista(id);
    }
}
