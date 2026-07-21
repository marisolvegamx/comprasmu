package com.example.comprasmu;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.comprasmu.data.dao.ConfiguracionRepositoryImpl;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeCompra;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.ProductoExhibido;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.remote.ListaCompraResponse;
import com.example.comprasmu.data.remote.RespInfEtapaResponse;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.GeocercaRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
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
import com.google.android.material.tabs.TabLayout;

import java.io.File;
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

    public static void actualizarInformeDetalle(InfEtapaDetRepoImpl idrepository, RespInfEtapaResponse infoResp) {

        Log.i("InformesGenViewModel", "actualizando bd informes");
        //primero los inserts
        InformeEtapaDet informeEtapaDetOrig;
        if (infoResp != null) {

            if (infoResp.getInformeEtapaDet() != null && infoResp.getInformeEtapaDet().size() > 0) {

                for (InformeEtapaDet det:infoResp.getInformeEtapaDet()
                ) {
                    //busco

                    informeEtapaDetOrig=idrepository.findsimple(det.getId());
                    if(informeEtapaDetOrig!=null) {
                        //modifico
                        informeEtapaDetOrig.setRuta_foto(det.getRuta_foto());
                        informeEtapaDetOrig.setQr(det.getQr());
                        informeEtapaDetOrig.setNum_muestra(det.getNum_muestra());
                        informeEtapaDetOrig.setDescripcionId(det.getDescripcionId());
                        informeEtapaDetOrig.setDescripcion(det.getDescripcion());
                        informeEtapaDetOrig.setNum_caja(det.getNum_caja());
                    }
                    else
                        informeEtapaDetOrig=det;
                    //actualizo
                    idrepository.insert(informeEtapaDetOrig);
                }

            }

        }


    }
    public static void eliminarInforme(ComprasLog miLog, VisitaRepositoryImpl visitaRepository, ProductoExhibidoRepositoryImpl prodeRepository,InformeCompraRepositoryImpl infrepo, InformeCompra inf, ImagenDetRepositoryImpl imdRepository, InformeComDetRepositoryImpl idrepo, ListaCompraDetRepositoryImpl lcRepository, String directorio){

        //reviso si es el unico de la visita, para borrar la visita
        List<InformeCompra>  masinformes=infrepo.getAllByVisitasimple(inf.getVisitasId());

        if(masinformes!=null&&masinformes.size()==1) {
            //solo hay 1 informe y puedo eliminar la visita
            Log.i("dESCARGAlISTAcOMPRA","elimino la visita"+inf.getVisitasId());
            miLog.info("dESCARGAlISTAcOMPRA","eliminarInforme","elimino la visita"+inf.getVisitasId());
            Visita eliminar= visitaRepository.findsimple(inf.getVisitasId());
            eliminarVisita(eliminar,visitaRepository,imdRepository,prodeRepository,directorio);
        }
        Log.i("DescagaListaCompraAuto","elimino el informe"+inf.getId());
        infrepo.deleteInformeCompra(inf.getId());
        borrarImagenesxInforme(miLog,inf,imdRepository,idrepo,lcRepository,directorio);
    }

    public static void eliminarVisita(Visita eliminar, VisitaRepositoryImpl visitaRepository, ImagenDetRepositoryImpl imdRepository, ProductoExhibidoRepositoryImpl prodeRepository, String directorio){
        if(eliminar!=null) {

            ImagenDetalle img1=imdRepository.findsimple(eliminar.getFotoFachada());
            if(img1!=null) {//borro el archivo
                File fdelete = new File(directorio+img1.getRuta());
                if (fdelete.exists())
                    fdelete.delete();
            }
            //elimino las imagenes
            imdRepository.deleteById(eliminar.getFotoFachada());
            List<ProductoExhibido> prods= prodeRepository.getAllByVisitaSimple(eliminar.getId());

            //elimino prods
            if(prods!=null&&prods.size()>0)
                for (ProductoExhibido prod:prods) {

                    ImagenDetalle img2=imdRepository.findsimple(eliminar.getFotoFachada());
                    if(img2!=null)
                    { //borro el archivo
                        File fdelete2 = new File(directorio+img2.getRuta());
                        if (fdelete2.exists())
                            fdelete2.delete();}
                    imdRepository.deleteById(prod.getImagenId());
                }
            prodeRepository.deleteAllByVisita(eliminar.getId());

            visitaRepository.delete(eliminar);


        }
    }
    public static  void borrarImagenesxInforme(ComprasLog miLog,InformeCompra inf, ImagenDetRepositoryImpl imdRepository,InformeComDetRepositoryImpl idrepo, ListaCompraDetRepositoryImpl lcRepository, String directorio){
       // String directorio=application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)+"/";
        ImagenDetalle img1 = imdRepository.findsimple(inf.getTicket_compra());
        if(img1!=null) {//borro el archivo
            File fdelete = new File(directorio+img1.getRuta());
            if (fdelete.exists())
                fdelete.delete();
        }
        img1 = imdRepository.findsimple(inf.getCondiciones_traslado());
        //borro el archivo
        if(img1!=null) {
            File fdelete = new File(directorio+img1.getRuta());
            if (fdelete.exists())
                fdelete.delete();
        }
        //busco los detalles
        List<InformeCompraDetalle> det=idrepo.getAllSencillo(inf.getId());
        if(det!=null)
            for (InformeCompraDetalle infd : det) {
                List<ImagenDetalle> fotos= imdRepository.getFotosInfDet(infd);
                if(fotos!=null)
                    for(ImagenDetalle img:fotos){
                        if(img!=null) {
                            File fdelete = new File(directorio+img.getRuta());
                            if (fdelete.exists())
                                fdelete.delete();
                        }
                    }
                Log.d("DEScargaListaCompraAuto","eliminando detalles inf compra:"+infd.getComprasId()+"--det:"+infd.getComprasDetId());
                miLog.info("DEScargaListaCompraAuto","borrarImagenesxInforme","eliminando detalles inf compra:"+infd.getComprasId()+"--det:"+infd.getComprasDetId());

                //ajusto cantidades
                //solo si es normal
                if(infd.getTipoMuestra()!=3) {
                    ListaCompraDetalle compradet = lcRepository.findsimple(infd.getComprasId(), infd.getComprasDetId());
                    if (compradet != null && compradet.getComprados() > 0) {
                        int nvacant = compradet.getComprados() - 1;
                        Log.d("DEScargaListaCompraAuto","actualizando comprados compradet:"+ infd.getComprasDetId()+" compraid:" +infd.getComprasId());
                        miLog.info("DEScargaListaCompraAuto","borrarImagenesxInforme","actualizando comprados compradet:"+ infd.getComprasDetId()+" compraid:" +infd.getComprasId());

                        lcRepository.actualizarComprados(infd.getComprasDetId(), infd.getComprasId(), nvacant);
                    }
                }
                //borro los detalles
                idrepo.delete(infd);
            }
    }


}
