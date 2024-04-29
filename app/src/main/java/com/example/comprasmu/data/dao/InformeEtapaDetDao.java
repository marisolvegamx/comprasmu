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

  @Query("SELECT count(num_caja) FROM informe_etapa_det where informeEtapaId=:informe and descripcionId=12")
  public  abstract int getTotalCajsxInf(int informe);

  @Query("update informe_etapa_det set estatusSync=:estatus WHERE id=:id")
    public abstract void actualizarEstatusSync(int id, int estatus);

  @Query("update informe_etapa_det set estatus=:estatus WHERE id=:id")
  public abstract void actualizarEstatus(int id, int estatus);

  @Query("SELECT * FROM informe_etapa_det where id=:id")
    public abstract LiveData<InformeEtapaDet> find( int id);
    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa order by id desc")
    public abstract List<InformeEtapaDet> getUltimo( int id,int etapa);
  @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa and (estatus>0 or estatus is null) order by id desc")
  public abstract List<InformeEtapaDet> getUltimonocan( int id,int etapa);

    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:inf and etapa=:etapa and descripcionId=11  order by id desc")
    public abstract List<InformeEtapaDet> getUltimaMuestra( int inf, int etapa);

  @Query("SELECT count(*) FROM informe_etapa_det where informeEtapaId=:inf and etapa=:etapa and descripcionId=11 and (estatus is null or estatus>0) order by id desc")
  public abstract int totalMuestrasEtiq( int inf, int etapa);

    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa and num_caja=:caja order by id desc")
    public abstract List<InformeEtapaDet> getUltimoCaja( int id,int etapa, int caja);

    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and descripcion=:descripcion")
    public abstract LiveData<InformeEtapaDet> getByDescripcion( int id,String descripcion);

    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and descripcion=:descripcion and num_caja=:caja")
    public abstract LiveData<InformeEtapaDet> getByDescripcionCaja( int id,String descripcion, int caja);

  @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and descripcion=:descripcion and num_caja=:caja")
  public abstract InformeEtapaDet getByDescripcionCajaSim( int id,String descripcion, int caja);

  @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and descripcionId=:descripcion and num_caja=:caja")
  public abstract InformeEtapaDet getByDescripCajaSim( int id,int descripcion, int caja);

  @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa and num_caja=:numcaja  order by num_muestra")
    public abstract List<InformeEtapaDet> getByCaja( int id,int etapa, int numcaja);
  @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa and num_caja=:numcaja and descripcionId=11  order by num_muestra")
  public abstract List<InformeEtapaDet> getMuestraByCaja( int id,int etapa, int numcaja);

    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa and num_caja=:numcaja order by id")
    public abstract LiveData<List<InformeEtapaDet>> getByCajaEmp( int id,int etapa, int numcaja);

    @Query("SELECT imagen_detalle.* FROM informe_etapa_det inner join imagen_detalle on informe_etapa_det.ruta_foto=imagen_detalle.id where informeEtapaId=:informe and etapa=:etapa ")
    public  abstract LiveData<List<ImagenDetalle>> getImagenxInf(int informe, int etapa);

    @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa and ruta_foto=:numfoto")
    public abstract LiveData<InformeEtapaDet> getBynumfoto( int id,int etapa, int numfoto);

    @Query("DELETE FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa and num_caja=:numcaja")
    public abstract void deleteByCaja( int id,int etapa, int numcaja);

  @Query("DELETE FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa and num_caja=:numcaja and descripcionId in (12,13,14)")
  public abstract void deleteFotosCajaEtiq( int id,int etapa, int numcaja);

  @Query("SELECT * FROM informe_etapa_det where id=:id")
    public abstract InformeEtapaDet findsimple( int id);

    @Query("SELECT count(*) FROM informe_etapa_det where informeEtapaId=:idinf and num_caja=:numcaja and etapa=3 and descripcionId=11")
    public abstract int totalMuesxCaja( int  numcaja, int idinf);


    @Query("SELECT count(*) FROM informe_etapa_det where  num_caja=:numcaja and etapa=3 and descripcionId=11")
    public abstract int totalMuesxCaja( int  numcaja);



  @Query("SELECT informe_etapa_det.id, informeEtapaId, informe_etapa_det.etapa, informe_etapa_det.estatusSync,ruta_foto,qr," +
            " count(informe_etapa_det.id) as num_muestra,descripcionId, descripcion, num_caja, informe_etapa_det.estatus FROM informe_etapa_det " +
          "inner join informe_etapa on informe_etapa.id=informeEtapaId where   informe_etapa.etapa=:etapa and ciudadNombre=:ciudad and clientesId=:cliente and (informe_etapa.estatus is null or informe_etapa.estatus>0) group by num_caja order by num_caja")
    public abstract List<InformeEtapaDet> getDetEtiqxCdCli(int etapa, String ciudad, int cliente);

  @Query("SELECT informe_etapa_det.id, informeEtapaId, informe_etapa_det.etapa, informe_etapa_det.estatusSync,ruta_foto,qr," +
          " count(informe_etapa_det.id) as num_muestra,descripcionId, descripcion, num_caja, informe_etapa_det.estatus " +
          "FROM informe_etapa_det inner join informe_etapa on informe_etapa.id=informeEtapaId " +
          "where   informe_etapa.etapa=:etapa and descripcionId=11 and ciudadNombre=:ciudad and clientesId=:cliente and (informe_etapa.estatus is null or informe_etapa.estatus>0) group by num_caja order by num_caja")
  public abstract List<InformeEtapaDet> getMuestrasEtiqxCdCli(int etapa, String ciudad, int cliente);


  @Query("SELECT informe_etapa_det.id, informeEtapaId, informe_etapa_det.etapa, informe_etapa_det.estatusSync,ruta_foto,qr," +
          " count(informe_etapa_det.id) as num_muestra,descripcionId, descripcion, num_caja, informe_etapa_det.estatus " +
          "FROM informe_etapa_det inner join informe_etapa on informe_etapa.id=informeEtapaId where   informe_etapa.etapa=:etapa and informeEtapaId=:infid and descripcionId=11 group by num_caja order by num_caja")
  public abstract List<InformeEtapaDet> getDetEtiqxInf(int etapa, int infid);

  @Query("SELECT informe_etapa_det.id, informeEtapaId, informe_etapa_det.etapa, informe_etapa_det.estatusSync,ruta_foto,qr," +
          " count(informe_etapa_det.id) as num_muestra,descripcionId, descripcion, num_caja, informe_etapa_det.estatus FROM informe_etapa_det " +
          "inner join informe_etapa on informe_etapa.id=informeEtapaId where   informe_etapa.etapa=:etapa and informe_etapa.indice=:indice and ciudadNombre=:ciudad and clientesId=:cliente and  (informe_etapa.estatus is null or informe_etapa.estatus>0) and descripcionId=11 group by num_caja order by num_caja")
  public abstract List<InformeEtapaDet> getDetEtiqxCdCli2(int etapa, String ciudad, int cliente,String indice);

  //este no lo uso
  @Query("SELECT informe_etapa_det.id, informeEtapaId, informe_etapa_det.etapa, informe_etapa_det.estatusSync,ruta_foto,qr," +
            " count(informe_etapa_det.id) as num_muestra,descripcionId, descripcion, num_caja, informe_etapa_det.estatus FROM informe_etapa_det " +
            " inner join informe_etapa on informe_etapa.id=informeEtapaId where   informe_etapa.etapa=:etapa and informe_etapa.indice=:indice and ciudadNombre=:ciudad and clientesId=:cliente group by num_caja order by num_caja")
    public abstract List<InformeEtapaDet> resumenEtiq(int etapa,String indice,int cliente,String ciudad);

    @Query("SELECT max(num_caja) FROM informe_etapa_det inner join informe_etapa on informe_etapa.id=informeEtapaId where   informe_etapa.etapa=:etapa and ciudadNombre=:ciudad and  (informe_etapa.estatus is null or informe_etapa.estatus>0)")
    public abstract int totalCajasEtiq(int etapa,String ciudad);

    @Query("SELECT count(*) FROM informe_etapa_det  inner join informe_etapa on informe_etapa.id=informeEtapaId  where   informe_etapa.etapa=:i and clientesId=:cliente")
    public abstract int totalMuestrasEtiqxCli(int i, int cliente);

    @Query("SELECT * FROM informe_etapa_det where etapa=:etapa and qr=:qr and (estatus is null or estatus>0)")
    public abstract InformeEtapaDet getByQr( String qr, int etapa);

    @Query("SELECT * FROM informe_etapa_det where etapa=:etapa and qr=:qr")
    public abstract InformeEtapaDet getByQr2( String qr, int etapa);

  @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:informe and descripcionId>11 order by num_caja, descripcionId")
    public  abstract List<InformeEtapaDet> getInfDetCalCaja(int informe);

    @Query("update informe_etapa_det set estatusSync=:estatus WHERE informeEtapaId=:idinforme")
    public abstract void actEstatusSyncLis(int idinforme, int estatus);

    @Query("delete  from informe_etapa_det")
    public abstract void deleteAll() ;

    @Query("DELETE FROM informe_etapa_det where informeEtapaId=:id and etapa=:etapa and descripcionId in (12,13,14)")
    public abstract void deleteCajaEtiq( int id,int etapa);

  @Query("update informe_etapa_det set qr=:qr WHERE id=:id")
  public abstract void actualizarQr(int id, String qr);

  /*busco las muestras canceladas de etiquetado***/
  @Query("SELECT * FROM informe_etapa_det  inner join informe_etapa on informe_etapa.id=informeEtapaId  where   informe_etapa.etapa=:etapa and informe_etapa_det.estatus=:estatus and informe_etapa.estatus<>2 and informe_etapa.indice=:indice and descripcionId=11")
  public abstract LiveData<List<InformeEtapaDet>> getCanceladasEtiq(int etapa,String indice, int estatus);

  @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:idinf and estatus=:estatus and etapa=3 and descripcionId=11")
  public abstract List<InformeEtapaDet> getEditadosEtiq(  int idinf, int estatus);

  @Query("SELECT * FROM informe_etapa_det where informeEtapaId=:infid and num_muestra=:nummuestra")
  public abstract InformeEtapaDet findxNummuestra( int infid, int nummuestra);
}
