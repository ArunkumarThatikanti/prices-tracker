package com.example.real_timepricestracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.real_timepricestracker.R;
import com.example.real_timepricestracker.model.Price;

import java.util.List;

public class PricesAdapter extends RecyclerView.Adapter<PricesAdapter.PriceViewHolder>{
    private List<Price> priceList;

    public PricesAdapter(List<Price> priceList) {
        this.priceList = priceList;
    }

    @NonNull
    @Override
    public PriceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_price, parent, false);
        return new PriceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PriceViewHolder holder, int position) {
        Price price = priceList.get(position);
        holder.nameText.setText(price.getName());
        holder.priceText.setText("$ "+String.valueOf(price.getPrice()));
        String changeVal = String.valueOf(price.getChangeValue());
        if(changeVal.charAt(0)=='-'){
            holder.changeText.setTextColor(0xffff0000);
        }
        holder.changeText.setText("$ "+changeVal);
    }

    @Override
    public int getItemCount() {
        return priceList.size();
    }

    static class PriceViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, priceText, changeText;


        public PriceViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.nameText);
            priceText = itemView.findViewById(R.id.valueText);
            changeText = itemView.findViewById(R.id.changeText);
        }
    }
}
