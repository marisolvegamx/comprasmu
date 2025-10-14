package com.example.comprasmu.data.modelos;

import java.util.List;

public class MenuVideo {

   private int vid_id;
   private String vid_nombreopcion;
   private int vid_nivelopcion;
   private int vid_superopcion;
   private String vid_liga;
   private int vid_orden;
   private List<MenuVideo> submenu;

    public int getVid_nivelopcion() {
        return vid_nivelopcion;
    }

    public void setVid_nivelopcion(int vid_nivelopcion) {
        this.vid_nivelopcion = vid_nivelopcion;
    }

    public int getVid_superopcion() {
        return vid_superopcion;
    }

    public void setVid_superopcion(int vid_superopcion) {
        this.vid_superopcion = vid_superopcion;
    }

    public String getVid_liga() {
        return vid_liga;
    }

    public void setVid_liga(String vid_liga) {
        this.vid_liga = vid_liga;
    }

    public int getVid_orden() {
        return vid_orden;
    }

    public void setVid_orden(int vid_orden) {
        this.vid_orden = vid_orden;
    }

    public int getVid_id() {
        return vid_id;
    }

    public void setVid_id(int vid_id) {
        this.vid_id = vid_id;
    }

    public String getVid_nombreopcion() {
        return vid_nombreopcion;
    }

    public void setVid_nombreopcion(String vid_nombreopcion) {
        this.vid_nombreopcion = vid_nombreopcion;
    }

    public List<MenuVideo> getSubmenu() {
        return submenu;
    }

    public void setSubmenu(List<MenuVideo> submenu) {
        this.submenu = submenu;
    }
}
