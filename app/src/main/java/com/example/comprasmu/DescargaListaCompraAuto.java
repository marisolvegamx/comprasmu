package com.example.comprasmu;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.remote.ListaCompraResponse;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.GeocercaRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.SiglaRepositoryImpl;
import com.example.comprasmu.data.repositories.SustitucionRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.utils.ComprasLog;

import java.util.List;

public class DescargaListaCompraAuto  {

    ListaCompraDetRepositoryImpl lcdrepo;
    ListaCompraRepositoryImpl lcrepo;
    Activity act;
    private ComprasLog comprasLog;
    final String TAG="DescargaListaCompraAuto";
    InformeComDetRepositoryImpl infdrepo;
    public DescargaListaCompraAuto(ComprasLog comprasLog,
                                     ListaCompraRepositoryImpl lcrepo,ListaCompraDetRepositoryImpl lcdrepo,  InformeComDetRepositoryImpl infdrepo) {


        this.lcdrepo=lcdrepo;
        this.lcrepo=lcrepo;
        this.act=act;
        this.infdrepo = infdrepo;
        this.comprasLog = comprasLog;

    }


    public void actualizar(ListaCompraResponse compraResp) {
        //primero los inserts

        if (compraResp != null) {
            if (compraResp.getInserts() != null) {
                if (compraResp.getInserts().getListaCompra() != null) {
                    Log.d(TAG, "listacomp<" + compraResp.getInserts().getListaCompra());
                    comprasLog.grabarError(TAG, "actualizar lista compra", "listacomp<");

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
                        long id = lcdrepo.insert(detalle);
                        //Log.d(TAG,"**insertando"+id);


                    }
                    //reviso los que se eliminaron
                    if (compraResp.getInserts().getListaCompraDetalle() != null && compraResp.getInserts().getListaCompraDetalle().size() > 0) {
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


        }
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
        List<InformeCompraDetalle> prods=infdrepo.findByCompra(compra.getListaId(), compra.getId());
        Log.d(TAG,"eliminar? "+compra.getProductoNombre()+"--"+compra.getListaId()+"--"+ compra.getId());
        if(prods==null||prods.size()<1)
            lcdrepo.delete(compra);

    }




}
