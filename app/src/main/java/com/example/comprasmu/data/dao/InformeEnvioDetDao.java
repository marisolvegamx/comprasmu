package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;
import com.example.comprasmu.data.modelos.InformeEnvioDet;
import com.example.comprasmu.data.modelos.InformeEnvioPaq;
import com.example.comprasmu.data.modelos.InformeWithDetalle;

@Dao
public abstract class InformeEnvioDetDao extends  BaseDao<InformeEnvioDet> {

    @Query("SELECT * FROM informe_envio_det where informeEtapaId=:informe")
    public  abstract InformeEnvioDet findSimple(int informe);

    @Query("DELETE FROM informe_envio_det where informeEtapaId=:informe")
    public  abstract void deleteById(int informe);

  @Query("update informe_envio_det set estatusSync=:estatus WHERE informeEtapaId=:id")
  public abstract void actualizarEstatusSync(int id, int estatus);

  @Query("update informe_envio_det set estatus=:estatus WHERE informeEtapaId=:id")
  public abstract void actualizarEstatus(int id, int estatus);

  @Query("SELECT * FROM informe_envio_det where informeEtapaId=:id")
  public abstract LiveData<InformeEnvioDet> find( int id);

  @Transaction
  @Query("SELECT * FROM informe_etapa WHERE id = :id ")
  public abstract InformeEnvioPaq getInformeEnviosimple(int id);


}
