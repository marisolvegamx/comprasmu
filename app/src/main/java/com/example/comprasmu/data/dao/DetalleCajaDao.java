package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.SolicitudWithCor;

import java.util.List;

@Dao
public abstract class DetalleCajaDao extends  BaseDao<DetalleCaja>{

    @Query("SELECT * FROM detalle_caja where   informeEtapaId=:informeid")
    public abstract LiveData<List<DetalleCaja>> getDetalles(int informeid);

    @Query("SELECT * FROM detalle_caja where  informeEtapaId=:informeid")
    public abstract List<DetalleCaja> getdetallesSimple(int informeid);

    @RawQuery(observedEntities = DetalleCaja.class)
    public abstract LiveData<List<DetalleCaja>> getdetalleByFiltros(SupportSQLiteQuery query);

    @Query("SELECT max(id) as ultimo " +
            "FROM detalle_caja ")
    public abstract int getUltimoId();

    @Query("update detalle_caja set estatusSync=:estatus WHERE id=:id")
    public abstract  void actualizarEstatusSync(int id, int estatus);

    @Query("SELECT * FROM detalle_caja WHERE id =:id")
    public abstract   DetalleCaja findSimple(int id);
    @Query("SELECT * FROM detalle_caja WHERE id =:id")
    public abstract   LiveData<DetalleCaja> find(int id);
    @Query("DELETE FROM detalle_caja WHERE id =:id")
    public abstract   void deleteDetalle(int id);
    @Query("SELECT * FROM detalle_caja WHERE estatusSync =:estatus ")
    public abstract   List<DetalleCaja> getxEstatusSync(int estatus);


}
