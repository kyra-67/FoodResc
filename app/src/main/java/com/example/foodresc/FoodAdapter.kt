package com.example.foodresc

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(
    private var foods: List<Food>,
    private val onClick: (Food) -> Unit
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    // Simpan rujukan komponen dalam satu kad
    class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivFood: ImageView = view.findViewById(R.id.ivFood)
        val tvFoodName: TextView = view.findViewById(R.id.tvFoodName)
        val tvFoodInfo: TextView = view.findViewById(R.id.tvFoodInfo)
        val tvFoodArea: TextView = view.findViewById(R.id.tvFoodArea)
        val tvFoodExpiry: TextView = view.findViewById(R.id.tvFoodExpiry)
    }

    // Cipta satu kad kosong dari item_food.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    // Isi data makanan ke dalam kad
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foods[position]
        holder.ivFood.setImageResource(food.imageRes)
        holder.tvFoodName.text = food.name
        holder.tvFoodInfo.text = "${food.category} • ${food.quantity}"
        holder.tvFoodArea.text = "📍 ${food.area}"
        holder.tvFoodExpiry.text = "Collect by ${food.expiry}"
        holder.itemView.setOnClickListener { onClick(food) }
    }

    // Berapa banyak kad
    override fun getItemCount(): Int = foods.size

    // Tukar senarai bila search/filter berubah
    fun updateData(newFoods: List<Food>) {
        foods = newFoods
        notifyDataSetChanged()
    }
}