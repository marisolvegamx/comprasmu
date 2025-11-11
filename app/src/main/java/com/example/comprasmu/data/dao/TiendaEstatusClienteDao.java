package com.example.comprasmu.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import com.example.comprasmu.data.modelos.TiendaEstatusCliente;
import java.util.List;

@Dao
public abstract class TiendaEstatusClienteDao extends BaseDao<TiendaEstatusCliente>{

    @Query("SELECT * FROM tienda_estatuscliente")
    public  abstract LiveData<List<TiendaEstatusCliente>> findAll();

    @Query("delete FROM tienda_estatuscliente")
    public  abstract void deleteAll();

    @Query("SELECT * FROM tienda_estatuscliente where une_id=:id and clientesId=:clientesId")
    public abstract LiveData<TiendaEstatusCliente> find(int id, int clientesId);

    @Query("SELECT * FROM tienda_estatuscliente where une_id=:idTienda")
    public abstract List<TiendaEstatusCliente> findByTienda(int idTienda);



}
