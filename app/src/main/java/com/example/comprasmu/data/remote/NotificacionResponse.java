package com.example.comprasmu.data.remote;

import com.example.comprasmu.ui.notificaciones.NotificacionGen;

import java.util.List;

public class NotificacionResponse {
    String status;
    List<NotificacionGen> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<NotificacionGen> getData() {
        return data;
    }

    public void setData(List<NotificacionGen> data) {
        this.data = data;
    }
}
