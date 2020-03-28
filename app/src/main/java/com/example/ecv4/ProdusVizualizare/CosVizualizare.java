package com.example.ecv4.ProdusVizualizare;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ecv4.InterfataProduse.ItemClickListener;
import com.example.ecv4.R;

public class CosVizualizare extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView produsNume,produsPret,produsCantitate;
    private ItemClickListener itemClickListener;

    public CosVizualizare(@NonNull View itemView) {
        super(itemView);

        produsNume = itemView.findViewById(R.id.cos_produs_nume);
        produsPret = itemView.findViewById(R.id.cos_produs_pret);
        produsCantitate = itemView.findViewById(R.id.cos_produs_cantitate);

    }

    @Override
    public void onClick(View v)
    {
        itemClickListener.onClick(v,getAdapterPosition(),false);

    }

    public void setItemClickListener(ItemClickListener itemClickListener)
    {
        this.itemClickListener = itemClickListener;
    }
}
