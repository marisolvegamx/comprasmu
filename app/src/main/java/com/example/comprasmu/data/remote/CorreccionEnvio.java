package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.Correccion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.List;

public class CorreccionEnvio {

    private Correccion correccion;
    private List<Correccion> correcciones;
    private String claveUsuario;
    private String indice;

    public Correccion getCorreccion() {
        return correccion;
    }

    public void setCorreccion(Correccion correccion) {
        this.correccion = correccion;
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

    public List<Correccion> getCorrecciones() {
        return correcciones;
    }

    public void setCorrecciones(List<Correccion> correcciones) {
        this.correcciones = correcciones;
    }

    public String toJson(CorreccionEnvio informe) {
        //  this.inf_visitasIdlocal=informe.getVisitasId();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();


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
