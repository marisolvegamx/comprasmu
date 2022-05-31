package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.modelos.VisitaWithInformes;

import java.util.List;
@Dao
public abstract class VisitaDao extends  BaseDao<Visita> {

    @RawQuery(observedEntities = Visita.class)
    public abstract LiveData<List<Visita>> getVisitaByFiltros(SupportSQLiteQuery query);

    @Transaction
    @RawQuery(observedEntities = VisitaWithInformes.class)
    public abstract LiveData<List<VisitaWithInformes>> getVisitaWithInformesByFiltros(SupportSQLiteQuery query);

    @Query("DELETE FROM visitas where indice=:indice")
    public abstract void deleteListasByIndice(String indice);

    @Query("select max(id) from visitas")
    public abstract int getUltimoId();
    @Query("SELECT * FROM visitas")
    public  abstract LiveData<List<Visita>>findAll();
    @Query("SELECT * FROM visitas")
    public  abstract List<Visita> findAllsimple();
    @Query("SELECT * FROM visitas where id=:id")
    public  abstract Visita findsimple(int id);

    @Query("SELECT * FROM visitas where indice=:indice")
    public  abstract LiveData<List<Visita>> findAllByIndice(String indice);
    @Query("SELECT * FROM visitas where id=:id")
    public abstract LiveData<Visita> find( int id);
    @Query("update visitas set estatus=:estatus where id=:id")
    public abstract void actualizarEstatus(int id, int estatus) ;
    @Query("DELETE FROM visitas where indice=:indice")
    public abstract void deleteVisitaByIndice(String indice);

    @Query("SELECT * FROM visitas WHERE indice=:indice and estatusSync=:estatusSync")
    public abstract List<Visita> getVisitasxEst(String indice, int estatusSync);

    @Transaction
    @Query("SELECT * FROM visitas WHERE indice=:indice and estatusSync=2")
    public abstract List<VisitaWithInformes> getVisitasWithInformesByIndice(String indice);

    @Transaction
    @Query("SELECT * FROM visitas WHERE id=:id")
    public abstract LiveData<VisitaWithInformes> getVisitaWithInformesById(int id);
    @Query("update visitas set estatusSync=:estatus WHERE id=:id")
    public abstract void actualizarEstatusSync(int id, int estatus);



}
