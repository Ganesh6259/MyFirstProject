package com.example.myapplication

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RequestAdapter(
    private val requests: List<FoodRequest>,
    private val showActions: Boolean = false,
    private val onActionTaken: () -> Unit = {}
) : RecyclerView.Adapter<RequestAdapter.RequestViewHolder>() {

    class RequestViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvRequesterName: TextView = view.findViewById(R.id.tvReqOrphanageName)
        val tvFoodType: TextView = view.findViewById(R.id.tvReqFoodType)
        val tvQuantity: TextView = view.findViewById(R.id.tvReqQuantity)
        val tvLocation: TextView = view.findViewById(R.id.tvReqLocation)
        val tvStatus: TextView = view.findViewById(R.id.tvReqStatus)
        val layoutActions: LinearLayout = view.findViewById(R.id.layoutActionButtons)
        val btnAccept: Button = view.findViewById(R.id.btnAccept)
        val btnDeny: Button = view.findViewById(R.id.btnDeny)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RequestViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_food_request, parent, false)
        return RequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: RequestViewHolder, position: Int) {
        val request = requests[position]
        
        val typeLabel = if (request.requesterType == UserType.ORPHANAGE) "Orphanage" else "Individual"
        holder.tvRequesterName.text = "$typeLabel: ${request.requesterName}"
        
        // Display multiple items
        holder.tvFoodType.text = "Items: " + request.items.joinToString(", ") { it.name }
        holder.tvQuantity.text = "Quantities: " + request.items.joinToString(", ") { it.quantity }
        
        holder.tvLocation.text = "Location: ${request.location}"
        holder.tvStatus.text = "Status: ${request.status}"

        when (request.status) {
            "Accepted" -> holder.tvStatus.setTextColor(Color.parseColor("#388E3C"))
            "Denied" -> holder.tvStatus.setTextColor(Color.RED)
            else -> holder.tvStatus.setTextColor(Color.parseColor("#F57C00"))
        }

        if (showActions && request.status == "Pending") {
            holder.layoutActions.visibility = View.VISIBLE
            holder.btnAccept.setOnClickListener {
                request.status = "Accepted"
                notifyItemChanged(position)
                onActionTaken()
            }
            holder.btnDeny.setOnClickListener {
                request.status = "Denied"
                notifyItemChanged(position)
                onActionTaken()
            }
        } else {
            holder.layoutActions.visibility = View.GONE
        }
    }

    override fun getItemCount() = requests.size
}