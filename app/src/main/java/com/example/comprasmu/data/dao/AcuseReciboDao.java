package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.AcuseRecibo;

import java.util.List;

@Dao
public abstract class AcuseReciboDao extends  BaseDao<AcuseRecibo> {

    @RawQuery(observedEntities = AcuseRecibo.class)
    public abstract LiveData<List<AcuseRecibo>> getByFiltros(SupportSQLiteQuery query);

    @RawQuery(observedEntities = AcuseRecibo.class)
    public abstract List<AcuseRecibo> getByFiltrosSimp(SupportSQLiteQuery query);


    @Query("SELECT * FROM acuse_recibo")
    public  abstract LiveData<List<AcuseRecibo>> findAll();

    @Query("SELECT * FROM acuse_recibo")
    public  abstract List<AcuseRecibo> getAllSencillo();

    @Query("delete from acuse_recibo where indice=:indice and ciudad=:cd")
    public  abstract  void deleteByindice(String indice, String cd);

    @Query("delete from acuse_recibo")
    public  abstract  void deleteAll();

    @Query("SELECT * FROM acuse_recibo where indice=:indice  and ciudad=:cd")
    public abstract LiveData<AcuseRecibo> findByindice(String indice,String cd);

    @Query("SELECT * FROM acuse_recibo where indice=:indice  and ciudad=:cd")
    public abstract AcuseRecibo findsimple( String indice,String cd);





}
