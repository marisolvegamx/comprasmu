package com.example.comprasmu.data.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.SiglaDao;
import com.example.comprasmu.data.modelos.Sigla;

import java.util.List;

public class SiglaRepositoryImpl extends BaseRepository<Sigla> {

    private static SiglaDao dao;


    private static SiglaRepositoryImpl INSTANCE;

    public SiglaRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=comprasDataBase.getSiglaDao();
    }

   // public LiveData<List<Sigla>> getAll(int cat) {
   //     return dao.findAll();
    //}
    public  Sigla getByClienteSig( String siglas, int cliente){
     return dao.getByClienteSig(siglas,cliente );
    }



    @Override
    public LiveData<List<Sigla>> getAll() {
        return dao.findAll();
    }

    @Override
    public List<Sigla> getAllsimple() {
        return dao.findAllsimple();
    }

    @Override
    public LiveData<Sigla> find(int id) {
        return dao.find(id);
    }

    @Override
    public Sigla findsimple(int id) {
        return dao.findsimple(id);
    }

    @Override
    public long insert(Sigla object) {

         return  dao.insert(object);
    }


    public void insertAll(List<Sigla> object) {
        dao.insertAll(object);
    }


    @Override
    public void delete(Sigla object) {
        dao.delete(object);
    }


    public void deleteAll() {
        dao.deleteAll();
    }
}
