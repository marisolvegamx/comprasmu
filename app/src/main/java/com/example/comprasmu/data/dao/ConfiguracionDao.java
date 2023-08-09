package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import com.example.comprasmu.data.modelos.Configuracion;
import java.util.List;

@Dao
public abstract class ConfiguracionDao extends  BaseDao<Configuracion> {

    @RawQuery(observedEntities = Configuracion.class)
    public abstract LiveData<List<Configuracion>> getByFiltros(SupportSQLiteQuery query);

    @RawQuery(observedEntities = Configuracion.class)
    public abstract List<Configuracion> getByFiltrosSimp(SupportSQLiteQuery query);


    @Query("SELECT * FROM configuracion")
    public  abstract LiveData<List<Configuracion>> findAll();

    @Query("SELECT * FROM configuracion")
    public  abstract List<Configuracion> getAllSencillo();

    @Query("delete from configuracion where clave=:clave")
    public  abstract  void deleteByClave(String clave);

    @Query("delete from configuracion")
    public  abstract  void deleteAll();

    @Query("SELECT * FROM configuracion where clave=:clave")
    public abstract LiveData<Configuracion> findByClave( String clave);

    @Query("SELECT * FROM configuracion where clave=:clave")
    public abstract Configuracion findsimple( String clave);





}
