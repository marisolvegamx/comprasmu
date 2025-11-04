package com.example.comprasmu.data.repositories;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import com.example.comprasmu.data.dao.TiendaDao;
import com.example.comprasmu.data.modelos.Tienda;
import java.util.ArrayList;
import java.util.List;

public class TiendaRepositoryImpl extends BaseRepository<Tienda> {

    private static TiendaDao dao;
    private static TiendaRepositoryImpl INSTANCE;



    public static TiendaRepositoryImpl getInstance(TiendaDao comprasdao) {
        if (INSTANCE == null) {
            dao=comprasdao;
            synchronized (TiendaRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE=new TiendaRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }



    public LiveData<List<Tienda>> gettiendasByFiltros( String ciudad, int periodo, int tipo, int cadena, int clienteId) {
        List<String> params= new ArrayList<>();

        String query="Select tienda_estatus.une_id, tienda_estatus.une_descripcion," +
                " tienda_estatus.une_tipoTienda," +
                " tienda_estatus.une_tipotienda, tienda_estatus.une_direccion, tienda_estatus.ciudad," +
                " tienda_estatus.une_cla_ciudad, tienda_estatus.pais, tienda_estatus.une_cla_pais, " +
                " tienda_estatus.une_puntocardinal, tienda_estatus.une_coordenadasxy," +
                " tienda_estatus.une_cadenacomercial, tienda_estatus.une_dir_referencia," +
                " sum(case tienda_estatus.clientesId   when 4 and totplantas=estpep then 0 else 1 end)   estpep," +
                " sum( case tienda_estatus.clientesId  when 5 and totplantas=estpep then 0 else 1 end)   estpen," +
                " sum(case tienda_estatus.clientesId   when 6 and totplantas=estpep then 0 else 1 end)   estele," +
                " sum( case tienda_estatus.clientesId  when 7 and totplantas=estpep then 0 else 1 end)   estjum " +
                " from (Select tienda.une_id, tienda.une_descripcion, tienda.une_tipoTienda," +
                " tienda.une_tipotienda, tienda.une_direccion, tienda.ciudad," +
                " tienda.une_cla_ciudad, tienda.pais, tienda.une_cla_pais, " +
                " tienda.une_puntocardinal, tienda.une_coordenadasxy," +
                " tienda.une_cadenacomercial, tienda.une_dir_referencia,tienda_estatuscliente.clientesId ," +
                " sum(case tienda_estatuscliente.clientesId   when 4 then 1 else 0 end)   estpep," +
                " sum( case tienda_estatuscliente.clientesId  when 5 then 1 else 0 end)   estpen," +
                " sum(case tienda_estatuscliente.clientesId   when 6 then 1 else 0 end)   estele," +
                " sum( case tienda_estatuscliente.clientesId  when 7 then 1 else 0 end)   estjum ," +
                " tienda_estatuscliente.plantasId" +
                " from tienda  " +
                " left join tienda_estatuscliente on tienda.une_id=tienda_estatuscliente.une_id  " +
                " where trim(ciudad)=trim(?) and (periodo<=? or periodo is null)" +
                " group by tienda.une_id " +
                " ) as tienda_estatus" +
                " inner join (SELECT plantasId, clientesId, count(*) as totplantas FROM lista_compras" +
                " where trim(ciudadNombre)=trim(?)" +
                " group by clientesId, ciudadesId) as lista on lista.plantasId=tienda_estatus.plantasId" ;

        params.add(ciudad+"");
        params.add(periodo+"");
        params.add(ciudad+"");
        if(tipo>0) {
            query = query + " and une_tipotienda=?";
            params.add(tipo+"");
        }
        if(cadena>0) {
            query = query + " and une_cadenacomercial=?";
            params.add(cadena+"");
        }
        query=query+" group by tienda_estatus.une_id,tienda_estatus.clientesId" +
                    "     order by tienda_estatus.une_id" ;
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,params.toArray()
        );
        Log.d("TiendaRepositoryImpl","query "+query);
        for (String param:params
             ) {
            Log.d("TiendaRepo","--zzzzzzzzzzzzz"+param);
        }
        return dao.getTiendasByFiltros( sqlquery);
    }


    public List<Tienda> gettiendasByFiltrosSimp( String ciudad) {
        List<String> params= new ArrayList<>();

        String query="Select *" +
                "   from tienda  " +
                " where trim(ciudad)=trim(?)";
        params.add(ciudad+"");
        query=query+"  order by tienda.une_id" ;
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,params.toArray()
        );
        Log.d("TiendaRepositoryImpl","query "+query);
        for (String param:params
        ) {
            Log.d("TiendaRepo","--"+param);
        }
        return dao.getTiendasByFiltrosSimp( sqlquery);
    }

    public List<Tienda> getTiendasByPlantaSimp( String ciudad, int plantaId) {
        List<String> params= new ArrayList<>();

        String query="Select tienda.*" +
                "   from tienda  " +
                " inner join tienda_estatuscliente on tienda.une_id=tienda_estatuscliente.une_id" +
                " where trim(ciudad)=trim(?)" +
                " and plantasId=?";
        params.add(ciudad+"");
        params.add(plantaId+"");
        query=query+"  order by tienda.une_id" ;
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,params.toArray()
        );
        Log.d("TiendaRepositoryImpl","query "+query);
        for (String param:params
        ) {
            Log.d("TiendaRepo","--"+param);
        }
        return dao.getTiendasByFiltrosSimp( sqlquery);
    }

    @Override
    public LiveData<List<Tienda>> getAll() {
      return dao.findAll();
    }

    //no está implementada
    @Override
    public List<Tienda> getAllsimple() {
        return null;
    }


    @Override
    public LiveData<Tienda> find(int id) {
        return dao.find(id);
    }

    public List<Tienda> getByCiudad(String ciudadNombre) {
        return dao.getByCiudad(ciudadNombre);
    }

    @Override
    public Tienda findsimple(int id) {
        return null;
    }

    @Override
    public void delete(Tienda object) {
        dao.delete(object);
    }

    public void deleteAll(){
        dao.deleteAll();
    }
    @Override
    public void insertAll(List<Tienda> objects) {
        dao.insertAll(objects);
    }

    @Override
    public long insert(Tienda object) {
        return dao.insert(object);
    }

}
