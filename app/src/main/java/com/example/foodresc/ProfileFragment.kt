package com.example.foodresc

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvName = view.findViewById<TextView>(R.id.tvName)
        val tvRole = view.findViewById<TextView>(R.id.tvRole)
        val tvEmail = view.findViewById<TextView>(R.id.tvEmail)
        val tvPhone = view.findViewById<TextView>(R.id.tvPhone)
        val btnLogout = view.findViewById<MaterialButton>(R.id.btnLogout)

        val user = DummyData.currentUser

        tvName.text = user?.name
        tvRole.text = if (user?.role == "volunteer") "Volunteer Collector" else "Community User"
        tvEmail.text = user?.email
        tvPhone.text = "Phone: ${user?.phone}"

        btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Log Out")
                .setMessage("Are you sure you want to log out?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Log Out") { _, _ -> logout() }
                .show()
        }
    }

    private fun logout() {
        DummyData.currentUser = null
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}