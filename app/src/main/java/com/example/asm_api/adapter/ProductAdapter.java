package com.example.asm_api.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.asm_api.R;
import com.example.asm_api.databinding.ItemProductBinding;
import com.example.asm_api.model.Fruit;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Fruit> list;
    private FruitClick fruitClick;

    public ProductAdapter(Context context, ArrayList<Fruit> list, FruitClick fruitClick) {
        this.context = context;
        this.list = list;
        this.fruitClick = fruitClick;
    }

    public interface FruitClick {
        void delete(Fruit fruit);
        void edit(Fruit fruit);
        void showDetail(Fruit fruit);

        void onFruitClick(Fruit fruit);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemProductBinding binding = ItemProductBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Fruit fruit = list.get(position);
        holder.binding.tvName.setText(fruit.getName());
        holder.binding.tvPriceQuantity.setText("price :" + fruit.getPrice() + " - quantity:" + fruit.getQuantity());
        String url = fruit.getImage().get(0);
        String newUrl = url.replace("localhost", "10.0.2.2");
        Glide.with(context)
                .load(newUrl)
                .thumbnail(Glide.with(context).load(R.drawable.baseline_broken_image_24))
                .into(holder.binding.img);

        // Handle click events
        holder.binding.getRoot().setOnClickListener(v -> fruitClick.showDetail(fruit));
//        holder.binding.btnEdit.setOnClickListener(v -> fruitClick.edit(fruit));
//        holder.binding.btnDelete.setOnClickListener(v -> fruitClick.delete(fruit));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public ViewHolder(ItemProductBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}