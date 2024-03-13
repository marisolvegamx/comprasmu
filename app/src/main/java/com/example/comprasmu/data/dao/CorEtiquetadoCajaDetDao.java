package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;

import com.example.comprasmu.data.modelos.CorEtiquetadoCajaDet;

import java.util.List;

@Dao
public abstract class CorEtiquetadoCajaDetDao extends  BaseDao<CorEtiquetadoCajaDet>{

    @Query("SELECT * FROM coretiquetado_cajadet where   coretiquetadocId=:corEtiId")
    public abstract LiveData<List<CorEtiquetadoCajaDet>> getCorEtiquetadoCajaDetxcor( int corEtiId);


    @Query("SELECT * FROM coretiquetado_cajadet where   coretiquetadocId=:corEtiId")
    public abstract List<CorEtiquetadoCajaDet> getCorEtiquetadoCajaDetSimp( int corEtiId);
    @Query("SELECT * FROM coretiquetado_cajadet where  coretiquetadocId=:corEtiId and numfotoant=:numfoto")
    public abstract List<CorEtiquetadoCajaDet> getCorrecxnumfotoSimple(int corEtiId,int numfoto);


    @Query("DELETE FROM coretiquetado_cajadet where id=:id")
    public abstract void deleteCorEtiquetadoCajaDet(int id);



    @Query("update coretiquetado_cajadet set estatus=:estatus WHERE id=:id")
    public abstract  void actualizarEstatus(int id, int estatus);

    @Query("update coretiquetado_cajadet set estatusSync=:estatus WHERE id=:id")
    public abstract  void actualizarEstatusSync(int id, int estatus);

    @Query("SELECT * FROM coretiquetado_cajadet WHERE id =:id")
    public abstract   CorEtiquetadoCajaDet findSimple(int id);
    @Query("SELECT * FROM coretiquetado_cajadet WHERE id =:id")
    public abstract   LiveData<CorEtiquetadoCajaDet> find(int id);
    @Query("SELECT * FROM coretiquetado_cajadet WHERE estatusSync =:estatus ")
    public abstract   List<CorEtiquetadoCajaDet> getxEstatusSync(int estatus);

   }
