package com.example.comprasmu.ui.ayuda;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.comprasmu.R;
import com.example.comprasmu.utils.Constantes;

/**

 */
public class AyudaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AyudaFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    // TODO: Rename and change types and number of parameters
    public static AyudaFragment newInstance() {
        AyudaFragment fragment = new AyudaFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String urlayuda= Constantes.URLSERV+"cue_indiceayuda.php";
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_ayuda, container, false);
        WebView webView = (WebView)root.findViewById(R.id.ayudawebview);
        webView.clearCache(true);
        WebSettings mWebSettings = webView.getSettings();
      //  mWebSettings.setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient());
        /*{
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                Intent intent;

                Object AppConstants;
                if (url.contains("")) {
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);

                    return true;
                }
            }
        });*/
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.loadUrl(urlayuda);
        return root;
    }
}