package com.example.comprasmu.data.repositories;


import androidx.lifecycle.LiveData;
import androidx.sqlite.db.SimpleSQLiteQuery;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaWithDetalle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListaCompraRepositoryImpl extends BaseRepository<ListaCompra> {

    private static ListaCompraDao dao;
    private static ListaCompraRepositoryImpl INSTANCE;



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
    public List<ListaCompra> getByPlanta(int idPlanta, String indice ) {

        String query="Select * from lista_compras where " +
                " plantasId=? and indice=?";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{idPlanta, indice});
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }
    public LiveData<List<ListaCompra>> getAllByIndiceCiudad(String indice,String idCiudad) {

        String query="Select * from lista_compras where indice=?" +
                "and ciudadNombre like ? order by clientesId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{indice,idCiudad});
        return dao.getListaCompraByFiltros( sqlquery);
    }

    public LiveData<List<ListaCompra>> getAllCdByIndice(String indice) {

        String query="Select * from lista_compras where indice=?" +
                " group by ciudadNombre";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{indice});
        return dao.getListaCompraByFiltros( sqlquery);
    }
    public List<ListaCompra> getAllByIndiceCiudadSimpl(String indice,String idCiudad) {

        String query="Select * from lista_compras where indice=?" +
                "and ciudadNombre like ? order by clientesId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{indice,idCiudad});
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }

    public List<ListaCompra> getAllByIndiceCiudadEtaSimpl(String indice,String idCiudad,String etapa) {

        String query="Select * from lista_compras where indice=?" +
                "and ciudadNombre like ? and lis_etapaactual=? order by clientesId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{indice,idCiudad, etapa});
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }
//que no esten canceladas
    public List<ListaCompra> getAllByIndiceCiudadEtaSimpl2(String indice,String idCiudad,String etapa) {

        String query="Select * from lista_compras where indice=?" +
                "and ciudadNombre like ? and lis_etapaactual=?";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{indice,idCiudad, etapa});
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }
    public LiveData<List<ListaCompra>> getAllByIndiceCiudadEta(String indice,String idCiudad,String etapa) {

        String query="Select * from lista_compras where indice=?" +
                "and ciudadNombre like ? and lis_etapaactual=? order by clientesId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{indice,idCiudad, etapa});
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
         query=query+               " group by clientesId order by clientesId ";
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
        query=query+ " group by clientesId order by clientesId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,params.toArray()
        );
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }

    public List<ListaCompra> getClientesByIndiceCiudadSimplxet(String indice,String idCiudad,int etapa) {
        List<String> params= new ArrayList<>();
        params.add(indice);
        String query="Select * from lista_compras where indice=? and lis_etapaactual=?";
        params.add(etapa+"");
        if(idCiudad!=null&&!idCiudad.equals("")) {
            query = query + " and ciudadNombre like ?";
            params.add(idCiudad);
        }
        query=query+ " group by clientesId order by clientesId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,params.toArray()
        );
     //   Log.d("ListaCompraRepositoryImpl","clientes "+query);
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }

    public List<ListaCompra> getClientesByIndicexetapa(String indice,int etapa) {
        List<String> params= new ArrayList<>();
        params.add(indice);
        String query="Select * from lista_compras where indice=? and lis_etapaactual=?";
        params.add(etapa+"");

        query=query+ " group by clientesId,ciudadNombre order by clientesId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,params.toArray()
        );
        //   Log.d("ListaCompraRepositoryImpl","clientes "+query);
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }

    public List<ListaCompra> getClientesByIndiceCiudadSimplsp(String indice,String idCiudad, int cliente) {
        List<String> params= new ArrayList<>();
        params.add(indice);
        String query="Select * from lista_compras where indice=? and clientesId!=?";
        params.add(cliente+"");
        if(!idCiudad.equals("")) {
            query = query + " and ciudadNombre like ?";
            params.add(idCiudad);
        }

        query=query+" group by clientesId order by clientesId";
      //  Log.d("ListaComrep",query+"--"+indice+"--"+idCiudad+"--"+cliente);
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,params.toArray()
        );
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }
    public LiveData<List<ListaCompra>> getAllByIndiceCiudadCliente(String indice,String idCiudad,int idCliente) {

        String query="Select * from lista_compras where indice=?" +
                "and ciudadNombre=? and clientesId=? order by clientesId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{indice,idCiudad,idCliente});
        return dao.getListaCompraByFiltros( sqlquery);
    }
    public List<ListaCompra> getAllByIndiceCiudadClienteSim(String indice,String ciudad,int idCliente) {

        String query="Select * from lista_compras where indice=?" +
                "and ciudadNombre=? and clientesId=? order by clientesId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{indice,ciudad,idCliente});
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }

    public List<ListaCompra> getAllByIndiceCiudadClienteSim(String indice,String ciudad,int idCliente, int etapa) {

        String query="Select * from lista_compras where indice=?" +
                "and ciudadNombre=? and clientesId=? and lis_etapaactual=? order by clientesId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{indice,ciudad,idCliente, etapa});
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }

    public List<ListaCompra> getClieByIndiceCiudadSimplxetReac(String indice,String idCiudad,int etapa, int reactivado) {
        List<String> params= new ArrayList<>();
        params.add(indice);
        String query="Select * from lista_compras where indice=? and lis_etapaactual=? and lis_reactivado=?";
        params.add(etapa+"");
        params.add(reactivado+"");
        if(idCiudad!=null&&!idCiudad.equals("")) {
            query = query + " and ciudadNombre like ?";
            params.add(idCiudad);
        }
        query=query+ " group by clientesId order by clientesId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,params.toArray()
        );
        //Log.d("ListaCompraRepositoryImpl","clientes "+query);
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }

    public List<ListaCompra> getClieByIndiceSimplxetReac(String indice,int etapa, int reactivado) {
        List<String> params= new ArrayList<>();
        params.add(indice);
        String query="Select * from lista_compras where indice=? and lis_etapaactual=? and lis_reactivado=?";
        params.add(etapa+"");
        params.add(reactivado+"");
        query=query+ " group by clientesId, ciudadNombre order by clientesId";
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,params.toArray()
        );
        //Log.d("ListaCompraRepositoryImpl","clientes "+query);
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }
    public List<ListaCompra> getTodosCliByIndiceCdSimplxet(String indice,String idCiudad,int etapa) {
        List<String> params= new ArrayList<>();
        params.add(indice);
        String query="Select * from lista_compras where indice=?";

        if(idCiudad!=null&&!idCiudad.equals("")) {
            query = query + " and ciudadNombre like ?";
            params.add(idCiudad);
        }
        query=query+ " group by clientesId HAVING MIN(lis_etapaactual) ="+etapa+" AND MAX(lis_etapaactual) ="+etapa+" ;";
        //params.add(etapa+"");
      //  params.add(etapa+"");
        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,params.toArray()
        );
      //  Log.d("ListaCompraRepositoryImpl","getTodosCliByIndiceCdSimplxet "+query+"--"+params.toString());
        return dao.getListaCompraByFiltrosSimple( sqlquery);
    }
    public LiveData<List<ListaWithDetalle>> getListaWithDetalleByFiltros(String indice, int idPlanta, int idCliente ) {

       String query="Select * from lista_compras  " +
                " where indice=? " +
                "and plantasId=?";

        SimpleSQLiteQuery sqlquery = new SimpleSQLiteQuery(
                query,
                new Object[]{indice,idPlanta});
      //  Log.d("LISTACOMPRREPIMP",indice+"---"+idPlanta+"--"+sqlquery.getSql());


        return dao.getListasWithDetalleByFiltros( sqlquery);
    }


        @Override
    public LiveData<List<ListaCompra>> getAll() {
      return dao.findAll();
    }

    @Override
    public List<ListaCompra> getAllsimple() {
        return dao.findAllsimple();
    }


    public List<ListaCompra> getIndice() {
        return dao.findIndice();
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

    public int getClientexPlanta( String indice, int planta){
        ListaCompra comprat= dao.getClientexPlanta( indice, planta);
        if(comprat!=null)
            return comprat.getClientesId();
        return 0;
    }

    public String getClientexId(  int clienteId){
        ListaCompra comprat= dao.getClientexId( clienteId);
        if(comprat!=null)
            return comprat.getClienteNombre();
        return "";
    }


    @Override
    public long insert(ListaCompra object) {
        return dao.insert(object);
    }
    public long updateSC(ListaCompra object) {
        return dao.insert(object);
    }

    //devuelve un arreglo de la forma [clientes][ciudad]
    public List<ListaCompraDao.TotalPlantas> getTotalPlantasxCliente(String ciudad , int etapa){
        return dao.getTotalPlantasxClienteEtapa( ciudad, etapa);
    }
}
