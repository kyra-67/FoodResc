package com.example.foodresc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText

class BrowseFragment : Fragment(R.layout.fragment_browse) {

    private lateinit var adapter: FoodAdapter
    private lateinit var rvFoods: RecyclerView
    private lateinit var tvEmpty: TextView

    // Pilihan semasa user
    private var searchText = ""
    private var selectedCategory = "All"
    private var selectedArea = "All Areas"

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etSearch = view.findViewById<TextInputEditText>(R.id.etSearch)
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupCategory)
        val actvArea = view.findViewById<AutoCompleteTextView>(R.id.actvArea)
        rvFoods = view.findViewById(R.id.rvFoods)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        // 1. Sediakan senarai
        adapter = FoodAdapter(emptyList()) { food ->
            val intent = Intent(requireContext(), FoodDetailActivity::class.java)
                    intent.putExtra("FOOD_ID", food.id)
                startActivity(intent)
        }
        rvFoods.layoutManager = LinearLayoutManager(requireContext())
        rvFoods.adapter = adapter

        // 2. Search: tapis setiap kali user menaip
        etSearch.doAfterTextChanged { text ->
            searchText = text.toString().trim()
            applyFilter()
        }

        // 3. Chip kategori: cipta satu chip untuk setiap kategori
        val categories = listOf("All") + DummyData.categories
        for (category in categories) {
            val chip = Chip(requireContext())
            chip.text = category
            chip.isCheckable = true
            chip.isChecked = category == selectedCategory
            chip.setOnClickListener {
                selectedCategory = category
                applyFilter()
            }
            chipGroup.addView(chip)
        }

        // 4. Dropdown kawasan
        val areas = listOf("All Areas") + DummyData.areas
        actvArea.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, areas))
        actvArea.setText(selectedArea, false)
        actvArea.setOnItemClickListener { _, _, position, _ ->
            selectedArea = areas[position]
            applyFilter()
        }

        // 5. Papar senarai pertama kali
        applyFilter()
    }

    override fun onResume() {
        super.onResume()
        applyFilter()
    }

    // Tapis makanan ikut semua pilihan, kemudian kemaskini senarai
    private fun applyFilter() {
        val currentEmail = DummyData.currentUser?.email

        val result = DummyData.foods.filter { food ->
            food.status == "available" &&
                    food.donorEmail != currentEmail &&
                    food.name.contains(searchText, ignoreCase = true) &&
                    (selectedCategory == "All" || food.category == selectedCategory) &&
                    (selectedArea == "All Areas" || food.area == selectedArea)
        }

        adapter.updateData(result)
        rvFoods.visibility = if (result.isEmpty()) View.GONE else View.VISIBLE
        tvEmpty.visibility = if (result.isEmpty()) View.VISIBLE else View.GONE
    }
}