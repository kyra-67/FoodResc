package com.example.foodresc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var sectionActive: LinearLayout
    private lateinit var tvNoFood: TextView
    private lateinit var rvAvailable: RecyclerView
    private lateinit var activeAdapter: ReservationAdapter
    private lateinit var foodAdapter: FoodAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tvGreeting).text =
            "Hi, ${DummyData.currentUser?.name} 👋"

        view.findViewById<MaterialButton>(R.id.btnDonate).setOnClickListener {
            startActivity(Intent(requireContext(), DonateFoodActivity::class.java))
        }

        val main = activity as? MainActivity

        view.findViewById<View>(R.id.cardSearch).setOnClickListener { main?.openBrowse() }
        view.findViewById<MaterialButton>(R.id.btnFindFood).setOnClickListener { main?.openBrowse() }
        view.findViewById<TextView>(R.id.tvViewAll).setOnClickListener { main?.openBrowse() }

        // Setiap kategori buka Browse dengan kategori tu dipilih
        val categoryViews = mapOf(
            R.id.catMeals to "Meals",
            R.id.catBakery to "Bakery",
            R.id.catFruits to "Fruits",
            R.id.catVegetables to "Vegetables",
            R.id.catDrinks to "Drinks"
        )
        for ((id, category) in categoryViews) {
            view.findViewById<View>(id).setOnClickListener { main?.openBrowse(category) }
        }

        sectionActive = view.findViewById(R.id.sectionActive)
        tvNoFood = view.findViewById(R.id.tvNoFood)
        rvAvailable = view.findViewById(R.id.rvAvailable)
        val rvActive = view.findViewById<RecyclerView>(R.id.rvActive)

        // Active Reservation: guna kad reservation, tanpa butang
        activeAdapter = ReservationAdapter(emptyList(), actionText = { null }, onAction = { })
        rvActive.layoutManager = LinearLayoutManager(requireContext())
        rvActive.adapter = activeAdapter

        // Available Food: tekan kad buka detail
        foodAdapter = FoodAdapter(emptyList()) { food ->
            val intent = Intent(requireContext(), FoodDetailActivity::class.java)
            intent.putExtra("FOOD_ID", food.id)
            startActivity(intent)
        }
        rvAvailable.layoutManager = LinearLayoutManager(requireContext())
        rvAvailable.adapter = foodAdapter
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val myEmail = DummyData.currentUser?.email

        // 1. Tempahan terkini yang masih berjalan
        val active = DummyData.reservations.lastOrNull {
            it.recipientEmail == myEmail &&
                    it.status in listOf("pending", "accepted", "picked_up")
        }
        if (active != null) {
            activeAdapter.updateData(listOf(active))
            sectionActive.visibility = View.VISIBLE
        } else {
            sectionActive.visibility = View.GONE
        }

        // 2. 3 makanan terbaru yang boleh ditempah
        val available = DummyData.foods
            .filter { it.status == "available" && it.donorEmail != myEmail }
            .reversed()
            .take(3)
        foodAdapter.updateData(available)
        rvAvailable.visibility = if (available.isEmpty()) View.GONE else View.VISIBLE
        tvNoFood.visibility = if (available.isEmpty()) View.VISIBLE else View.GONE
    }
}