package com.example.comprasmu.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.ComprasDataBase;

import com.example.comprasmu.data.dao.ImagenDetalleDao;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;


import java.util.List;

public class ImagenDetRepositoryImpl extends BaseRepository<ImagenDetalle> {

    private ImagenDetalleDao dao;

    private LiveData<List<ImagenDetalle>> allImagenDetalle;

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

    @Override
    public long insert(ImagenDetalle object) {
        return dao.insert(object);
    }

}
