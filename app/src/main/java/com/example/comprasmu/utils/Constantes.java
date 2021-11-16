package com.example.comprasmu.utils;

import com.example.comprasmu.data.modelos.DescripcionGenerica;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Constantes {

    public static final String TAG = "Comprasmu";
    public static final int REQUEST_CHECK_SETTINGS =0 ;
    public static String INDICEACTUAL;
    public static int PLANTASEL;
    public static int CIUDADSEL;
    public static final int REQUEST_CODE_TAKE_PHOTO = 1;
    //TODO falta iniciar el usuario en el main
    public static  String CLAVEUSUARIO ;
    public static  String CIUDADTRABAJO ;
    public static  int IDCIUDADTRABAJO ;
    public static  int IDPAISTRABAJO;
    public static  String PAISTRABAJO;
    public static final String ESTADO = "estado";
    public static final String MENSAJE = "mensaje";
    public static final String ID_GASTO = "idGasto";
    public static String []meses={"Enero","Febrero","Marzo","Abril", "Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
    public static String []anios={"2021","2022","2023","2024", "2025","2026","2027","2028","2029","2030"};
     public static final String SUCCESS ="1" ;
    public static final String FAILED = "2";
    /****estatus sync***/
    public static final int PENDIENTE =0 ;

    public static final int SINCRONIZANDO = 1;
    public static final int ENVIADO =2;
    /********************/
    public static SimpleDateFormat  vistasdf=new SimpleDateFormat("dd-MM-yy HH:mm:ss");
    public static SimpleDateFormat  sdfcaducidad=new SimpleDateFormat("dd-MM-yy");

    public static String []listaindices;
    public static final String ACCOUNT_TYPE ="com.example.ejemploimagen.account" ;
    public static String[] ESTATUSINFORME={"CANCELADO","ABIERTO","FINALIZADO","CON INFORME"};
    public static String[] ESTATUSSYNC={"PENDIENTE","ENVIANDO","ENVIADO"};

    public static List<DescripcionGenerica> clientesAsignados;
    public static HashMap<Integer,String> plantasAsignadas;
    public static String ni_clientesel;
    public static int NM_TOTALISTA;
    public static int DP_TIPOTIENDA;
}
