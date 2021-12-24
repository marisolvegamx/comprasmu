package com.example.comprasmu.data.repositories;


import android.util.Log;

import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.LoggedInUser;
import com.example.comprasmu.data.modelos.LoginDataSource;
import com.example.comprasmu.data.modelos.Result;
import com.example.comprasmu.ui.login.LoginActivity;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {

    private static volatile LoginRepository instance;

    private PeticionesServidor petServ;

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    // private constructor : singleton access
    public LoginRepository() {
        this.petServ=new PeticionesServidor("");
    }

    public static LoginRepository getInstance() {
        if (instance == null) {
            instance = new LoginRepository();
        }
        return instance;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;

    }

   /* private void setLoggedInUser(LoggedInUser user) {
        this.user = user;
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }*/

    public void login(String username, String password, LoginActivity.LoginListener listener) {
        // handle login
        Log.i("LoginRepository","haciendo peticion");
         petServ.autenticar(username, password,listener);

    }
}