package com.play.neotech_one;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ConfirmarordenActivity extends AppCompatActivity {
    private EditText nombre, correo, direccion, telefono;
    private Button confirmar;
    private String totalPago = "";
    private FirebaseAuth auth;
    private String CurrentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmarorden);
        totalPago=getIntent().getStringExtra("Total");
        Toast.makeText(this, "Total a Pagar: S/."+totalPago, Toast.LENGTH_SHORT).show();

        auth=FirebaseAuth.getInstance();
        CurrentUserId=auth.getCurrentUser().getUid();
        nombre=(EditText) findViewById(R.id.final_nombre);
        correo=(EditText) findViewById(R.id.final_correo);
        direccion=(EditText) findViewById(R.id.final_direccion);
        telefono=(EditText)findViewById(R.id.final_telefono);
        confirmar=(Button) findViewById(R.id.final_boton_confirmar);
        confirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VerificarDatos();
            }
        });
    }

    private void VerificarDatos() {
        if (TextUtils.isEmpty(nombre.getText().toString())){
            Toast.makeText(this, "Por favor ingrese su Nombre", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(direccion.getText().toString())){
            Toast.makeText(this, "Por favor ingrese su Direccion", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(telefono.getText().toString())){
            Toast.makeText(this, "Por favor ingrese su Telefono", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(correo.getText().toString())) {
            Toast.makeText(this, "Por favor ingrese su Correo", Toast.LENGTH_SHORT).show();
        }else{
            ConfirmarOrden();
        }
    }

    private void ConfirmarOrden() {
        final String CurrenDate, CurrenTime;
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat dateFormat= new SimpleDateFormat("MM-dd-yyyy");
        CurrenDate= dateFormat.format(calendar.getTime());

        SimpleDateFormat dateFormat1= new SimpleDateFormat("HH:mm:ss");
        CurrenTime= dateFormat1.format(calendar.getTime());

        final DatabaseReference OrdenesRef= FirebaseDatabase.getInstance().getReference().child("Ordenes").child(CurrentUserId);

        HashMap<String, Object> map= new HashMap<>();
        map.put("total", totalPago);
        map.put("nombre", nombre.getText().toString());
        map.put("direccion", direccion.getText().toString());
        map.put("telefono", telefono.getText().toString());
        map.put("correo", correo.getText().toString());
        map.put("fecha", CurrenDate);
        map.put("hora", CurrenTime);
        map.put("estado", "No Enviado");
        OrdenesRef.updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    FirebaseDatabase.getInstance().getReference().child("Carrito")
                            .child("Usuario Compra").child(CurrentUserId).removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Toast.makeText(ConfirmarordenActivity.this, "Tu orden fue tomada con exito", Toast.LENGTH_SHORT).show();
                                        Intent intent= new Intent(ConfirmarordenActivity.this, PrincipalActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }
}