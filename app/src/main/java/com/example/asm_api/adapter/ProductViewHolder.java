package com.example.asm_api.adapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.asm_api.model.Fruit;

import java.util.ArrayList;

public class ProductViewHolder extends RecyclerView.ViewHolder {
    // Khai báo các view trong ViewHolder
    private ProductAdapter.FruitClick listener;
    private ArrayList<Fruit> fruits;

    public ProductViewHolder(@NonNull View itemView, ProductAdapter.FruitClick listener, ArrayList<Fruit> fruits) {
        super(itemView);
        this.listener = listener;
        this.fruits = fruits;

        // Ánh xạ view vào biến

        // Lắng nghe sự kiện click trên itemView
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy đối tượng Fruit tương ứng với vị trí của item được click
                Fruit fruit = fruits.get(getAdapterPosition());

                // Gọi phương thức onFruitClick thông qua listener đã được định nghĩa
                listener.onFruitClick(fruit);
            }
        });
    }
}