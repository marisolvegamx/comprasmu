package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SupportSQLiteQuery;
import com.example.comprasmu.data.modelos.InfEtapaWithDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import java.util.List;

@Dao
public abstract class InformeEtapaDao extends  BaseDao<InformeEtapa>{

    @Query("SELECT * FROM informe_etapa where etapa=:etapa and indice=:indice and plantasId=:plantaid")
    public abstract LiveData<List<InformeEtapa>> getInformes(int etapa, String indice, int plantaid);
    @Query("SELECT * FROM informe_etapa where etapa=:etapa and indice=:indice")
    public abstract LiveData<List<InformeEtapa>> getInformesAll(int etapa, String indice);

    @Query("SELECT * FROM informe_etapa where etapa=:etapa")
    public abstract List<InformeEtapa> getInformesSimple(int etapa);
    @Query("SELECT * FROM informe_etapa where etapa=:etapa and indice=:indice")
    public abstract List<InformeEtapa> getAllSimp(int etapa, String indice);

    @Transaction
    @RawQuery(observedEntities = InfEtapaWithDetalle.class)
    public abstract   LiveData<List<InfEtapaWithDetalle>> getInfWithDetByFiltr(SupportSQLiteQuery query);

    @Transaction
    @Query("SELECT * FROM informe_etapa WHERE id = :id ")
    public abstract   LiveData<InfEtapaWithDetalle> getInfEtapaWithDetById(int id);


    @Query("SELECT * FROM informe_etapa WHERE id = :uuid")
    public abstract LiveData<InformeEtapa>  getInforme(int uuid);

    @Query("SELECT * FROM informe_etapa WHERE   indice=:indice and (estatus=:estatus) and etapa=:etapa order by id desc")
    public abstract LiveData<List<InformeEtapa>>  getInformesxEstatus(String indice, int etapa, int estatus);

    @Query("SELECT * FROM informe_etapa WHERE   indice=:indice and (estatus=1) and etapa=:etapa order by id desc")
    public abstract List<InformeEtapa>  getInformePendSimp(String indice, int etapa);

    @Query("SELECT * FROM informe_etapa WHERE  indice=:indice and (estatus=1) and etapa=:etapa order by id desc")
    public abstract LiveData<List<InformeEtapa>>  getInformePend(String indice, int etapa);


    @RawQuery(observedEntities = InformeEtapa.class)
    public abstract LiveData<List<InformeEtapa>> getInformesByFiltros(SupportSQLiteQuery query);

    /*@RawQuery(observedEntities = InformeEtapavisita.class)
    LiveData<List<InformeEtapavisita>> getInformesVisitaByFiltros(SupportSQLiteQuery query);
*/
    @Query("SELECT * FROM informe_etapa where etapa=:etapa and indice=:indice and plantasId=:plantaid limit 1")
    public abstract InformeEtapa getInformexPlant(int etapa, String indice, int plantaid);

    @Query("SELECT * FROM informe_etapa where etapa=:etapa and indice=:indice and plantasId=:plantaid and estatus>:estatus limit 1")
    public abstract InformeEtapa getInformexPlantEst(int etapa, String indice, int plantaid, int estatus);
    @Query("SELECT * FROM informe_etapa where etapa=:etapa and indice=:indice and plantasId=:plantaid and estatus=:estatus limit 1")
    public abstract InformeEtapa getInformexPlantEst2(int etapa, String indice, int plantaid, int estatus);


    @Query("SELECT * FROM informe_etapa where etapa=:etapa and indice=:indice and clientesId=:clienteid")
    public abstract List<InformeEtapa> getInformesxCli(int etapa, String indice, int clienteid);


    @Query("DELETE FROM informe_etapa where id=:id")
    public abstract void deleteInforme(int id);

    @Query("SELECT max(id) as ultimo " +
            "FROM informe_etapa where indice=:indice ")
    public abstract int getUltimoId(String indice);

    @Query("update informe_etapa set estatus=:estatus WHERE id=:id")
    public abstract  void actualizarEstatus(int id, int estatus);

    @Query("update informe_etapa set etapa=:etapa WHERE id=:id")
    public abstract  void actualizarEtapa(int id, int etapa);

    @Query("update informe_etapa set estatusSync=:estatus WHERE id=:id")
    public abstract  void actualizarEstatusSync(int id, int estatus);
    @Query("update informe_etapa set comentarios=:comentarios WHERE id=:id")
    public abstract  void actualizarComentariosPrep(int id,String comentarios);

    @Query("update informe_etapa set comentarios=:comentarios WHERE id=:id")
    public abstract  void actualizarComentariosEtiq(int id,String comentarios);

    @Query("update informe_etapa set comentarios=:comentarios WHERE id=:id")
    public abstract  void actualizarComentariosEmp(int id,String comentarios);
    @Query("update informe_etapa set total_muestras=:totmuestras WHERE id=:id")
    public abstract  void actualizarMuestrasEtiq(int id, int totmuestras);


    @Query("SELECT * FROM informe_etapa WHERE id =:id")
    public abstract   InformeEtapa findSimple(int id);
    @Query("SELECT * FROM informe_etapa WHERE id =:id")
    public abstract   LiveData<InformeEtapa> find(int id);
    @Query("SELECT * FROM informe_etapa WHERE estatusSync =:estatus and indice=:indice")
    public abstract   List<InformeEtapa> getxEstatusSync(String indice,int estatus);



    @Query("select * from informe_etapa where indice=:indice and etapa=3 and estatusSync =2 group by clientesId")
    public abstract   List<InformeEtapa> getClientesconInf(String indice);

    @Query("delete  from informe_etapa where indice=:indice")
    public abstract void deleteByIndice( String indice);
}
