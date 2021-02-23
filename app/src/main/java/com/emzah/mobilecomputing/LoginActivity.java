package com.emzah.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.emzah.mobilecomputing.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText userNameEt , passwordEt ;
    private MaterialButton loginBtn;
    private SharedPreferences sharedPreferences;
    private String userName = "", passWord ="" ;
    private TextView signUpLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sharedPreferences = getSharedPreferences("myprefs", Context.MODE_PRIVATE);

        initializeViews();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doLogIn();
            }
        });

        signUpLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        });
    }

    private void doLogIn() {
        userName = userNameEt.getText().toString();
        passWord = passwordEt.getText().toString();
       if (userName.length()==0){
            userNameEt.setError("Username is required");
            return;
        }
        if(userName.length()<4){
            userNameEt.setError("User name cannot be less than 4 characters");
            return;
        }
        if (passWord.length()==0){
            passwordEt.setError("Password is required");
            return;
        }
        if(passWord.length()<4){
            userNameEt.setError("Password cannot be less than 4 characters");
            return;
        }
        if (isCredentialsValid()){
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);

        }else {
            Toast.makeText(this, "Wrong credentials", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isCredentialsValid() {
        Gson gson = new Gson();
        String jsonUser = sharedPreferences.getString(userName,"");
        User user = gson.fromJson(jsonUser,User.class);
        if (user !=null){
            if (user.getUserName().equalsIgnoreCase(userName)&& user.getPassword().equalsIgnoreCase(passWord)){
                return true;
            }
        }
        return false;
    }

    

    private void initializeViews() {
        userNameEt = findViewById(R.id.et_username);
        passwordEt = findViewById(R.id.et_password);
       loginBtn = findViewById(R.id.login_btn);
       signUpLabel = findViewById(R.id.sign_up_label);
    }
}