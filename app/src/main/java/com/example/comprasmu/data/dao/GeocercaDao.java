package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.Reactivo;

import java.util.List;

@Dao
public abstract class GeocercaDao extends  BaseDao<Geocerca> {



        @Query("SELECT * FROM geocerca")
        public  abstract LiveData<List<Geocerca>> findAll();

        @Query("SELECT * FROM geocerca where geo_id=:id")
        public abstract LiveData<Geocerca> find( int id);


        @Query("SELECT * FROM geocerca")
        public  abstract List<Geocerca> findAllsimple();

        @Query("SELECT * FROM geocerca where ciudad=:ciudad")
        public abstract List<Geocerca> findsimplexCd( String ciudad);

}


