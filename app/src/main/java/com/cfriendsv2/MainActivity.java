package com.cfriendsv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void IniciarSesion(View view){
        Intent i = new Intent(this, InicioSesion.class);
        startActivity(i);
        finish();
    }

    public void Registrarse(View view){
        Intent i = new Intent(this, Registro.class);
        startActivity(i);
        finish();
    }
}

