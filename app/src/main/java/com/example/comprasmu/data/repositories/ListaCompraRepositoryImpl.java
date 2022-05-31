package com.example.comprasmu.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.sqlite.db.SimpleSQLiteQuery;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.BaseDao;
import com.example.comprasmu.data.dao.InformeCompraDao;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaWithDetalle;

import java.util.ArrayList;
import java.util.List;

public class ListaCompraRepositoryImpl extends BaseRepository<ListaCompra> {

    private static ListaCompraDao dao;

    private LiveData<List<InformeCompra>> allInformeCompra;
    private static ListaCompraRepositoryImpl INSTANCE;
    /*public ListaCompraRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=comprasDataBase.getListaCompraDao();
    }*/

    public static ListaCompraRepositoryImpl getInstance(ListaCompraDao comprasdao) {
        if (INSTANCE == null) {
            dao=comprasdao;
            synchronized (ListaCompraRepositoryImpl.class) {
                if (INSTANCE == null) {
                    INSTANCE=new ListaCompraRepositoryImpl();
                }
            }
        }
        return INSTANCE;
    }
    public LiveData<List<ListaCompra>> getAllByIndice(String indice) {
        return dao.findAllByIndice(indice);
    }
    public List<ListaCompra> getAllByIndicesimple(String indice) {
        return dao.findAllByIndicesimple(indice);
    }
    public LiveData<ListaCompra> getByFiltros(String indice,int idPlanta, int idCliente ) {

        String query="Select * from lista_compras where indice=?" +
                "and plantasId=?";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
               new Object[]{indice,idPlanta});
        return dao.getListaByFiltros( sqlquery);
    }
    public List<ListaCompra> getByPlanta(int idPlanta ) {

        String query="Select * from lista_compras where " +
                " plantasId=?";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{idPlanta});
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }
    public LiveData<List<ListaCompra>> getAllByIndiceCiudad(String indice,String idCiudad) {

        String query="Select * from lista_compras where indice=?" +
                "and ciudadNombre like ? group by plantasId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{indice,idCiudad});
        return dao.getListaCompraByFiltros( sqlquery);
    }
    public LiveData<List<ListaCompra>> getAllByIndiceCiudadSimpl(String indice,String idCiudad) {

        String query="Select * from lista_compras where indice=?" +
                "and ciudadNombre like ? group by plantasId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{indice,idCiudad});
        return dao.getListaCompraByFiltros( sqlquery);
    }

    public LiveData<List<ListaCompra>> getClientesByIndiceCiudad(String indice,String idCiudad) {
        List<String> params= new ArrayList<>();
        params.add(indice);
        String query="Select * from lista_compras where indice=?";
        if(!idCiudad.equals("")) {
            query = query + " and ciudadNombre like ?";
            params.add(idCiudad);
        }
         query=query+               " group by clientesId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,params.toArray()
               );
        return dao.getListaCompraByFiltros( sqlquery);
    }
    public List<ListaCompra> getClientesByIndiceCiudadSimpl(String indice,String idCiudad) {
        List<String> params= new ArrayList<>();
        params.add(indice);
        String query="Select * from lista_compras where indice=?";
        if(idCiudad!=null&&!idCiudad.equals("")) {
            query = query + " and ciudadNombre like ?";
            params.add(idCiudad);
        }
        query=query+ " group by clientesId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,params.toArray()
        );
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }

    public List<ListaCompra> getClientesByIndiceCiudadSimplsp(String indice,String idCiudad) {
        List<String> params= new ArrayList<>();
        params.add(indice);
        String query="Select * from lista_compras where indice=? and clientesId!=4";
        if(!idCiudad.equals("")) {
            query = query + " and ciudadNombre like ?";
            params.add(idCiudad);
        }
        query=query+               " group by clientesId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,params.toArray()
        );
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }
    public LiveData<List<ListaCompra>> getAllByIndiceCiudadCliente(String indice,String idCiudad,int idCliente) {

        String query="Select * from lista_compras where indice=?" +
                "and ciudadNombre=? and clientesId=? group by plantasId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{indice,idCiudad,idCliente});
        return dao.getListaCompraByFiltros( sqlquery);
    }
    public List<ListaCompra> getAllByIndiceCiudadClienteSim(String indice,String ciudad,int idCliente) {

        String query="Select * from lista_compras where indice=?" +
                "and ciudadNombre=? and clientesId=? group by plantasId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{indice,ciudad,idCliente});
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }
    public LiveData<List<ListaWithDetalle>> getListaWithDetalleByFiltros(String indice, int idPlanta, int idCliente ) {
      /*  String query="Select " +
                "lc.id," +
                "   lc.plantasId," +
                " lc.plantaNombre," +
                "lc.siglas," +
                "lc.clientesId," +
                "lc.clienteNombre," +
                "lc.ciudadesId," +
                "lc.ciudadNombre," +
                "lc.createdBy," +
                "lc.indice," +
                "lc.estatus," +
                "lc.lis_nota," +
                "lcd.id," +
                "lcd.listaId," +
                "lcd.productosId," +
                "lcd.productoNombre," +
                "lcd.tamanio," +
                "lcd.tamanioId," +
                "lcd.empaque," +
                "lcd.empaquesId," +
                "lcd.tipoAnalisis," +
                "lcd.analisisId," +
                "lcd.cantidad," +
                "lcd.codigosNoPermitidos," +
                "lcd.nvoCodigo," +
                "lcd.estatus," +
                "lcd.comprados," +
                "lcd.tipoMuestra," +
                "lcd.nombreTipoMuestra," +
                "lcd.categoriaid," +
                "lcd.categoria," +
                "lcd.lid_fechapermitida," +
                "lcd.lid_fecharestringida," +
                "lcd.lid_orden" +
                " from lista_compras as lc " +
                "inner join lista_compras_detalle as lcd " +
                "on lcd.listaId=lc.id  where lc.indice=? " +
                "and plantasId=? order by "+
                "productosId," +
                "tamanioId ASC," +
                "empaquesId ASC," +
                "analisisId ASC,tipoMuestra";*/
       String query="Select * from lista_compras  " +
                " where indice=? " +
                "and plantasId=?";
        Log.d("wiiiiiiii",query);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{indice,idPlanta});
        Log.d("LISTACOMPRREPIMP",indice+"---"+idPlanta+"--"+sqlquery.getSql());


        return dao.getListasWithDetalleByFiltros( sqlquery);
    }


        @Override
    public LiveData<List<ListaCompra>> getAll() {
      return dao.findAll();
    }

    @Override
    public List<ListaCompra> getAllsimple() {
        return null;
    }

    @Override
    public LiveData<ListaCompra> find(int id) {
        return dao.find(id);
    }

    @Override
    public ListaCompra findsimple(int id) {
        return null;
    }


    @Override
    public void delete(ListaCompra object) {
        dao.delete(object);
    }

    @Override
    public void insertAll(List<ListaCompra> objects) {
        dao.insertAll(objects);
    }

    public LiveData<List<ListaCompra>> getPlantas( String indice){
        return dao.findPlantas( indice);
    }

    public LiveData<List<ListaCompra>> getCiudades( String indice){
        return dao.findCiudades( indice);
    }

    @Override
    public long insert(ListaCompra object) {
        return dao.insert(object);
    }
    public long updateSC(ListaCompra object) {
        return dao.insert(object);
    }


}
