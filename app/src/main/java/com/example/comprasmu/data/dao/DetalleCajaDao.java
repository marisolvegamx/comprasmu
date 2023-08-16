package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.SolicitudWithCor;

import java.util.List;

@Dao
public abstract class DetalleCajaDao extends  BaseDao<DetalleCaja>{

    @Query("SELECT * FROM detalle_caja where   informeEtapaId=:informeid")
    public abstract LiveData<List<DetalleCaja>> getDetalles(int informeid);

    @Query("SELECT * FROM detalle_caja where  informeEtapaId=:informeid")
    public abstract List<DetalleCaja> getdetallesSimple(int informeid);

    @Query("SELECT * FROM detalle_caja where  informeEtapaId=:informeid and num_caja=:caja")
    public abstract DetalleCaja getdetallexCaja(int informeid, int caja);

    @RawQuery(observedEntities = DetalleCaja.class)
    public abstract LiveData<List<DetalleCaja>> getdetalleByFiltros(SupportSQLiteQuery query);

    @Query("SELECT max(id) as ultimo " +
            "FROM detalle_caja ")
    public abstract int getUltimoId();
    @Query("SELECT * FROM detalle_caja where informeEtapaId=:id order by id desc")
    public abstract List<DetalleCaja> getUltimoxInf(int id);

    @Query("SELECT * FROM detalle_caja where informeEtapaId=:id   and num_caja=:caja order by id desc")
    public abstract List<DetalleCaja> getUltimoxInfcaj(int id, int caja);

    @Query("update detalle_caja set estatusSync=:estatus WHERE id=:id")
    public abstract  void actualizarEstatusSync(int id, int estatus);

    @Query("update detalle_caja set estatusSync=:estatus WHERE informeEtapaId=:infid")
    public abstract  void actualizarEstSyncxInf(int infid, int estatus);

    @Query("update detalle_caja set alto=:alto, ancho=:ancho, peso=:peso, largo=:largo  WHERE id=:id and num_caja=:numcaja")
    public abstract  void actualizarDims(int id, String largo, String ancho, String alto, String peso, int numcaja);

    @Query("SELECT * FROM detalle_caja WHERE id =:id")
    public abstract   DetalleCaja findSimple(int id);
    @Query("SELECT * FROM detalle_caja WHERE id =:id")
    public abstract   LiveData<DetalleCaja> find(int id);
    @Query("DELETE FROM detalle_caja WHERE id =:id")
    public abstract   void deleteDetalle(int id);
    @Query("SELECT * FROM detalle_caja WHERE estatusSync =:estatus ")
    public abstract   List<DetalleCaja> getxEstatusSync(int estatus);

    @Query("delete from detalle_caja where informeEtapaId=:informe")
    public  abstract  void deleteByInforme(int informe);


}
