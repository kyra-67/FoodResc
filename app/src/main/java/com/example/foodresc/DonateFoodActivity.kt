package com.example.foodresc

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
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

class DonateFoodActivity : AppCompatActivity() {

    private lateinit var tilFoodName: TextInputLayout
    private lateinit var tilCategory: TextInputLayout
    private lateinit var tilQuantity: TextInputLayout
    private lateinit var tilArea: TextInputLayout
    private lateinit var tilAddress: TextInputLayout
    private lateinit var tilExpiryDate: TextInputLayout
    private lateinit var tilExpiryTime: TextInputLayout

    private lateinit var etFoodName: TextInputEditText
    private lateinit var actvCategory: AutoCompleteTextView
    private lateinit var etQuantity: TextInputEditText
    private lateinit var actvArea: AutoCompleteTextView
    private lateinit var etAddress: TextInputEditText
    private lateinit var etExpiryDate: TextInputEditText
    private lateinit var etExpiryTime: TextInputEditText

    private val expiryCalendar: Calendar = Calendar.getInstance()
    private var dateSelected = false
    private var timeSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_donate_food)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        tilFoodName = findViewById(R.id.tilFoodName)
        tilCategory = findViewById(R.id.tilCategory)
        tilQuantity = findViewById(R.id.tilQuantity)
        tilArea = findViewById(R.id.tilArea)
        tilAddress = findViewById(R.id.tilAddress)
        tilExpiryDate = findViewById(R.id.tilExpiryDate)
        tilExpiryTime = findViewById(R.id.tilExpiryTime)

        etFoodName = findViewById(R.id.etFoodName)
        actvCategory = findViewById(R.id.actvCategory)
        etQuantity = findViewById(R.id.etQuantity)
        actvArea = findViewById(R.id.actvArea)
        etAddress = findViewById(R.id.etAddress)
        etExpiryDate = findViewById(R.id.etExpiryDate)
        etExpiryTime = findViewById(R.id.etExpiryTime)

        // Isi dropdown dari DummyData
        actvCategory.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_list_item_1, DummyData.categories)
        )
        actvArea.setAdapter(
            ArrayAdapter(this, android.R.layout.simple_list_item_1, DummyData.areas)
        )
        actvCategory.setOnItemClickListener { _, _, _, _ -> tilCategory.error = null }
        actvArea.setOnItemClickListener { _, _, _, _ -> tilArea.error = null }

        etExpiryDate.setOnClickListener { showDatePicker() }
        etExpiryTime.setOnClickListener { showTimePicker() }
        findViewById<MaterialButton>(R.id.btnSubmit).setOnClickListener { submitDonation() }
    }

    private fun showDatePicker() {
        val today = Calendar.getInstance()
        val dialog = DatePickerDialog(this, { _, year, month, day ->
            expiryCalendar.set(year, month, day)
            dateSelected = true
            etExpiryDate.setText(SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH).format(expiryCalendar.time))
            tilExpiryDate.error = null
        }, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH))

        dialog.datePicker.minDate = today.timeInMillis
        dialog.show()
    }

    private fun showTimePicker() {
        val now = Calendar.getInstance()
        TimePickerDialog(this, { _, hour, minute ->
            expiryCalendar.set(Calendar.HOUR_OF_DAY, hour)
            expiryCalendar.set(Calendar.MINUTE, minute)
            expiryCalendar.set(Calendar.SECOND, 0)
            timeSelected = true
            etExpiryTime.setText(SimpleDateFormat("h:mm a", Locale.ENGLISH).format(expiryCalendar.time))
            tilExpiryTime.error = null
        }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false).show()
    }

    private fun submitDonation() {
        val name = etFoodName.text.toString().trim()
        val category = actvCategory.text.toString()
        val quantity = etQuantity.text.toString().trim()
        val area = actvArea.text.toString()
        val address = etAddress.text.toString().trim()

        // Padam error lama
        listOf(tilFoodName, tilCategory, tilQuantity, tilArea, tilAddress, tilExpiryDate, tilExpiryTime)
            .forEach { it.error = null }

        // Validation
        var isValid = true
        if (name.isEmpty()) {
            tilFoodName.error = "Please enter the food name"
            isValid = false
        }
        if (category.isEmpty()) {
            tilCategory.error = "Please choose a category"
            isValid = false
        }
        if (quantity.isEmpty()) {
            tilQuantity.error = "Please enter the quantity"
            isValid = false
        }
        if (area.isEmpty()) {
            tilArea.error = "Please choose a collection area"
            isValid = false
        }
        if (address.length < 10) {
            tilAddress.error = "Please enter a complete pickup address"
            isValid = false
        }
        if (!dateSelected) {
            tilExpiryDate.error = "Please choose a date"
            isValid = false
        }
        if (!timeSelected) {
            tilExpiryTime.error = "Please choose a time"
            isValid = false
        }
        if (!isValid) return

        if (expiryCalendar.before(Calendar.getInstance())) {
            tilExpiryTime.error = "Collect-by time must be in the future"
            return
        }

        // Simpan makanan baru
        val user = DummyData.currentUser ?: return
        val newId = (DummyData.foods.maxOfOrNull { it.id } ?: 0) + 1
        val expiryText = "${etExpiryDate.text}, ${etExpiryTime.text}"

        DummyData.foods.add(
            Food(
                newId, name, category, quantity, area, address, expiryText,
                imageForCategory(category), user.email, "available"
            )
        )

        Toast.makeText(this, "Thank you! Your food is now listed.", Toast.LENGTH_LONG).show()
        finish()
    }

    // Gambar ikut kategori (tukar ke gambar sebenar masa polish)
    private fun imageForCategory(category: String): Int {
        return R.drawable.logo_foodresc
    }
}