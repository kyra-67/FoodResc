package com.example.foodresc

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class TasksFragment : Fragment(R.layout.fragment_tasks) {

    private lateinit var adapter: ReservationAdapter
    private lateinit var rvTasks: RecyclerView
    private lateinit var tvEmpty: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvTasks = view.findViewById(R.id.rvTasks)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        adapter = ReservationAdapter(
            emptyList(),
            actionText = { reservation ->
                when (reservation.status) {
                    "accepted" -> "Mark as Picked Up"
                    "picked_up" -> "Mark as Delivered"
                    else -> null
                }
            },
            onAction = { reservation -> confirmUpdate(reservation) }
        )
        rvTasks.layoutManager = LinearLayoutManager(requireContext())
        rvTasks.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    // Tugas milik volunteer ni yang belum selesai
    private fun loadData() {
        val myEmail = DummyData.currentUser?.email
        val tasks = DummyData.reservations.filter {
            it.volunteerEmail == myEmail &&
                    (it.status == "accepted" || it.status == "picked_up")
        }
        adapter.updateData(tasks)
        rvTasks.visibility = if (tasks.isEmpty()) View.GONE else View.VISIBLE
        tvEmpty.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun confirmUpdate(reservation: Reservation) {
        val nextStatus = if (reservation.status == "accepted") "picked_up" else "delivered"
        val message = if (nextStatus == "picked_up")
            "Confirm that you have collected the food from the donor?"
        else
            "Confirm that the food has been delivered to the recipient?"

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Update Status")
            .setMessage(message)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Confirm") { _, _ ->
                reservation.status = nextStatus

                // Bila selesai, makanan ditanda "collected"
                if (nextStatus == "delivered") {
                    DummyData.foods.find { it.id == reservation.foodId }?.status = "collected"
                }

                val toast = if (nextStatus == "picked_up") "Status updated: Picked Up"
                else "Delivery completed. Thank you!"
                Toast.makeText(requireContext(), toast, Toast.LENGTH_SHORT).show()
                loadData()
            }
            .show()
    }
}