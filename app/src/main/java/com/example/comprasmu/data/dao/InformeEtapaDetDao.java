package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;

import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.ImagenDetalle;
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

    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa and num_caja=:caja order by id desc")
    public abstract List<InformeEtapaDet> getUltimoCaja( int id,int etapa, int caja);

    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and descripcion=:descripcion")
    public abstract LiveData<InformeEtapaDet> getByDescripcion( int id,String descripcion);

    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and descripcion=:descripcion and num_caja=:caja")
    public abstract LiveData<InformeEtapaDet> getByDescripcionCaja( int id,String descripcion, int caja);

    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa and num_caja=:numcaja order by num_muestra")
    public abstract List<InformeEtapaDet> getByCaja( int id,int etapa, int numcaja);

    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa and num_caja=:numcaja order by id")
    public abstract LiveData<List<InformeEtapaDet>> getByCajaEmp( int id,int etapa, int numcaja);

    @Query("SELECT imagen_detalle.* FROM informe_etapa_det inner join imagen_detalle on informe_etapa_det.ruta_foto=imagen_detalle.id where informeEtapaId=:informe and etapa=:etapa ")
    public  abstract LiveData<List<ImagenDetalle>> getImagenxInf(int informe, int etapa);

    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa and ruta_foto=:numfoto")
    public abstract LiveData<InformeEtapaDet> getBynumfoto( int id,int etapa, int numfoto);

    @Query("DELETE FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa and num_caja=:numcaja")
    public abstract void deleteByCaja( int id,int etapa, int numcaja);

    @Query("SELECT * FROM informe_etapa_det where id=:id")
    public abstract InformeEtapaDet findsimple( int id);

    @Query("SELECT count(*) FROM informe_etapa_det where informeEtapaId=:idinf and num_caja=:numcaja and etapa=3")
    public abstract int totalMuesxCaja( int  numcaja, int idinf);

    @Query("SELECT count(*) FROM informe_etapa_det where  num_caja=:numcaja and etapa=3")
    public abstract int totalMuesxCaja( int  numcaja);

    @Query("SELECT informe_etapa_det.id, informeEtapaId, informe_etapa_det.etapa, informe_etapa_det.estatusSync,ruta_foto,qr," +
            " count(informe_etapa_det.id) as num_muestra,descripcionId, descripcion, num_caja FROM informe_etapa_det inner join informe_etapa on informe_etapa.id=informeEtapaId where   informe_etapa.etapa=:etapa and clientesId=:cliente group by num_caja")
    public abstract List<InformeEtapaDet> totalCajasEtiqxCli(int etapa, int cliente);

    @Query("SELECT max(num_caja) FROM informe_etapa_det where   etapa=:i ")
    public abstract int totalCajasEtiq(int i);

    @Query("SELECT count(*) FROM informe_etapa_det  inner join informe_etapa on informe_etapa.id=informeEtapaId  where   informe_etapa.etapa=:i and clientesId=:cliente")
    public abstract int totalMuestrasEtiqxCli(int i, int cliente);

    @Query("SELECT * FROM informe_etapa_det where etapa=:etapa and qr=:qr ")
    public abstract InformeEtapaDet getByQr( String qr, int etapa);


    @Query("update informe_etapa_det set estatusSync=:estatus WHERE informeEtapaId=:idinforme")
    public abstract void actEstatusSyncLis(int idinforme, int estatus);

    @Query("delete  from informe_etapa_det")
    public abstract void deleteAll() ;
}
