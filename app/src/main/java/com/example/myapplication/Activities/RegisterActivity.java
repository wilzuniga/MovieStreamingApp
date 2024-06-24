package com.example.myapplication.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    //Autenticador Firebase
    public  static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    private OkHttpClient client = new OkHttpClient();

    private EditText userEdt, passEdt;
    private Button RegBtn;

    private TextView textView8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        client = new OkHttpClient();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initView();

    }

    private void initView() {
        userEdt = findViewById(R.id.editTextRegisterUser);
        passEdt = findViewById(R.id.editTextRegisterPass);
        RegBtn = findViewById(R.id.RegBtn);

        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userEdt.getText().toString();
                String pass = passEdt.getText().toString();

                //si esta vacia
                if (user.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Ingresar usuario y contraseña", Toast.LENGTH_SHORT).show();
                } else{
                    //Autenticacion usuario
                    try {
                        POSTSignUp(user, pass);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }


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
    public void POSTSignUp(String user, String email) throws JSONException {
        //put the user in a JSON

        JSONObject userJSON = new JSONObject();
        userJSON.put("email",user);
        userJSON.put("password",email);

        String userJsonString = userJSON.toString();
        Log.d("JSON", "userJsonString: " + userJsonString);
        //post user data to the backend
        RequestBody body = RequestBody.create(userJsonString, JSON);
        Request request = new Request.Builder()
                .url("https://api.cosomovies.xyz/api/login/signup")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("JSONFAILURE", e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.d("JSON", "CALLBACK SUCCESS");

                            // Log the response code
                            int responseCode = response.code();
                            Log.d("JSON", "Response Code: " + responseCode);

                            // Log the response body
                            String responseBody = response.body().string();
                            Log.d("JSON", "Response Body: " + responseBody);

                            if (responseCode == 200) {
                                Toast.makeText(RegisterActivity.this, "ACCOUNT CREATED", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                String token = email;
                                intent.putExtra("user", token);
                                startActivity(intent);
                            } else {
                                Toast.makeText(RegisterActivity.this, "FAILED CREATING ACCOUNT: " + responseBody, Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.d("JSON", "Exception: " + e.getMessage());
                            Toast.makeText(RegisterActivity.this, "FAILED CREATING ACCOUNT", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

}