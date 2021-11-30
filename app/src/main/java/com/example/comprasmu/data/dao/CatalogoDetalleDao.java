package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RawQuery;
import androidx.room.Transaction;
import androidx.sqlite.db.SupportSQLiteQuery;

import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.ListaWithDetalle;

import java.util.List;

@Dao
public abstract class CatalogoDetalleDao {

    @Query("DELETE FROM catalogos_detalle where cad_idcatalogo=:cat")
    public abstract void deleteCatalogoByTipo(int cat);




    @Query("SELECT * FROM catalogos_detalle where cad_idcatalogo=:cat")
    public  abstract List<CatalogoDetalle> findAll(int cat);

    @Query("SELECT * FROM catalogos_detalle where cad_idcatalogo=:cat and cad_idopcion=:id")
    public abstract LiveData<CatalogoDetalle> find( int id,int cat);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insert(CatalogoDetalle object);
   /* @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<T> object);*/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public abstract void insertAll(List<CatalogoDetalle> products);

    @Delete
    public abstract void delete(CatalogoDetalle object);

    @Query("DELETE  FROM catalogos_detalle where cad_idcatalogo=:idcat")
    public abstract void deletexIdCat(int idcat);




  /*  @Query("DELETE FROM catalogos_detalle where cad_idcatalogo=:cat and cad_idopcion=:id")
    public abstract void delete( int id,int cat);
*/
}
