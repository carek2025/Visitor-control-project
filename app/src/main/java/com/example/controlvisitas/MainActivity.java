package com.example.controlvisitas;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editNombre, editEmpresa, editPropositoVisita;
    private Button btnEntrada, btnSalida;
    private ListView listVisitas;
    private ArrayList<String> visitas;
    private ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editNombre = findViewById(R.id.EditNombre);
        editEmpresa = findViewById(R.id.EditEmpresa);
        editPropositoVisita = findViewById(R.id.EditPropositoVisita);
        btnEntrada = findViewById(R.id.btnEntrada);
        btnSalida = findViewById(R.id.btnSalida);
        listVisitas = findViewById(R.id.list_visitas);

        visitas = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, visitas);
        listVisitas.setAdapter(adapter);

        if(savedInstanceState != null){
            visitas=savedInstanceState.getStringArrayList("visitas");
            if(visitas != null){
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,visitas);
                listVisitas.setAdapter(adapter);
            }
        }

        btnEntrada.setOnClickListener(v->registrarVisita());

        btnSalida.setOnClickListener(v->{
            if(visitas.isEmpty()){
                Toast.makeText(this,"No hay visitas para amrcar salida",Toast.LENGTH_SHORT).show();
            }else{
                EditText inputNombre = new EditText(this);
                inputNombre.setHint("Ingrese el nombre del visitante");
                new AlertDialog.Builder(this).setTitle("Registrar Salida").setMessage("Ingregse el nombre del visitante para marcar su salida").setView(inputNombre).setPositiveButton("Confirmar", (dialog, which)-> {
                    String nombreBuscado = inputNombre.getText().toString().trim();
                    if(nombreBuscado.isEmpty()){
                        Toast.makeText(this, "Por favor, ingrese un nombre", Toast.LENGTH_SHORT).show();
                        return ;
                    }
                    boolean encontrada = false;
                    for (int i =0;i<visitas.size();i++){
                        String visita = visitas.get(i);
                        if (visita.toLowerCase().startsWith(nombreBuscado.toLowerCase()+" -")){
                            visitas.remove(i);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(this, "Salida registrada para " + nombreBuscado, Toast.LENGTH_SHORT).show();
                            encontrada = true;
                            break;
                        }
                    }
                    if(!encontrada){
                        Toast.makeText(this, "No se encontró una visita con el nombre " + nombreBuscado, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancelar",null).show();
            }
        });

        listVisitas.setOnItemClickListener((parent, view, position, id)->{
            String visita = visitas.get(position);
            String nombre = visita.split(" - ")[0];
            new AlertDialog.Builder(this).setTitle("Confirmar Salida").setMessage("¿Desea marcar la salida de "+nombre+"?").setPositiveButton("Si",(dialog, which) -> {
                visitas.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(this,"Salida registrada para "+nombre,Toast.LENGTH_SHORT).show();
            }).setNegativeButton("No",null).show();
        });
    }
    private void registrarVisita(){
        String nombre=editNombre.getText().toString().trim();
        String empresa=editEmpresa.getText().toString().trim();
        String proposito=editPropositoVisita.getText().toString().trim();

        if(nombre.isEmpty() || empresa.isEmpty() ||proposito.isEmpty()){
            new AlertDialog.Builder(this).setTitle("Error al registrar entrada").setMessage("Complete todos los campos para registrarse").setPositiveButton("Regresar",(dialog,which)->{
                Toast.makeText(this,"Regresando...",Toast.LENGTH_SHORT).show();
            }).setNegativeButton("salir",null).show();
        }else{
            Toast.makeText(this,"Entrada registrada correctamente",Toast.LENGTH_SHORT).show();
            String visita=nombre+" - "+empresa+" - "+proposito;
            visitas.add(visita);
            adapter.notifyDataSetChanged();
            limpiarCampos();
        }
    }
    private void limpiarCampos(){
        editNombre.setText("");
        editEmpresa.setText("");
        editPropositoVisita.setText("");
    }
    @Override
    protected void onStart(){
        super.onStart();
        Toast.makeText(this,"Actividad iniciada",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onResume(){
        super.onResume();
        adapter.notifyDataSetChanged();
        Toast.makeText(this,"Actividad reanudada",Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onPause(){
        super.onPause();
        Toast.makeText(this, "Actividad pausada", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onStop(){
        super.onStop();
        Toast.makeText(this, "Actividad detenida", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("visitas",visitas);
    }
}