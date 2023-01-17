package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.DetalleCaja;
import com.example.comprasmu.data.modelos.ImagenDetalle;
import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.List;

public class InformeEtapaEnv {

    private InformeEtapa informeEtapa;
    private List<InformeEtapaDet> informeEtapaDet;
    private List<DetalleCaja> detalleCaja;
    private String claveUsuario;
    private String indice;
    private List<ImagenDetalle> imagenDetalles;

    public InformeEtapa getInformeEtapa() {
        return informeEtapa;
    }

    public void setInformeEtapa(InformeEtapa informeEtapa) {
        this.informeEtapa = informeEtapa;
    }

    public List<InformeEtapaDet> getInformeEtapaDet() {
        return informeEtapaDet;
    }

    public void setInformeEtapaDet(List<InformeEtapaDet> informeEtapaDet) {
        this.informeEtapaDet = informeEtapaDet;
    }

    public List<DetalleCaja> getDetalleCaja() {
        return detalleCaja;
    }

    public void setDetalleCaja(List<DetalleCaja> detalleCaja) {
        this.detalleCaja = detalleCaja;
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

    public List<ImagenDetalle> getImagenDetalles() {
        return imagenDetalles;
    }

    public void setImagenDetalles(List<ImagenDetalle> imagenDetalles) {
        this.imagenDetalles = imagenDetalles;
    }

    public String toJson(InformeEtapaEnv informe) {
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
