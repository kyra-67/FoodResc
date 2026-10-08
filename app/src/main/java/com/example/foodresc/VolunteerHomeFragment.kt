package com.example.foodresc

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class VolunteerHomeFragment : Fragment(R.layout.fragment_volunteer_home) {

    private lateinit var adapter: ReservationAdapter
    private lateinit var rvRequests: RecyclerView
    private lateinit var tvEmpty: TextView

    private lateinit var tvPendingCount: TextView
    private lateinit var tvActiveCount: TextView
    private lateinit var tvDeliveredCount: TextView
    private lateinit var sectionCurrentTask: LinearLayout
    private lateinit var currentAdapter: ReservationAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvGreeting = view.findViewById<TextView>(R.id.tvGreeting)
        rvRequests = view.findViewById(R.id.rvRequests)
        tvEmpty = view.findViewById(R.id.tvEmpty)

        tvGreeting.text = "Hi, ${DummyData.currentUser?.name} 👋"

        adapter = ReservationAdapter(
            emptyList(),
            actionText = { "Accept" },
            onAction = { reservation -> confirmAccept(reservation) }
        )
        rvRequests.layoutManager = LinearLayoutManager(requireContext())
        rvRequests.adapter = adapter

        tvPendingCount = view.findViewById(R.id.tvPendingCount)
        tvActiveCount = view.findViewById(R.id.tvActiveCount)
        tvDeliveredCount = view.findViewById(R.id.tvDeliveredCount)
        sectionCurrentTask = view.findViewById(R.id.sectionCurrentTask)

        val rvCurrentTask = view.findViewById<RecyclerView>(R.id.rvCurrentTask)
        currentAdapter = ReservationAdapter(
            emptyList(),
            actionText = { "Update Status" },
            onAction = { (activity as? VolunteerMainActivity)?.openTasks() }
        )
        rvCurrentTask.layoutManager = LinearLayoutManager(requireContext())
        rvCurrentTask.adapter = currentAdapter
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    // Ambil semua tempahan yang masih tunggu volunteer
    private fun loadData() {
        val pending = DummyData.reservations.filter { it.status == "pending" }
        adapter.updateData(pending)
        rvRequests.visibility = if (pending.isEmpty()) View.GONE else View.VISIBLE
        tvEmpty.visibility = if (pending.isEmpty()) View.VISIBLE else View.GONE

        // Kad ringkasan
        val myEmail = DummyData.currentUser?.email
        val myTasks = DummyData.reservations.filter { it.volunteerEmail == myEmail }
        val active = myTasks.filter { it.status == "accepted" || it.status == "picked_up" }

        tvPendingCount.text = pending.size.toString()
        tvActiveCount.text = active.size.toString()
        tvDeliveredCount.text = myTasks.count { it.status == "delivered" }.toString()

        // Current Task: tugas aktif pertama
        if (active.isNotEmpty()) {
            currentAdapter.updateData(listOf(active.first()))
            sectionCurrentTask.visibility = View.VISIBLE
        } else {
            sectionCurrentTask.visibility = View.GONE
        }
    }

    private fun confirmAccept(reservation: Reservation) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Accept Pickup")
            .setMessage("Accept this pickup on ${reservation.pickupDate}, ${reservation.pickupTime}?")
            .setNegativeButton("Cancel", null)
            .setPositiveButton("Accept") { _, _ ->
                reservation.status = "accepted"
                reservation.volunteerEmail = DummyData.currentUser?.email
                Toast.makeText(requireContext(), "Task accepted. Check your Tasks tab.", Toast.LENGTH_LONG).show()
                loadData()
            }
            .show()
    }
}