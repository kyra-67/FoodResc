package com.example.foodresc

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class VolunteerHistoryFragment : Fragment(R.layout.fragment_volunteer_history) {

    private lateinit var adapter: ReservationAdapter
    private lateinit var rvHistory: RecyclerView
    private lateinit var tvEmpty: TextView
    private lateinit var tvSummary: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvHistory = view.findViewById(R.id.rvHistory)
        tvEmpty = view.findViewById(R.id.tvEmpty)
        tvSummary = view.findViewById(R.id.tvSummary)

        adapter = ReservationAdapter(
            emptyList(),
            actionText = { null },   // history: tiada butang
            onAction = { }
        )
        rvHistory.layoutManager = LinearLayoutManager(requireContext())
        rvHistory.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val myEmail = DummyData.currentUser?.email
        val history = DummyData.reservations
            .filter { it.volunteerEmail == myEmail && it.status == "delivered" }
            .reversed()   // yang terbaru di atas

        adapter.updateData(history)
        tvSummary.text = "You have completed ${history.size} deliveries"
        rvHistory.visibility = if (history.isEmpty()) View.GONE else View.VISIBLE
        tvEmpty.visibility = if (history.isEmpty()) View.VISIBLE else View.GONE
    }
}