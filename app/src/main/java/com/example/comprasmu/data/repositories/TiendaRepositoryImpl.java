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

        String query="select" +
                " tienda.une_id," +
                " tienda.une_descripcion," +
                " tienda.une_tipotienda," +
                " tienda.une_direccion," +
                " tienda.ciudad," +
                " tienda.une_cla_ciudad," +
                " tienda.pais," +
                " tienda.une_cla_pais," +
                " tienda.une_puntocardinal," +
                " tienda.une_coordenadasxy," +
                " tienda.une_cadenacomercial," +
                " tienda.une_dir_referencia," +
                " tienda_estatuscliente.clientesId ," +
               /* " case when estpep<>2 then sum(case tienda_estatuscliente.clientesId when 4 then 1 else 0 end)  else 2 end estpep," +
                " case when estpen<>2 then sum( case tienda_estatuscliente.clientesId when 5 then 1 else 0 end) else 2 end estpen," +
                " case when estele<>2 then sum(case tienda_estatuscliente.clientesId when 6 then 1 else 0 end)  else 2 end estele," +
                " case when estjum<>2 then sum( case tienda_estatuscliente.clientesId when 7 then 1 else 0 end) else 2 end estjum ," +*/
                " estpep," +
                " estpen," +
                " estele," +
                " estjum," +
                " tienda_estatuscliente.plantasId" +
                " from" +
                " tienda" +
                " left join tienda_estatuscliente on" +
                " tienda.une_id = tienda_estatuscliente.une_id" +
                " where" +
                " trim(ciudad)= trim(?)" +
                " and (periodo <= ?" +
                "  or periodo is null) " ;

        params.add(ciudad+"");
        params.add(periodo+"");
        if(tipo>0) {
            query = query + " and tienda.une_tipotienda=?";
            params.add(tipo+"");
        }
        if(cadena>0) {
            query = query + " and tienda.une_cadenacomercial=?";
            params.add(cadena+"");
        }
        query=query+" group by tienda.une_id" +
                    "     order by tienda.une_id" ;
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

    public LiveData<List<Tienda>> getTiendasIndiceAct( String ciudad, int periodo, int tipo, int cadena) {
        List<String> params= new ArrayList<>();

        String query="select" +
                " tiendas.une_id," +
                " tiendas.une_descripcion," +
                " tiendas.une_tipotienda," +
                " tiendas.une_direccion," +
                " tiendas.ciudad," +
                " tiendas.une_cla_ciudad," +
                " tiendas.pais," +
                " tiendas.une_cla_pais," +
                " tiendas.une_puntocardinal," +
                " tiendas.une_coordenadasxy," +
                " tiendas.une_cadenacomercial," +
                " tiendas.une_dir_referencia," +
                " tienda_estatuscliente.clientesId ," +
                " estpep," +
                " estpen," +
                " estele," +
                " estjum," +
                " tienda_estatuscliente.plantasId" +
                " from" +
                " (select * from visitas inner join " +
                "                 tienda on tiendaid= tienda.une_id" +
                "                      group by  tienda.une_id) as tiendas " +
                "                 left join tienda_estatuscliente on" +
                "                 tiendas.une_id = tienda_estatuscliente.une_id" +
                " where" +
                " trim(ciudad)= trim(?)" +
                " and (periodo <= ?" +
                "  or periodo is null) " ;

        params.add(ciudad+"");
        params.add(periodo+"");

        if(tipo>0) {
            query = query + " and tiendas.une_tipotienda=?";
            params.add(tipo+"");
        }
        if(cadena>0) {
            query = query + " and tiendas.une_cadenacomercial=?";
            params.add(cadena+"");
        }
        query=query+" group by tiendas.une_id" +
                "     order by tiendas.une_id" ;
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

}
