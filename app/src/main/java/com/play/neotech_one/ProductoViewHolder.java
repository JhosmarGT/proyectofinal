package com.play.neotech_one;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ProductoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView productoNom, productoDescrip, productoPrecio, porductoCantidad;
    public ImageView productoImagen;
    public ItemClickListener listener;
    public ProductoViewHolder(@NonNull View itemView) {
        super(itemView);
        productoNom= (TextView) itemView.findViewById(R.id.producto_nombre);
        productoDescrip= (TextView) itemView.findViewById(R.id.producto_descripcion);
        productoPrecio= (TextView) itemView.findViewById(R.id.producto_precio);
        porductoCantidad= (TextView) itemView.findViewById(R.id.producto_cantidad);
        productoImagen= (ImageView) itemView.findViewById(R.id.producto_imagen);
    }
    public void setItemClickListener(ItemClickListener listener){
        this.listener= listener;
    }
    @Override
    public void onClick(View view) {

    }
}
