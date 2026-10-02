package com.example.foodresc

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MyActivityFragment : Fragment(R.layout.fragment_my_activity) {

    private lateinit var reservationAdapter: ReservationAdapter
    private lateinit var donationAdapter: FoodAdapter
    private lateinit var rvActivity: RecyclerView
    private lateinit var tvEmpty: TextView

    private var showingReservations = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toggleTab = view.findViewById<MaterialButtonToggleGroup>(R.id.toggleTab)
        rvActivity = view.findViewById(R.id.rvActivity)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        // Adapter untuk tab My Reservations
        reservationAdapter = ReservationAdapter(
            emptyList(),
            actionText = { if (it.status == "pending") "Cancel Reservation" else null },
            onAction = { confirmCancel(it) }
        )

        // Adapter untuk tab My Donations (tunjuk status)
        donationAdapter = FoodAdapter(emptyList(), showStatus = true) { }

        rvActivity.layoutManager = LinearLayoutManager(requireContext())

        // Tukar tab
        toggleTab.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                showingReservations = checkedId == R.id.btnTabReservations
                loadData()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun loadData() {
        val myEmail = DummyData.currentUser?.email
        val isEmpty: Boolean

        if (showingReservations) {
            val list = DummyData.reservations.filter { it.recipientEmail == myEmail }.reversed()
            rvActivity.adapter = reservationAdapter
            reservationAdapter.updateData(list)
            tvEmpty.text = "You have no reservations yet.\nFind food in the Browse tab."
            isEmpty = list.isEmpty()
        } else {
            val list = DummyData.foods.filter { it.donorEmail == myEmail }.reversed()
            rvActivity.adapter = donationAdapter
            donationAdapter.updateData(list)
            tvEmpty.text = "You have not donated any food yet."
            isEmpty = list.isEmpty()
        }

        rvActivity.visibility = if (isEmpty) View.GONE else View.VISIBLE
        tvEmpty.visibility = if (isEmpty) View.VISIBLE else View.GONE
    }

    private fun confirmCancel(reservation: Reservation) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Cancel Reservation")
            .setMessage("Are you sure you want to cancel this reservation?")
            .setNegativeButton("No", null)
            .setPositiveButton("Yes, Cancel") { _, _ ->
                reservation.status = "cancelled"
                // Pulangkan makanan supaya orang lain boleh tempah
                DummyData.foods.find { it.id == reservation.foodId }?.status = "available"
                Toast.makeText(requireContext(), "Reservation cancelled", Toast.LENGTH_SHORT).show()
                loadData()
            }
            .show()
    }
}