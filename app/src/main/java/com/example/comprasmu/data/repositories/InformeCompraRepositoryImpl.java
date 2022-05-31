package com.example.comprasmu.data.repositories;

import android.content.Context;
import android.util.Log;
import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.InformeCompraDao;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeWithDetalle;
import java.util.ArrayList;
import java.util.List;

public class InformeCompraRepositoryImpl   {


    private InformeCompraDao icDao;
    private LiveData<List<InformeCompra>> allInformeCompra;

    public InformeCompraRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        icDao = comprasDataBase.getInformeCompraDao();

    }
    public InformeCompraDao getDao(){
        return icDao;
    }

    public InformeCompra findSimple(int id){
       return icDao.findSimple(id);
    }
    public LiveData<List<InformeCompra>> getPlantasByIndice(String indice, String nombretienda, String ciudad, String planta, int cliente) {

        String query="Select * from lista_compras where indice=?";
        ArrayList<String> filtros=new ArrayList<String>();
        filtros.add(indice);
        if(nombretienda!=null&&!nombretienda.equals("")) {
            query = query + " and nombretienda like ?";

            filtros.add("%"+nombretienda+"%");
        }
        if(ciudad!=null&&!ciudad.equals("")) {
            query = query + " and ciudad like ?";

            filtros.add("%"+ciudad+"%");
        }
        if(planta!=null&&!planta.equals("")) {
            query = query + " and plantaNombre like ?";

            filtros.add("%"+planta+"%");
        }
        if(cliente>0) {
            query = query + " and clientesId = ?";
            filtros.add(cliente+"");
        }
             query= query+  " group by plantasId";
        Object[] params=filtros.toArray();

        for(int i=0;i<params.length;i++)
            Log.d("InformeCompraRepo","***"+params[i]);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                filtros.toArray());
        return icDao.getInformesByFiltros( sqlquery);
    }

   /* public static InformeCompraRepositoryImpl getsInstance() {
        if (sInstance == null) {
            sInstance = new InformeCompraRepositoryImpl();
        }
        return sInstance;
    }*/





    public LiveData<List<InformeCompra>> getAllInformeCompra() {
       return  icDao.getInformes();

    }

    public LiveData<List<InformeCompra>> getAllByVisita(int visitaId) {
        return  icDao.getInformesByVisita(visitaId);

    }
    public List<InformeCompra> getAllByVisitasimple(int visitaId) {
        return  icDao.getInformesByVisitasimple(visitaId);

    }
    public int getLastConsecutivoInforme(String indice, int cliente) {

         return  icDao.getLastConsecutivoInforme( indice, cliente);

    }


    public LiveData<List<InformeCompra>> getSearchResults (String indice, String nombretienda, String ciudad, int planta, int cliente) {
        String query="Select * from informe_compras " +
                "where 1=1";
        ArrayList<String> filtros=new ArrayList<String>();
        if(indice!=null&&!indice.equals("")) {
            query += " and indice=?";
            filtros.add(indice);
        }
        if(nombretienda!=null&&!nombretienda.equals("")) {
            query = query + " and nombretienda like '%?%'";
            filtros.add(nombretienda);
        }
        if(ciudad!=null&&!ciudad.equals("")) {
            query = query + " and ciudad like '%?%'";
            filtros.add(ciudad);
        }
        if(planta>0) {
            query = query + " and plantasId = ?";
            filtros.add(planta+"");
        }
        if(cliente>0) {
            query = query + " and clientesId = ?";
            filtros.add(cliente+"");
        }
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
               filtros.toArray());

        return icDao.getInformesByFiltros(sqlquery);
    }


    public long insertInformeCompra(InformeCompra newInformeCompra) {
       return icDao.addInforme(newInformeCompra);
    }

    public void  insertAll(List<InformeCompra> newInformeCompra) {
         icDao.insertAll(newInformeCompra);
    }

    public long getUltimo() {
        return icDao.getUltimoId();
    }


    public void actualizarEstatus(int id, int estatus) {

         icDao.actualizarEstatus(id, estatus);
    }
    public void actualizarEstatusSync(int id, int estatus) {

        icDao.actualizarEstatusSync(id, estatus);
    }

    public List<InformeCompra> getInformesPendSubir(String indice) {

        return icDao.getInformexEstatus(indice, 0);
    }



    public void deleteInformeCompra(int id) {
       icDao.deleteInforme( id);
    }


    public void deleteInformeCompraByVisita(int visita) {
        icDao.deleteInformesByVisita(visita);
    }


    public LiveData<InformeWithDetalle> getInformeWithDetalleById(int id, int estatus) {
        return icDao.getInformeWithDetalleById(id);
    }

    public InformeWithDetalle getInformeWithDetalleByIdsimple(int id) {
        return icDao.getInformeWithDetalleByIdsimple(id);
    }
    public LiveData<InformeCompra> getInformeCompra(int id) {
        return icDao.getInforme(id);
    }


    public LiveData<List<InformeWithDetalle>> getInformeWithDetalleByVisita(int visita) {
        return icDao.getInformeWithDetalleByVisita(visita);
    }

    public LiveData<List<InformeCompra>> getInformesByVisita(int visita) {
        return icDao.getInformesByVisita(visita);
    }


    public LiveData<List<InformeCompraDao.InformeCompravisita>> getInformesVisitas (String indice, String nombretienda, String ciudad, int planta, String cliente) {
        String query="Select * from InformeCompravisita " +
                "where 1=1";
        ArrayList<String> filtros=new ArrayList<String>();
        if(indice!=null&&!indice.equals("")) {
            query =query+ " and indice=?";
            filtros.add(indice);
        }
        if(nombretienda!=null&&!nombretienda.equals("")) {
            query = query + " and tiendaNombre like ?";
            filtros.add("%"+nombretienda+"%");
        }
        if(ciudad!=null&&!ciudad.equals("")) {
            query = query + " and ciudad like ?";
            filtros.add("%"+ciudad+"%");
        }
        if(planta>0) {
            query = query + " and plantasId=?";
        //    query = query + " and plantaNombre like ?";
            filtros.add(planta+"");
        }
        if(cliente!=null&&!cliente.equals("")) {
            query = query + " and clienteNombre like ?";
            filtros.add("%"+cliente+"%");
        }

        Object[] params=filtros.toArray();

        for(int i=0;i<params.length;i++)
            Log.d("InformeCompraRepo","***"+params[i]);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,filtros.toArray()
                );

       return icDao.getInformesWithVisita(sqlquery);
    }


   /* public LiveData<List<InformeCompraDao.InformeCompravisita>> getInformesVisitas (String indice, String nombretienda, String ciudad, int planta, int cliente) {
        String query="SELECT indice, createdAt as fecha,clienteNombre, plantaNombre,nombreTienda, " +
                "            informe_compras.estatus as estatusinforme, informe_compras.id as idinforme " +
                "            FROM informe_compras, visitas " +
                "            WHERE informe_compras.visitasId = visitas.id ";
        ArrayList<String> filtros=new ArrayList<String>();
        if(indice!=null&&!indice.equals("")) {
            query += " and visitas.indice=?";
            filtros.add(indice);
        }
        if(nombretienda!=null&&!nombretienda.equals("")) {
            query = query + " and visita.tiendaNombre like '%?%'";
            filtros.add(nombretienda);
        }
        if(ciudad!=null&&!ciudad.equals("")) {
            query = query + " and visita.ciudad like '%?%'";
            filtros.add(ciudad);
        }
        if(planta>0) {
            query = query + " and plantasId = ?";
            filtros.add(planta+"");
        }
        if(cliente>0) {
            query = query + " and clientesId = ?";
            filtros.add(cliente+"");
        }
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                filtros.toArray());

        return icDao.getInformesVisitaByFiltros(sqlquery);
    }*/


}
