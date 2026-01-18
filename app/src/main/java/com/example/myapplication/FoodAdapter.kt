package com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(
    private val donations: List<FoodDonation>,
    private val showActions: Boolean = false
) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvFoodName: TextView = view.findViewById(R.id.tvItemFoodName)
        val tvQuantity: TextView = view.findViewById(R.id.tvItemQuantity)
        val tvRestaurant: TextView = view.findViewById(R.id.tvItemRestaurant)
        val tvAddress: TextView = view.findViewById(R.id.tvItemAddress)
        val tvStatus: TextView = view.findViewById(R.id.tvItemStatus)
        val layoutActions: LinearLayout = view.findViewById(R.id.layoutDonationActions)
        val btnAccept: Button = view.findViewById(R.id.btnAcceptDonation)
        val btnDeny: Button = view.findViewById(R.id.btnDenyDonation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food_donation, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val donation = donations[position]
        
        // Display multiple items
        holder.tvFoodName.text = donation.items.joinToString(", ") { it.name }
        holder.tvQuantity.text = "Quantities: " + donation.items.joinToString(", ") { it.quantity }
        
        val typeLabel = if (donation.donorType == UserType.RESTAURANT) "Restaurant" else "Donor"
        holder.tvRestaurant.text = "From $typeLabel: ${donation.donorName}"
        holder.tvAddress.text = "Address: ${donation.address}"
        holder.tvStatus.text = "Status: ${donation.status}"

        when (donation.status) {
            "Accepted" -> holder.tvStatus.setTextColor(Color.parseColor("#388E3C"))
            "Denied" -> holder.tvStatus.setTextColor(Color.RED)
            else -> holder.tvStatus.setTextColor(Color.parseColor("#388E3C"))
        }

        if (showActions && donation.status == "Available") {
            holder.layoutActions.visibility = View.VISIBLE
            holder.btnAccept.setOnClickListener {
                donation.status = "Accepted"
                notifyItemChanged(position)
            }
            holder.btnDeny.setOnClickListener {
                donation.status = "Denied"
                notifyItemChanged(position)
            }
        } else {
            holder.layoutActions.visibility = View.GONE
        }
    }

    override fun getItemCount() = donations.size
}