package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;

import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.InformeEtapaDet;

import java.util.List;

@Dao
public abstract class InformeEtapaDetDao extends  BaseDao<InformeEtapaDet> {

    @RawQuery(observedEntities = InformeEtapaDet.class)
    public abstract LiveData<List<InformeEtapaDet>> getInformeCompraDetByFiltros(SupportSQLiteQuery query);



    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:informe")
    public  abstract LiveData<List<InformeEtapaDet>> findAll(int informe);
    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:informe and etapa=:etapa")
    public  abstract LiveData<List<InformeEtapaDet>> getAllxEtapa(int informe, int etapa);

    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:informe")
    public  abstract List<InformeEtapaDet> getAllSencillo(int informe);
    @Query("DELETE FROM informe_etapa_det where informeEtapaId=:informe and estatusSync=0")
    public  abstract void deleteByInforme(int informe);

  @Query("update informe_etapa_det set estatusSync=:estatus WHERE id=:id")
    public abstract void actualizarEstatusSync(int id, int estatus);

     @Query("SELECT * FROM informe_etapa_det where id=:id")
    public abstract LiveData<InformeEtapaDet> find( int id);
    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa order by id desc")
    public abstract List<InformeEtapaDet> getUltimo( int id,int etapa);

    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and descripcion=:descripcion")
    public abstract LiveData<InformeEtapaDet> getByDescripcion( int id,String descripcion);

    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa and num_caja=:numcaja order by num_muestra")
    public abstract List<InformeEtapaDet> getByCaja( int id,int etapa, int numcaja);


    @Query("DELETE FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa and num_caja=:numcaja")
    public abstract void deleteByCaja( int id,int etapa, int numcaja);

    @Query("SELECT * FROM informe_etapa_det where id=:id")
    public abstract InformeEtapaDet findsimple( int id);

    @Query("update informe_etapa_det set estatusSync=:estatus WHERE informeEtapaId=:idinforme")
    public abstract void actEstatusSyncLis(int idinforme, int estatus);
}
