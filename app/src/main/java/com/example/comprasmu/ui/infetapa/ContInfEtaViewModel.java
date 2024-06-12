package com.example.comprasmu.ui.infetapa;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;

import java.util.List;

public class ContInfEtaViewModel extends AndroidViewModel {
    private final InfEtapaRepositoryImpl infEtaRepository;
    private final InfEtapaDetRepoImpl infEtaDetRepository;
    Application application;

    public ContInfEtaViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
        this.infEtaRepository = new InfEtapaRepositoryImpl(application);

        this.infEtaDetRepository=new InfEtapaDetRepoImpl(application);
    }
    public LiveData<List<InformeEtapa>> getInformesPend(String indice,int etapa){
        if(etapa==1||etapa==4||etapa==5||etapa==6)
            return   infEtaRepository.getInformesPend(indice, etapa);
        else
            return   infEtaRepository.getInformesPendRe(indice, etapa);
    }

    public void eliminarInformeEta(int id){

        infEtaDetRepository.deleteByInf(id);
        infEtaRepository.deleteInformeEtapa(id);

    }
    public InformeEtapa getInformeNoCancel(String indice,int etapa){

            return   infEtaRepository.getInformeNoCancel(indice, etapa);


    }

}