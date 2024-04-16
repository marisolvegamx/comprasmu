package com.example.comprasmu.data.repositories;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.SustitucionDao;
import com.example.comprasmu.data.modelos.Sustitucion;

import java.util.ArrayList;
import java.util.List;

public class SustitucionRepositoryImpl  extends BaseRepository<Sustitucion> {

    private final SustitucionDao dao;



    public SustitucionRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=comprasDataBase.getSustitucionDao();
    }
    public LiveData<List<Sustitucion>> getByFiltros(int categoria, String productoNombre, int empaque,int tamanio, int clienteId ) {

        String query="Select * from sustitucion where 1=1";
        ArrayList<String> filtros=new ArrayList<String>();

        if(categoria>0) {
            query =query+ " and categoriasId=?";
            filtros.add(categoria+"");
        }
        if(productoNombre!=null&&!productoNombre.equals("")) {
            query = query + " and nomproducto=?";
            filtros.add(productoNombre);
        }
        if(empaque>0) {
            query = query + " and su_tipoempaque=?";
            filtros.add(empaque+"");
        }
        if(tamanio>0) {
            query = query + " and su_tamanio=?";
            filtros.add(tamanio+"");
        }
        if(clienteId>0) {
            query = query + " and clientesId=?";
            filtros.add(clienteId+"");
        }

        // Object[] params=filtros.toArray();
        Log.d("query",query+" "+filtros.toString()+"");

        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,filtros.toArray()
        );

        return dao.getByFiltros(sqlquery);
    }

    public List<Sustitucion> getByFiltrosFrut(int categoria, String productoNombre, int productoorigId,int tamanioorigid,int empaqueorigid) {

        String query="Select * from sustitucion where  categoriasId=1";



        query = query + " and nomproducto like '%"+productoNombre+"%' and su_producto||'.'||su_tamanio||'.'||su_tipoempaque<>'"+productoorigId+"."+tamanioorigid+"."+empaqueorigid+"'";

        //  filtros.add(productoNombre);

        // Object[] params=filtros.toArray();


        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query
        );

        return dao.getByFiltrosSimp(sqlquery);
    }

    public List<Sustitucion> getByFiltrosKerm(int categoria, String productoNombre, int productoorigId,int tamanioorigid,int empaqueorigid) {

        String query="Select * from sustitucion where  categoriasId="+categoria;



        query = query + " and nomproducto like '%"+productoNombre+"%' and su_producto||'.'||su_tamanio||'.'||su_tipoempaque<>'"+productoorigId+"."+tamanioorigid+"."+empaqueorigid+"'";

        //  filtros.add(productoNombre);

        // Object[] params=filtros.toArray();


        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query
        );

        return dao.getByFiltrosSimp(sqlquery);
    }

    public List<Sustitucion> getByFiltrosJumSim(int categoria, String productoNombre, int empaque,int tamanio,int clienteId, int plantaId , int productoorigId) {

        String query="Select * from sustitucion where 1=1 and su_producto||'.'||su_tamanio||'.'||su_tipoempaque<>'"+productoorigId+"."+tamanio+"."+empaque+"'";
        ArrayList<String> filtros=new ArrayList<String>();

        if(categoria>0) {
            query =query+ " and categoriasId=?";
            filtros.add(categoria+"");
        }

        if(empaque>0) {
            query = query + " and su_tipoempaque=?";
            filtros.add(empaque+"");
        }
        if(tamanio>0) {
            query = query + " and su_tamanio=?";
            filtros.add(tamanio+"");
        }
        if(clienteId>0) {
            query = query + " and clientesId=?";
            filtros.add(clienteId+"");
        }
        if(plantaId>0) {
            query = query + " and plantasId=?";
            filtros.add(plantaId+"");
        }
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,filtros.toArray()
        );
        Log.d("SUsTREPOIMP",categoria+"--"+ productoNombre+"--"+empaque+"--"+tamanio+"--"+ clienteId+"--"+plantaId);
        Log.d("SUsTREPOIMP",query);
        return dao.getByFiltrosSimp(sqlquery);
    }


    public LiveData<List<Sustitucion>> getByFiltrosConCods(String categoria, String productoNombre, String empaque,String tamanio, int analisissel) {

        String query="Select  id_sustitucion," +

                "    clientesId," +
                "    su_tipoempaque," +
                "    su_producto," +
                "    su_tamanio," +
                "     nomproducto," +
                "     nomtamanio," +
                "     nomempaque," +
                "    categoriasId," +
                "     nomcategoria," +
                "     caducidad as codigosnoperm from sustitucion" +
                " inner join informe_detalle on informe_datelle.productoId=sustitucion.su_producto" +
                " and informe_detalle.empaquesId=sustitucion.su_tipoempaque and informe_detalle.tamanioId=sustitucion.su_tamanio" +
                " and tipoAnalisis=" +analisissel+
                " where 1=1";
        ArrayList<String> filtros=new ArrayList<String>();

        if(categoria!=null&&!categoria.equals("")) {
            query =query+ " and nomcategoria=?";
            filtros.add(categoria);
        }
        if(productoNombre!=null&&!productoNombre.equals("")) {
            query = query + " and nomproducto=?";
            filtros.add(productoNombre);
        }
        if(empaque!=null&&!empaque.equals("")) {
            query = query + " and nomempaque=?";
            filtros.add(empaque);
        }
        if(tamanio!=null&&!tamanio.equals("")) {
            query = query + " and nomtamanio=?";
            filtros.add(tamanio);
        }


        // Object[] params=filtros.toArray();
        Log.d("query",filtros.toArray()+"");

        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,filtros.toArray()
        );

        return dao.getByFiltros(sqlquery);
    }

    public LiveData<List<Sustitucion>> getAll() {
        return dao.findAll();
    }

    @Override
    public List<Sustitucion> getAllsimple() {
        return dao.getAllSencillo();
    }



    @Override
    public Sustitucion findsimple(int id) {
        return dao.findsimple(id);
    }

    @Override
    public LiveData<Sustitucion> find(int id) {
        return dao.find(id);
    }




    public void delete(Sustitucion object) {
        dao.delete(object);
    }
    public void deleteAll(){
        dao.deleteAll();
    }


    public void insertAll(List<Sustitucion> objects) {
        dao.insertAll(objects);
    }


    public long insert(Sustitucion object) {
        return dao.insert(object);
    }

}