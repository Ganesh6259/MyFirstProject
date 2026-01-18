package com.example.myapplication

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

enum class UserType {
    RESTAURANT, INDIVIDUAL_DONOR, ORPHANAGE, NEEDED_INDIVIDUAL, DELIVERY_BOY
}

@Parcelize
data class UserAccount(
    var email: String? = null,
    var password: String? = null,
    var phoneNumber: String? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var dob: String? = null,
    var gender: String? = null,
    var profile: UserProfile? = null
) : Parcelable

@Parcelize
data class UserProfile(
    val id: String,
    val name: String,
    val contact: String,
    val address: String,
    val role: UserType,
    val orphanageCapacity: String? = null,
    val restaurantFoodType: String? = null,
    val organizationType: String? = null,
    val additionalNotes: String? = null
) : Parcelable

@Parcelize
data class FoodItem(
    val name: String,
    val quantity: String
) : Parcelable

@Parcelize
data class FoodDonation(
    val id: String,
    val items: List<FoodItem>,
    val address: String,
    val donorId: String,
    val donorName: String,
    val donorType: UserType,
    var status: String = "Available",
    var claimedById: String? = null
) : Parcelable

@Parcelize
data class FoodRequest(
    val id: String,
    val items: List<FoodItem>,
    val location: String,
    val requesterId: String,
    val requesterName: String,
    val requesterType: UserType,
    var status: String = "Pending",
    var claimedById: String? = null
) : Parcelable

object FoodRepository {
    val donations = mutableListOf<FoodDonation>()
    val requests = mutableListOf<FoodRequest>()
    
    val accounts = mutableListOf<UserAccount>()
    var currentAccount: UserAccount? = null
}