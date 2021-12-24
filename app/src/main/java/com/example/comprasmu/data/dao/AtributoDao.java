package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;

import androidx.room.Query;

import com.example.comprasmu.data.modelos.Atributo;


import java.util.List;

@Dao
public abstract class AtributoDao extends BaseDao<Atributo> {

    @Query("DELETE FROM atributos where id_atributo=:cat")
    public abstract void delete(int cat);




    @Query("SELECT * FROM atributos ")
    public  abstract LiveData<List<Atributo>> findAll();

    @Query("SELECT * FROM atributos ")
    public  abstract List<Atributo> findAllsimple();

    @Query("SELECT * FROM atributos where id_atributo=:id")
    public abstract LiveData<Atributo> find( int id);
    @Query("SELECT * FROM atributos where id_atributo=:id")
    public abstract Atributo findsimple( int id);
    //TODO agregar el cliente
    @Query("SELECT * FROM atributos where id_tipoempaque=:id and at_idcliente=:cliente")
    public abstract List<Atributo>  getByEmpaqueCliente( int id, int cliente);








  /*  @Query("DELETE FROM atributos where cad_idcatalogo=:cat and cad_idopcion=:id")
    public abstract void delete( int id,int cat);
*/
}
