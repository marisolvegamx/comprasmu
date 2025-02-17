package com.example.comprasmu.ui.gasto;

import android.view.View;

import com.example.comprasmu.data.remote.NotificacionResponse;
import com.example.comprasmu.data.remote.PostResponse;

import java.util.List;

public interface IListenerRevRec {
     void guardarEstatus(PostResponse response);

     void guardarRes(PostResponse respuesta);

    void guardarResNotif(NotificacionResponse response);
}
