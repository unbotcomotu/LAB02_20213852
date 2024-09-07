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
import java.util.List;
import java.util.Objects;


public class Juego extends AppCompatActivity {

    List<HashMap<String, Object>> letras;
    HashMap<String,Object> partida;
    List<HashMap<String,Object>>partidas;
    String nombreJugador;
    Handler handler = new Handler();
    Integer contador = 0;
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

        inicializarPartida();

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
            abrirEstadisticas();
        }
        return super.onOptionsItemSelected(item);
    }

    private void inicializarPartida(){
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

        handler.post(runnable);
    }

    private void abrirEstadisticas(){
        Intent intent=new Intent(this, Estadisticas.class);
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

    private void establecerBoton(char caracter,Integer id){
        TextView boton=findViewById(id);
        boton.setAlpha(1f);
        boton.setOnClickListener(view -> {
            intento(caracter);
            boton.setAlpha(0.5f);
            boton.setOnClickListener(view1 -> {});
        });
    }


    private void intento(char caracter){
        Integer cantidadIntentos=(Integer)partida.get("intentos");
        Boolean acierto=false;
        for(HashMap<String,Object> letra:letras){
            if(letra.get("caracter").equals(caracter)){
                Log.d("letraPosicion",String.valueOf((Integer)letra.get("posicion")));
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
            Log.d("aiudaaa",String.valueOf(totalDescubiertos));
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
        handler.removeCallbacks(runnable);
        findViewById(R.id.A).setOnClickListener(view -> {});
        findViewById(R.id.B).setOnClickListener(view -> {});
        findViewById(R.id.C).setOnClickListener(view -> {});
        findViewById(R.id.D).setOnClickListener(view -> {});
        findViewById(R.id.E).setOnClickListener(view -> {});
        findViewById(R.id.F).setOnClickListener(view -> {});
        findViewById(R.id.G).setOnClickListener(view -> {});
        findViewById(R.id.H).setOnClickListener(view -> {});
        findViewById(R.id.I).setOnClickListener(view -> {});
        findViewById(R.id.J).setOnClickListener(view -> {});
        findViewById(R.id.K).setOnClickListener(view -> {});
        findViewById(R.id.L).setOnClickListener(view -> {});
        findViewById(R.id.M).setOnClickListener(view -> {});
        findViewById(R.id.N).setOnClickListener(view -> {});
        findViewById(R.id.O).setOnClickListener(view -> {});
        findViewById(R.id.P).setOnClickListener(view -> {});
        findViewById(R.id.Q).setOnClickListener(view -> {});
        findViewById(R.id.R).setOnClickListener(view -> {});
        findViewById(R.id.S).setOnClickListener(view -> {});
        findViewById(R.id.T).setOnClickListener(view -> {});
        findViewById(R.id.U).setOnClickListener(view -> {});
        findViewById(R.id.V).setOnClickListener(view -> {});
        findViewById(R.id.W).setOnClickListener(view -> {});
        findViewById(R.id.X).setOnClickListener(view -> {});
        findViewById(R.id.Y).setOnClickListener(view -> {});
        findViewById(R.id.Z).setOnClickListener(view -> {});
        contador=0;
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
            case 0:
                imagen.setImageResource(R.drawable.antennatotal);
                break;
        }
    }

    private void nuevoJuego(View view){
        partidas.add(partida);

        inicializarPartida();
    }


    //La función para poder implementar el contador se realizó con ayuda de un LLM.
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            contador++;
            handler.postDelayed(this, 1000);
        }
    };
}