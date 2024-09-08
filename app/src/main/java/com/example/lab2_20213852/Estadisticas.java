package com.example.lab2_20213852;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Estadisticas extends AppCompatActivity {

    List<HashMap<String,Object>> partidas;
    String nombreJugador;
    HashMap<String,Object> partida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_estadisticas);

        Intent intent=getIntent();
        partidas=(List<HashMap<String,Object>>) intent.getSerializableExtra("partidas");
        partida=(HashMap<String, Object>) intent.getSerializableExtra("partida");
        nombreJugador=intent.getStringExtra("nombreJugador");
        LinearLayout contenedorScroll=findViewById(R.id.contenedorScroll);
        generarNuevoResultadoHistorial(partida,partidas.size(),contenedorScroll);
        for(int i=partidas.size()-1;i>=0;i--){
            generarNuevoResultadoHistorial(partidas.get(i),i,contenedorScroll);
        }
        TextView textoJugador=findViewById(R.id.nombreJugador);
        textoJugador.setText("Jugador: "+nombreJugador);
        Log.d("nombreJugador",nombreJugador);
        Button botonIniciarJuegoEstadisticas=findViewById(R.id.botonNuevoJuegoEstadisticas);
        botonIniciarJuegoEstadisticas.setOnClickListener(view -> {
            iniciarJuego(view);
        });
    }


    private void generarNuevoResultadoHistorial(HashMap<String,Object> partida,Integer posicion,LinearLayout contenedor){
        TextView resultado=new TextView(this);
        Boolean enCurso=(Boolean)partida.get("enCurso");
        if(enCurso!=null&&enCurso){
            partida.remove("enCurso");
            resultado.setText("Juego "+(posicion+1)+": En curso");
        }else {
            Integer tiempo=(Integer) partida.get("tiempo");
            resultado.setText("Juego "+(posicion+1)+": "+(tiempo==null?"Canceló":"Terminó en "+tiempo+"s"));
        }
        resultado.setTextSize(20);
        resultado.setTextColor(Color.WHITE);
        resultado.setPadding(0,20,0,20);
        contenedor.addView(resultado);
    }

    private void iniciarJuego(View view){
        Intent intent=new Intent(this, Juego.class);
        partidas.add(partida);
        intent.putExtra("nombreJugador",nombreJugador);
        intent.putExtra("partidas",(Serializable) partidas);
        setResult(RESULT_OK,intent);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home) {
            Intent intent = new Intent(this, Juego.class);
            intent.putExtra("nombreJugador",nombreJugador);
            intent.putExtra("partidas",(Serializable) partidas);
            intent.putExtra("partida",partida);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}