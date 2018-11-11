package cc.vipazoo.www.ui;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cc.vipazoo.www.ui.controller.LoginController;
import cc.vipazoo.www.ui.model.User;

public class LoginActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "cc.vipazoo.www.ui.LoginActivity.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void LogIn(View view) {
        // get the username and password
        EditText editText1 = findViewById(R.id.editTextUsername);
        String username = editText1.getText().toString();
        EditText editText2 = findViewById(R.id.editTextPassword);
        String password = editText2.getText().toString();

        // HERE add thing to get the real username and password
        LoginController loginController = new LoginController(username, password);

        try {
            // status is the msg got from server
            String status = loginController.login();
            if(status.equals("登录成功")) {
                // jump to main activity
                Intent intent = new Intent(this, MainActivity.class);
                // save the user data
                intent.putExtra(EXTRA_MESSAGE, loginController.getUser());
                startActivity(intent);
            }
            else {
                // login failed
                Toast.makeText(this, status, Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e) {
            Log.e("TAG", e.toString());
            Toast.makeText(this, "Something wrong happens", Toast.LENGTH_SHORT).show();
        }
    }

    public void JumpToRegister(View view) {
        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
    }

}