package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import com.example.comprasmu.data.modelos.SolicitudCor;

import java.util.Date;
import java.util.List;

@Dao
public abstract class SolicitudCorDao extends  BaseDao<SolicitudCor>{

    @Query("SELECT * FROM solicitud_cor where etapa=:etapa and indice=:indice and plantasId=:plantaid and (estatus=:estatus or estatus=5)")
    public abstract LiveData<List<SolicitudCor>> getSolicitudesPlan(int etapa, String indice, int plantaid, int estatus);

    @Query("SELECT * FROM solicitud_cor where etapa=:etapa and indice=:indice and (estatus=:estatus or estatus=5)")
    public abstract LiveData<List<SolicitudCor>> getSolicitudes(int etapa, String indice,  int estatus);

    @Query("SELECT count(id) FROM solicitud_cor where etapa=:etapa and indice=:indice  and (estatus=:estatus or estatus=5)")
    public abstract LiveData<Integer> getTotSols(int etapa, String indice, int estatus);

    @Query("SELECT count(id) FROM solicitud_cor where etapa=:etapa and indice=:indice  and (estatus=:estatus or estatus=5) and plantasId=:plantaId")
    public abstract int getTotSolsxPlanta(int etapa, String indice,  int estatus, int plantaId);

    @Query("SELECT count(id) FROM solicitud_cor where indice=:indice  and (estatus=:estatus or estatus=5) and plantasId=:plantaId")
    public abstract int getTotSolsxPlantaAll( String indice,  int estatus, int plantaId);

    @Query("SELECT count(solicitud_cor.id) FROM solicitud_cor inner join visitas on val_vis_id=visitas.id where solicitud_cor.etapa=:etapa and solicitud_cor.indice=:indice  and (solicitud_cor.estatus=:estatus or solicitud_cor.estatus=5) and ciudad=:ciudad")
    public abstract int getTotSolsVis(int etapa, String indice,  int estatus, String ciudad);

    @Query("SELECT count(solicitud_cor.id) from solicitud_cor inner join lista_compras on solicitud_cor.plantasId=lista_compras.plantasId " +
            "where solicitud_cor.etapa=:etapa and solicitud_cor.indice=:indice  and (solicitud_cor.estatus=:estatus or solicitud_cor.estatus=5) and ciudadNombre=:cd")
    public abstract LiveData<Integer> getTotSolsxCd(int etapa, String indice,  int estatus, String cd);

    @Query("SELECT count(id) FROM solicitud_cor where   indice=:indice  and (estatus=:estatus or estatus=5)")
    public abstract int getTotSolsGen(String indice, int estatus);


    @Query("SELECT count(solicitud_cor.id) from solicitud_cor inner join informe_etapa on solicitud_cor.informesId=informe_etapa.id " +
            "where solicitud_cor.etapa=:etapa and solicitud_cor.indice=:indice  and (solicitud_cor.estatus=:estatus or solicitud_cor.estatus=5) and ciudadNombre=:cd")
    public abstract LiveData<Integer> getTotSolsEtiqxCd(int etapa, String indice,  int estatus, String cd);


    @Query("SELECT * FROM solicitud_cor WHERE   indice=:indice and (estatus=1 or estatus=5)  order by id desc")
    public abstract LiveData<List<SolicitudCor>>  getSolicitudPendAll(String indice);

    @Query("SELECT * FROM solicitud_cor WHERE  indice=:indice and (estatus=1) and etapa=:etapa order by id desc")
    public abstract LiveData<List<SolicitudCor>>  getSolicitudPend(String indice, int etapa);


    @RawQuery(observedEntities = SolicitudCor.class)
    public abstract LiveData<List<SolicitudCor>> getSolicitudesByFiltros(SupportSQLiteQuery query);

    @Query("DELETE FROM solicitud_cor where id=:id and numFoto=:numfoto")
    public abstract void deleteSolicitud(int id, int numfoto);

    @Query("SELECT max(id) as ultimo " +
            "FROM solicitud_cor ")
    public abstract int getUltimoId();

    @Query("update solicitud_cor set estatus=:estatus WHERE id=:id and numfoto=:numfoto")
    public abstract  void actualizarEstatus(int id, int numfoto,int estatus);



    @Query("update solicitud_cor set estatusSync=:estatus WHERE id=:id and numfoto=:numfoto")
    public abstract  void actualizarEstatusSync(int id,int numfoto, int estatus);


    @Query("SELECT * FROM solicitud_cor WHERE id = :id and numFoto=:numfoto")
    public abstract SolicitudCor findSimple(int id,int numfoto);
    @Query("SELECT * FROM solicitud_cor WHERE id = :id and numFoto=:numfoto")
    public abstract   LiveData<SolicitudCor> find(int id,int numfoto);

    @Query("SELECT * FROM solicitud_cor WHERE estatusSync =:estatus and indice=:indice")
    public abstract   List<SolicitudCor> getxEstatusSync(String indice,int estatus);

    @Query("update solicitud_cor set estatus=:estatus,motivo=:motivo, contador=:contador, createdAt=:fecha WHERE id=:id and numfoto=:numfoto")
    public abstract void actualizarEst(String motivo, int contador, Date fecha,int estatus,int id,int numfoto);

    @Query("update solicitud_cor set motivo=:motivo, contador=:contador, createdAt=:fecha WHERE id=:idsol and numfoto=:numfoto")
    public abstract void actualizar(String motivo, int contador, Date fecha,  int idsol, int numfoto);

    @Query("delete from solicitud_cor where indice=:indice ")
    public abstract  void deleteByIndice(String indice);

    @Query("select * from solicitud_cor where indice=:indice ")
    public abstract  LiveData<List<SolicitudCor>> getByIndice(String indice);

    @Query("SELECT * FROM solicitud_cor where etapa=:etapa and indice=:indice and val_vis_id>0 and (estatus=:estatus or estatus=5)")
    public abstract LiveData<List<SolicitudCor>> getSolicitudesVis(int etapa, String indice, int estatus);

}
