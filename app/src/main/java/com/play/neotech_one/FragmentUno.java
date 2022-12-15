package com.play.neotech_one;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class FragmentUno extends Fragment {
    private View fragmento;
    private ImageView celulares;
    private ImageView laptops;
    private ImageView impresoras;
    private ImageView videojuegos;
    private ImageView cpus;
    private ImageView pcs;
    private ImageView otros;

    public FragmentUno() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmento= inflater.inflate(R.layout.fragment_uno, container, false);

        celulares= (ImageView)fragmento.findViewById(R.id.celulares);

        laptops= (ImageView)fragmento.findViewById(R.id.laptop);

        impresoras= (ImageView)fragmento.findViewById(R.id.impresora);

        videojuegos= (ImageView)fragmento.findViewById(R.id.videojuego);

        cpus= (ImageView)fragmento.findViewById(R.id.cpu);

        pcs= (ImageView)fragmento.findViewById(R.id.pc);

        otros= (ImageView)fragmento.findViewById(R.id.otro);

        celulares.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), AgregarproductoActivity.class);
                intent.putExtra("categoria", "celulares");
                startActivity(intent);
            }
        });

        laptops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), AgregarproductoActivity.class);
                intent.putExtra("categoria", "laptops");
                startActivity(intent);
            }
        });

        impresoras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), AgregarproductoActivity.class);
                intent.putExtra("categoria", "impresoras");
                startActivity(intent);
            }
        });

        videojuegos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), AgregarproductoActivity.class);
                intent.putExtra("categoria", "videojuegos");
                startActivity(intent);
            }
        });

        cpus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), AgregarproductoActivity.class);
                intent.putExtra("categoria", "cpus");
                startActivity(intent);
            }
        });

        pcs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), AgregarproductoActivity.class);
                intent.putExtra("categoria", "pcs");
                startActivity(intent);
            }
        });

        otros.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getContext(), AgregarproductoActivity.class);
                intent.putExtra("categoria", "otros");
                startActivity(intent);
            }
        });



        return fragmento;
    }
}