package com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DeliveryAdapter(
    private val tasks: List<Any>, // Can be FoodDonation or FoodRequest
    private val onAction: (Any) -> Unit
) : RecyclerView.Adapter<DeliveryAdapter.DeliveryViewHolder>() {

    class DeliveryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvItemFoodName)
        val tvDetail: TextView = view.findViewById(R.id.tvItemQuantity)
        val tvFrom: TextView = view.findViewById(R.id.tvItemRestaurant)
        val tvTo: TextView = view.findViewById(R.id.tvItemAddress)
        val tvStatus: TextView = view.findViewById(R.id.tvItemStatus)
        val btnAction: Button = view.findViewById(R.id.btnAcceptDonation) // Reusing ID for "Pickup/Deliver"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeliveryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food_donation, parent, false)
        return DeliveryViewHolder(view)
    }

    override fun onBindViewHolder(holder: DeliveryViewHolder, position: Int) {
        val task = tasks[position]
        val layoutActions = holder.itemView.findViewById<View>(R.id.layoutDonationActions)
        layoutActions.visibility = View.VISIBLE
        holder.itemView.findViewById<View>(R.id.btnDenyDonation).visibility = View.GONE

        if (task is FoodDonation) {
            holder.tvTitle.text = "Donation: " + task.items.joinToString(", ") { it.name }
            holder.tvDetail.text = "Qty: " + task.items.joinToString(", ") { it.quantity }
            holder.tvFrom.text = "Pickup: ${task.donorName} (${task.donorType})"
            holder.tvTo.text = "To: ${task.address}"
            holder.tvStatus.text = "Status: ${task.status}"
            
            holder.btnAction.text = if (task.status == "Accepted") "Mark as Picked up" else "Mark as Delivered"
        } else if (task is FoodRequest) {
            holder.tvTitle.text = "Request: " + task.items.joinToString(", ") { it.name }
            holder.tvDetail.text = "Qty: " + task.items.joinToString(", ") { it.quantity }
            holder.tvFrom.text = "Pickup: From donor responding to ${task.requesterName}"
            holder.tvTo.text = "To: ${task.location} (${task.requesterType})"
            holder.tvStatus.text = "Status: ${task.status}"
            
            holder.btnAction.text = if (task.status == "Accepted") "Mark as Picked up" else "Mark as Delivered"
        }

        holder.btnAction.setOnClickListener {
            onAction(task)
        }
    }

    override fun getItemCount() = tasks.size
}