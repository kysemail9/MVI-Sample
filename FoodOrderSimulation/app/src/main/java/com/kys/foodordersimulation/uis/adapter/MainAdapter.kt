package com.kys.foodordersimulation.uis.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kys.foodordersimulation.R
import com.kys.foodordersimulation.data.model.FoodOrder
import kotlinx.android.synthetic.main.food_order_row.view.*

class MainAdapter(
    private var foodOrders: ArrayList<FoodOrder>,
    private val callbacks: FoodOrderItemChangeStateCallbacks
) : PagingDataAdapter<FoodOrder, MainAdapter.DataViewHolder>(DiffUtilCallback()) {


    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(foodOrder: FoodOrder, callbacks: FoodOrderItemChangeStateCallbacks) {
            itemView.textViewOrderNumber.text = foodOrder.orderId
            itemView.textViewOrderDetails.text = foodOrder.orderDetails
            itemView.buttonOrderState.text = foodOrder.orderStateNameNow
            /* Glide.with(itemView.imageViewAvatar.context)
                .load(foodOrder.avatar)
                .into(itemView.imageViewAvatar)*/
            itemView.buttonOrderState.setOnClickListener {
                Log.e("K", "K item STAGE clicked at index $absoluteAdapterPosition")
                callbacks.onFoodOrderItemStateClicked(
                    foodOrder = foodOrder,
                    absoluteAdapterPosition
                )
            }
            itemView.setOnClickListener {
                Log.e("K", "K item clicked at index $absoluteAdapterPosition")
                callbacks.onFoodOrderItemClicked(foodOrder = foodOrder, absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.food_order_row, parent,
                false
            )
        )

    override fun getItemCount(): Int = foodOrders.size

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(foodOrders[position], callbacks)

    fun addData(list: List<FoodOrder>) {
        foodOrders.addAll(list)
    }

    interface FoodOrderItemChangeStateCallbacks {
        fun onFoodOrderItemStateClicked(foodOrder: FoodOrder, adapterPosition: Int)
        fun onFoodOrderItemClicked(foodOrder: FoodOrder, adapterPosition: Int)
    }

    class DiffUtilCallback : DiffUtil.ItemCallback<FoodOrder>() {
        override fun areItemsTheSame(oldItem: FoodOrder, newItem: FoodOrder): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: FoodOrder, newItem: FoodOrder): Boolean {
            return oldItem.orderId == oldItem.orderId && oldItem.orderDetails == oldItem.orderDetails
        }
    }

    fun removeItem(position: Int) {
        foodOrders.removeAt(position)
        notifyDataSetChanged()
    }

    fun updateData(foodOrdersNew: ArrayList<FoodOrder>) {
        foodOrders = foodOrdersNew
        notifyDataSetChanged()
    }

    fun getList(): ArrayList<FoodOrder> {
        return foodOrders
    }
}