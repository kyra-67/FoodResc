package com.example.foodresc

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class FoodDetailActivity : AppCompatActivity() {

    private lateinit var food: Food
    private lateinit var tilPickupDate: TextInputLayout
    private lateinit var tilPickupTime: TextInputLayout
    private lateinit var etPickupDate: TextInputEditText
    private lateinit var etPickupTime: TextInputEditText

    // Tarikh & masa yang dipilih (null = belum pilih)
    private var pickupCalendar: Calendar? = null
    private var timeSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_food_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Ambil makanan ikut ID yang dihantar dari Browse
        val foodId = intent.getIntExtra("FOOD_ID", -1)
        val foundFood = DummyData.foods.find { it.id == foodId }
        if (foundFood == null) {
            Toast.makeText(this, "Food not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }
        food = foundFood

        // 2. Sambungkan komponen
        val ivFood = findViewById<ImageView>(R.id.ivFood)
        val tvFoodName = findViewById<TextView>(R.id.tvFoodName)
        val tvFoodInfo = findViewById<TextView>(R.id.tvFoodInfo)
        val tvFoodExpiry = findViewById<TextView>(R.id.tvFoodExpiry)
        val tvFoodAddress = findViewById<TextView>(R.id.tvFoodAddress)
        val tvDonor = findViewById<TextView>(R.id.tvDonor)
        val btnReserve = findViewById<MaterialButton>(R.id.btnReserve)
        tilPickupDate = findViewById(R.id.tilPickupDate)
        tilPickupTime = findViewById(R.id.tilPickupTime)
        etPickupDate = findViewById(R.id.etPickupDate)
        etPickupTime = findViewById(R.id.etPickupTime)

        // 3. Papar maklumat makanan
        val donorName = DummyData.users.find { it.email == food.donorEmail }?.name ?: "Unknown"
        ivFood.setImageResource(food.imageRes)
        tvFoodName.text = food.name
        tvFoodInfo.text = "${food.category} • ${food.quantity}"
        tvFoodExpiry.text = "Collect by ${food.expiry}"
        tvFoodAddress.text = "📍 ${food.address}"
        tvDonor.text = "Donated by $donorName"

        // 4. Butang
        etPickupDate.setOnClickListener { showDatePicker() }
        etPickupTime.setOnClickListener { showTimePicker() }
        btnReserve.setOnClickListener { reserveFood() }
    }

    private fun showDatePicker() {
        val today = Calendar.getInstance()
        val dialog = DatePickerDialog(this, { _, year, month, day ->
            val cal = pickupCalendar ?: Calendar.getInstance()
            cal.set(year, month, day)
            pickupCalendar = cal
            etPickupDate.setText(SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(cal.time))
            tilPickupDate.error = null
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))

        dialog.datePicker.minDate = today.timeInMillis   // tak boleh pilih tarikh lepas
        dialog.show()
    }

    private fun showTimePicker() {
        val now = Calendar.getInstance()
        TimePickerDialog(this, { _, hour, minute ->
            val cal = pickupCalendar ?: Calendar.getInstance()
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            cal.set(Calendar.SECOND, 0)
            pickupCalendar = cal
            timeSelected = true
            etPickupTime.setText(SimpleDateFormat("h:mm a", Locale.ENGLISH).format(cal.time))
            tilPickupTime.error = null
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false).show()
    }

    private fun reserveFood() {
        tilPickupDate.error = null
        tilPickupTime.error = null

        // Validation
        var isValid = true
        if (etPickupDate.text.isNullOrEmpty()) {
            tilPickupDate.error = "Please choose a pickup date"
            isValid = false
        }
        if (!timeSelected) {
            tilPickupTime.error = "Please choose a pickup time"
            isValid = false
        }
        if (!isValid) return

        if (pickupCalendar!!.before(Calendar.getInstance())) {
            tilPickupTime.error = "This pickup time has already passed"
            return
        }

        if (food.status != "available") {
            Toast.makeText(this, "Sorry, this food is no longer available", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // Simpan reservation baru
        val user = DummyData.currentUser ?: return
        val newId = (DummyData.reservations.maxOfOrNull { it.id } ?: 0) + 1

        DummyData.reservations.add(
            Reservation(
                newId, food.id, user.email, null,
                etPickupDate.text.toString(), etPickupTime.text.toString(), "pending"
            )
        )
        food.status = "reserved"

        Toast.makeText(this, "Reservation successful! Waiting for a volunteer.", Toast.LENGTH_LONG).show()
        finish()
    }
}