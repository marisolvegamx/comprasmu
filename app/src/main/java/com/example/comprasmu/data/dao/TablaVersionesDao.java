package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.TablaVersiones;

import java.util.List;

@Dao
public abstract class TablaVersionesDao extends BaseDao<TablaVersiones>{

    @Query("SELECT * FROM tabla_versiones WHERE nombreTabla = :nombre")
    public abstract LiveData<TablaVersiones> getVersionByNombreTabla(String nombre);

    @Query("SELECT * FROM tabla_versiones")
    public  abstract LiveData<List<TablaVersiones>> findAll();

    @Query("SELECT * FROM tabla_versiones where id=:id")
     public abstract LiveData<TablaVersiones> find( int id);

}
