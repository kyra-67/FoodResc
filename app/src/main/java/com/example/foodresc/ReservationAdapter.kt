package com.example.foodresc

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class ReservationAdapter(
    private var reservations: List<Reservation>,
    private val actionText: (Reservation) -> String?,
    private val onAction: (Reservation) -> Unit
) : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    class ReservationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivFood: ImageView = view.findViewById(R.id.ivFood)
        val tvFoodName: TextView = view.findViewById(R.id.tvFoodName)
        val tvAddress: TextView = view.findViewById(R.id.tvAddress)
        val tvPickup: TextView = view.findViewById(R.id.tvPickup)
        val tvPerson: TextView = view.findViewById(R.id.tvPerson)
        val tvStatus: TextView = view.findViewById(R.id.tvStatus)
        val btnAction: MaterialButton = view.findViewById(R.id.btnAction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_reservation, parent, false)
        return ReservationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservations[position]
        val food = DummyData.foods.find { it.id == reservation.foodId }
        val recipient = DummyData.users.find { it.email == reservation.recipientEmail }

        holder.ivFood.setImageResource(food?.imageRes ?: R.drawable.logo_foodresc)
        holder.tvFoodName.text = food?.name ?: "Unknown food"
        holder.tvAddress.text = "📍 ${food?.address ?: "-"}"
        holder.tvPickup.text = "🗓 ${reservation.pickupDate}, ${reservation.pickupTime}"
        holder.tvPerson.text = "Recipient: ${recipient?.name ?: "-"}"

        // Chip status: teks + warna
        val context = holder.itemView.context
        val (label, colorRes) = when (reservation.status) {
            "pending" -> "Waiting for Volunteer" to R.color.status_pending
            "accepted" -> "Accepted" to R.color.status_accepted
            "picked_up" -> "Picked Up" to R.color.status_picked_up
            "delivered" -> "Delivered" to R.color.status_delivered
            "cancelled" -> "Cancelled" to R.color.text_gray
            else -> reservation.status to R.color.text_gray
        }
        holder.tvStatus.text = label
        holder.tvStatus.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))

        // Butang tindakan: sorok kalau teks null
        val text = actionText(reservation)
        if (text == null) {
            holder.btnAction.visibility = View.GONE
        } else {
            holder.btnAction.visibility = View.VISIBLE
            holder.btnAction.text = text
            holder.btnAction.setOnClickListener { onAction(reservation) }
        }
    }

    override fun getItemCount(): Int = reservations.size

    fun updateData(newReservations: List<Reservation>) {
        reservations = newReservations
        notifyDataSetChanged()
    }
}