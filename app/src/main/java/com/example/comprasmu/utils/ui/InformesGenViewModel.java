package com.example.comprasmu.utils.ui;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.repositories.CorreccionRepoImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;

import java.util.List;


public class InformesGenViewModel extends AndroidViewModel {

    private final InfEtapaRepositoryImpl ierepository;
    private final InfEtapaDetRepoImpl idrepository;
    private final CorreccionRepoImpl correpo;
    private final ImagenDetRepositoryImpl imagenDetRepository;
    public InformesGenViewModel(Application application) {
        super(application);

        ierepository=new InfEtapaRepositoryImpl(application);
        idrepository=new InfEtapaDetRepoImpl(application);
        correpo=new CorreccionRepoImpl(application);
        this.imagenDetRepository=new ImagenDetRepositoryImpl(application);
    }


    public LiveData<List<InformeEtapa>> cargarEtapa(int etapa, String indice, int plantaid){
        return ierepository.getAll(etapa, indice, plantaid);

    }
    public LiveData<List<InformeEtapa>> cargarEtapaAll(int etapa, String indice){
        return ierepository.getAllsp(etapa, indice);

    }
    public LiveData<InformeEtapa> getInforme(int id, String indice){
        return ierepository.find(id);

    }
    public LiveData<List<InformeEtapaDet>> getfotosPrep(int id){
        return idrepository.getAllxEtapa(id, 1);

    }
    public LiveData<List<InformeEtapaDet>> getfotosxetapa(int id, int etapa){
        return idrepository.getAllxEtapa(id, etapa);

    }

    public ImagenDetalle getfotoxid(String id){

            int idim = Integer.parseInt(id);
            return imagenDetRepository.findsimple(idim);


    }



}