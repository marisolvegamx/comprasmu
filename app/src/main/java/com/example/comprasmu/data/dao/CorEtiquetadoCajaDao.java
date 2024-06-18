package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import com.example.comprasmu.data.modelos.CorEtiquetadoCaja;
import com.example.comprasmu.data.modelos.Correccion;

import java.util.List;

@Dao
public abstract class CorEtiquetadoCajaDao extends  BaseDao<CorEtiquetadoCaja>{

    @Query("SELECT * FROM coretiquetado_caja where   solicitudId=:solicitudId and indice=:indice ")
    public abstract LiveData<List<CorEtiquetadoCaja>> getCoretiquetadoCajas( int solicitudId, String indice);

    @Query("SELECT * FROM coretiquetado_caja where   solicitudId=:solicitudId and indice=:indice and numfoto=:numfoto ")
    public abstract List<CorEtiquetadoCaja> getCorEtiquetadoCajasSimp(int solicitudId, String indice,int numfoto);

    @RawQuery(observedEntities = CorEtiquetadoCaja.class)
    public abstract LiveData<List<CorEtiquetadoCaja>> getByFiltros(SupportSQLiteQuery query);

  /*  @Query("SELECT * FROM coretiquetado_caja inner join solicitud_cor on coretiquetado_caja.solicitudId=solicitud_cor.id" +
            " and coretiquetado_caja.numfoto=solicitud_cor.numfoto  WHERE coretiquetado_caja.id = :id")
    public abstract LiveData<Correccion> getWithSol(int id);*/

    @Query("DELETE FROM coretiquetado_caja where id=:id")
    public abstract void delete(int id);

    @Query("SELECT max(id) as ultimo " +
            "FROM coretiquetado_caja where indice=:indice ")
    public abstract int getUltimoId(String indice);

    @Query("update coretiquetado_caja set estatus=:estatus WHERE id=:id")
    public abstract  void actualizarEstatus(int id, int estatus);

    @Query("update coretiquetado_caja set estatusSync=:estatus WHERE id=:id")
    public abstract  void actualizarEstatusSync(int id, int estatus);

    @Query("SELECT * FROM coretiquetado_caja WHERE id =:id")
    public abstract   CorEtiquetadoCaja findSimple(int id);
    @Query("SELECT * FROM coretiquetado_caja WHERE id =:id")
    public abstract   LiveData<CorEtiquetadoCaja> find(int id);

    @Query("delete from coretiquetado_caja where indice=:indice ")
    public abstract  void deleteByIndice(String indice);

    @Query("select * from coretiquetado_caja where indice=:indice ")
    public abstract  LiveData<List<CorEtiquetadoCaja>> getByIndice(String indice);

    @Query("select * from coretiquetado_caja where indice=:indice ")
    public abstract List<CorEtiquetadoCaja> getAllSim(String indice);
}
