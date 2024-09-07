package com.example.lab2_20213852;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;


public class Juego extends AppCompatActivity {

    List<HashMap<String, Object>> letras=new ArrayList<>();
    HashMap<String,Object> partida=new HashMap<>();
    List<HashMap<String,Object>>partidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_juego);
        Intent intent=getIntent();
        String nombreJugador=intent.getStringExtra("nombreJugador");
        String palabraElegida=intent.getStringExtra("palabraElegida");
        partidas=(List<HashMap<String,Object>>) intent.getSerializableExtra("partidas");
        LinearLayout layoutLetras=findViewById(R.id.letrasAdivinar);
        LinearLayout layoutGuiones=findViewById(R.id.guionesBajo);



        partida.put("nombreJugador",nombreJugador);
        partida.put("intentos",6);
        char[]caracteres=palabraElegida.toCharArray();
        for(int i=0;i<caracteres.length;i++){
            crearNuevaLetraInvisible(layoutLetras,layoutGuiones,caracteres[i],i);
            HashMap<String,Object>letra=new HashMap<>();
            letra.put("posicion",i);
            letra.put("caracter",caracteres[i]);
            letra.put("descubierto",false);
            letras.add(letra);
        }
        establecerBoton('A',R.id.A);
        establecerBoton('B',R.id.B);
        establecerBoton('C',R.id.C);
        establecerBoton('D',R.id.D);
        establecerBoton('E',R.id.E);
        establecerBoton('F',R.id.F);
        establecerBoton('G',R.id.G);
        establecerBoton('H',R.id.H);
        establecerBoton('I',R.id.I);
        establecerBoton('J',R.id.J);
        establecerBoton('K',R.id.K);
        establecerBoton('L',R.id.L);
        establecerBoton('M',R.id.M);
        establecerBoton('N',R.id.N);
        establecerBoton('O',R.id.O);
        establecerBoton('P',R.id.P);
        establecerBoton('Q',R.id.Q);
        establecerBoton('R',R.id.R);
        establecerBoton('S',R.id.S);
        establecerBoton('T',R.id.T);
        establecerBoton('U',R.id.U);
        establecerBoton('V',R.id.V);
        establecerBoton('W',R.id.W);
        establecerBoton('X',R.id.X);
        establecerBoton('Y',R.id.Y);
        establecerBoton('Z',R.id.Z);


        Button botonNuevoJuego=findViewById(R.id.botonNuevoJuego);
        botonNuevoJuego.setOnClickListener(view -> {
            nuevoJuego(view);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_juego,menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void crearNuevaLetraInvisible(LinearLayout layoutLetras,LinearLayout layoutGuiones,char caracter,Integer posicion){
        TextView nuevaLetra=new TextView(this);
        nuevaLetra.setText(String.valueOf(caracter));
        nuevaLetra.setGravity(Gravity.CENTER);
        nuevaLetra.setTextColor(Color.TRANSPARENT);
        nuevaLetra.setPadding(14,0,14,0);
        nuevaLetra.setTextSize(50);
        nuevaLetra.setId(1000+posicion);
        LinearLayout.LayoutParams paramsLetra=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        nuevaLetra.setLayoutParams(paramsLetra);

        TextView nuevoGuion=new TextView(this);
        nuevoGuion.setText("_");
        nuevoGuion.setGravity(Gravity.CENTER);
        nuevoGuion.setPadding(10,0,10,0);
        nuevoGuion.setTextSize(80);
        nuevoGuion.setId(2000+posicion);
        LinearLayout.LayoutParams paramsGuion=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        nuevoGuion.setLayoutParams(paramsGuion);

        layoutLetras.addView(nuevaLetra);
        layoutGuiones.addView(nuevoGuion);
    }

    private void establecerBoton(char caracter,Integer id){
        TextView boton=findViewById(id);
        boton.setOnClickListener(view -> {
            intento(caracter);
        });
    }

    private void intento(char caracter){
        Integer cantidadIntentos=(Integer)partida.get("intentos");
        Boolean acierto=false;
        for(HashMap<String,Object> letra:letras){
            if(letra.get("caracter").equals(caracter)){
                TextView letraEncontrada=findViewById(1000+(Integer)letra.get("posicion"));
                letraEncontrada.setTextColor(Color.BLACK);
                acierto=true;
            }
        }
        if(!acierto){
            cambiarFoto(cantidadIntentos);
            partida.put("intentos",cantidadIntentos-1);
            if(cantidadIntentos==1){
                Toast.makeText(this,"Perdiste", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void cambiarFoto(Integer cantidadIntentos){
        ImageView imagen=findViewById(R.id.monigote);
        switch (cantidadIntentos){
            case 6:
                imagen.setImageResource(R.drawable.antennahead);
                break;
            case 5:
                imagen.setImageResource(R.drawable.antennatorso);
                break;
            case 4:
                imagen.setImageResource(R.drawable.antennabrazoizquierdo);
                break;
            case 3:
                imagen.setImageResource(R.drawable.antennabrazoderecho);
                break;
            case 2:
                imagen.setImageResource(R.drawable.antennapiernaizquierda);
                break;
            case 1:
                imagen.setImageResource(R.drawable.antennapiernaderecha);
                break;
        }
    }

    private void nuevoJuego(View view){
        Intent intent=new Intent(this, MainActivity.class);
        partidas.add(partida);
        intent.putExtra("partidas",(Serializable) partidas);
        setResult(RESULT_OK,intent);
        startActivity(intent);
    }
}