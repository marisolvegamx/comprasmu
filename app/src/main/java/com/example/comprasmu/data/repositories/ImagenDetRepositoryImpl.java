package com.example.comprasmu.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.R;
import com.example.comprasmu.data.ComprasDataBase;

import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.dao.ImagenDetalleDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.ui.informe.NuevoinformeViewModel;
import com.example.comprasmu.utils.Constantes;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImagenDetRepositoryImpl extends BaseRepository<ImagenDetalle> {

    private final ImagenDetalleDao dao;
    List<ImagenDetalle> fotos ;

    public ImagenDetRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
         dao=comprasDataBase.getImagenDetalleDao();
    }

    @Override
    public LiveData<List<ImagenDetalle>> getAll() {
      return dao.findAll();
    }

    @Override
    public List<ImagenDetalle> getAllsimple() {
        return dao.findAllsimple();
    }


    @Override
    public LiveData<ImagenDetalle> find(int id) {
        return dao.find(id);
    }

    public ImagenDetalle findsimple(int id) {
        return dao.findsimple(id);
    }

    public ImagenDetalle findByRuta(String ruta) {
        return dao.findByRuta(ruta);
    }
    public ImagenDetalle findsimpleInd(int id, String indice) {
        return dao.findInd(id, indice);
    }

  //  public ImagenDetalle findsimple(int id) {
    //    return dao.find(id);
  //  }

    public LiveData<List<ImagenDetalle>> findList(List<Integer> fotos) {
        return dao.findinList(fotos);
    }
    public List<ImagenDetalle> findListsencillo(List<Integer> fotos) {
        return dao.findinListsencillo(fotos);
    }

    public void deleteList(List<Integer> fotos) {

         dao.deleteList(fotos);
    }
    public void deleteById(int id) {

        dao.deleteById(id);
    }

    public LiveData<String> findRuta(int id) {
        return dao.findRuta(id);
    }


    public long getUltimo() {

        return dao.getUltimoId();
    }

    public long insertImg(ImagenDetalle object) {

        return dao.insertImg(object);
    }

    @Override
    public void delete(ImagenDetalle object) {
        dao.delete(object);
    }

    public void actualizarEstatusSync(int id, int estatus) {

        dao.actualizarEstatusSync(id, estatus);
    }
    public List<ImagenDetalle> getByIndice(String indice) {

        return dao.getByIndice(indice);
    }
    public void cancelar(int id, int estatus) {

        dao.cancelar(id, estatus);
    }

   /* public void deleteByInforme(int informe) {
        dao.deleteByInforme(informe);
    }*/

    @Override
    public void insertAll(List<ImagenDetalle> objects) {
        dao.insertAll(objects);
    }

    public  LiveData<List<ImagenDetalle>> getImagenDetallePendientesSync(){
        return  dao.getImagenDetalleByEstatusSync(1,0);
    }

    public  List<ImagenDetalle> getImagenPendSyncsimple(){
        return  dao.getImagenByEstSyncsimple(1,0);
    }
    public  List<ImagenDetalle> getImagenPendSyncsimple2(long fecha){
        return  dao.getImagenByEstSyncsim2(1,0,fecha);
    }

    public List<ImagenDetalle> getFotosInfDet(InformeCompraDetalle informe) {

       fotos = new ArrayList<>();



        ponerFoto( informe.getFoto_codigo_produccion());

        // ponerFoto(informe.getEnergia());
        //  ponerFoto(getString(R.string.foto_num_tienda),informe.getFoto_num_tienda());
        // ponerFoto(getString(R.string.foto_codigo_produccion)informe.getMarca_traslape());
        ponerFoto( informe.getFoto_atributoa());
        ponerFoto( informe.getFoto_atributob());
        ponerFoto(informe.getFoto_atributoc());
        ponerFoto(informe.getEtiqueta_evaluacion());
        return fotos;
    }
    public List<ImagenDetalle> getFotosInf(InformeCompra informe) {
        fotos = new ArrayList<>();
        ponerFoto( informe.getTicket_compra());
        // ponerFoto(informe.getEnergia());
        //  ponerFoto(getString(R.string.foto_num_tienda),informe.getFoto_num_tienda());
        // ponerFoto(getString(R.string.foto_codigo_produccion)informe.getMarca_traslape());
        ponerFoto( informe.getCondiciones_traslado());

        return fotos;
    }
    public void ponerFoto( int idfoto){
        fotos.add(findsimple(idfoto));

    }
    @Override
    public long insert(ImagenDetalle object) {
        return dao.insert(object);
    }

    public void deleteByIndice(String indice) {
        dao.deleteByIndice(indice);
    }
}
