package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Dao;
import androidx.room.DatabaseView;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeWithDetalle;
import com.example.comprasmu.data.modelos.Visita;


import java.util.Date;
import java.util.List;

@Dao
public interface InformeCompraDao {

    @Query("SELECT * FROM informe_compras")
    LiveData<List<InformeCompra>> getInformes();

    @Transaction
    @RawQuery(observedEntities = InformeWithDetalle.class)
    public  LiveData<List<InformeWithDetalle>> getInformesWithDetalleByFiltros(SupportSQLiteQuery query);

    @Transaction
    @Query("SELECT * FROM informe_compras WHERE id = :id ")
    public  LiveData<InformeWithDetalle> getInformeWithDetalleById(int id);


    @Query("SELECT * FROM informe_compras WHERE id = :uuid")
    LiveData<InformeCompra>  getInforme(int uuid);


    @RawQuery(observedEntities = InformeCompra.class)
    LiveData<List<InformeCompra>> getInformesByFiltros(SupportSQLiteQuery query);

    /*@RawQuery(observedEntities = InformeCompravisita.class)
    LiveData<List<InformeCompravisita>> getInformesVisitaByFiltros(SupportSQLiteQuery query);
*/
    @Query("SELECT * FROM informe_compras WHERE estatus =:estatus")
    LiveData<List<InformeCompra>> getInformeByEstatus(int estatus);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long addInforme(InformeCompra informe);

    @Query("DELETE FROM informe_compras where id=:id")
    void deleteInforme(int id);

    @Query("DELETE FROM informe_compras where visitasId=:visita")
    void deleteInformesByVisita(int visita);

    @Query("SELECT COALESCE(max(consecutivo),0) as ultimo " +
            "FROM informe_compras inner join visitas on visitasId=visitas.id " +
            "where indice=:indice and clientesId=:cliente")
    int getLastConsecutivoInforme(String indice, int cliente);

    @Query("update informe_compras set estatus=:estatus WHERE id=:id")
    public void actualizarEstatus(int id, int estatus);
    @Query("update informe_compras set estatusSync=:estatus WHERE id=:id")
    public void actualizarEstatusSync(int id, int estatus);

    @Transaction
    @Query("SELECT * FROM informe_compras WHERE visitasId=:visita and estatusSync=2")
    LiveData<List<InformeWithDetalle>> getInformeWithDetalleByVisita(int visita);

    @Query("SELECT * FROM informe_compras WHERE visitasId =:visitaId")
    abstract LiveData<List<InformeCompra>> getInformesByVisita(int visitaId);

    @Query("SELECT * FROM informe_compras WHERE visitasId =:visitaId")
    abstract List<InformeCompra> getInformesByVisitasimple(int visitaId);

    @RawQuery(observedEntities = InformeCompravisita.class)
    public abstract LiveData<List<InformeCompravisita>> getInformesWithVisita(SupportSQLiteQuery query);

    @Transaction
    @Query("SELECT * FROM informe_compras WHERE id = :id ")
    public abstract  InformeWithDetalle getInformeWithDetalleByIdsimple(int id);


   /* @Query("SELECT indice, createdAt as fecha,clienteNombre, plantaNombre, " +
            "informe_compras.estatus as estatusinforme, informe_compras.id as idinforme  " +
            "FROM informe_compras, visitas " +
            "WHERE informe_compras.visitasId = visitas.id")
    public LiveData<List<InformeCompravisita>> getInformesWithVisita();*/


    @DatabaseView("SELECT informe_compras.id as idinforme, " +
            "informe_compras.visitasId, " +
            "informe_compras.consecutivo, " +
            "informe_compras.plantaNombre, " +
            "informe_compras.clienteNombre, " +
            "informe_compras.estatus, " +
            "informe_compras.estatusSync, " +
            "indice, tiendaNombre, ciudad ,createdAt FROM informe_compras " +
            "INNER JOIN visitas ON informe_compras.visitasId = visitas.id")
    public static class InformeCompravisita {
        public int idinforme;
        public int visitasId;
        public int consecutivo;
        public String plantaNombre;
        public String clienteNombre;
        public int estatus;
        public int estatusSync;
        public String indice;

        public String tiendaNombre;

        public String ciudad;
        public Date createdAt;
    }

}
