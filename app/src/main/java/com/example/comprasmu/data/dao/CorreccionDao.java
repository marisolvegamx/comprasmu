package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;

import androidx.room.Transaction;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.InformeWithDetalle;
import com.example.comprasmu.data.modelos.SolicitudWithCor;

import java.util.List;

@Dao
public abstract class CorreccionDao extends  BaseDao<Correccion>{

    @Query("SELECT * FROM correccion where   solicitudId=:solicitudId and indice=:indice ")
    public abstract LiveData<List<Correccion>> getCorrecciones( int solicitudId, String indice);


    @Query("SELECT * FROM correccion where   solicitudId=:solicitudId and indice=:indice ")
    public abstract List<Correccion> getCorreccionesSimp( int solicitudId, String indice);

    @Query("SELECT * FROM correccion where  solicitudId=:solicitudId and numfoto=:numfoto and indice=:indice")
    public abstract List<Correccion> getCorreccxsolSimple(int solicitudId,int numfoto, String indice);

    @Query("SELECT * FROM correccion WHERE id = :uuid")
    public abstract LiveData<Correccion>  getCorreccion(int uuid);

    @RawQuery(observedEntities = Correccion.class)
    public abstract LiveData<List<Correccion>> getCorreccionesByFiltros(SupportSQLiteQuery query);

    @RawQuery(observedEntities = SolicitudWithCor.class)
    public abstract LiveData<SolicitudWithCor> getCorreSolByFiltro(SupportSQLiteQuery query);

    @RawQuery(observedEntities = SolicitudWithCor.class)
    public abstract LiveData<List<SolicitudWithCor>> getCorreSolByFiltros(SupportSQLiteQuery query);

    @Query("SELECT * FROM correccion inner join solicitud_cor on correccion.solicitudId=solicitud_cor.id" +
            " and correccion.numfoto=solicitud_cor.numfoto  WHERE correccion.id = :id")
    public abstract LiveData<SolicitudWithCor> getWithSol(int id);

    @Query("DELETE FROM correccion where id=:id")
    public abstract void deleteCorreccion(int id);

    @Query("SELECT max(id) as ultimo " +
            "FROM correccion where indice=:indice ")
    public abstract int getUltimoId(String indice);

    @Query("update correccion set estatus=:estatus WHERE id=:id")
    public abstract  void actualizarEstatus(int id, int estatus);

    @Query("update correccion set estatusSync=:estatus WHERE id=:id")
    public abstract  void actualizarEstatusSync(int id, int estatus);

    @Query("SELECT * FROM correccion WHERE id =:id")
    public abstract   Correccion findSimple(int id);
    @Query("SELECT * FROM correccion WHERE id =:id")
    public abstract   LiveData<Correccion> find(int id);
    @Query("SELECT * FROM correccion WHERE estatusSync =:estatus ")
    public abstract   List<Correccion> getxEstatusSync(int estatus);

    @Query("delete from correccion where indice=:indice ")
    public abstract  void deleteByIndice(String indice);

    @Query("select * from correccion where indice=:indice ")
    public abstract  LiveData<List<Correccion>> getByIndice(String indice);
}
