package com.example.comprasmu.data.remote;

import com.example.comprasmu.data.modelos.Contrato;
import com.example.comprasmu.data.modelos.SolicitudCor;
import com.example.comprasmu.data.modelos.TablaVersiones;
import com.example.comprasmu.utils.Constantes;

import java.util.Date;

public interface IActualListener {

    int actualizarCorre(SolCorreResponse corrResp, int etapa);

    void actualizarInformes(RespInformesResponse infoResp);
}
