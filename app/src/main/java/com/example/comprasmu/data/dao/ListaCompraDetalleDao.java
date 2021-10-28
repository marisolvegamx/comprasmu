package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;

import java.util.List;

@Dao
public abstract class ListaCompraDetalleDao extends BaseDao<ListaCompraDetalle> {

    @Query("SELECT * FROM lista_compras_detalle WHERE listaId = :uuid")
     public abstract LiveData<List<ListaCompraDetalle>> getListaDetallesByLista(int uuid);

    @Query("SELECT * FROM lista_compras_detalle WHERE listaId = :uuid")
    public abstract List<ListaCompraDetalle> getListaDetallesByListasimple(int uuid);
    @Query("SELECT * FROM lista_compras_detalle")
    public abstract LiveData<List<ListaCompraDetalle>> findAll();

    @Query("SELECT * FROM lista_compras_detalle where id=:id")
    public abstract LiveData<ListaCompraDetalle> find( int id);
    @Query("SELECT * FROM lista_compras_detalle where id=:id")
    public abstract ListaCompraDetalle findsimple( int id);

    @Query("delete FROM lista_compras_detalle WHERE listaId = :id")
    public abstract void deleteByLista(int id);

    @Query("update lista_compras_detalle set comprados=comprados+:cantidad WHERE id = :id")
    public abstract void actualizarCompra(int id,int cantidad);

    @RawQuery(observedEntities = ListaCompraDetalle.class)
    public abstract LiveData<List<ListaCompraDetalle>> getDetallesByFiltros(SupportSQLiteQuery query);
}
