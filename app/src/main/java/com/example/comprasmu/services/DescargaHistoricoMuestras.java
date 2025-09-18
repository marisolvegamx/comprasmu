package com.example.comprasmu.services;


import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.HistoricoMuestras;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.repositories.HistoricoMuestrasRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.utils.ComprasLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Observer;
//todo agregar eliminar tabla con el borrado de indice
/*****proceso para descargar y sincronizar las muestras compradas de 2 meses anteriores de pepsi para
 *poder obtener los codigos no permitidos en las sustituciones
 * se descargara cuando consulte por primera vez la sustitucion de esa planta y se eliminaran con el resto
 * de las tablas al cambiar de indice***/
public class DescargaHistoricoMuestras
{
    SimpleDateFormat sdfformat;
    Context act;
    private ComprasLog flog;
    private HistoricoMuestrasRepositoryImpl historicoMuestraRepo;
    final String TAG="DescargaHistoricoMuestras";
    PeticionesServidor peticionesServidor;
    final String usuario, indice;
    Observer observer;
    private final LifecycleOwner lifecycleOwner;
    MutableLiveData<Boolean> observable;
    public DescargaHistoricoMuestras(String dirLog, HistoricoMuestrasRepositoryImpl historicoMuestraRepo,String usuario, String indice, PeticionesServidor peticionesServidor, LifecycleOwner lifecycleOwner) {
            this.act=act;
            sdfformat=new SimpleDateFormat("yyyy-MM-dd");
            flog = ComprasLog.getSingleton();
            flog.crearLog( dirLog);
            this.historicoMuestraRepo=historicoMuestraRepo;
            this.usuario = usuario;
            this.indice=indice;
            this.peticionesServidor=peticionesServidor;

             this.lifecycleOwner =lifecycleOwner ;
    }

    public MutableLiveData<Boolean> ejecutar(int plantaId) {
            observable=new MutableLiveData<>();
            flog.grabarError(TAG,"ejecutar", "iniciando descarga");

            getMuestras(plantaId);
            return observable;
    }

    //revisa si tengo las muestras de la tabla si no las hay obtiene el historico por planta
    private void getMuestras (int plantaId){

            //reviso si tengo algo en la tabla
            List<HistoricoMuestras> registros= historicoMuestraRepo.getByPlanta(plantaId);
            if(registros.isEmpty()) {
                LiveData<List<HistoricoMuestras>> respuesta = peticionesServidor.getHistoricoMuestras(indice, plantaId);
                respuesta.observe(lifecycleOwner, new androidx.lifecycle.Observer<List<HistoricoMuestras>>() {
                    @Override
                    public void onChanged(List<HistoricoMuestras> historicoMuestras) {
                        //inserto en la tabla
                        insertarHistorico(historicoMuestras);
                    }
                });
            }else
            observable.setValue(false);
    }

    private void insertarHistorico(List<HistoricoMuestras> historicoMuestras) {
        if(historicoMuestras!=null){

            historicoMuestraRepo.insertAll(historicoMuestras);

        }
        observable.setValue(true);
    }



}
