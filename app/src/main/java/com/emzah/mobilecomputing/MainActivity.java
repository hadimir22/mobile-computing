package com.emzah.mobilecomputing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.emzah.mobilecomputing.model.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText userNameEt , passwordEt , confirmPasswordEt;
    private MaterialButton signUpBtn;
    private SharedPreferences sharedPreferences;
    private String userName = "", passWord ="", confirmPassword ="";
    private TextView loginLabel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         sharedPreferences = getSharedPreferences("myprefs", Context.MODE_PRIVATE);

        initializeViews();
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSignUp();
            }
        });
        loginLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });

    }

    private void doSignUp() {
        userName = userNameEt.getText().toString();
        passWord = passwordEt.getText().toString();
        confirmPassword = confirmPasswordEt.getText().toString();
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
        if (confirmPassword.length()==0){
            confirmPasswordEt.setError("Confirm Password is required");
            return;
        }
        if (!passWord.equalsIgnoreCase(confirmPassword)){
            Toast.makeText(this, "Passwords doesn't match", Toast.LENGTH_SHORT).show();
            return;
        }
        if (userExists()){
            Toast.makeText(this, "User Already exists", Toast.LENGTH_SHORT).show();

        }
        else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            User user =new User(userName,passWord);
            Gson gson = new Gson();
            String userJson = gson.toJson(user);
            editor.putString(userName,userJson);
            editor.apply();
            userNameEt.setText("");
            passwordEt.setText("");
            confirmPasswordEt.setText("");
            Intent intent = new Intent(this,HomeActivity.class);
            startActivity(intent);
        }

    }

    private boolean userExists() {
        Gson gson = new Gson();
        String jsonUser = sharedPreferences.getString(userName,"");
        User user = gson.fromJson(jsonUser,User.class);
        if (user !=null){
            return true;
        }
         return false;
    }

    private void initializeViews() {
        userNameEt = findViewById(R.id.et_username);
        passwordEt = findViewById(R.id.et_password);
        confirmPasswordEt = findViewById(R.id.et_confirm_password);
        signUpBtn = findViewById(R.id.sign_up_btn);
        loginLabel = findViewById(R.id.login_label);
    }
}

