package com.example.lab2_20213852;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;


public class Juego extends AppCompatActivity {

    List<HashMap<String, Object>> letras;
    HashSet<String> letrasEscogidas;
    HashMap<String,Object> partida;
    List<HashMap<String,Object>>partidas;
    String nombreJugador;
    Handler handler = new Handler();
    Integer contador;
    LinearLayout layoutLetras;
    LinearLayout layoutGuiones;
    TextView mensajeFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_juego);
        Intent intent=getIntent();
        partidas=(List<HashMap<String,Object>>) intent.getSerializableExtra("partidas");
        if(partidas==null){
            partidas=new ArrayList<>();
        }

        nombreJugador=intent.getStringExtra("nombreJugador");



        mensajeFinal=findViewById(R.id.mensajeFinal);
        layoutLetras=findViewById(R.id.letrasAdivinar);
        layoutGuiones=findViewById(R.id.guionesBajo);


        partida=(HashMap<String, Object>)intent.getSerializableExtra("partida");
        if(partida==null){
            inicializarPartida();
        }else {
            restaurarPartida();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_juego,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Integer idItem=item.getItemId();
        if(idItem==R.id.estadisticas){
            handler.removeCallbacks(runnable);
            abrirEstadisticas();
        }
        return super.onOptionsItemSelected(item);
    }

    private void restaurarPartida(){
        layoutGuiones.removeAllViews();
        layoutLetras.removeAllViews();
        mensajeFinal.setTextColor(Color.TRANSPARENT);
        letras=(List<HashMap<String, Object>>)partida.get("letras");
        letrasEscogidas=(HashSet<String>)partida.get("letrasEscogidas");
        contador=(Integer)partida.get("tiempo");
        cambiarFoto((Integer) partida.get("intentos")+1);
        partida.put("intentos",6);
        for(int i=0;i<letras.size();i++){
            crearNuevaLetraInvisible((char)letras.get(i).get("caracter"),i);
        }
        for(int i=65;i<91;i++){
            establecerBoton((char)i);
        }
        for(String letraEscogida:letrasEscogidas){
            clicarBoton(findViewById(obtenerIdIntPorIdStr(letraEscogida)),letraEscogida.toCharArray()[0]);
        }

        Button botonNuevoJuego=findViewById(R.id.botonNuevoJuego);
        botonNuevoJuego.setOnClickListener(view -> {
            nuevoJuego(view);
        });

        handler.post(runnable);
    }

    private void inicializarPartida(){
        contador=0;
        letrasEscogidas=new HashSet<>();
        layoutGuiones.removeAllViews();
        layoutLetras.removeAllViews();
        mensajeFinal.setTextColor(Color.TRANSPARENT);
        partida=new HashMap<>();
        letras=new ArrayList<>();
        ImageView imagen=findViewById(R.id.monigote);
        imagen.setImageResource(R.drawable.antennatotal);
        String[]listaPalabras={"FIBRA","REDES","ANTENA","PROPA","CLOUD","TELECO"};
        String palabraElegida=listaPalabras[(int)Math.floor(Math.random()*listaPalabras.length)];
        partida.put("intentos",6);
        char[]caracteres=palabraElegida.toCharArray();
        for(int i=0;i<caracteres.length;i++){
            crearNuevaLetraInvisible(caracteres[i],i);
            HashMap<String,Object>letra=new HashMap<>();
            letra.put("posicion",i);
            letra.put("caracter",caracteres[i]);
            letra.put("descubierto",false);
            letras.add(letra);
        }
        partida.put("letras",letras);
        for(int i=65;i<91;i++){
            establecerBoton((char)i);
        }

        Button botonNuevoJuego=findViewById(R.id.botonNuevoJuego);
        botonNuevoJuego.setOnClickListener(view -> {
            nuevoJuego(view);
        });

        handler.post(runnable);
    }

    private void abrirEstadisticas(){
        Intent intent=new Intent(this, Estadisticas.class);
        Boolean enCurso=(Boolean) partida.get("enCurso");
        if(enCurso==null){
            partida.put("enCurso",true);
        }
        partida.put("letrasEscogidas",letrasEscogidas);
        partida.put("tiempo",contador);
        intent.putExtra("partida",partida);
        intent.putExtra("partidas",(Serializable) partidas);
        intent.putExtra("nombreJugador",nombreJugador);
        setResult(RESULT_OK,intent);
        startActivity(intent);
    }

    private void crearNuevaLetraInvisible(char caracter,Integer posicion){
        //Mediante ayuda online se consiguió saber cómo se podía insertar un nuevo elemento en la vista mediante código de Java

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

    private void establecerBoton(char caracter){
        Integer id=obtenerIdIntPorIdStr(String.valueOf(caracter));
        TextView boton=findViewById(id);
        boton.setAlpha(1f);
        boton.setOnClickListener(view -> clicarBoton(boton,caracter));
    }

    private Integer obtenerIdIntPorIdStr(String id){
        return getResources().getIdentifier(id,"id",getPackageName());//esta función se consiguió mediante ayuda de una LLM
    }

    private void clicarBoton(TextView boton,char caracter){
        intento(caracter);
        boton.setAlpha(0.5f);
        boton.setOnClickListener(view1 -> {});
        letrasEscogidas.add(String.valueOf(caracter));
    }

    private void intento(char caracter){
        Integer cantidadIntentos=(Integer)partida.get("intentos");
        Boolean acierto=false;
        for(HashMap<String,Object> letra:letras){
            if(letra.get("caracter").equals(caracter)){
                TextView letraEncontrada=findViewById(1000+(Integer)letra.get("posicion"));
                letraEncontrada.setTextColor(Color.BLACK);
                letra.put("descubierto",true);
                acierto=true;
            }
        }
        if(!acierto){
            cambiarFoto(cantidadIntentos);
            partida.put("intentos",cantidadIntentos-1);
            if(cantidadIntentos==1){
                finalizarPartida(false);
            }
        }else {
            Integer totalDescubiertos=0;
            for(HashMap<String,Object>letra:letras){
                if((Boolean)letra.get("descubierto")){
                    totalDescubiertos++;
                }
            }
            if(totalDescubiertos==letras.size()){
                finalizarPartida(true);
            }
        }
    }

    private void finalizarPartida(Boolean victoria){
        mensajeFinal.setTextColor(Color.BLACK);
        if(victoria){
            mensajeFinal.setText("Ganó / Terminó en "+contador+"s");
        }else{
            mensajeFinal.setText("Perdiste :(");
        }
        partida.put("tiempo",contador);
        partida.put("enCurso",false);
        handler.removeCallbacks(runnable);
        for(int i=65;i<91;i++){
            findViewById(obtenerIdIntPorIdStr(String.valueOf((char)i))).setOnClickListener(view -> {});
        }
    }

    private void cambiarFoto(Integer cantidadIntentos){
        ImageView imagen=findViewById(R.id.monigote);
        switch (cantidadIntentos){
            case 7:
                imagen.setImageResource(R.drawable.antennatotal);
                break;
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
            case 0:
                imagen.setImageResource(R.drawable.antennatotal);
                break;
        }
    }

    private void nuevoJuego(View view){
        partida.put("letrasEscogidas",letrasEscogidas);
        partidas.add(partida);
        inicializarPartida();
    }


    //La función para poder implementar el contador se realizó con ayuda de un LLM.
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            contador++;
            if(contador==30){
                Toast.makeText(getApplicationContext(), "¿Tanto tiempo? ¿Así quieres egresar?",Toast.LENGTH_LONG).show();
            }
            handler.postDelayed(this, 1000);
        }
    };
}