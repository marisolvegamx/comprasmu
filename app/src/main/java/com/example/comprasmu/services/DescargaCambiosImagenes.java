package com.example.comprasmu.services;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.comprasmu.data.PeticionesServidor;

import com.example.comprasmu.data.modelos.Contrato;

import com.example.comprasmu.data.modelos.ImagenDetalle;

import com.example.comprasmu.data.modelos.TablaVersiones;

import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;

import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.utils.ComprasLog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Observable;

/*****proceso para descargar la imagenes que cambiaron por una correccion
 *  se usa en las imagenes de gastos y  antes de una nueva solicitud de correccion***/
public class DescargaCambiosImagenes
{

        TablaVersionesRepImpl tablaVersionesRepo;
        SimpleDateFormat sdfformat;
        Context act;
        private ComprasLog flog;
        DescargaCambImgagenListener listenprin;
        ImagenDetRepositoryImpl imagenDetalleRepo;
        final String TAG="DescargaCambiosImagenes";
        PeticionesServidor ps;
        final String usuario, indice;
        MutableLiveData<Boolean> observable;
        public DescargaCambiosImagenes(String dirLog,TablaVersionesRepImpl tablaVersionesRepo, ImagenDetRepositoryImpl imagenDetalleRepo, String usuario, String indice, MutableLiveData<Boolean> observable) {
            this.act=act;
            sdfformat=new SimpleDateFormat("yyyy-MM-dd");
            flog = ComprasLog.getSingleton();
            flog.crearLog( dirLog);
            this.tablaVersionesRepo=tablaVersionesRepo;
            this.imagenDetalleRepo=imagenDetalleRepo;
            this.usuario = usuario;
            this.indice=indice;
            this.observable=observable;
        }

        public void ejecutar() {

            flog.grabarError(TAG,"ejecutar", "iniciando descarga");

            getCambios();

        }


        private void getCambios (){
            listenprin=new DescargaCambImgagenListener();
            ps=new PeticionesServidor(usuario);
            TablaVersiones comp=tablaVersionesRepo.getVersionByNombreTabla(Contrato.TBLIMAGENDETALLE);

            String fecha="2025-01-01"; //para la 1era vez
            if(comp!=null&&!comp.getVersion().equals("")){
                fecha=sdfformat.format(comp.getVersion());
            }
            //siempre actualizo
            ps.pedirCambiosImagenes(indice,fecha,listenprin);

        }


    public class DescargaCambImgagenListener {
        public DescargaCambImgagenListener() {


        }

        public void finalizar() {
            Log.i(TAG,"finalizando");
            observable.setValue(true);

        }


        public void actualizarImagenes(List<ImagenDetalle> listaImagenes) {
            //primero los inserts
            if (listaImagenes != null) {
                for (ImagenDetalle imagen : listaImagenes
                ) {
                    flog.grabarError(TAG,"actualizando","imagen"+imagen.getId());

                    imagenDetalleRepo.actualizarRuta(imagen.getId(), imagen.getRuta(), imagen.getUpdatedAt());
                }
                //actualizar version en tabla
                TablaVersiones tinfo = new TablaVersiones();
                tinfo.setNombreTabla(Contrato.TBLIMAGENDETALLE);
                Date fecha1 = new Date();
                Log.d(TAG, "insertando fecha version:" + fecha1);

                tinfo.setVersion(fecha1);
                tinfo.setIndice(indice);
                tinfo.setTipo("I");

                tablaVersionesRepo.insertUpdate(tinfo);


            }

            finalizar();

        }
    }

}
