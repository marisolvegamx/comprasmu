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


    @Query("SELECT * FROM tabla_versiones")
    public  abstract LiveData<List<Tienda>> findAll();

    @Query("delete FROM tabla_versiones")
    public  abstract void deleteAll();

    @Query("SELECT * FROM tabla_versiones where id=:id")
    public abstract LiveData<Tienda> find(int id);

    @RawQuery(observedEntities = Tienda.class)
    public abstract LiveData<List<Tienda>> getTiendasByFiltros(SupportSQLiteQuery query);

}
