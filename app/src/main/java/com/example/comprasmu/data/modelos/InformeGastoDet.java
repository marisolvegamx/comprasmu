package com.example.comprasmu.data.modelos;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

    @Entity(tableName = "informe_gasto_det")
    public class InformeGastoDet {

        @PrimaryKey(autoGenerate = true)
        private int id;
        private int informeEtapaId;
        private int conceptoId;
        private String concepto;
        private float importe;
        private String descripcion;
        private boolean comprobante;
        private int fotocomprob;
        private int estatus; //1-abierto 2-finalizado 0-cancelado
        private int estatusSync;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getInformeEtapaId() {
            return informeEtapaId;
        }

        public void setInformeEtapaId(int informeEtapaId) {
            this.informeEtapaId = informeEtapaId;
        }

        public int getConceptoId() {
            return conceptoId;
        }

        public void setConceptoId(int conceptoId) {
            this.conceptoId = conceptoId;
        }

        public String getConcepto() {
            return concepto;
        }

        public void setConcepto(String concepto) {
            this.concepto = concepto;
        }

        public float getImporte() {
            return importe;
        }

        public void setImporte(float importe) {
            this.importe = importe;
        }

        public String getDescripcion() {
            return descripcion;
        }

        public void setDescripcion(String descripcion) {
            this.descripcion = descripcion;
        }

        public boolean isComprobante() {
            return comprobante;
        }

        public void setComprobante(boolean comprobante) {
            this.comprobante = comprobante;
        }

        public int getFotocomprob() {
            return fotocomprob;
        }

        public void setFotocomprob(int fotocomprob) {
            this.fotocomprob = fotocomprob;
        }

        public int getEstatus() {
            return estatus;
        }

        public void setEstatus(int estatus) {
            this.estatus = estatus;
        }

        public int getEstatusSync() {
            return estatusSync;
        }

        public void setEstatusSync(int estatusSync) {
            this.estatusSync = estatusSync;
        }
    }
