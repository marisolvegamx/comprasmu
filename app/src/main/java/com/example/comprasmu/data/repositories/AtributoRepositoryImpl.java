package com.example.comprasmu.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.AtributoDao;

import com.example.comprasmu.data.modelos.Atributo;


import java.util.List;

public class AtributoRepositoryImpl extends BaseRepository<Atributo> {

    private static AtributoDao dao;


    private static AtributoRepositoryImpl INSTANCE;

    public AtributoRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=comprasDataBase.getAtributoDao();
    }

   // public LiveData<List<Atributo>> getAll(int cat) {
   //     return dao.findAll();
    //}
    public  List<Atributo> getByEmpaque( int id, int cliente){
     return dao.getByEmpaqueCliente(id,cliente );
    }



    @Override
    public LiveData<List<Atributo>> getAll() {
        return dao.findAll();
    }

    @Override
    public List<Atributo> getAllsimple() {
        return dao.findAllsimple();
    }

    @Override
    public LiveData<Atributo> find(int id) {
        return dao.find(id);
    }

    @Override
    public Atributo findsimple(int id) {
        return dao.findsimple(id);
    }

    @Override
    public long insert(Atributo object) {

         return  dao.insert(object);
    }


    public void insertAll(List<Atributo> object) {
        dao.insertAll(object);
    }


    @Override
    public void delete(Atributo object) {
        dao.delete(object);
    }





}
