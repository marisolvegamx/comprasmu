package com.example.comprasmu.data.modelos;

import com.google.gson.Gson;

public class InformeToJson {

    public String toJson(InformeCompra informe) {


        Gson gson = new Gson();
        String JSON = gson.toJson(informe);
        return  JSON;

    }
}
