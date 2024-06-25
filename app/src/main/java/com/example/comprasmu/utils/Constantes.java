package com.example.comprasmu.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.comprasmu.data.modelos.DescripcionGenerica;
import com.example.comprasmu.data.modelos.ListaCompra;
import com.example.comprasmu.data.modelos.ListaCompraDetalle;
import com.example.comprasmu.ui.informedetalle.NuevoDetalleViewModel;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Constantes {

    public static final String TAG = "Comprasmu";
    public static final int REQUEST_CHECK_SETTINGS =0 ;
    public static String SIMBOLOMON="$" ; //todo falta definirlo al inicio
    public static String INDICEACTUAL; //esta en la forma m-yyyyy
    public static int PLANTASEL;
    public static int CIUDADSEL;
    public static final int REQUEST_CODE_TAKE_PHOTO = 1;
    public static int ETAPAMENU; //para mostrar el boton
    public static  String CLAVEUSUARIO ;
    public static  String CIUDADTRABAJO ;
    public static  int IDCIUDADTRABAJO ;
    public static  int IDPAISTRABAJO;
    public static  String PAISTRABAJO;
    public static final String ESTADO = "estado";
    public static final String MENSAJE = "mensaje";

    public static String []meses={"Enero","Febrero","Marzo","Abril", "Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    public static String []anios={"2021","2022","2023","2024", "2025","2026","2027","2028","2029","2030"};
     public static final String SUCCESS ="1" ;
    public static final String FAILED = "2";
    /****estatus sync***/
    public static final int PENDIENTE =0 ;
    public static boolean LOGGEADO;
    public static boolean ACTUALIZADO;

    public static int SINCRONIZANDO;
    public static final int ENVIADO =2;
    public static final int ENVIANDO =1;
    /********************/
    public static SimpleDateFormat  vistasdf=new SimpleDateFormat("dd-MM-yy HH:mm:ss");
    public static SimpleDateFormat  sdfcaducidad=new SimpleDateFormat("dd-MM-yy");
    public static SimpleDateFormat  sdfsolofecha=new SimpleDateFormat("dd-MM-yyyy");

    public static String []listaindices;
    public static final String ACCOUNT_TYPE ="com.example.ejemploimagen.account" ;
    public static String[] ESTATUSINFORME={"CANCELADO","ABIERTO","FINALIZADO","CON INFORME","ABIERTO"}; //EL segundo abiero es para cuando se reactiva compras y se vuleve a abrir etiquetado
    public static String[] ESTATUSSYNC={"PENDIENTE","ENVIANDO","ENVIADO"};
    public static String[] PUNTOCARDINAL={"NORTE","SUR","ESTE","OESTE","CENTRO"};
   public static String[] ETAPAS={"0","PREPARACION","COMPRA","ETIQUETADO","EMPAQUE"};
    public static boolean varciudades;
    public static List<DescripcionGenerica> clientesAsignados;
    public static String URLSERV="https://muesmerc.com/comprasv1/";
    //local
  //  public static String URLPRUEBAS1="https://muesmerc.com/comprasv1/";

     public static String URLPRUEBAS1="http://192.168.1.170/comprasv1/";
    //muesmerc
    //public static String URLPRUEBAS1="http://192.168.1.95/comprasv1/";
   // public static String URLPRUEBAS1="https://phpstack-1150317-4003300.cloudwaysapps.com/comprasv1/";
//nva
  // public static String URLPRUEBAS1="http://192.168.1.68/comprasv1/";
   // public static String URLPRUEBAS2="https://phpstack-1150317-4003300.cloudwaysapps.com/comprasv1/";
    public static String ni_clientesel;
    public static int ni_plantasel;
    public static String ni_plantanombre;
    public static int NM_TOTALISTA;
    public static int DP_CONSECUTIVO;
    public static int LC_CONSECUTIVO;
    public static int DP_TIPOTIENDA;

    public static NuevoDetalleViewModel.ProductoSel productoSel;
    public static HashMap <Integer,String>TIPOTIENDA;
    public static String CONFROTAR="rotar"; //0 no roto 1 roto
   public static class VarDetalleProd{
       public static   String tomadode;
        public static   String nvoatra;
        public static  String nvoatrb;
        public static String nvoatrc;
       public static String nvoatrd;
    }
    public static class VarListCompra{
        public static   int idListaSel;
        public static ListaCompra listaSelec;
        public static int plantaSel;
        public static ListaCompraDetalle detallebuSel;


    }
    public static int ETAPAACTUAL;

    public static void definirTrabajo(Activity act) {
        SharedPreferences prefe = act.getSharedPreferences("comprasmu.datos", Context.MODE_PRIVATE);
        Constantes.CIUDADTRABAJO = prefe.getString("ciudadtrabajo", "");
        Constantes.IDCIUDADTRABAJO=prefe.getInt("idciudadtrabajo",0);

    }
}
