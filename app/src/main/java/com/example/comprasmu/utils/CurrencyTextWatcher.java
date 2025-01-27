package com.example.comprasmu.utils;

import android.text.Editable;
import android.text.TextWatcher;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyTextWatcher implements TextWatcher {

        boolean mEditing;

        public CurrencyTextWatcher() {
            mEditing = false;
        }

        public synchronized void afterTextChanged(Editable s) {
            if(!mEditing) {
                mEditing = true;

                String digits = s.toString().replaceAll("\\D", "");
                NumberFormat nf = NumberFormat.getCurrencyInstance();
                DecimalFormatSymbols dfs = new DecimalFormatSymbols();
                dfs.setCurrencySymbol("$");
                ((DecimalFormat) nf).setDecimalFormatSymbols(dfs);
                // new DecimalFormat("#.00")
               // ((DecimalFormat) nf).setDecimalFormatSymbols(dfs);
               // DecimalFormat myFormatter = new DecimalFormat("$.");
                try{
                    String formatted = nf.format(Double.parseDouble(digits)/100);
                    s.replace(0, s.length(), formatted);
                    //myFormatter.format(s);
                } catch (NumberFormatException nfe) {
                    s.clear();
                }

                mEditing = false;
            }
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        public void onTextChanged(CharSequence s, int start, int before, int count) { }


}
