package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaWithDetalle;
import com.example.comprasmu.data.modelos.Reactivo;

import java.util.List;

@Dao
public abstract class ReactivoDao extends  BaseDao<Reactivo> {

        @RawQuery(observedEntities = Reactivo.class)
        public abstract LiveData<List<Reactivo>> getReactivoByFiltros(SupportSQLiteQuery query);


        @Query("SELECT * FROM reactivos")
        public  abstract LiveData<List<Reactivo>> findAll();

        @Query("SELECT * FROM reactivos where id=:id")
        public abstract LiveData<Reactivo> find( int id);
}


