package com.example.comprasmu;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;


import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.Geocerca;
import com.example.comprasmu.data.modelos.InformeCompraDetalle;

import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.data.modelos.Visita;
import com.example.comprasmu.data.remote.InformeEnvio;
import com.example.comprasmu.data.remote.ListaCompraResponse;
import com.example.comprasmu.data.remote.RespInfEtapaResponse;
import com.example.comprasmu.data.remote.RespInformesResponse;
import com.example.comprasmu.data.repositories.AtributoRepositoryImpl;
import com.example.comprasmu.data.repositories.CatalogoDetalleRepositoryImpl;
import com.example.comprasmu.data.repositories.CorreccionRepoImpl;
import com.example.comprasmu.data.repositories.DetalleCajaRepoImpl;
import com.example.comprasmu.data.repositories.GeocercaRepositoryImpl;
import com.example.comprasmu.data.repositories.ImagenDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InfEtapaDetRepoImpl;
import com.example.comprasmu.data.repositories.InfEtapaRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeComDetRepositoryImpl;
import com.example.comprasmu.data.repositories.InformeCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraDetRepositoryImpl;
import com.example.comprasmu.data.repositories.ListaCompraRepositoryImpl;
import com.example.comprasmu.data.repositories.ProductoExhibidoRepositoryImpl;
import com.example.comprasmu.data.repositories.SustitucionRepositoryImpl;
import com.example.comprasmu.data.repositories.TablaVersionesRepImpl;
import com.example.comprasmu.data.repositories.VisitaRepositoryImpl;
import com.example.comprasmu.ui.tiendas.PeticionMapaCd;
import com.example.comprasmu.utils.Constantes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DescargasIniAsyncTask extends AsyncTask<String, Void, Void> {
    AlertDialog alertDialog;
    CatalogoDetalleRepositoryImpl cdrepo;
    SustitucionRepositoryImpl sustRepo;
    TablaVersionesRepImpl tvRepo;
    AtributoRepositoryImpl atRepo;
    SimpleDateFormat sdfdias;
    ListaCompraDetRepositoryImpl lcdrepo;
    ListaCompraRepositoryImpl lcrepo;
    InformeCompraRepositoryImpl inforepo;
    VisitaRepositoryImpl visRepo;
    InformeComDetRepositoryImpl infdrepo;
    ImagenDetRepositoryImpl imagenDetRepo;
    ProductoExhibidoRepositoryImpl prodrepo;
    GeocercaRepositoryImpl georep;
    InformeCompraRepositoryImpl infrepo;
    boolean notificar=false;
    Activity act;
    int actualiza;
   // DescargaRespAsyncTask.ProgresoRespListener proglist;
    final String TAG="DescargasIniAsyncTask";
   // private static final String DOWNLOAD_PATH = "https://muesmerc.mx/comprasv1/fotografias";
  //  private   String DESTINATION_PATH ;
    private final ProgresoListener miproglis;

    public DescargasIniAsyncTask(Activity act, CatalogoDetalleRepositoryImpl cdrepo,
                                 TablaVersionesRepImpl tvRepo,
                                 AtributoRepositoryImpl atRepo, ListaCompraDetRepositoryImpl lcdrepo, ListaCompraRepositoryImpl lcrepo,ProgresoListener miproglis, SustitucionRepositoryImpl sustRepo,GeocercaRepositoryImpl georep) {

        this.cdrepo=cdrepo;
        this.atRepo=atRepo;
        this.tvRepo=tvRepo;
        this.lcdrepo=lcdrepo;
        this.lcrepo=lcrepo;
        this.sustRepo=sustRepo;
        this.act=act;
        sdfdias=new SimpleDateFormat("dd-MM-yyyy");
       // this.proglist=proglist;
        this.georep=georep;
        this.miproglis=miproglis;

    }

    @Override
    protected Void doInBackground(String... indice) {

      //  if(NavigationDrawerActivity.isOnlineNet()) {
            Log.d("DescargasIniAsyncTask","iniciando descarga");
            if (indice[1].equals("act")) //vengo del fragment de actualizar lista
                actualiza=1;
           // if (indice[0].equals("cat")) //descargo cats tmb
            catalogos();
            buscarZonas();

            listacompras(); //aqui esta informes


      /*  }else
              {
            notificar = true;
                  Log.d("DescargasIniAsyncTask"," sin internet");
          //  cancel(true);

        }*/
        return null;

    }

    private void catalogos()
    {
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);


        TablaVersiones cats=tvRepo.getVersionByNombreTabla("catalogos");


        if(cats!=null){
            //si hoy ya se actualizó no actualizo
            Log.d(TAG,"comprobando vers catalog*"+sdfdias.format(cats.getVersion())+"--"+(sdfdias.format(new Date())));
            //no actualice
            if(actualiza==1) {
                if(NavigationDrawerActivity.isOnlineNet()) {
                    ps.getCatalogos(cdrepo, tvRepo, atRepo);
                    ps.getSustitucion(tvRepo, sustRepo);
                }
                else

                    notificar = true;
                  /*  act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("DescargasIniAsyncTask","estas al dia*");

                          //  proglist.cerrarAlerta();
                         //   proglist.todoBien();
                        }
                    });*/
            }else
            if(!sdfdias.format(cats.getVersion()).equals(sdfdias.format(new Date()))){
                if(NavigationDrawerActivity.isOnlineNet()) {
                    ps.getCatalogos(cdrepo, tvRepo, atRepo);
                    ps.getSustitucion(tvRepo,sustRepo);
                }

                else

                    notificar = true;

            }

        }else {   //primera vez
            Log.d("DescargasIniAsyncTask","iniciando descarga cats");
            if(NavigationDrawerActivity.isOnlineNet()) {
                ps.getCatalogos(cdrepo, tvRepo, atRepo);
                ps.getSustitucion(tvRepo, sustRepo);
            }
            else
                notificar = true;
        }

    }

    private void listacompras(){
        Log.d("DescargasIniAsyncTask", "descargando listas");

        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        TablaVersiones comp=tvRepo.getVersionByNombreTablasmd(Contrato.TBLLISTACOMPRAS,Constantes.INDICEACTUAL);
        TablaVersiones det=tvRepo.getVersionByNombreTablasmd(Contrato.TBLLISTACOMPRASDET,Constantes.INDICEACTUAL);
        DescargasIniAsyncTask.DescargaIniListener listener=new DescargaIniListener();
        Log.d(TAG,"ssss"+comp);

        if(comp!=null){
            if(actualiza==1) {
                //siempre actualizo
                if(NavigationDrawerActivity.isOnlineNet())
                    ps.getListasdeCompra(null,null,Constantes.INDICEACTUAL,listener);
                else
                    notificar = true;
                  /*  act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("DescargasIniAsyncTask","estas al dia*");

                            proglist.cerrarAlerta();
                            proglist.todoBien();
                        }
                    });*/
            }else {
                Log.d(TAG,sdfdias.format(new Date())+"ssss"+sdfdias.format(comp.getVersion()));
                 if (!sdfdias.format(comp.getVersion()).equals(sdfdias.format(new Date()))) {
                    if (NavigationDrawerActivity.isOnlineNet())
                        ps.getListasdeCompra(comp, det, Constantes.INDICEACTUAL, listener);
                    else
                        notificar = true;

                }
                 informes();
            }


        }else {   //primera vez
            Log.d("DescargasIniAsyncTask", "primera vez");
            if(NavigationDrawerActivity.isOnlineNet())
            ps.getListasdeCompra(comp, det, Constantes.INDICEACTUAL, listener);
            else
                notificar = true;
        }
    }

    public void buscarZonas(){
        TablaVersiones cats=tvRepo.getVersionByNombreTabla("geocercas");
        //peticion al servidor

        PeticionMapaCd petmap=new PeticionMapaCd(Constantes.CLAVEUSUARIO);


        if(cats!=null){
            //si hoy ya se actualizó no actualizo
            Log.d(TAG,"comprobando vers geocercas*"+sdfdias.format(cats.getVersion())+"--"+(sdfdias.format(new Date())));
            //no actualice
            if(actualiza==1) {
                if(NavigationDrawerActivity.isOnlineNet()) {
                  pedirZonas(petmap);
                }
                else

                    notificar = true;
                  /*  act.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("DescargasIniAsyncTask","estas al dia*");

                          //  proglist.cerrarAlerta();
                         //   proglist.todoBien();
                        }
                    });*/
            }else
            if(!sdfdias.format(cats.getVersion()).equals(sdfdias.format(new Date()))){
                if(NavigationDrawerActivity.isOnlineNet()) {
                    pedirZonas(petmap);
                }

                else

                    notificar = true;

            }

        }else {   //primera vez
            Log.d("DescargasIniAsyncTask","iniciando descarga geocercas");
            if(NavigationDrawerActivity.isOnlineNet()) {
                pedirZonas(petmap);
            }
            else
                notificar = true;
        }



    }
    public void pedirZonas(PeticionMapaCd petmap) {
        DescargaIniListener listener=new DescargaIniListener();
        petmap.getZonas("", Constantes.INDICEACTUAL,listener); //se agregarian filtros despues
        // Log.d(TAG,"--"+pais+"--"+ciudad+"..."+planta+".."+cliente);
        //petmap.getTiendas("1","1",25,4,"2022-01-01","2022-04-01","",""); //se agregarian filtros despues


    }



  /*  private void informes() {
        if(getTotVisitas()==0) { //veo si tengo datos en el servidor
           // InformeComDetRepositoryImpl ifodrepo=new InformeComDetRepositoryImpl(act);
            inforepo=new InformeCompraRepositoryImpl(act);
            infdrepo=new InformeComDetRepositoryImpl(act);
          imagenDetRepo=new ImagenDetRepositoryImpl(act);
            prodrepo=new ProductoExhibidoRepositoryImpl(act);

            getRespaldo();
           // DescargasIniAsyncTask.DescargaIniListener listener=new DescargaIniListener();

           //

        }

    }*/


    /*private void getRespaldo(){
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        DescargasIniAsyncTask.DescargaIniListener listener=new DescargaIniListener();

        //   DescargaRespAsyncTask.DescargaRespListener listener=new DescargaRespAsyncTask.DescargaRespListener();
        ps.pedirRespaldo(Constantes.INDICEACTUAL,listener);

    }*/


    private void getRespaldo(){
        miproglis.estatusInf(1);
        Log.d("DescargasIniAsyncTask", "descargando respaldo");
        infrepo = new InformeCompraRepositoryImpl(act);
        infdrepo = new InformeComDetRepositoryImpl(act);
        imagenDetRepo = new ImagenDetRepositoryImpl(act);
        prodrepo = new ProductoExhibidoRepositoryImpl(act);
        visRepo=new VisitaRepositoryImpl(act);
        lcdrepo=new ListaCompraDetRepositoryImpl(act);
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        DescargaRespListener listener=new DescargaRespListener();
        ps.pedirRespaldo(Constantes.INDICEACTUAL,listener);

    }




    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
      /*  if(notificar)
            notificarSinConexion();*/
      //  Log.e("DescargasIniAsyT","---"+actualiza);

    }



        public class DescargaIniListener{
            public DescargaIniListener(){
                //if(proglist!=null&&actualiza==1)
                  //  proglist.cerrarAlerta();
            }
            public void noactualizar(String response){
                //a ver como la regresamos
             /*  if(actualiza==1&&proglist!=null){
                    proglist.cerrarAlerta(notificar);
                }*/
                miproglis.estatusLis(0);
                if(actualiza==0)
                    informes();
                else
                    miproglis.cerrarAlerta(false);




            }
            public void noactualizar2(String response){
                //a ver como la regresamos
             /*  if(actualiza==1&&proglist!=null){
                    proglist.cerrarAlerta(notificar);
                }*/
                if (actualiza==1)
                miproglis.cerrarAlerta(notificar);

            }
            public void insertarZonas(List<Geocerca> zonas){

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


            }
            public void actualizar(ListaCompraResponse compraResp) {
                //primero los inserts
                Log.d("Descargaini","actualizando listas>>");

                if (compraResp.getInserts() != null) {
                    if(compraResp.getInserts().getListaCompra()!=null) {
                       // Log.d("Descargaini","resp2>>"+compraResp.getInserts().getListaCompra());

                        lcrepo.insertAll(compraResp.getInserts().getListaCompra()); //inserto blblbl
                    }
                   // Log.d("Descargaini","resp3>>"+compraResp.getInserts().getListaCompraDetalle());

                    if(compraResp.getInserts().getListaCompraDetalle()!=null){
                            //como puede que ya existan reviso primero e inserto unoxuno
                            for(ListaCompraDetalle detalle:compraResp.getInserts().getListaCompraDetalle()){
                                ListaCompraDetalle existe=lcdrepo.findsimple(detalle.getListaId(),detalle.getId());
                                if(existe==null){
                                   // lcrepo.insert(detalle);

                                }else
                                {  //no reemplazo los comprados ni los nuevos codigos
                                    detalle.setComprados(existe.getComprados());
                                    detalle.setNvoCodigo(existe.getNvoCodigo());
                                    //lcrepo.updateSC(compra);
                                }
                                lcdrepo.insert(detalle);


                            }
                        }
                       // lcdrepo.insertAll(compraResp.getInserts().getListaCompraDetalle());
                }
                //los updates
                if (compraResp.getUpdates() != null) {

                    if(compraResp.getUpdates().getListaCompra()!=null)
                        lcrepo.insertAll(compraResp.getUpdates().getListaCompra()); //inserto blblbl
                    if(compraResp.getUpdates().getListaCompraDetalle()!=null) {
                        //como puede que ya existan reviso primero e inserto unoxuno
                        for(ListaCompraDetalle detalle:compraResp.getInserts().getListaCompraDetalle()){
                            ListaCompraDetalle existe=lcdrepo.findsimple(detalle.getListaId(),detalle.getId());
                            if(existe==null){
                                // lcrepo.insert(detalle);

                            }else
                            {   //mantengo los comprados y codigos nevos
                                detalle.setComprados(existe.getComprados());
                                detalle.setNvoCodigo(existe.getNvoCodigo());
                                //lcrepo.updateSC(compra);
                            }
                            lcdrepo.insert(detalle);

                        }
                       // lcdrepo.updateAll(compraResp.getUpdates().getListaCompraDetalle());
                    }
                }

                //actualizar version en tabla
                TablaVersiones tinfo = new TablaVersiones();
                tinfo.setNombreTabla(Contrato.TBLLISTACOMPRAS);
                Date fecha1=new Date();
                Log.d("DescargasAsyncTask","insertando fecha version 1"+fecha1);

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
                miproglis.estatusLis(0);
                if(actualiza==0)
                 informes(); //solo en la descarga incial

                else
                if (actualiza==1)
                    miproglis.cerrarAlerta(false);
            }



        }
    public void informes(){
        PeticionesServidor ps=new PeticionesServidor(Constantes.CLAVEUSUARIO);
        DescargaRespListener listener=new DescargaRespListener();
        int ban=0;
        if(getTotVisitas()==0) {
            //    DescargaRespAsyncTask.DescargaRespListener listener=new DescargaRespAsyncTask.DescargaRespListener();

            //  DescargaRespAsyncTask task = new DescargaRespAsyncTask( act, actualiza,proglist);
            getRespaldo();

            //   task.execute("", "act"); //para saber que estoy actualizando
        }else ban=1;


        if(getTotInfEtapas()==0){


            ps.pedirRespaldo2(Constantes.INDICEACTUAL,listener);
        }else ban=1;
        if(getTotCorre()==0)
        {
           ps.pedirRespaldoCor(Constantes.INDICEACTUAL,listener);
        }else ban=1;
            if (ban==1&&actualiza==1)

            miproglis.cerrarAlerta(false);


    }
    public int getTotVisitas(){
        visRepo=new VisitaRepositoryImpl(act);
        int ultimo=visRepo.getUltimo(Constantes.INDICEACTUAL);
        Log.d(TAG, "consecutivo encontrado"+ultimo);

        return ultimo;
    }

    public int getTotInfEtapas(){
        InfEtapaRepositoryImpl ierepo=new InfEtapaRepositoryImpl(act);
        int ultimo=(int)ierepo.getUltimo(Constantes.INDICEACTUAL);
        Log.d(TAG, "consecutivo  infe encontrado"+ultimo);

        return ultimo;
    }
    public int getTotCorre(){
        CorreccionRepoImpl correccionRepo=new CorreccionRepoImpl(act);
        int ultimo=(int)correccionRepo.getUltimo(Constantes.INDICEACTUAL);
        Log.d(TAG, "getTotCorre "+ultimo);

        return ultimo;
    }


    public interface ProgresoListener {
         void cerrarAlerta(boolean res);
         void todoBien(RespInformesResponse infoResp);
         void estatusInf(int es);
         void estatusLis(int es);
         void imagenesEtapa(RespInfEtapaResponse infoResp);

    }
    public void notificarSinConexion(){
        AlertDialog.Builder builder=new AlertDialog.Builder(act);
        builder.setCancelable(true);
        builder.setIcon(android.R.drawable.stat_sys_download);
        builder.setTitle(act.getString(R.string.atencion));
        builder.setMessage("La aplicación necesita actualizarse. Por favor revise su conexión a internet o active sus datos");


        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton(act.getString(R.string.aceptar),new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        AlertDialog alert=builder.create();
        alert.show();
    }

    public class DescargaRespListener {
        public DescargaRespListener() {
            //if(proglist!=null&&actualiza==1)
            //  proglist.cerrarAlerta();
        }

        public void noactualizar(RespInformesResponse infoResp){
            //a ver como la regresamos
            miproglis.todoBien(infoResp);
            miproglis.estatusInf(0);
        }
        public void noactualizarE( ){
            //a ver como la regresamos
            //  miproglis.todoBien(infoResp);
            miproglis.estatusInf(0);
            if(actualiza==1)
                miproglis.cerrarAlerta(false);
        }
        public void noactualizarCor( ){
            //a ver como la regresamos
            //  miproglis.todoBien(infoResp);
            miproglis.estatusInf(0);
            if(actualiza==1)
                miproglis.cerrarAlerta(false);
        }



        public void actualizarInformes(RespInformesResponse infoResp) {
            Log.d(TAG,"actualizando bd informes");
            //primero los inserts
            if (infoResp.getVisita() != null) {
                //reviso cada uno y las inserto
                for (Visita vis : infoResp.getVisita()) {
                    visRepo.insert(vis); //inserto blblbl
                }
            }
            if (infoResp.getInformeCompra() != null && infoResp.getInformeCompra().size() > 0) {
                //como puede que ya existan reviso primero e inserto unoxuno
                infrepo.insertAll(infoResp.getInformeCompra());
            }
            if (infoResp.getInformeCompraDetalles() != null && infoResp.getInformeCompraDetalles().size() > 0) {
                //como puede que ya existan reviso primero e inserto unoxuno
                infdrepo.insertAll(infoResp.getInformeCompraDetalles());
            }


            if (infoResp.getImagenDetalles() != null && infoResp.getImagenDetalles().size() > 0) {
                //como puede que ya existan reviso primero e inserto unoxuno
                imagenDetRepo.insertAll(infoResp.getImagenDetalles());
             //   descargarImagenes(infoResp.getImagenDetalles());

            }
            if (infoResp.getProductosEx() != null && infoResp.getProductosEx().size() > 0) {
                //como puede que ya existan reviso primero e inserto unoxuno
                prodrepo.insertAll(infoResp.getProductosEx());
            }
            //sumo en la lista de compra
            //   List<InformeCompraDetalle> detalles=infdrepo.getAllsimple();
            //ajusto cantidades
            Log.d(TAG,"ajusto cantidades");

            if(infoResp.getInformeCompraDetalles()!=null)
                for(InformeCompraDetalle det:infoResp.getInformeCompraDetalles()){
                    Log.d(TAG,"ttttt"+det.getComprasId()+"--"+det.getComprasDetId());
                    if(det.getTipoMuestra()!=3) {//para normal o catchup
                        ListaCompraDetalle compradet=lcdrepo.findsimple(det.getComprasId(),det.getComprasDetId());
                        if(compradet!=null) {
                            //  Log.d(TAG,"ttttt"+det.getComprasId()+"--"+det.getId());
                            Log.d(TAG, "sss" + compradet.getProductoNombre() + "--" + compradet.getComprados());

                            int nvacant = compradet.getComprados() + 1;
                            //  lcdrepo.actualizarComprados(det.getId(),det.getComprasId(),nvacant);
                            //actualizo lo codigos comprados
                            String listaCodigos = "";
                            SimpleDateFormat sdfcodigo = new SimpleDateFormat("dd-MM-yy");
                            String nuevoCodigo = sdfcodigo.format(det.getCaducidad());
                            //no aumento el comprado solo el codigo

                            if (compradet.getNvoCodigo() != null)
                            //reviso que no existe
                            {
                                if (!compradet.getNvoCodigo().contains(nuevoCodigo))
                                    listaCodigos = nuevoCodigo + ";" + compradet.getNvoCodigo();
                            } else
                                listaCodigos = nuevoCodigo;
                            compradet.setNvoCodigo(listaCodigos);

                            compradet.setComprados(nvacant);
                            //actualizo
                            lcdrepo.insert(compradet);
                        }
                    }
                    else{
                        ListaCompraDetalle compradet = lcdrepo.findsimple(det.getComprasId(), det.getComprasDetId());
                        if(compradet!=null) {
                            //  Log.d(TAG,"ttttt"+det.getComprasId()+"--"+det.getId());
                            Log.d(TAG, "sss" + compradet.getProductoNombre() + "--" + compradet.getComprados());

                            int nvacant = compradet.getComprados() + 1;
                            compradet.setComprados(nvacant);
                            //actualizo
                            lcdrepo.insert(compradet);
                            //el codigo va en el comprado
                            ListaCompraDetalle compradetbu = lcdrepo.findsimple(det.getComprasIdbu(), det.getComprasDetIdbu());
                            if (compradetbu != null) {
                                //  Log.d(TAG,"ttttt"+det.getComprasId()+"--"+det.getId());
                                Log.d(TAG, "sss" + compradetbu.getProductoNombre() + "--" + compradetbu.getComprados());
                                //actualizo lo codigos comprados
                                String listaCodigos = "";
                                SimpleDateFormat sdfcodigo = new SimpleDateFormat("dd-MM-yy");
                                String nuevoCodigo = sdfcodigo.format(det.getCaducidad());
                                //no aumento el comprado solo el codigo

                                if (compradetbu.getNvoCodigo() != null)
                                //reviso que no existe
                                {
                                    if (!compradetbu.getNvoCodigo().contains(nuevoCodigo))
                                        listaCodigos = nuevoCodigo + ";" + compradetbu.getNvoCodigo();
                                } else
                                    listaCodigos = nuevoCodigo;
                                compradetbu.setNvoCodigo(listaCodigos);


                                //actualizo
                                lcdrepo.insert(compradetbu);
                            }
                        }
                    }


                }

            miproglis.estatusInf(0);
            miproglis.todoBien( infoResp);

        }

        public void actualizarInfEtapa(RespInfEtapaResponse response){
            InfEtapaRepositoryImpl erepo=new InfEtapaRepositoryImpl(act);
            InfEtapaDetRepoImpl edrepo=new InfEtapaDetRepoImpl(act);

            DetalleCajaRepoImpl cajrepo=new DetalleCajaRepoImpl(act);


            if(response.getInformeEtapa()!=null){
                Log.d(TAG,"actualizarInfEtapa "+response.getInformeEtapa().size());
                erepo.insertAll(response.getInformeEtapa());
            }
            if(response.getInformeEtapaDet()!=null){
                edrepo.insertAll(response.getInformeEtapaDet());
            }
            if(response.getDetalleCaja()!=null){
                cajrepo.insertAll(response.getDetalleCaja());
            }
            miproglis.imagenesEtapa(response);
            if(actualiza==1)
            miproglis.cerrarAlerta(false);
        }

        public void actualizarCorre(RespInfEtapaResponse response){

            CorreccionRepoImpl correpo=new CorreccionRepoImpl(act);


            if(response.getCorrecciones()!=null){
                correpo.insertAll(response.getCorrecciones());
            }
            miproglis.imagenesEtapa(response);
            if(actualiza==1)
                miproglis.cerrarAlerta(false);
        }
    }
}
