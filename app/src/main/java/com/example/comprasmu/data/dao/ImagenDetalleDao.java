package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.ImagenDetalle;


import java.util.Date;
import java.util.List;

@Dao
public abstract class ImagenDetalleDao extends BaseDao<ImagenDetalle> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract long insertImg(ImagenDetalle object);

    @Query("SELECT * FROM imagen_detalle WHERE estatus =:estatus")
    public abstract LiveData<List<ImagenDetalle>> getImagenDetalleByEstatus(int estatus);

    @Query("SELECT * FROM imagen_detalle WHERE estatus =:estatus and estatusSync=:estatusSync")
    public abstract LiveData<List<ImagenDetalle>> getImagenDetalleByEstatusSync(int estatus, int estatusSync);

    @Query("SELECT * FROM imagen_detalle")
    public  abstract LiveData<List<ImagenDetalle>> findAll();

    @Query("SELECT max(id) FROM imagen_detalle")
    public  abstract long getUltimoId();


    @Query("SELECT * FROM imagen_detalle")
    public  abstract List<ImagenDetalle> findAllsimple();

    @Query("SELECT * FROM imagen_detalle WHERE id IN (:filterValues)")
    public  abstract LiveData<List<ImagenDetalle>> findinList(List<Integer> filterValues);
    @Query("SELECT * FROM imagen_detalle WHERE id IN (:filterValues)")
    public  abstract List<ImagenDetalle> findinListsencillo(List<Integer> filterValues);

    @Query("SELECT * FROM imagen_detalle WHERE indice=:indice")
    public  abstract List<ImagenDetalle> getByIndice(String indice);

    @Query("delete FROM imagen_detalle WHERE id IN (:filterValues)")
    public  abstract void deleteList(List<Integer> filterValues);

    @Query("SELECT * FROM imagen_detalle where id=:id")
    public  abstract LiveData<ImagenDetalle> find( int id);

    @Query("SELECT * FROM imagen_detalle where id=:id and indice=:indice")
    public  abstract ImagenDetalle findInd( int id, String indice);

    @Query("SELECT * FROM imagen_detalle where id=:id")
    public  abstract ImagenDetalle findsimple( int id);
    @Query("delete FROM imagen_detalle where id=:id")
    public  abstract void deleteById( int id);
    @Query("SELECT ruta FROM imagen_detalle where id=:id")
    public  abstract LiveData<String> findRuta( int id);

    @Query("SELECT * FROM imagen_detalle where ruta=:ruta")
    public  abstract ImagenDetalle findByRuta( String ruta);

    @Query("update imagen_detalle set estatusSync=:estatus WHERE id=:id")
    public abstract void actualizarEstatusSync(int id, int estatus);

    @Query("update imagen_detalle set estatusSync=0, estatus=:estatus WHERE id=:id")
    public abstract void cancelar(int id, int estatus);

    @Query("delete FROM imagen_detalle where indice=:indice")
    public abstract void deleteByIndice(String indice);

    @Query("SELECT * FROM imagen_detalle WHERE estatus =:estatus and estatusSync=:estatusSync and createdAt<:fecha")
    public abstract List<ImagenDetalle> getImagenByEstSyncsim2(int estatus, int estatusSync, long fecha);

    @Query("SELECT * FROM imagen_detalle WHERE id=:id and  estatusSync=:estatusSync and indice=:indice")
    public abstract ImagenDetalle getImagenByIdEstSyncsim(int id, int estatusSync,String indice);

    @Query("SELECT * FROM imagen_detalle WHERE estatus =:estatus and estatusSync=:estatusSync ")
    public abstract List<ImagenDetalle> getImagenByEstSyncsimple(int estatus, int estatusSync);
//    @Query("SELECT * FROM imagen_detalle WHERE informesId=:informe")
 //   public abstract LiveData<List<ImagenDetalle>> getImagenesByInforme(int informe);

    //solo las que ya se sincronizaron
  //  @Query("delete FROM imagen_detalle WHERE estatusSync =2 and informesId=:informe")
  //  public abstract void deleteByInforme(int informe);
}
