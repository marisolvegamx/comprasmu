package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.HistoricoMuestras;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;

import java.util.List;

@Dao
public abstract class HistoricoMuestrasDao extends BaseDao<HistoricoMuestras> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertHistorico(HistoricoMuestras object);

    @Query("SELECT * FROM historico_muestras")
    public  abstract LiveData<List<HistoricoMuestras>> findAll();

    @Query("SELECT * FROM historico_muestras")
    public  abstract List<HistoricoMuestras> findAllsimple();

    @Query("SELECT * FROM historico_muestras WHERE inf_indice=:indice")
    public  abstract List<HistoricoMuestras> getByIndice(String indice);

    @Query("SELECT * FROM historico_muestras WHERE plantaId=:plantaId")
    public  abstract List<HistoricoMuestras> getByPlanta(int plantaId);
    @Query("SELECT * " +
            "FROM historico_muestras " +
            " where productoId=:producto and presentacion=:tamanio" +
            " and empaquesId=:empaque and tipoAnalisis=:analisis " +
            " and inf_indice in (:indice1, :indice2) and plantaId=:planta" +
            " group by caducidad order by caducidad desc" )
    public abstract List<HistoricoMuestras> getByProducto(String indice1,String indice2, int planta, int producto, int analisis, int empaque, String tamanio);

    @RawQuery(observedEntities = ListaCompraDetalle.class)
    public abstract List<HistoricoMuestras> getDetallesByFiltros(SupportSQLiteQuery query);

    @Query("delete FROM historico_muestras")
    public  abstract void deleteAll();

}
