package com.example.comprasmu.data.remote;


import com.example.comprasmu.data.modelos.InformeEtapa;
import com.example.comprasmu.data.modelos.InformeEtapaDet;
import java.util.List;

public class RespNotifEtiqResponse {

    private List<InformeEtapa> etiq_elim;
    private List<InformeEtapaDet> etiq_cancel;
    private List<InformeEtapa> emp_elim;

    public List<InformeEtapa> getEtiq_elim() {
        return etiq_elim;
    }

    public void setEtiq_elim(List<InformeEtapa> etiq_elim) {
        this.etiq_elim = etiq_elim;
    }

    public List<InformeEtapaDet> getEtiq_cancel() {
        return etiq_cancel;
    }

    public void setEtiq_cancel(List<InformeEtapaDet> etiq_cancel) {
        this.etiq_cancel = etiq_cancel;
    }

    public List<InformeEtapa> getEmp_elim() {
        return emp_elim;
    }

    public void setEmp_elim(List<InformeEtapa> emp_elim) {
        this.emp_elim = emp_elim;
    }
}