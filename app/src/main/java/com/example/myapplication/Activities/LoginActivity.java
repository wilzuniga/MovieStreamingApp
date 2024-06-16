
package com.example.myapplication.Activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Button;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

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


public class LoginActivity extends AppCompatActivity {
    //Autenticador Firebase
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private FirebaseAuth mAuth;
    private OkHttpClient client = new OkHttpClient();
    private EditText userEdt, passEdt;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
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

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userEdt.getText().toString();
                String pass = passEdt.getText().toString();

                //si esta vacia
                if (user.isEmpty() || pass.isEmpty()) {

                } else{
                    //Autenticacion usuario
                    mAuth.createUserWithEmailAndPassword(user, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        //Create new user
                                        Toast.makeText(LoginActivity.this, "User:Created", Toast.LENGTH_SHORT).show();
                                        //at same time send a http request to load the user data
                                        try {
                                            POSTRequest(user);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                    } else {

                                        System.out.println("createUserWithEmail:failure");
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }


                /*else if(user.equals("coso") && pass.equals("coso")){
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else{
                    Toast.makeText(LoginActivity.this, "Usuario o contraseña incorrecta", Toast.LENGTH_SHORT).show();
                } /*/
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
    public void POSTRequest(String user) throws JSONException {
        //put the user in a JSON
        JSONObject userJSON = new JSONObject();
        userJSON.put("user",user);

        String userJsonString = user.toString();
        //post user data to the backend
        RequestBody body = RequestBody.create(userJsonString, JSON);
        Request request = new Request.Builder()
                .url("http://localhost:3001/signup")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            System.out.println(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } );

    }

}