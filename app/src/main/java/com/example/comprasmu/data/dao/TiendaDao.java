package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import com.example.comprasmu.data.modelos.Tienda;
import java.util.List;

@Dao
public abstract class TiendaDao extends BaseDao<Tienda>{

    @Query("SELECT * FROM tienda")
    public  abstract LiveData<List<Tienda>> findAll();

    @Query("delete FROM tienda")
    public  abstract void deleteAll();

    @Query("SELECT * FROM tienda where une_id=:id")
    public abstract LiveData<Tienda> find(int id);
    @Query("SELECT * FROM tienda where trim(ciudad)=trim(:ciudad)")
    public abstract List<Tienda> getByCiudad(String ciudad);

    @RawQuery(observedEntities = Tienda.class)
    public abstract LiveData<List<Tienda>> getTiendasByFiltros(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Tienda.class)
    public abstract List<Tienda> getTiendasByFiltrosSimp(SupportSQLiteQuery query);

}
