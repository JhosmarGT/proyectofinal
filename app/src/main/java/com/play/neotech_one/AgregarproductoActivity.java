package com.play.neotech_one;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AgregarproductoActivity extends AppCompatActivity {
    private String CurrentUserId;
    private FirebaseAuth auth;
    private ImageView imagenpro;
    private EditText nombrepro, descripcionpro, preciocomprapro, precioventapro, cantidadpro  ;
    private Button agregarproducto;
    private static final int Gallery_Pick = 1;
    private Uri imagenUri;
    private String productoRandonKey, downloadUri;
    private StorageReference ProductoImagenRef;
    private DatabaseReference ProductoRef;
    private ProgressDialog dialog;
    private String Categoria, Nom, Desc, PrecioComp, PrecioVent, Cant , CurrentDate, CurrentTime;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregarproducto);

        auth= FirebaseAuth.getInstance();
        CurrentUserId= auth.getCurrentUser().getUid();



        Categoria = getIntent().getExtras().get("categoria").toString();
        ProductoImagenRef = FirebaseStorage.getInstance().getReference().child("Imagenes Productos");
        ProductoRef= FirebaseDatabase.getInstance().getReference().child("Product");
        imagenpro = (ImageView) findViewById(R.id.imagen_pro);
        nombrepro = (EditText) findViewById(R.id.nombre_pro);
        descripcionpro = (EditText) findViewById(R.id.descripcion_pro);
        preciocomprapro = (EditText) findViewById(R.id.preciocompra_pro);
        precioventapro = (EditText) findViewById(R.id.precioventa_pro);
        cantidadpro = (EditText) findViewById(R.id.cantidad_pro);
        agregarproducto = (Button) findViewById(R.id.agrega_producto);


        dialog = new ProgressDialog(this);

        imagenpro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AbrirGaleria();
            }
        });

        agregarproducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarProducto();
            }
        });
    }

    private void AbrirGaleria() {
        Intent intent= new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/");
        startActivityForResult(intent,Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Gallery_Pick && resultCode==RESULT_OK && data != null){
            imagenUri= data.getData();
            imagenpro.setImageURI(imagenUri);
        }

    }

    private void ValidarProducto() {
        Nom= nombrepro.getText().toString();
        Desc= descripcionpro.getText().toString();
        PrecioComp= preciocomprapro.getText().toString();
        PrecioVent= precioventapro.getText().toString();
        Cant= cantidadpro.getText().toString();
        if (imagenUri== null){
            Toast.makeText(this, "Primero agrega una imagen", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Nom)){
            Toast.makeText(this, "Debes de ingresar el nombre del producto", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Desc)){
            Toast.makeText(this, "Debes de ingresar la descripcion del producto", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(PrecioComp)){
            Toast.makeText(this, "Debes de ingresar el precio de compra", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(PrecioVent)){
            Toast.makeText(this, "Debes de ingresar el precio de venta", Toast.LENGTH_SHORT).show();
        }else if (TextUtils.isEmpty(Cant)){
            Toast.makeText(this, "Debes de ingresar la cantidad de productos", Toast.LENGTH_SHORT).show();
        }else{
            GuardarInformacionProducto();
        }
    }
    private void GuardarInformacionProducto() {
        dialog.setTitle("Guardando producto");
        dialog.setMessage("Por favor espere mientras se guarda su producto");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentDataFormat= new SimpleDateFormat("MM-dd-yyyy");
        CurrentDate= currentDataFormat.format(calendar.getTime());
        SimpleDateFormat CurrentTimeFormat= new SimpleDateFormat("HH:mm:ss");
        CurrentTime= CurrentTimeFormat.format(calendar.getTime());
        productoRandonKey= CurrentDate+ CurrentTime;

        final StorageReference filePath= ProductoImagenRef.child(imagenUri.getLastPathSegment()+ productoRandonKey+ ".jpg");
        final UploadTask uploadTask= filePath.putFile(imagenUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String mensaje= e.toString();
                Toast.makeText(AgregarproductoActivity.this, "Error:"+ mensaje, Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AgregarproductoActivity.this, "Imagen guardada con exito", Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask= uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        downloadUri= filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()){
                            downloadUri= task.getResult().toString();
                            Toast.makeText(AgregarproductoActivity.this, "Imagen guardada en Firebase", Toast.LENGTH_SHORT).show();
                            GuardarEnFirebase();
                        }else{
                            Toast.makeText(AgregarproductoActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void GuardarEnFirebase() {
        HashMap<String, Object> map= new HashMap<>();
        map.put("pid", productoRandonKey);
        map.put("fecha", CurrentDate);
        map.put("hora", CurrentTime);
        map.put("descripcion", Desc);
        map.put("nombre", Nom);
        map.put("preciocom", PrecioComp);
        map.put("precioven", PrecioVent);
        map.put("cantidad", Cant);
        map.put("imagen", downloadUri);
        map.put("categoria", Categoria);

        ProductoRef.child(productoRandonKey).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Intent intent= new Intent(AgregarproductoActivity.this, AdminActivity.class);
                    startActivity(intent);
                    dialog.dismiss();
                    Toast.makeText(AgregarproductoActivity.this, "Solicitud exitosa", Toast.LENGTH_SHORT).show();
                }else{
                    dialog.dismiss();
                    String mensaje= task.getException().toString();
                    Toast.makeText(AgregarproductoActivity.this, "Error..."+ mensaje, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}