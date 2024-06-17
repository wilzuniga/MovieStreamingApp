
package com.example.myapplication.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import android.view.View;



import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class LoginActivity extends AppCompatActivity {
    //Autenticador Firebase
    public  static final MediaType JSON = MediaType.get("application/json; charset=utf-8");



    private OkHttpClient client = new OkHttpClient();
    private EditText userEdt, passEdt;
    private Button loginBtn;

    private TextView textView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        client = new OkHttpClient();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();
    }

    private void initView() {
        userEdt = findViewById(R.id.editTextText2);
        passEdt = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.loginBtn);
        textView8 = findViewById(R.id.textView8);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userEdt.getText().toString();
                String pass = passEdt.getText().toString();

                //si esta vacia
                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Ingresar usuario y contraseña", Toast.LENGTH_SHORT).show();
                } else {
                    //si no esta vacia
                    try {
                        POSTsignIn(user, pass);
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }

                }



            }

        });

        textView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    //oculta la barra de navegacion y la barra de estado
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    //post Request to the backend
    public void POSTsignIn(String email, String password) throws JSONException {
        //put the user in sa JSON
        JSONObject userJSON = new JSONObject();
        userJSON.put("email",email);
        userJSON.put("password",password);

        String userJsonString = userJSON.toString();
        Log.d("JSON", "userJsonString: " + userJsonString);

        //post user data to the backend
        RequestBody body = RequestBody.create(userJsonString, JSON);
        Request request = new Request.Builder()
                .url("https://api.cosomovies.xyz/api/login/signin")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {

                Log.d("JSONFAILURE","CALLBACK FAILURE");

            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("JSON", "response: " + response.body().string());
                            Toast.makeText(LoginActivity.this, "AUTH SUCCESS", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        } catch (Exception e) {
                            Toast.makeText(LoginActivity.this, "AUTH FAILED", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } );

    }

}