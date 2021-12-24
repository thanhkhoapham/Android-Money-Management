package com.example.admin.demomanagentthuchi;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText et_Email, et_Password;
    Button bt_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_Email = findViewById(R.id.et_Email);
        et_Password = findViewById(R.id.et_Password);
        bt_Login = findViewById(R.id.bt_login);

        bt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = et_Email.getText().toString();
                String Pass = et_Password.getText().toString();

                boolean kt=false;
                if (Email.length() == 0) {
                    kt=true;
                    et_Email.setError("Email không được để trống");
                }
                if (Pass.length() < 6) {
                    kt=true;
                    et_Password.setError("PassWord phải lớn hơn 6 kí tự");
                }
                if (kt==false){
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(i);
                }


            }
        });
    }
}
