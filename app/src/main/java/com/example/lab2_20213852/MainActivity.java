package com.example.lab2_20213852;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button botonIniciarJuego=findViewById(R.id.botonIniciarJuego);
        botonIniciarJuego.setOnClickListener(view -> {
                iniciarJuego(view);
        });
        registerForContextMenu(findViewById(R.id.titulo));
    }

    private void iniciarJuego(View view){
        Intent intent=new Intent(this, Juego.class);
        TextView inputText=findViewById(R.id.inputText);
        String nombreJugador=inputText.getText().toString();
        intent.putExtra("nombreJugador",nombreJugador);
        setResult(RESULT_OK,intent);
        startActivity(intent);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Integer idItem=item.getItemId();
        TextView titulo=findViewById(R.id.titulo);
        if(idItem==R.id.seleccionarRojo){
            titulo.setTextColor(Color.RED);
        }else if(idItem==R.id.seleccionarMorado){
            titulo.setTextColor(Color.rgb(139,21,242));
        }else if(idItem==R.id.seleccionarVerde){
            titulo.setTextColor(Color.GREEN);
        }
        return super.onContextItemSelected(item);
    }
}