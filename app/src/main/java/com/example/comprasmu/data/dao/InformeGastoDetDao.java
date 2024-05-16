package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeGastoDet;
import java.util.List;

@Dao
public abstract class InformeGastoDetDao extends  BaseDao<InformeGastoDet> {

    @RawQuery(observedEntities = InformeGastoDet.class)
    public abstract LiveData<List<InformeGastoDet>> getGastoDetByFiltros(SupportSQLiteQuery query);

    @Query("SELECT * FROM informe_gasto_det where informeEtapaId=:informe")
    public  abstract LiveData<List<InformeGastoDet>> findAll(int informe);
    
    @Query("SELECT * FROM informe_gasto_det where informeEtapaId=:informe")
    public  abstract List<InformeGastoDet> getAllSencillo(int informe);

    @Query("DELETE FROM informe_gasto_det where informeEtapaId=:informe and estatusSync=0")
    public  abstract void deleteByInforme(int informe);

 

  @Query("update informe_gasto_det set estatusSync=:estatus WHERE id=:id")
    public abstract void actualizarEstatusSync(int id, int estatus);

  @Query("update informe_gasto_det set estatus=:estatus WHERE id=:id")
  public abstract void actualizarEstatus(int id, int estatus);

  @Query("SELECT * FROM informe_gasto_det where id=:id")
    public abstract LiveData<InformeGastoDet> find( int id);
    @Query("SELECT * FROM informe_gasto_det where informeEtapaId=:id order by id desc")
    public abstract List<InformeGastoDet> getUltimo( int id);
  @Query("SELECT * FROM informe_gasto_det where informeEtapaId=:id  and (estatus>0 or estatus is null) order by id desc")
  public abstract List<InformeGastoDet> getUltimonocan( int id);


    @Query("SELECT * FROM informe_gasto_det where informeEtapaId=:id and conceptoId=:concepto")
    public abstract LiveData<InformeGastoDet> getByConcepto( int id,int concepto);



    @Query("SELECT imagen_detalle.* FROM informe_gasto_det inner join imagen_detalle on informe_gasto_det.fotocomprob=imagen_detalle.id where informeEtapaId=:informe ")
    public  abstract LiveData<List<ImagenDetalle>> getImagenxInf(int informe, int etapa);


  @Query("SELECT * FROM informe_gasto_det where id=:id")
    public abstract InformeGastoDet findsimple( int id);


    @Query("update informe_gasto_det set estatusSync=:estatus WHERE informeEtapaId=:idinforme")
    public abstract void actEstatusSyncLis(int idinforme, int estatus);

    @Query("delete  from informe_gasto_det")
    public abstract void deleteAll() ;


}
