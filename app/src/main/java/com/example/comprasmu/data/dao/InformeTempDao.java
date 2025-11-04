package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.comprasmu.data.modelos.InformeTemp;

import java.util.List;

@Dao
public abstract class InformeTempDao extends  BaseDao<InformeTemp>  {

    @Query("SELECT * FROM informe_temp")
    public abstract LiveData<List<InformeTemp>> getInformes();

    @Query("SELECT * FROM informe_temp")
    public abstract List<InformeTemp> getInformessimple();

    @Query("SELECT * FROM informe_temp WHERE id = :uuid")
    public abstract LiveData<InformeTemp>  getInforme(int uuid);

    @Query("SELECT numMuestra FROM informe_temp WHERE tabla='ID'" +
            " group by numMuestra")
    public abstract List<Integer>  getMuestras();

    @Query("SELECT * FROM informe_temp WHERE id = :uuid")
    public abstract InformeTemp  getInformesimple(int uuid);
    @Query("SELECT * FROM informe_temp WHERE nombre_campo =:campo")
    public abstract InformeTemp  getInformesByNombre(String campo);
    @Query("SELECT * FROM informe_temp WHERE nombre_campo =:campo and numMuestra=:numMuestra")
    public abstract InformeTemp  getInformesByNombreMues(String campo,int numMuestra);
    @Query("SELECT * from informe_temp where isPregunta=:pregunta order by id desc")
    public abstract List<InformeTemp>  getUltimo(boolean pregunta);

    @Query("SELECT * from informe_temp where id<:id and isPregunta=:pregunta order by id desc")
    public abstract List<InformeTemp>  getUltimoxId(int id, boolean pregunta);
    @Query("SELECT * FROM informe_temp WHERE tabla=:tabla and numMuestra=:numMuestra")
    public abstract List<InformeTemp>  getInformesByTabla(String tabla,int numMuestra);
    @Query("SELECT * FROM informe_temp WHERE tabla=:tabla")
    public abstract List<InformeTemp>  getInformesByTablaI(String tabla);

    /*no se usa*/
    @Query("DELETE FROM informe_temp where nombre_campo!='clientesId' and nombre_campo!='primeraMuestra'" +
            " and nombre_campo!='segundaMuestra' and  nombre_campo!='terceraMuestra'")
    public abstract void deleteMenosCliente();

    @Query("DELETE FROM informe_temp where nombre_campo!='clientesId' and nombre_campo!='plantasId' and nombre_campo!='plantaNombre' and nombre_campo!='primeraMuestra'" +
            " and nombre_campo!='segundaMuestra' and  nombre_campo!='terceraMuestra' and  nombre_campo!='confirmaMuestra' and numMuestra=:nummuestra")
    public abstract void deleteMuestra(int nummuestra);

    @Query("DELETE FROM informe_temp")
    public abstract void deleteAll();


    @Query("SELECT * FROM informe_temp WHERE nombre_campo in ('productoId','producto','presentacion'" +
            ",'empaque','empaquesId','numMuestra','tipoAnalisis','nombreAnalisis','comprasId','comprasDetId'," +
            "'siglaspla','plantasId','plantaNombre','clienteNombre','clientesId','codigosnop'," +
            "'tamanioId','tipoMuestra','nombreTipoMuestra')")
    public abstract List<InformeTemp> getProductoSel();
    @Query("SELECT * FROM informe_temp WHERE id =:visitaId")
    public abstract List<InformeTemp> getInformessimple(int visitaId);


}
