package com.example.comprasmu;

import android.util.Log;

import com.example.comprasmu.data.dao.TablaVersionesDao;
import com.example.comprasmu.data.modelos.Contrato;

import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.remote.ListaCompraResponse;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;

import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.utils.ComprasLog;
import com.example.comprasmu.utils.Constantes;

import java.util.Date;
import java.util.List;

public class DescargasListaCompraImpl {
    private static final String TAG ="DescargasListaCompraImpl" ;
    private ComprasLog flog;

    public DescargasListaCompraImpl(ComprasLog flog) {
        this.flog = flog;
    }

    public void actualizarListaCompra(ListaCompraResponse compraResp, ListaCompraRepositoryImpl lcrepo, ListaCompraDetRepositoryImpl lcdrepo, TablaVersionesRepImpl tvRepo, InformeComDetRepositoryImpl infdrepo) {
        //primero los inserts
        if(compraResp!=null) {
            if (compraResp.getInserts() != null) {
                if (compraResp.getInserts().getListaCompra() != null) {
                    Log.d(TAG,"listacomp<"+compraResp.getInserts().getListaCompra());
                    flog.grabarError(TAG,"actualizar lista compra","listacomp<");

                    lcrepo.insertAll(compraResp.getInserts().getListaCompra()); //inserto blblbl
                }

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
                        long id=lcdrepo.insert(detalle);

                    }
                    //reviso los que se eliminaron
                    Log.d(TAG,"LOS QUE SE ELIMINARON");

                    if(compraResp.getInserts().getListaCompra()!=null&&compraResp.getInserts().getListaCompra().size()>0) {
                        //  Log.d(TAG,"buscando elim");
                        List<ListaCompra> liscompapp = lcrepo.getAllByIndicesimple(Constantes.INDICEACTUAL);
                        for (ListaCompra compra : liscompapp
                        ) {
                            //veo si está en el json si no es que se elimina, solo checo que no
                            //tenga informe
                            eliminarListaCompra(compra, compraResp.getInserts().getListaCompra(), infdrepo, lcrepo);
                        }
                    }
                    //reviso los que se eliminaron
                    if(compraResp.getInserts().getListaCompraDetalle()!=null&&compraResp.getInserts().getListaCompraDetalle().size()>0) {
                        //  Log.d(TAG,"buscando elim");
                        List<ListaCompraDetalle> liscompapp = lcdrepo.getAllSimpl();
                        for (ListaCompraDetalle compra : liscompapp
                        ) {
                            //veo si está en el json si no es que se elimina, solo checo que no
                            //tenga informe
                            eliminarListaCompraDet(compra, compraResp.getInserts().getListaCompraDetalle(), infdrepo, lcdrepo);
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

    }

    private void eliminarListaCompraDet(ListaCompraDetalle compra, List<ListaCompraDetalle> json, InformeComDetRepositoryImpl infdrepo, ListaCompraDetRepositoryImpl lcdrepo){
        for (ListaCompraDetalle jcompra: json) {
            if(jcompra.getId()==compra.getId()&&compra.getListaId()==jcompra.getListaId()){
                // Log.d(TAG,compra.getProductoNombre()+"--"+jcompra.getProductoNombre()+".."+compra.getListaId()+"--"+ compra.getId());
                return;
            }
        }

        //si llego aqui, no lo encontré por lo que se eliminó
        //busco que no tenga informe

        List<InformeCompraDetalle> prods=infdrepo.findByCompra(compra.getListaId(), compra.getId());
        Log.d(TAG,"prbabbl elimine "+compra.getProductoNombre()+"--"+compra.getListaId()+"--"+ compra.getId());
        if(prods==null||prods.size()<1)
            lcdrepo.delete(compra);

    }
    private void eliminarListaCompra(ListaCompra compra, List<ListaCompra> json, InformeComDetRepositoryImpl infdrepo, ListaCompraRepositoryImpl lcrepo){
        for (ListaCompra jcompra: json) {
           // Log.d(TAG,"LOS QUE SE ELIMINARON json"+jcompra.getId()+"--"+compra.getId());
            if(jcompra.getId()==compra.getId()){
                // Log.d(TAG,compra.getProductoNombre()+"--"+jcompra.getProductoNombre()+".."+compra.getListaId()+"--"+ compra.getId());
                return;
            }
        }

        //si llego aqui, no lo encontré por lo que se eliminó
        //busco que no tenga informe

        List<InformeCompraDetalle> prods=infdrepo.findByCompra(compra.getId());
        Log.d(TAG,"prbabbl elimine "+compra.getId());
        if(prods==null||prods.size()<1)
            lcrepo.delete(compra);

    }
}
