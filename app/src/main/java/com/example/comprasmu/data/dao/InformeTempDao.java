package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.DatabaseView;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeTemp;
import com.example.comprasmu.data.modelos.InformeWithDetalle;

import java.util.Date;
import java.util.List;

@Dao
public abstract class InformeTempDao extends  BaseDao<InformeTemp>  {

    @Query("SELECT * FROM informe_temp")
    public abstract LiveData<List<InformeTemp>> getInformes();

    @Query("SELECT * FROM informe_temp")
    public abstract List<InformeTemp> getInformessimple();



    @Query("SELECT * FROM informe_temp WHERE id = :uuid")
    public abstract LiveData<InformeTemp>  getInforme(int uuid);

    @Query("SELECT * FROM informe_temp WHERE id = :uuid")
    public abstract InformeTemp  getInformesimple(int uuid);
    @Query("SELECT * FROM informe_temp WHERE nombre_campo =:campo")
    public abstract InformeTemp  getInformesByNombre(String campo);
    @Query("SELECT * from informe_temp where isPregunta=:pregunta order by id desc")
    public abstract InformeTemp  getUltimo(boolean pregunta);
    @Query("SELECT * FROM informe_temp WHERE tabla=:tabla")
    public abstract List<InformeTemp>  getInformesByTabla(String tabla);


    @Query("DELETE FROM informe_temp")
    public abstract void deleteAll();


    @Query("SELECT * FROM informe_temp WHERE nombre_campo in ('productoId','producto','presentacion'" +
            ",'empaque','empaquesId','numMuestra','tipoAnalisis','nombreAnalisis','comprasId','comprasDetId'," +
            "'siglas','plantasId','plantaNombre','clienteNombre','clientesId','codigosnop')")
    public abstract List<InformeTemp> getProductoSel();
    @Query("SELECT * FROM informe_temp WHERE id =:visitaId")
    public abstract List<InformeTemp> getInformessimple(int visitaId);


    @Query("SELECT * FROM informe_temp WHERE nombre_campo = :id ")
    public abstract  InformeTemp getInformeByNombre(String id);




}
