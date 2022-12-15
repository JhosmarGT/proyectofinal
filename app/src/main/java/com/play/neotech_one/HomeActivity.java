package com.play.neotech_one;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private Button botonusuario, botonadministrador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        botonusuario=(Button) findViewById(R.id.botonusuario);
        botonadministrador=(Button) findViewById(R.id.botonadministrador);

        botonusuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        botonadministrador.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(HomeActivity.this, LoginadminActivity.class);
                startActivity(intent);
            }
        });
    }
}