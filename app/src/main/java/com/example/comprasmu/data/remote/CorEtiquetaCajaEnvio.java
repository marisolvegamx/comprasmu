package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.CorEtiquetadoCaja;
import com.example.comprasmu.data.modelos.CorEtiquetadoCajaDet;
import com.example.comprasmu.data.modelos.Correccion;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.List;

public class CorEtiquetaCajaEnvio {

    private CorEtiquetadoCaja correccion;
    private List<CorEtiquetadoCajaDet> cordetalles;
    private List<ImagenDetalle> imagenes;
    private String claveUsuario;
    private String indice;

    public CorEtiquetadoCaja getCorreccion() {
        return correccion;
    }

    public void setCorreccion(CorEtiquetadoCaja correccion) {
        this.correccion = correccion;
    }

    public List<CorEtiquetadoCajaDet> getCordetalles() {
        return cordetalles;
    }

    public void setCordetalles(List<CorEtiquetadoCajaDet> cordetalles) {
        this.cordetalles = cordetalles;
    }

    public String getClaveUsuario() {
        return claveUsuario;
    }

    public void setClaveUsuario(String claveUsuario) {
        this.claveUsuario = claveUsuario;
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public List<ImagenDetalle> getImagenes() {
        return imagenes;
    }

    public void setImagenes(List<ImagenDetalle> imagenes) {
        this.imagenes = imagenes;
    }

    public String toJson(CorEtiquetaCajaEnvio informe) {
        //  this.inf_visitasIdlocal=informe.getVisitasId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();


      //  Gson gson = new Gson();
       // String informejson=gson.toJson(informe.informeEtapa);
        String JSON = gson.toJson(informe);
        return  JSON;
        /*ObjectMapper mapper=new ObjectMapper();
        mapper.setDateFormat(sdf);
        String json = null;
        try {
            json = mapper.writeValueAsString(informe);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            Log.e("InformeEnvio","hubo un error al serializar"+e.getMessage());
        }
        return json;*/

        // System.out.println(json);

    }
}
