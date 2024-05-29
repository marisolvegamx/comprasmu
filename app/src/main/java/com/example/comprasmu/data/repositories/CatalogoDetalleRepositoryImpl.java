package com.example.comprasmu.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.ComprasDataBase;

import com.example.comprasmu.data.dao.CatalogoDetalleDao;
import com.example.comprasmu.data.modelos.CatalogoDetalle;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;


import java.util.List;

public class CatalogoDetalleRepositoryImpl  extends BaseRepository<CatalogoDetalle> {

    private static CatalogoDetalleDao dao;

    private LiveData<List<CatalogoDetalle>> allCatalogoDetalle;
    private static CatalogoDetalleRepositoryImpl INSTANCE;

    public CatalogoDetalleRepositoryImpl(Context context) {
        ComprasDataBase comprasDataBase = ComprasDataBase.getInstance(context.getApplicationContext());
        dao=comprasDataBase.getCatalogoDao();
    }

    public List<CatalogoDetalle> getAll(int cat) {
        return dao.findAll(cat);
    }

    public List<CatalogoDetalle> getxCatalogo(String cat)
    {
        int idcat=0;
        switch(cat){
            case "atributos":
                idcat=1;
                break;
            case "ubicacion_muestra":
                idcat= Contrato.CatalogosId.TOMADOSDE;
                break;
            case "tipo tienda":
                idcat=Contrato.CatalogosId.TIPODETIENDA;
                break;
            case "causas":
                idcat=Contrato.CatalogosId.CAUSAS;
                break;
            case "cadena_comercial":
                idcat=Contrato.CatalogosId.CADENACOMER;
                break;
            case Contrato.TablaInformeDet.CAUSA_SUSTITUCIONID:
                idcat=Contrato.CatalogosId.CAUSASSUST;
                break;
            case "tipo_gastos":
                idcat=27;
                break;
        }

        return dao.findAll(idcat);
    }

    public LiveData<CatalogoDetalle> find(int id, int cat) {
        return dao.find(id,cat);
    }

    @Override
    public LiveData<List<CatalogoDetalle>> getAll() {
        return null;
    }

    @Override
    public List<CatalogoDetalle> getAllsimple() {
        return null;
    }

    @Override
    public LiveData<CatalogoDetalle> find(int id) {
        return null;
    }

    @Override
    public CatalogoDetalle findsimple(int id) {
        return null;
    }

    @Override
    public long insert(CatalogoDetalle object) {
         dao.insert(object);
         return 0;
    }


    public void insertAll(List<CatalogoDetalle> object) {
        dao.insertAll(object);
    }


    @Override
    public void delete(CatalogoDetalle object) {
        dao.delete(object);
    }

    public void deletexIdCat(int idcat) {
        dao.deletexIdCat(idcat);
    }




}
