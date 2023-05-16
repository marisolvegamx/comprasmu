package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;


import com.example.comprasmu.data.modelos.Sigla;

import java.util.List;

@Dao
public abstract class SiglaDao extends BaseDao<Sigla> {

    @Query("DELETE FROM siglas where id=:cat")
    public abstract void delete(int cat);




    @Query("SELECT * FROM siglas ")
    public  abstract LiveData<List<Sigla>> findAll();

    @Query("SELECT * FROM siglas ")
    public  abstract List<Sigla> findAllsimple();

    @Query("SELECT * FROM siglas where id=:id")
    public abstract LiveData<Sigla> find( int id);
    @Query("SELECT * FROM siglas where id=:id")
    public abstract Sigla findsimple( int id);

    @Query("SELECT * FROM siglas where siglas=:sigla and clientesId=:cliente")
    public abstract Sigla  getByClienteSig( String sigla, int cliente);

    @Query("delete FROM siglas")
    public abstract void deleteAll();








  /*  @Query("DELETE FROM siglas where cad_idcatalogo=:cat and cad_idopcion=:id")
    public abstract void delete( int id,int cat);
*/
}
