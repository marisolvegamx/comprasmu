package com.example.comprasmu.data.modelos;

import com.google.gson.Gson;

import java.util.Date;

public class InformeToJson {

  /*  public int inf_consecutivo;
    public int inf_visitasIdlocal;
    public boolean inf_segunda_muestra;
    public boolean inf_tercera_muestra;
    public int inf_usuario;
    public String inf_comentarios;
    public int inf_estatus;
    public Date inf_created_at;
    public Date inf_updated_at;
    public boolean inf_primera_muestra;
    public int inf_plantasid;

    public int  inf_ticket_compra;
    public int inf_condiciones_traslado;*/

    public String toJson(InformeCompra informe) {
      //  this.inf_visitasIdlocal=informe.getVisitasId();


        Gson gson = new Gson();
        String JSON = gson.toJson(informe);
        return  JSON;

    }
}
