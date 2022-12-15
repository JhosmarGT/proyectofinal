package com.play.neotech_one;

import android.app.ProgressDialog;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginadminActivity extends AppCompatActivity {
    private EditText numero, codigo;
    private Button enviarnumero, enviarcodigo;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String VerificacionID;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private FirebaseAuth auth;
    private ProgressDialog dialog;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginadmin);
        numero=(EditText) findViewById(R.id.numeroadmin);
        codigo=(EditText) findViewById(R.id.codigoadmin);
        enviarnumero=(Button) findViewById(R.id.enviarnumeroadmin);
        enviarcodigo=(Button) findViewById(R.id.enviarcodigoadmin);

        auth=FirebaseAuth.getInstance();
        dialog=new ProgressDialog(this);

        enviarnumero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phoneNumber= numero.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)){
                    Toast.makeText(LoginadminActivity.this, "Ingresa tu numero primero...", Toast.LENGTH_SHORT).show();
                }else {
                    dialog.setTitle("Validando numero");
                    dialog.setMessage("Por favor esper mientras validamos su numero");
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(true);


                    PhoneAuthOptions options= PhoneAuthOptions.newBuilder(auth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(LoginadminActivity.this)
                            .setCallbacks(callbacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options); //ENVIA EL NUMERO
                }
            }
        });
        enviarcodigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numero.setVisibility(View.GONE);
                enviarnumero.setVisibility(View.GONE);
                String VerificacionCode= codigo.getText().toString();
                if (TextUtils.isEmpty(VerificacionCode)){
                    Toast.makeText(LoginadminActivity.this, "Ingresa el codigo recibido", Toast.LENGTH_SHORT).show();
                }else {
                    dialog.setTitle("Verificando");
                    dialog.setMessage("Espere por favor...");
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(true);
                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(VerificacionID, VerificacionCode);
                    IngresadoConExito(credential);
                }
            }
        });
        callbacks= new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                IngresadoConExito(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                dialog.dismiss();
                Toast.makeText(LoginadminActivity.this, "Fallo en el inicio Causas: \n1. Numero invalido\n2. Sin conexion a internet\n3. Sin codigo de region", Toast.LENGTH_SHORT).show();
                numero.setVisibility(View.VISIBLE);
                enviarnumero.setVisibility(View.VISIBLE);
                codigo.setVisibility(View.GONE);
                enviarcodigo.setVisibility(View.GONE);
            }


            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                VerificacionID= s;
                resendingToken=token;
                dialog.dismiss();
                Toast.makeText(LoginadminActivity.this, "Codigo enviado satisfactoriamente, revisa tu bandeja de netrada", Toast.LENGTH_SHORT).show();
                numero.setVisibility(View.GONE);
                enviarnumero.setVisibility(View.GONE);
                codigo.setVisibility(View.VISIBLE);
                enviarcodigo.setVisibility(View.VISIBLE);
            }
        };
    }
    private void IngresadoConExito(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    dialog.dismiss();
                    Toast.makeText(LoginadminActivity.this, "Ingresado con exito", Toast.LENGTH_SHORT).show();
                    EnviaralaPrincipal();
                }else {
                    String err=task.getException().toString();
                    Toast.makeText(LoginadminActivity.this, "Error..."+err, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser=auth.getCurrentUser();
        if (firebaseUser !=null){
            EnviaralaPrincipal();
        }
    }
    private void EnviaralaPrincipal() {
        Intent intent= new Intent(LoginadminActivity.this, AdminActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("phone",phoneNumber);
        startActivity(intent);
        finish();
    }
}