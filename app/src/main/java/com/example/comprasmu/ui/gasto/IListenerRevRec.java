package com.example.comprasmu.ui.gasto;

import android.view.View;

import com.example.comprasmu.data.remote.PostResponse;

public interface IListenerRevRec {
     void guardarEstatus(PostResponse response);

     void guardarRes(PostResponse respuesta);
}
