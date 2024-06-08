package com.example.comprasmu.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.AcuseReciboDao;
import com.example.comprasmu.data.modelos.AcuseRecibo;

import java.util.List;

public class AcuseReciboRepositoryImpl extends BaseRepository<AcuseRecibo> {

    private final AcuseReciboDao dao;



    public AcuseReciboRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=comprasDataBase.getAcuseReciboDao();
    }



    public LiveData<List<AcuseRecibo>> getAll() {
      return dao.findAll();
    }

    @Override
    public List<AcuseRecibo> getAllsimple() {
        return dao.getAllSencillo();
    }

    //todo no tengo
    @Override
    public LiveData<AcuseRecibo> find(int id) {
        return null;
    }
    //todo no tengo
    @Override
    public AcuseRecibo findsimple(int id) {
        return null;
    }


    public AcuseRecibo findsimple(String indice) {
        return dao.findsimple(indice);
    }


    public LiveData<AcuseRecibo> findByindice(String indice) {
        return dao.findByindice(indice);
    }



    public void deleteAll(){
     dao.deleteAll();
    }


    public void insertAll(List<AcuseRecibo> objects) {
        dao.insertAll(objects);
    }


    public long insert(AcuseRecibo object) {
        return dao.insert(object);
    }

    @Override
    public void delete(AcuseRecibo object) {

    }

}
