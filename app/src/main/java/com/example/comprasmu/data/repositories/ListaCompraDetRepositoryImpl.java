package com.example.comprasmu.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.dao.ListaCompraDetalleDao;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ListaWithDetalle;

import java.util.ArrayList;
import java.util.List;

public class ListaCompraDetRepositoryImpl {

    private ListaCompraDetalleDao dao;



    public ListaCompraDetRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=comprasDataBase.getListaCompraDetalleDao();
    }
    public LiveData<List<ListaCompraDetalle>> getDetalleByFiltros(int idlista,String categoria, String productoNombre, String empaque,int tamanio,String analisis,int detorig ) {

        String query="select id,  listaId, productosId, productoNombre, " +
                "                   tamanio,  tamanioId,  empaque,   empaquesId, tipoAnalisis," +
                "             analisisId, cantidad,  nvoCodigo,estatus, " +
                "                     comprados,  tipoMuestra, nombreTipoMuestra, categoriaid,categoria," +
                "                  lid_fechapermitida,     lid_fecharestringida,  lid_orden,   lid_backup, " +
                "max(codfis)||';'||max(codsen)||';'||max(codtor)||';'||max(codmic) as codigosNoPermitidos from (select   id,  listaId, productosId, productoNombre," +
                "   tamanio,  tamanioId,  empaque,   empaquesId, tipoAnalisis," +
                "analisisId, cantidad,  codigosNoPermitidos,  nvoCodigo,estatus, " +
                "     comprados,  tipoMuestra, nombreTipoMuestra, categoriaid,categoria," +
                "  lid_fechapermitida,     lid_fecharestringida,  lid_orden,   lid_backup," +
                "case analisisId when 1 then codigosNoPermitidos else '' end  codfis," +
                " case analisisId when 2 then codigosNoPermitidos else '' end  codsen," +
                "case analisisId when 3 then codigosNoPermitidos else '' end  codtor," +
                "case analisisId when 4 then codigosNoPermitidos else '' end  codmic" +
                " from lista_compras_detalle where listaId=?";
        ArrayList<String> filtros=new ArrayList<String>();
        filtros.add(idlista+"");
        if(categoria!=null&&!categoria.equals("")) {
            query =query+ " and categoria=?";
            filtros.add(categoria);
        }
        if(productoNombre!=null&&!productoNombre.equals("")) {
            query = query + " and productoNombre=?";
            filtros.add(productoNombre);
        }
        if(empaque!=null&&!empaque.equals("")) {
            query = query + " and empaque=?";
            filtros.add(empaque);
        }
        if(tamanio>0) {
            query = query + " and tamanioId=?";
            filtros.add(tamanio+"");
        }
        if(analisis!=null&&!analisis.equals("")) {
            query = query + " and analisisId=?";
            filtros.add(analisis);
        }
        query=query+" )  prin group by productoNombre,empaque,tamanioId ";


        //Select * from lista_compras_detalle where listaId=54 and categoria='CARBONATADAS' and productoNombre='PEPSI' and empaque='PET' and tamanio=13

        Object[] params=filtros.toArray();

        for(int i=0;i<params.length;i++)
            Log.d("InformeCompraRepo","***"+params[i]);
        Log.d("InformeCompraRepo","****"+query);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,filtros.toArray()
        );

        return dao.getDetallesByFiltros(sqlquery);
    }


    public LiveData<List<ListaCompraDetalle>> consultaSensorial4(int idlista,String categoria, String productoNombre, String empaque,int tamanio,String analisis,int detorig ) {

        String query="Select * from lista_compras_detalle where listaId=?";
        ArrayList<String> filtros=new ArrayList<String>();
        filtros.add(idlista+"");

        if(productoNombre!=null&&!productoNombre.equals("")) {
            query = query + " and ((productoNombre=?";
            filtros.add(productoNombre);
        }
        if(empaque!=null&&!empaque.equals("")) {
            query = query + " and empaque=?";
            filtros.add(empaque);
        }
        if(tamanio>0) {
            query = query + " and tamanio=?) or (productoNombre!=? and empaque!=?))";
            filtros.add(tamanio+"");
            filtros.add(productoNombre);
            filtros.add(empaque);
        }

      //  query=query+" )  prin group by productoNombre,empaque,tamanioId ";

        Object[] params=filtros.toArray();

        for(int i=0;i<params.length;i++)
            Log.d("InformeCompraRepo","***"+params[i]);
        Log.d("InformeCompraRepo","****"+query);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,filtros.toArray()
        );

        return dao.getDetallesByFiltros(sqlquery);
    }

    public LiveData<List<ListaCompraDetalle>> consultaFisico4(int idlista,String categoria, String productoNombre, String empaque,int tamanio,String analisis,int detorig ) {

        String query="select id,  listaId, productosId, productoNombre, " +
                "                   tamanio,  tamanioId,  empaque,   empaquesId, tipoAnalisis," +
                "             analisisId, cantidad,  nvoCodigo,estatus, " +
                "                     comprados,  tipoMuestra, nombreTipoMuestra, categoriaid,categoria," +
                "                  lid_fechapermitida,     lid_fecharestringida,  lid_orden,   lid_backup, " +
                "max(codfis)||';'||max(codsen)||';'||max(codtor)||';'||max(codmic) as codigosNoPermitidos from (select   id,  listaId, productosId, productoNombre," +
                "   tamanio,  tamanioId,  empaque,   empaquesId, tipoAnalisis," +
                "analisisId, cantidad,  codigosNoPermitidos,  nvoCodigo,estatus, " +
                "     comprados,  tipoMuestra, nombreTipoMuestra, categoriaid,categoria," +
                "  lid_fechapermitida,     lid_fecharestringida,  lid_orden,   lid_backup," +
                "case analisisId when 1 then codigosNoPermitidos else '' end  codfis," +
                " case analisisId when 2 then codigosNoPermitidos else '' end  codsen," +
                "case analisisId when 3 then codigosNoPermitidos else '' end  codtor," +
                "case analisisId when 4 then codigosNoPermitidos else '' end  codmic" +
                " from lista_compras_detalle where listaId=?";
        ArrayList<String> filtros=new ArrayList<String>();
        filtros.add(idlista+"");
        if(categoria!=null&&!categoria.equals("")) {
            query =query+ " and ((categoria=?";
            filtros.add(categoria);
        }
        if(productoNombre!=null&&!productoNombre.equals("")) {
            query = query + " and productoNombre=?";
            filtros.add(productoNombre);
        }
        if(empaque!=null&&!empaque.equals("")) {
            query = query + " and empaque=?";
            filtros.add(empaque);
        }
        if(tamanio>0) {
            query = query + " and tamanioId=?))";
            filtros.add(tamanio+"");
          //  filtros.add(categoria+"");
        }

        query=query+" )  prin group by productoNombre,empaque,tamanioId ";


        Object[] params=filtros.toArray();

        for(int i=0;i<params.length;i++)
            Log.d("InformeCompraRepo","***"+params[i]);
        Log.d("InformeCompraRepo","****"+query);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,filtros.toArray()
        );

        return dao.getDetallesByFiltros(sqlquery);
    }
    //el ultimo parametro enviado debe ser diferente
    public LiveData<List<ListaCompraDetalle>> getDetalleByFiltrosUD(int idlista,String categoria, String productoNombre, String empaque,int tamanio ) {

        String query="select id,  listaId, productosId, productoNombre, " +
                "                   tamanio,  tamanioId,  empaque,   empaquesId, tipoAnalisis," +
                "             analisisId, cantidad,  nvoCodigo,estatus, " +
                "                     comprados,  tipoMuestra, nombreTipoMuestra, categoriaid,categoria," +
                "                  lid_fechapermitida,     lid_fecharestringida,  lid_orden,   lid_backup, " +
                "max(codfis)||';'||max(codsen)||';'||max(codtor)||';'||max(codmic) as codigosNoPermitidos from (select   id,  listaId, productosId, productoNombre," +
                "   tamanio,  tamanioId,  empaque,   empaquesId, tipoAnalisis," +
                "analisisId, cantidad,  codigosNoPermitidos,  nvoCodigo,estatus, " +
                "     comprados,  tipoMuestra, nombreTipoMuestra, categoriaid,categoria," +
                "  lid_fechapermitida,     lid_fecharestringida,  lid_orden,   lid_backup," +
                "case analisisId when 1 then codigosNoPermitidos else '' end  codfis," +
                " case analisisId when 2 then codigosNoPermitidos else '' end  codsen," +
                "case analisisId when 3 then codigosNoPermitidos else '' end  codtor," +
                "case analisisId when 4 then codigosNoPermitidos else '' end  codmic" +
                " from lista_compras_detalle where listaId=?";
        ArrayList<String> filtros=new ArrayList<String>();
        filtros.add(idlista+"");
        if(productoNombre==null||productoNombre.equals("")) //catego es el ultimo y es diferente
        { if(categoria!=null&&!categoria.equals("")) {
            query =query+ " and categoria!=?";
            filtros.add(categoria);
        }}else
        if(categoria!=null&&!categoria.equals("")) {
            query =query+ " and categoria=?";
            filtros.add(categoria);
        }
        if(empaque==null||empaque.equals("")) //prdo es el ultimo y es diferente
        {   if(productoNombre!=null&&!productoNombre.equals("")) {
                query = query + " and productoNombre!=?";
                filtros.add(productoNombre);
            }}
        else
        if(productoNombre!=null&&!productoNombre.equals("")) {
            query = query + " and productoNombre=?";
            filtros.add(productoNombre);
        }
        if(tamanio==0)
        { if(empaque!=null&&!empaque.equals("")) {
            query = query + " and empaque!=?";
            filtros.add(empaque);
        }}else
        if(empaque!=null&&!empaque.equals("")) {
            query = query + " and empaque=?";
            filtros.add(empaque);
        }

        if(tamanio>0) {
            query = query + " and tamanioId!=?";
            filtros.add(tamanio+"");
        }


        query=query+" )  prin group by productoNombre,empaque,tamanioId ";
        Object[] params=filtros.toArray();

         for(int i=0;i<params.length;i++)
          Log.d("InformeCompraRepo","***"+params[i]);
        Log.d("InformeCompraRepo","****"+query);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,filtros.toArray()
        );

        return dao.getDetallesByFiltros(sqlquery);
    }

    //el ultimo parametro enviado debe ser diferente
    public LiveData<List<ListaCompraDetalle>> consultaTorque2(int idlista,String categoria, String productoNombre, String empaque ) {

        String query="select id,  listaId, productosId, productoNombre, " +
                "                   tamanio,  tamanioId,  empaque,   empaquesId, tipoAnalisis," +
                "             analisisId, cantidad,  nvoCodigo,estatus, " +
                "                     comprados,  tipoMuestra, nombreTipoMuestra, categoriaid,categoria," +
                "                  lid_fechapermitida,     lid_fecharestringida,  lid_orden,   lid_backup, " +
                "max(codfis)||';'||max(codsen)||';'||max(codtor)||';'||max(codmic) as codigosNoPermitidos from (select   id,  listaId, productosId, productoNombre," +
                "   tamanio,  tamanioId,  empaque,   empaquesId, tipoAnalisis," +
                "analisisId, cantidad,  codigosNoPermitidos,  nvoCodigo,estatus, " +
                "     comprados,  tipoMuestra, nombreTipoMuestra, categoriaid,categoria," +
                "  lid_fechapermitida,     lid_fecharestringida,  lid_orden,   lid_backup," +
                "case analisisId when 1 then codigosNoPermitidos else '' end  codfis," +
                " case analisisId when 2 then codigosNoPermitidos else '' end  codsen," +
                "case analisisId when 3 then codigosNoPermitidos else '' end  codtor," +
                "case analisisId when 4 then codigosNoPermitidos else '' end  codmic" +
                " from lista_compras_detalle where listaId=?";
        ArrayList<String> filtros=new ArrayList<String>();
        filtros.add(idlista+"");

        if(categoria!=null&&!categoria.equals("")) {
            query =query+ " and categoria=?";
            filtros.add(categoria);
        }
       if(productoNombre!=null&&!productoNombre.equals("")) {
            query = query + " and productoNombre!=?";
            filtros.add(productoNombre);
        }
        if(empaque!=null&&!empaque.equals("")) {
            query = query + " and empaque=?";
            filtros.add(empaque);
        }
        query=query+" )  prin group by productoNombre,empaque,tamanioId ";
         Object[] params=filtros.toArray();

        for(int i=0;i<params.length;i++)
            Log.d("InformeCompraRepo","***"+params[i]);
        Log.d("InformeCompraRepo","****"+query);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,filtros.toArray()
        );

        return dao.getDetallesByFiltros(sqlquery);
    }
    //el ultimo parametro enviado debe ser diferente
    public LiveData<List<ListaCompraDetalle>> consultaTorque4(int idlista,String categoria, String empaque ) {

        String query="Select * from lista_compras_detalle where listaId=?";
        ArrayList<String> filtros=new ArrayList<String>();
        filtros.add(idlista+"");

        if(categoria!=null&&!categoria.equals("")) {
            query =query+ " and categoria=?";
            filtros.add(categoria);
        }

        if(empaque!=null&&!empaque.equals("")) {
            query = query + " and empaque!=?";
            filtros.add(empaque);
        }

        Object[] params=filtros.toArray();

     //   for(int i=0;i<params.length;i++)
      //      Log.d("InformeCompraRepo","***"+params[i]);
             Log.d("InformeCompraRepo","****"+query);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,filtros.toArray()
        );

        return dao.getDetallesByFiltros(sqlquery);
    }
    //el ultimo param debe ser diferente pero aqui viene el analisis
    public LiveData<List<ListaCompraDetalle>> getDetalleByFiltrosUDA(int idlista,String categoria,int analisis, String productoNombre, String empaque,int tamanio ) {

        String query="Select * from lista_compras_detalle where listaId=?";
        ArrayList<String> filtros=new ArrayList<String>();
        filtros.add(idlista+"");
        if(analisis==0) //catego es el ultimo y es diferente
        { if(categoria!=null&&!categoria.equals("")) {
            query =query+ " and categoria!=?";
            filtros.add(categoria);
        }}else
        if(categoria!=null&&!categoria.equals("")) {
            query =query+ " and categoria=?";
            filtros.add(categoria);
        }
        if(productoNombre==null||productoNombre.equals("")) //analisis es el ultimo y es diferente
        {
            if(analisis>0) {
                query = query + " and analisisId!=?";
                filtros.add(analisis+"");
            }
        }else
        if(analisis>0) {
            query = query + " and analisisId=?";
            filtros.add(analisis+"");
        }
        if(empaque==null||empaque.equals("")) //prdo es el ultimo y es diferente
        {   if(productoNombre!=null&&!productoNombre.equals("")) {
            query = query + " and productoNombre!=?";
            filtros.add(productoNombre);
        }}
        else
        if(productoNombre!=null&&!productoNombre.equals("")) {
            query = query + " and productoNombre=?";
            filtros.add(productoNombre);
        }
        if(tamanio==0)
        { if(empaque!=null&&!empaque.equals("")) {
            query = query + " and empaque!=?";
            filtros.add(empaque);
        }}else
        if(empaque!=null&&!empaque.equals("")) {
            query = query + " and empaque=?";
            filtros.add(empaque);
        }

        if(tamanio>0) {
            query = query + " and tamanioId!=?";
            filtros.add(tamanio+"");
        }



        Object[] params=filtros.toArray();

        for(int i=0;i<params.length;i++)
            Log.d("InformeCompraRepo","getDetalleByFiltrosUDA***"+params[i]);
        Log.d("InformeCompraRepo","getDetalleByFiltrosUDA****"+query);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,filtros.toArray()
        );

        return dao.getDetallesByFiltros(sqlquery);
    }

    //el ultimo param debe ser diferente pero aqui viene el analisis
    public LiveData<List<ListaCompraDetalle>> getDetalleByFiltrosUDA2(int idlista,String categoria,int analisis, String productoNombre, String empaque,int tamanio ) {

        String query="select id,  listaId, productosId, productoNombre, " +
                "                   tamanio,  tamanioId,  empaque,   empaquesId, tipoAnalisis," +
                "             analisisId, cantidad,  nvoCodigo,estatus, " +
                "                     comprados,  tipoMuestra, nombreTipoMuestra, categoriaid,categoria," +
                "                  lid_fechapermitida,     lid_fecharestringida,  lid_orden,   lid_backup, " +
                "max(codfis)||';'||max(codsen)||';'||max(codtor)||';'||max(codmic) as codigosNoPermitidos from (select   id,  listaId, productosId, productoNombre," +
                "   tamanio,  tamanioId,  empaque,   empaquesId, tipoAnalisis," +
                "analisisId, cantidad,  codigosNoPermitidos,  nvoCodigo,estatus, " +
                "     comprados,  tipoMuestra, nombreTipoMuestra, categoriaid,categoria," +
                "  lid_fechapermitida,     lid_fecharestringida,  lid_orden,   lid_backup," +
                "case analisisId when 1 then codigosNoPermitidos else '' end  codfis," +
                " case analisisId when 2 then codigosNoPermitidos else '' end  codsen," +
                "case analisisId when 3 then codigosNoPermitidos else '' end  codtor," +
                "case analisisId when 4 then codigosNoPermitidos else '' end  codmic" +
                " from lista_compras_detalle where listaId=?";
        ArrayList<String> filtros=new ArrayList<String>();
        filtros.add(idlista+"");
        if(analisis==0) //catego es el ultimo y es diferente
        { if(categoria!=null&&!categoria.equals("")) {
            query =query+ " and categoria!=?";
            filtros.add(categoria);
        }}else
        if(categoria!=null&&!categoria.equals("")) {
            query =query+ " and categoria=?";
            filtros.add(categoria);
        }
        if(productoNombre==null||productoNombre.equals("")) //analisis es el ultimo y es diferente
        {
            if(analisis>0) {
                query = query + " and analisisId!=?";
                filtros.add(analisis+"");
            }
        }else
        if(analisis>0) {
            query = query + " and analisisId=?";
            filtros.add(analisis+"");
        }
        if(empaque==null||empaque.equals("")) //prdo es el ultimo y es diferente
        {   if(productoNombre!=null&&!productoNombre.equals("")) {
            query = query + " and productoNombre!=?";
            filtros.add(productoNombre);
        }}
        else
        if(productoNombre!=null&&!productoNombre.equals("")) {
            query = query + " and productoNombre=?";
            filtros.add(productoNombre);
        }
        if(tamanio==0)
        { if(empaque!=null&&!empaque.equals("")) {
            query = query + " and empaque!=?";
            filtros.add(empaque);
        }}else
        if(empaque!=null&&!empaque.equals("")) {
            query = query + " and empaque=?";
            filtros.add(empaque);
        }

        if(tamanio>0) {
            query = query + " and tamanioId!=?";
            filtros.add(tamanio+"");
        }

        query=query+" )  prin group by productoNombre,empaque,tamanioId ";

        Object[] params=filtros.toArray();

        for(int i=0;i<params.length;i++)
            Log.d("InformeCompraRepo","getDetalleByFiltrosUDA***"+params[i]);
        Log.d("InformeCompraRepo","getDetalleByFiltrosUDA****"+query);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,filtros.toArray()
        );

        return dao.getDetallesByFiltros(sqlquery);
    }

    public LiveData<List<ListaCompraDetalle>> getAll() {
      return dao.findAll();
    }





    public LiveData<List<ListaCompraDetalle>> getAllByLista(int listasId) {
        return dao.getListaDetallesByLista(listasId);
    }
    public List<ListaCompraDetalle> getAllByListasimple(int listasId) {
        return dao.getListaDetallesByListasimple(listasId);
    }


    public LiveData<ListaCompraDetalle> find(int lista,int id) {
        return dao.find(lista,id);
    }

    public ListaCompraDetalle findsimple(int lista,int id) {
        return dao.findsimple(lista,id);
    }
    public ListaCompraDetalle getByProductoAna(int prodid,int empid,int tamid,int anaid, String indice, int planta) {
        return dao.getByProductoAna( prodid, empid, tamid, anaid,  indice, planta);
    }


    public void delete(ListaCompraDetalle object) {
        dao.delete(object);
    }


    public void insertAll(List<ListaCompraDetalle> objects) {
        dao.insertAll(objects);
    }



    public long insert(ListaCompraDetalle object) {
        return dao.insert(object);
    }
    public void actualizarComprados(int id,int listaid, int cantidad){
        dao.actualizarCompra(id,  listaid, cantidad);
    }

    public void deleteByLista(int id) {
        dao.deleteByLista(id);
    }
    public LiveData<List<ListaCompraDetalle>> getListaDetalleOrd(int listaid ) {
        return dao.getListasDetalleOrdByLista(listaid);
    }
}
