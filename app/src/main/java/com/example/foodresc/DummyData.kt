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
        User("Siti", "siti@foodresc.com", "0134567890", "123456", "community"),
        User("Hakim", "hakim@foodresc.com", "0112345678", "123456", "community"),
        User("Farah", "farah@foodresc.com", "0176543210", "123456", "volunteer")
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
            R.drawable.logo_foodresc, "siti@foodresc.com", "available"),
        Food(7, "Fresh Spinach", "Vegetables", "2 kg", "Cheras",
            "No. 3, Jalan Cheras Hartamas, Cheras", "Tomorrow, 7:00 PM",
            R.drawable.logo_foodresc, "hakim@foodresc.com", "available"),
        Food(8, "Orange Juice", "Drinks", "12 bottles", "Wangsa Maju",
            "Kedai Runcit Maju, Jalan 2/27A, Wangsa Maju", "Tomorrow, 12:00 PM",
            R.drawable.logo_foodresc, "hakim@foodresc.com", "available"),
        Food(9, "Curry Puffs", "Bakery", "20 pieces", "Ampang",
            "Gerai Karipap Mak Jah, Jalan Merdeka, Ampang", "Today, 10:00 PM",
            R.drawable.logo_foodresc, "hakim@foodresc.com", "available"),
        Food(10, "Watermelon Slices", "Fruits", "2 boxes", "Cheras",
            "No. 18, Jalan Cheras Perdana, Cheras", "Today, 9:00 PM",
            R.drawable.logo_foodresc, "community@foodresc.com", "reserved"),
        Food(11, "Mee Goreng", "Meals", "6 packs", "Gombak",
            "Restoran Selera Gombak, Jalan Gombak", "Today, 7:30 PM",
            R.drawable.logo_foodresc, "siti@foodresc.com", "reserved"),
        Food(12, "Fresh Tomatoes", "Vegetables", "1.5 kg", "Wangsa Maju",
            "No. 22, Jalan 4/27A, Wangsa Maju", "Tomorrow, 4:00 PM",
            R.drawable.logo_foodresc, "siti@foodresc.com", "available")
    )

    val reservations = mutableListOf(
        Reservation(1, 3, "siti@foodresc.com", null, "26 Sep 2026", "5:00 PM", "pending"),
        Reservation(2, 5, "siti@foodresc.com", "volunteer@foodresc.com", "20 Sep 2026", "3:00 PM", "delivered")
    )

    var currentUser: User? = null
}