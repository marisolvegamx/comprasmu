package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import com.example.comprasmu.data.modelos.SolicitudCor;
import java.util.List;

@Dao
public abstract class SolicitudCorDao extends  BaseDao<SolicitudCor>{

    @Query("SELECT * FROM solicitud_cor where etapa=:etapa and indice=:indice and plantasId=:plantaid and estatus=:estatus")
    public abstract LiveData<List<SolicitudCor>> getSolicitudes(int etapa, String indice, int plantaid, int estatus);

    @Query("SELECT count(id) FROM solicitud_cor where etapa=:etapa and indice=:indice  and estatus=:estatus")
    public abstract int getTotSols(int etapa, String indice,  int estatus);

    @Query("SELECT * FROM solicitud_cor WHERE id = :uuid")
    public abstract LiveData<SolicitudCor>  getSolicitud(int uuid);

    @Query("SELECT * FROM solicitud_cor WHERE   indice=:indice and (estatus=1) and etapa=:etapa order by id desc")
    public abstract List<SolicitudCor>  getSolicitudPendSimp(String indice, int etapa);

    @Query("SELECT * FROM solicitud_cor WHERE  indice=:indice and (estatus=1) and etapa=:etapa order by id desc")
    public abstract LiveData<List<SolicitudCor>>  getSolicitudPend(String indice, int etapa);


    @RawQuery(observedEntities = SolicitudCor.class)
    public abstract LiveData<List<SolicitudCor>> getSolicitudesByFiltros(SupportSQLiteQuery query);

    @Query("DELETE FROM solicitud_cor where id=:id")
    public abstract void deleteSolicitud(int id);

    @Query("SELECT max(id) as ultimo " +
            "FROM solicitud_cor ")
    public abstract int getUltimoId();

    @Query("update solicitud_cor set estatus=:estatus WHERE id=:id")
    public abstract  void actualizarEstatus(int id, int estatus);


    @Query("update solicitud_cor set estatusSync=:estatus WHERE id=:id")
    public abstract  void actualizarEstatusSync(int id, int estatus);


    @Query("SELECT * FROM solicitud_cor WHERE id =:id")
    public abstract SolicitudCor findSimple(int id);
    @Query("SELECT * FROM solicitud_cor WHERE id =:id")
    public abstract   LiveData<SolicitudCor> find(int id);

    @Query("SELECT * FROM solicitud_cor WHERE estatusSync =:estatus and indice=:indice")
    public abstract   List<SolicitudCor> getxEstatusSync(String indice,int estatus);


}
