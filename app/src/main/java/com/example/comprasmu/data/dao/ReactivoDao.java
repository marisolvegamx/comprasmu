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


        @Query("SELECT * FROM reactivos")
        public  abstract List<Reactivo> findAllsimple();

        @Query("SELECT * FROM reactivos where id=:id")
        public abstract Reactivo findsimple( int id);
        @Query("SELECT * FROM reactivos where nombreCampo=:nombre and clienteSel=:cliente")
        public abstract Reactivo findByNombre( String nombre, int cliente);

        @Query("SELECT * FROM reactivos where tabla in ('IE','ED','DC') and clienteSel=:cliente order by id" )
        public abstract List<Reactivo> getEmp(  int cliente);

        @Query("SELECT * FROM reactivos where clienteSel=:nombre")
        public abstract List<Reactivo> findByCliente( int nombre);
}


