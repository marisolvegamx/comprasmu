package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaWithDetalle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Dao
public abstract class ListaCompraDao  extends  BaseDao<ListaCompra> {

    @RawQuery(observedEntities = ListaCompra.class)
    public abstract LiveData<List<ListaCompra>> getListaCompraByFiltros(SupportSQLiteQuery query);

    @RawQuery(observedEntities = ListaCompra.class)
    public abstract List<ListaCompra> getListaCompraByFiltrosSimple(SupportSQLiteQuery query);


    @RawQuery(observedEntities = ListaWithDetalle.class)
    public abstract LiveData<List<ListaWithDetalle>> getListasWithDetalleByFiltros(SupportSQLiteQuery query);

    @RawQuery(observedEntities = ListaCompra.class)
    public abstract LiveData<ListaCompra> getListaByFiltros(SupportSQLiteQuery query);


    @Query("DELETE FROM lista_compras where indice=:indice")
    public abstract void deleteListasByIndice(String indice);

    @Query("SELECT * FROM lista_compras")
    public  abstract LiveData<List<ListaCompra>> findAll();
    @Query("SELECT * FROM lista_compras")
    public  abstract List<ListaCompra> findAllsimple();
    @Query("SELECT * FROM lista_compras group by indice")
    public  abstract List<ListaCompra> findIndice();

    @Query("SELECT * FROM lista_compras where indice=:indice")
    public  abstract LiveData<List<ListaCompra>> findAllByIndice(String indice);
    @Query("SELECT * FROM lista_compras where indice=:indice")
    public  abstract List<ListaCompra> findAllByIndicesimple(String indice);

    @Query("SELECT * FROM lista_compras where indice=:indice group by plantasId")
    public  abstract LiveData<List<ListaCompra>> findPlantas( String indice);

    @Query("SELECT * FROM lista_compras where indice=:indice group by ciudadNombre")
    public  abstract LiveData<List<ListaCompra>> findCiudades( String indice);

    @Query("SELECT * FROM lista_compras where indice=:indice and plantasId=:planta group by plantasId")
    public  abstract ListaCompra getClientexPlanta( String indice, int planta);

    @Query("SELECT * FROM lista_compras where clientesId=:clienteId limit 1")
    public  abstract ListaCompra getClientexId(  int clienteId);

    @Query("SELECT * FROM lista_compras where id=:id")
    public abstract LiveData<ListaCompra> find( int id);

    @Query("select" +
            " clientesId," +
            " count(*) as total" +
            " from" +
            " lista_compras" +
            " where" +
            " trim(ciudadNombre)= trim(:ciudad) and lis_etapaactual=:etapa" +
            " group by" +
            " clientesId," +
            " ciudadesId")
    public abstract List<TotalPlantas> getTotalPlantasxClienteEtapa(String ciudad, int etapa);

    public class TotalPlantas{
        int clientesId;
        int total;

        public int getClientesId() {
            return clientesId;
        }

        public void setClientesId(int clientesId) {
            this.clientesId = clientesId;
        }

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }
}
