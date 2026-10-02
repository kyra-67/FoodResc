package com.example.foodresc

data class User(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
    val role: String
)

data class Food(
    val id: Int,
    val name: String,
    val category: String,
    val quantity: String,
    val area: String,
    val address: String,
    val expiry: String,
    val imageRes: Int,
    val donorEmail: String,
    var status: String
)

data class Reservation(
    val id: Int,
    val foodId: Int,
    val recipientEmail: String,
    var volunteerEmail: String?,
    var pickupDate: String,
    var pickupTime: String,
    var status: String
)

object DummyData {

    val users = mutableListOf(
        User("Aisyah", "community@foodresc.com", "0123456789", "123456", "community"),
        User("Ali", "volunteer@foodresc.com", "0198765432", "123456", "volunteer"),
        User("Siti", "siti@foodresc.com", "0134567890", "123456", "community")
    )

    val categories = listOf("Meals", "Bakery", "Fruits", "Vegetables", "Drinks")
    val areas = listOf("Ampang", "Cheras", "Setapak", "Gombak", "Wangsa Maju")

    val foods = mutableListOf(
        Food(1, "Nasi Lemak", "Meals", "5 packs", "Ampang",
            "No. 12, Jalan Ampang Utama, Ampang", "Today, 9:00 PM",
            R.drawable.logo_foodresc, "community@foodresc.com", "available"),
        Food(2, "Roti Bun", "Bakery", "10 pieces", "Setapak",
            "Kedai Roti Mesra, Jalan Genting Kelang, Setapak", "Tomorrow, 10:00 AM",
            R.drawable.logo_foodresc, "siti@foodresc.com", "available"),
        Food(3, "Mixed Vegetables", "Vegetables", "3 kg", "Cheras",
            "Pasar Cheras, Jalan Cheras", "Tomorrow, 6:00 PM",
            R.drawable.logo_foodresc, "community@foodresc.com", "reserved"),
        Food(4, "Fresh Bananas", "Fruits", "3 combs", "Gombak",
            "No. 5, Jalan Gombak Setia, Gombak", "Tomorrow, 5:00 PM",
            R.drawable.logo_foodresc, "siti@foodresc.com", "available"),
        Food(5, "Mineral Water", "Drinks", "2 boxes", "Wangsa Maju",
            "No. 8, Jalan 1/27A, Wangsa Maju", "Mar 2027",
            R.drawable.logo_foodresc, "community@foodresc.com", "collected"),
        Food(6, "Chicken Rice", "Cooked Meal", "4 packs", "Setapak",
            "Restoran Selera, Jalan Usahawan, Setapak", "Today, 8:00 PM",
            R.drawable.logo_foodresc, "siti@foodresc.com", "available")
    )

    val reservations = mutableListOf(
        Reservation(1, 3, "siti@foodresc.com", null, "26 Sep 2026", "5:00 PM", "pending"),
        Reservation(2, 5, "siti@foodresc.com", "volunteer@foodresc.com", "20 Sep 2026", "3:00 PM", "delivered")
    )

    var currentUser: User? = null
}