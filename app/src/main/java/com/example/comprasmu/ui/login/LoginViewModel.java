package com.example.comprasmu.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;

import com.example.comprasmu.R;
import com.example.comprasmu.data.PeticionesServidor;
import com.example.comprasmu.data.modelos.LoggedInUser;
import com.example.comprasmu.data.modelos.LoginDataSource;
import com.example.comprasmu.data.modelos.Result;
import com.example.comprasmu.data.repositories.LoginRepository;


public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
   // private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;
    private PeticionesServidor petServ;

    public LoginViewModel() {
       this.petServ=new PeticionesServidor("");
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

   /* LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }*/

    public void login(String username, String password,LoginActivity.LoginListener listener) {
        // can be launched in a separate asynchronous job
        loginRepository=LoginRepository.getInstance();
        Log.i("LoginVM","??");
        loginRepository.login(username, password,listener);

       /* if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
        } else {
            loginResult.setValue(new LoginResult(R.string.login_failed));
        }*/
    }
    public void loginLocal( LoggedInUser luser,String username, String password,LoginActivity.LoginListener listener) {
        // can be launched in a separate asynchronous job
       if(luser.getUserId().equals(username)&&luser.getPassword().equals(password))
            listener.correcto();
       else
           listener.incorrecto("Usuario o contraseña incorrectos");
    }


    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}