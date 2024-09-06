package com.example.lab2_20213852;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button botonIniciarJuego=findViewById(R.id.botonIniciarJuego);
        botonIniciarJuego.setOnClickListener(view -> {
                iniciarJuego(view);
        });
    }

    private void iniciarJuego(View view){
        Intent intent=new Intent(this, Juego.class);
        TextView inputText=findViewById(R.id.inputText);
        String nombreJugador=inputText.getText().toString();
        String[]listaPalabras={"FIBRA","REDES","ANTENA","PROPA","CLOUD","TELECO"};
        intent.putExtra("nombreJugador",nombreJugador);
        intent.putExtra("palabraElegida",listaPalabras[(int)Math.floor(Math.random()*listaPalabras.length)]);
        setResult(RESULT_OK,intent);
        startActivity(intent);
    }
}