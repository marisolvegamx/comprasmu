package com.example.comprasmu.ui.mantenimiento;

import android.app.Application;
import android.content.Context;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.dao.ConfiguracionRepositoryImpl;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.Configuracion;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.utils.Constantes;

import java.util.List;

public class CiudadTrabajoViewModel extends AndroidViewModel {
    ListaCompraRepositoryImpl lcrepo;
    ConfiguracionRepositoryImpl confrepo;

    public CiudadTrabajoViewModel(@NonNull Application application) {
        super(application);
        //lcrepo=new ListaCompraRepositoryImpl();
        ListaCompraDao dao= ComprasDataBase.getInstance(application).getListaCompraDao();
        lcrepo = ListaCompraRepositoryImpl.getInstance(dao);
        confrepo=new ConfiguracionRepositoryImpl(application);
    }

    public LiveData<List<ListaCompra>> getCiudades(String indiceactual) {
        return lcrepo.getCiudades(indiceactual);
    }

    public void guardarConf(Configuracion config){
         confrepo.insert(config);
    }
    public Configuracion  getConfig(){

        Configuracion conf=confrepo.findsimple(Constantes.CONFROTAR);

        return conf;
    }

}