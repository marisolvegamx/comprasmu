package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.Sustitucion;

import java.util.List;

@Dao
public abstract class SustitucionDao extends  BaseDao<Sustitucion> {

    @RawQuery(observedEntities = Sustitucion.class)
    public abstract LiveData<List<Sustitucion>> getByFiltros(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Sustitucion.class)
    public abstract List<Sustitucion> getByFiltrosSimp(SupportSQLiteQuery query);


    @Query("SELECT * FROM sustitucion")
    public  abstract LiveData<List<Sustitucion>> findAll();

    @Query("SELECT * FROM sustitucion")
    public  abstract List<Sustitucion> getAllSencillo();

    @Query("delete from sustitucion where id_sustitucion=:id_sustitucion")
    public  abstract  void deleteById(int id_sustitucion);

    @Query("delete from sustitucion")
    public  abstract  void deleteAll();

    @Query("SELECT * FROM sustitucion where id_sustitucion=:id")
    public abstract LiveData<Sustitucion> find( int id);
    @Query("SELECT * FROM sustitucion where id_sustitucion=:id")
    public abstract Sustitucion findsimple( int id);

    @Query("SELECT * FROM sustitucion where su_producto=:idprod")
    public abstract Sustitucion findByProducto( int idprod);



}
