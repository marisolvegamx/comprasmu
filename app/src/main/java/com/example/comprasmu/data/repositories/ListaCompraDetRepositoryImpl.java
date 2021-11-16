package com.example.comprasmu.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.dao.ListaCompraDetalleDao;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ListaWithDetalle;

import java.util.ArrayList;
import java.util.List;

public class ListaCompraDetRepositoryImpl {

    private ListaCompraDetalleDao dao;



    public ListaCompraDetRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=comprasDataBase.getListaCompraDetalleDao();
    }
    public LiveData<List<ListaCompraDetalle>> getDetalleByFiltros(int idlista,String categoria, String productoNombre, String empaque,String tamanio,String analisis ) {

        String query="Select * from lista_compras_detalle where listaId=?";
        ArrayList<String> filtros=new ArrayList<String>();
        filtros.add(idlista+"");
        if(categoria!=null&&!categoria.equals("")) {
            query =query+ " and categoria=?";
            filtros.add(categoria);
        }
        if(productoNombre!=null&&!productoNombre.equals("")) {
            query = query + " and productoNombre=?";
            filtros.add(productoNombre);
        }
        if(empaque!=null&&!empaque.equals("")) {
            query = query + " and empaque=?";
            filtros.add(empaque);
        }
        if(tamanio!=null&&!tamanio.equals("")) {
            query = query + " and tamanio=?";
            filtros.add(tamanio);
        }
        if(analisis!=null&&!analisis.equals("")) {
            query = query + " and tipoAnalisis=?";
            filtros.add(analisis);
        }


        Object[] params=filtros.toArray();

        for(int i=0;i<params.length;i++)
            Log.d("InformeCompraRepo","***"+params[i]);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,filtros.toArray()
        );

        return dao.getDetallesByFiltros(sqlquery);
    }

    public LiveData<List<ListaCompraDetalle>> getAll() {
      return dao.findAll();
    }





    public LiveData<List<ListaCompraDetalle>> getAllByLista(int listasId) {
        return dao.getListaDetallesByLista(listasId);
    }
    public List<ListaCompraDetalle> getAllByListasimple(int listasId) {
        return dao.getListaDetallesByListasimple(listasId);
    }


    public LiveData<ListaCompraDetalle> find(int lista,int id) {
        return dao.find(lista,id);
    }

    public ListaCompraDetalle findsimple(int lista,int id) {
        return dao.findsimple(lista,id);
    }



    public void delete(ListaCompraDetalle object) {
        dao.delete(object);
    }


    public void insertAll(List<ListaCompraDetalle> objects) {
        dao.insertAll(objects);
    }

    public void updateAll(List<ListaCompraDetalle> detalles) {
        dao.updateAll(detalles);
    }

    public long insert(ListaCompraDetalle object) {
        return dao.insert(object);
    }
    public void actualizarComprados(int id,int listaid, int cantidad){
        dao.actualizarCompra(id,  listaid, cantidad);
    }

    public void deleteByLista(int id) {
        dao.deleteByLista(id);
    }
}
