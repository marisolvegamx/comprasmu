package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.PrimaryKey;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.InformeCompraDetalle;


import java.util.Date;
import java.util.List;

@Dao
public abstract class InformeCompraDetDao extends  BaseDao<InformeCompraDetalle> {

    @RawQuery(observedEntities = InformeCompraDetalle.class)
    public abstract LiveData<List<InformeCompraDetalle>> getInformeCompraDetByFiltros(SupportSQLiteQuery query);



    @Query("SELECT * FROM informe_detalle where informesId=:informe")
    public  abstract LiveData<List<InformeCompraDetalle>> findAll(int informe);

    @Query("SELECT * FROM informe_detalle where informesId=:informe")
    public  abstract List<InformeCompraDetalle> getAllSencillo(int informe);

    @Query("delete from informe_detalle where informesId=:informe and estatusSync=2")
    public  abstract  void deleteByInforme(int informe);



    @Query("update  informe_detalle set estatus=0, estatusSync=0 where informesId=:informe")
    public  abstract void cancelAll(int informe);

    @Query("update informe_detalle set estatusSync=:estatus WHERE id=:id")
    public abstract void actualizarEstatusSync(int id, int estatus);

    @Query("update informe_detalle set estatusSync=:estatus WHERE informesId=:id")
    public abstract void actualizarEstatusSyncxInfo(int id, int estatus);

    @Query("SELECT * FROM informe_detalle where id=:id")
    public abstract LiveData<InformeCompraDetalle> find( int id);
    @Query("SELECT * FROM informe_detalle where id=:id")
    public abstract InformeCompraDetalle findsimple( int id);

    @Query("SELECT * FROM informe_detalle where comprasId=:idcompra and comprasDetId=:iddet")
    public abstract InformeCompraDetalle findByCompra( int idcompra, int iddet);

    @Query("SELECT * FROM informe_detalle where comprasIdbu=:idcompra and comprasDetIdbu=:iddet")
    public abstract List<InformeCompraDetalle> findByCompraBu( int idcompra, int iddet);

    @Query("SELECT informe_detalle.foto_codigo_produccion FROM informe_detalle " +
            "            WHERE id =:id " +
            "union select energia FROM informe_detalle " +
            "       WHERE id =:id" +
            "     union select " +

            "informe_detalle.foto_num_tienda FROM informe_detalle " +
            "                 WHERE id =:id" +
            "     union select  " +
            "     informe_detalle.marca_traslape FROM informe_detalle" +
            "                 WHERE id =:id" +
            "       union select" +

            " foto_atributoa FROM informe_detalle " +
            "                WHERE id =:id" +
            "    union select" +

            "   foto_atributob FROM informe_detalle " +
            "                WHERE id =:id" +
            "      union select" +

            "     foto_atributoc FROM informe_detalle " +
            "               WHERE id =:id" +
            "   union select" +
            "    etiqueta_evaluacion " +
            "FROM informe_detalle " +
            "WHERE id =:id")
    public abstract List<Integer> getInformesWithImagen(int id);

    @Query("SELECT informe_detalle.id,informe_detalle.informesId,informe_detalle.estatus," +
            "informe_detalle.estatusSync,productoId,producto,presentacion,tamanioId," +
            "empaque,empaquesId,codigo, caducidad,origen, costo,foto_codigo_produccion  ," +
            "energia,producto_exhibido,foto_num_tienda,marca_traslape, atributoa," +
            "foto_atributoa,atributob,foto_atributob,atributoc,foto_atributoc,azucares," +
            "qr,etiqueta_evaluacion,tipoMuestra,nombreTipoMuestra,tipoAnalisis,nombreAnalisis," +
            "numMuestra,informe_detalle.comentarios,comprasId,comprasDetId,informe_detalle.createdAt,informe_detalle.updatedAt," +
            "comprasIdbu,comprasDetIdbu  " +
            "FROM informe_detalle " +
            " inner join informe_compras on informe_compras.id=informe_detalle.informesId" +
            " inner join visitas on visitas.id=informe_compras.visitasId" +
            " where productoId=:producto and presentacion=:tamanio" +
            " and empaquesId=:empaque and tipoAnalisis=:analisis " +
            " and visitas.indice=:indice and informe_compras.plantasId=:planta" )
    public abstract List<InformeCompraDetalle> getByProductoAna(String indice, int planta,int producto, int analisis, int empaque, String tamanio);

    public class InformeDetalleImagenes {

        public int id;

        public int foto_codigo_produccion;
        public int energia;
        public int producto_exhibido;
        public int foto_num_tienda;
        public int marca_traslape;

        public int foto_atributoa;

        public int foto_atributob;

        public int foto_atributoc;
        public int etiqueta_evaluacion;


    }
}
