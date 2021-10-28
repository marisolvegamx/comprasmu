package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.DatabaseView;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;


import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.ProductoExhibido;

import java.util.List;

@Dao
public abstract class ProductoExhibidoDao extends BaseDao<ProductoExhibido> {



    @Query("SELECT * FROM producto_exhibido WHERE estatusSync=:estatusSync")
    public abstract LiveData<List<ProductoExhibido>> getProductoExhibidoByEstatusSync(int estatusSync);

    @Query("SELECT * FROM producto_exhibido")
    public  abstract LiveData<List<ProductoExhibido>> findAll();

    @Query("SELECT * FROM producto_exhibido where id=:id")
    public  abstract LiveData<ProductoExhibido> find( int id);
    @Query("SELECT * FROM producto_exhibido where visitasId=:id")
    public  abstract LiveData<List<ProductoExhibido>> findByVisita( int id);

    @Query("update producto_exhibido set estatusSync=:estatus WHERE id=:id")
    public abstract void actualizarEstatusSync(int id, int estatus);

    @Query("update producto_exhibido set estatusSync=:estatus WHERE visitasId=:idinfo")
    public abstract void actualizarEstatusSyncxVisita(int idinfo, int estatus);

    @Query("SELECT  imagen_detalle.id, descripcion,ruta,estatus,producto_exhibido.estatusSync,indice FROM imagen_detalle " +
            " inner join    producto_exhibido on producto_exhibido.imagenId=imagen_detalle.id" +
            " where visitasId=:idVisita")
    public abstract LiveData<List<ImagenDetalle>> getImagenByVisita(int idVisita);

    @Query("SELECT  imagen_detalle.id, descripcion,ruta,estatus,producto_exhibido.estatusSync,indice FROM imagen_detalle " +
            " inner join    producto_exhibido on producto_exhibido.imagenId=imagen_detalle.id" +
            " where visitasId=:idVisita")
    public abstract List<ImagenDetalle> getImagenByVisitasimple(int idVisita);

    @Query("delete FROM producto_exhibido where visitasId=:idVisita")
    public abstract void deleteByVisita(int idVisita);

    @Query("SELECT * FROM ProductoExhibidoFoto where visitasId=:id")
    public  abstract LiveData<List<ProductoExhibidoFoto>> getAllByVisita( int id);

    @Query("delete  FROM producto_exhibido where id=:id")
    public  abstract void deleteById( int id);

    @DatabaseView("SELECT producto_exhibido.id as idprodex, " +
            "producto_exhibido.visitasId, " +
            "producto_exhibido.imagenId, " +
            "producto_exhibido.clienteId, " +
            "producto_exhibido.nombreCliente, " +

            "producto_exhibido.estatusSync, " +
            "ruta FROM producto_exhibido " +
            "INNER JOIN imagen_detalle ON producto_exhibido.imagenId = imagen_detalle.id")
    public static class ProductoExhibidoFoto {
        public int idprodex;
        public int visitasId;
        public int imagenId;
        public int clienteId;
        public String nombreCliente;
        public int estatusSync;
        public String ruta;
    }
}
