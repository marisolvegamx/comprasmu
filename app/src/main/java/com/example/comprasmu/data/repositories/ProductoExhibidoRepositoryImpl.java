package com.example.comprasmu.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.DatabaseView;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ProductoExhibidoDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.ProductoExhibido;


import java.util.Date;
import java.util.List;

public class ProductoExhibidoRepositoryImpl extends BaseRepository<ProductoExhibido> {

    private final ProductoExhibidoDao dao;



    public ProductoExhibidoRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
         dao=comprasDataBase.getProductoExhibidoDao();
    }
    @Override
    public LiveData<List<ProductoExhibido>> getAll() {
        return dao.findAll();
    }

    @Override
    public List<ProductoExhibido> getAllsimple() {
        return null;
    }


    public List<ProductoExhibido> getAllsimple(int visita) {
        return dao.getByVisitasimple(visita);
    }
    public List<ProductoExhibido> getAllsimplePendSync(int visita) {
        return dao.getByVisitasimplePendSync(visita,0);
    }

    public LiveData<List<ProductoExhibido>> getByVisita(int idVisita) {
      return dao.findByVisita( idVisita);
    }
    public void deleteByVisita(int idVisita) {
         dao.deleteByVisita( idVisita);
    }

    public LiveData<List<ImagenDetalle>> getImagenByVisita(int idVisita) {
        return dao.getImagenByVisita( idVisita);
    }
    public List<ImagenDetalle> getImagenByVisitasimple(int idVisita) {
        return dao.getImagenByVisitasimple( idVisita);
    }
    public List<ImagenDetalle> getImagenByVisitasimplePend(int idVisita) {
        return dao.getImagenByVisitasimplePend( idVisita);
    }



    @Override
    public LiveData<ProductoExhibido> find(int id) {
        return dao.find(id);
    }

    @Override
    public ProductoExhibido findsimple(int id) {
        return null;
    }


    @Override
    public void delete(ProductoExhibido object) {
        dao.delete(object);
    }

    public void deleteById(int idproducto) {
        dao.deleteById(idproducto);
    }


   /* public void deleteByInforme(int informe) {
        dao.deleteByInforme(informe);
    }*/
   public void actualizarEstatusSync(int id, int estatus) {

       dao.actualizarEstatusSync(id, estatus);
   }
    public void actualizarEstatusSyncxVisita(int idvisita, int estatus) {

        dao.actualizarEstatusSyncxVisita(idvisita, estatus);
    }

    @Override
    public void insertAll(List<ProductoExhibido> objects) {
        dao.insertAll(objects);
    }

    public  LiveData<List<ProductoExhibido>> getProductoExhibidoPendientesSync(){
        return  dao.getProductoExhibidoByEstatusSync(0);
    }

    @Override
    public long insert(ProductoExhibido object) {
        return dao.insert(object);
    }

    public  LiveData<List<ProductoExhibidoDao.ProductoExhibidoFoto>> getAllByVisita(int id){
       return  dao.getAllByVisita(id);
    }
    public  List<ProductoExhibidoDao.ProductoExhibidoFoto> getAllFotoByVisitaSimpl(int id){
        return  dao.getAllFotoByVisita(id);
    }
    public  List<ProductoExhibido> getAllByVisitaSimple(int id){
        return  dao.getAllByVisitaSimple(id);
    }

    public  void deleteAllByVisita(int id){
          dao.deleteAllByVisita(id);
    }

    public  LiveData<List<ProductoExhibidoDao.ProductoExhibidoFoto>> getAllByVisitaCli(int id, int cliente){
        return  dao.getAllByVisitaCliente(id,cliente);
    }
    public  List<ProductoExhibidoDao.ProductoExhibidoFoto> getAllByVisitaClisimp(int id, int cliente){
        return  dao.getAllByVisitaClienteSimp(id,cliente);
    }
    public List<ProductoExhibido> getProdPendSubir(String indice) {
        return dao.getByEstatusSync(indice,0);
    }
}
