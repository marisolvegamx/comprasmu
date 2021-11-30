package com.example.comprasmu.data.modelos;


import android.content.Context;
import android.content.SharedPreferences;

import com.example.comprasmu.utils.Constantes;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    /****verifico si ya tengo sus credenciales localmente
     *
     */
      public void logout() {
        // TODO: revoke authentication
    }
}