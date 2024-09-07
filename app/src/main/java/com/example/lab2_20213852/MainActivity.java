package com.example.lab2_20213852;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    List<HashMap<String,Object>>partidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button botonIniciarJuego=findViewById(R.id.botonIniciarJuego);
        botonIniciarJuego.setOnClickListener(view -> {
                iniciarJuego(view);
        });
        Intent intent=getIntent();
        partidas=(List<HashMap<String, Object>>)intent.getSerializableExtra("partidas");
        if(partidas==null){
            partidas=new ArrayList<>();
        }
        Log.d("cantidadPartidas",String.valueOf(partidas.size()));
    }

    private void iniciarJuego(View view){
        Intent intent=new Intent(this, Juego.class);
        TextView inputText=findViewById(R.id.inputText);
        String nombreJugador=inputText.getText().toString();
        String[]listaPalabras={"FIBRA","REDES","ANTENA","PROPA","CLOUD","TELECO"};
        intent.putExtra("nombreJugador",nombreJugador);
        intent.putExtra("palabraElegida",listaPalabras[(int)Math.floor(Math.random()*listaPalabras.length)]);
        intent.putExtra("partidas",(Serializable) partidas);
        setResult(RESULT_OK,intent);
        startActivity(intent);
    }
}