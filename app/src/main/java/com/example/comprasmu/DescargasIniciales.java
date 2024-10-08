package com.example.comprasmu;

import android.app.Activity;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.comprasmu.data.ComprasDataBase;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.dao.ListaCompraDao;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.TablaVersiones;

import com.example.comprasmu.data.remote.ListaCompraResponse;
import com.example.comprasmu.data.remote.RespInfEtapaResponse;
import com.example.comprasmu.data.remote.RespInformesResponse;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;

import com.example.comprasmu.data.repositories.GeocercaRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;

import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;

import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.SiglaRepositoryImpl;

import com.example.comprasmu.data.repositories.SustitucionRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;

import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.Constantes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DescargasIniciales {

        TablaVersionesRepImpl tvRepo;

        SimpleDateFormat sdfdias;
        ListaCompraDetRepositoryImpl lcdrepo;
        ListaCompraRepositoryImpl lcrepo;


        InformeComDetRepositoryImpl infdrepo;

        GeocercaRepositoryImpl georep;


        Context act;
        int actualiza;
        int procesos=0;
        int procesos_lev=0; //para saber cuantos si se corrieron
        private ComprasLog flog;
        DescargaIniListener listenprin;

        final String TAG="DescargasIniciales";


        public DescargasIniciales(Context act) {


            this.act=act;

            sdfdias=new SimpleDateFormat("dd-MM-yyyy");



            flog = ComprasLog.getSingleton();
            flog.crearLog(act.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getPath());

        }


        public void ejecutar() {

            flog.grabarError(TAG,"ejecutar", "iniciando descargas programadas");
            actualiza=1;
            tvRepo=new TablaVersionesRepImpl(this.act);
            ListaCompraDao dao= ComprasDataBase.getInstance(this.act).getListaCompraDao();
            lcdrepo=new ListaCompraDetRepositoryImpl(this.act);
            lcrepo=ListaCompraRepositoryImpl.getInstance(dao);

            georep=new GeocercaRepositoryImpl(this.act);

            listenprin=new DescargaIniListener();


            listacompras();


        }


        private void listacompras(){
            Log.d(TAG, "descargando listas"+actualiza);
            flog.grabarError(TAG,"listacompras","descargando listas actualiza="+actualiza);

            PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
           //por ahora no tiene caso la fecha de actualizacion porque no se registra en la tabla, falta mejorar esto en la app web
            // TablaVersiones comp=tvRepo.getVersionByNombreTablasmd(Contrato.TBLLISTACOMPRAS,Constantes.INDICEACTUAL);
           // TablaVersiones det=tvRepo.getVersionByNombreTablasmd(Contrato.TBLLISTACOMPRASDET,Constantes.INDICEACTUAL);
            DescargaIniListener listener=new DescargaIniListener();

            //siempre actualizo

            ps.getListasdeCompra(null,null,Constantes.INDICEACTUAL,listener);
            flog.grabarError(TAG,"listacompras"," siempre actualizo"+actualiza);


        }


    private void buscarListaDet(ListaCompraDetalle compra, List<ListaCompraDetalle> json){
        for (ListaCompraDetalle jcompra: json) {
            if(jcompra.getId()==compra.getId()&&compra.getListaId()==jcompra.getListaId()){
                // Log.d(TAG,compra.getProductoNombre()+"--"+jcompra.getProductoNombre()+".."+compra.getListaId()+"--"+ compra.getId());

                return;
            }
        }

        //si llego aqui, no lo encontré por lo que se eliminó
        //busco que no tenga informe
        infdrepo=new InformeComDetRepositoryImpl(act);
        List<InformeCompraDetalle> prods=infdrepo.findByCompra(compra.getListaId(), compra.getId());
        Log.d(TAG,"prbabbl elimine "+compra.getProductoNombre()+"--"+compra.getListaId()+"--"+ compra.getId());
        if(prods==null||prods.size()<1)
            lcdrepo.delete(compra);

    }
public class DescargaIniListener implements  IDescargaIniListener{
    public DescargaIniListener(){


    }
    public void finalizar(){
        Log.d(TAG,"finalizo descarga"+procesos+"--"+procesos_lev);


    }


    public void insertarZonas(List<Geocerca> zonas){
        Log.d(TAG,"fin zonas ");
        if (zonas != null && zonas.size() > 0) {

            //insertar

            for(Geocerca geo:zonas) {
                georep.insert(geo);
            }
            //actualizo tabla versiones
            TablaVersiones tv=new TablaVersiones();
            tv.setNombreTabla("geocercas");
            tv.setTipo("C");
            tv.setVersion(new Date());
            tvRepo.insertUpdate(tv);

            tv=null;
        }

        finalizar();
    }
    public void actualizar(ListaCompraResponse compraResp) {
        //primero los inserts

        if(compraResp!=null) {
            if (compraResp.getInserts() != null) {
                if (compraResp.getInserts().getListaCompra() != null) {
                    Log.d("Descargaini","listacomp<"+compraResp.getInserts().getListaCompra());
                    flog.grabarError(TAG,"actualizar lista compra","listacomp<");

                    lcrepo.insertAll(compraResp.getInserts().getListaCompra()); //inserto blblbl
                }
                // Log.d("Descargaini","resp3>>"+compraResp.getInserts().getListaCompraDetalle());

                if (compraResp.getInserts().getListaCompraDetalle() != null) {
                    //como puede que ya existan reviso primero e inserto unoxuno
                    for (ListaCompraDetalle detalle : compraResp.getInserts().getListaCompraDetalle()) {
                        ListaCompraDetalle existe = lcdrepo.findsimple(detalle.getListaId(), detalle.getId());
                        if (existe == null) {
                            // lcrepo.insert(detalle);

                        } else {  //no reemplazo los comprados ni los nuevos codigos
                            detalle.setComprados(existe.getComprados());
                            detalle.setNvoCodigo(existe.getNvoCodigo());
                            //lcrepo.updateSC(compra);
                        }
                        // Log.d(TAG,"insertando"+detalle.getListaId()+"--"+detalle.getId());
                        long id=lcdrepo.insert(detalle);
                        //Log.d(TAG,"**insertando"+id);



                    }
                    //reviso los que se eliminaron
                    if(compraResp.getInserts().getListaCompraDetalle()!=null&&compraResp.getInserts().getListaCompraDetalle().size()>0) {
                        //  Log.d(TAG,"buscando elim");
                        List<ListaCompraDetalle> liscompapp = lcdrepo.getAllSimpl();
                        for (ListaCompraDetalle compra : liscompapp
                        ) {
                            //veo si está en el json si no es que se elimina, solo checo que no
                            //tenga informe
                            buscarListaDet(compra, compraResp.getInserts().getListaCompraDetalle());
                        }
                    }
                }

                // lcdrepo.insertAll(compraResp.getInserts().getListaCompraDetalle());
            }
            //los updates
            if (compraResp.getUpdates() != null) {

                if (compraResp.getUpdates().getListaCompra() != null)
                    lcrepo.insertAll(compraResp.getUpdates().getListaCompra()); //inserto blblbl
                if (compraResp.getUpdates().getListaCompraDetalle() != null) {
                    //como puede que ya existan reviso primero e inserto unoxuno
                    for (ListaCompraDetalle detalle : compraResp.getInserts().getListaCompraDetalle()) {
                        ListaCompraDetalle existe = lcdrepo.findsimple(detalle.getListaId(), detalle.getId());
                        if (existe == null) {
                            // lcrepo.insert(detalle);

                        } else {   //mantengo los comprados y codigos nevos
                            detalle.setComprados(existe.getComprados());
                            detalle.setNvoCodigo(existe.getNvoCodigo());
                            //lcrepo.updateSC(compra);
                        }
                        lcdrepo.insert(detalle);

                    }
                    //reviso los que se eliminaron
                            /*List<ListaCompraDetalle> liscompapp=lcdrepo.getAllSimpl();
                            for (ListaCompraDetalle compra:liscompapp
                            ) {
                                //veo si está en el json si no es que se elimina, solo checo que no
                                //tenga informe
                                buscarListaDet(compra,compraResp.getInserts().getListaCompraDetalle());
                            }*/
                    // lcdrepo.updateAll(compraResp.getUpdates().getListaCompraDetalle());
                }
            }

            //actualizar version en tabla
            TablaVersiones tinfo = new TablaVersiones();
            tinfo.setNombreTabla(Contrato.TBLLISTACOMPRAS);
            Date fecha1 = new Date();
            Log.d(TAG, "insertando fecha version 1" + fecha1);

            tinfo.setVersion(fecha1);
            tinfo.setIndice(Constantes.INDICEACTUAL);
            tinfo.setTipo("I");
            TablaVersiones tinfod = new TablaVersiones();
            tinfod.setNombreTabla(Contrato.TBLLISTACOMPRASDET);
            tinfod.setVersion(fecha1);
            tinfod.setTipo("I");
            tinfod.setIndice(Constantes.INDICEACTUAL);
            tvRepo.insertUpdate(tinfo);
            tvRepo.insertUpdate(tinfod);

        }



        finalizar();

    }



}
}
