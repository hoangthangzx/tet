package com.metalsensor.gold.detector.finder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.metalsensor.gold.detector.finder.databinding.ItemCoinBinding
import com.metalsensor.gold.detector.finder.interfaces.ICoinClick
import com.metalsensor.gold.detector.finder.model.Coin
import com.metalsensor.gold.detector.finder.utils.Const

class CoinAdapter(
    private val context: Context,
    private val listener: ICoinClick,
) : RecyclerView.Adapter<CoinAdapter.ViewHolder>() {

    private val items = mutableListOf<Coin>()
    private val allItems = mutableListOf<Coin>()
    private var showShimmerForAll = false

    private val shimmerDrawable by lazy {
        ShimmerDrawable().apply {
            val shimmer = Shimmer.AlphaHighlightBuilder()
                .setDuration(800)
                .setBaseAlpha(0.9f)
                .setHighlightAlpha(1f)
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build()
            setShimmer(shimmer)
        }
    }
    fun setItems(newItems: List<Coin>) {
        items.clear()
        items.addAll(newItems)
        allItems.clear()
        allItems.addAll(newItems)
        notifyDataSetChanged()
    }
    fun filterItems(query: String) {
        items.clear()
        if (query.isEmpty()) {
            items.addAll(allItems) // Reset to full list
        } else {
            items.addAll(allItems.filter { it.name!!.contains(query, ignoreCase = true) })
        }
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemCoinBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Coin) {
            binding.apply {
                if (showShimmerForAll || item.obverseImage.isNullOrEmpty()) {
                    // Show shimmer if loading or image URL is null
                    imvcoin.setImageDrawable(shimmerDrawable)
                    tvname.text = ""
                } else {
                    // Load actual data
                    Glide.with(context)
                        .load(item.obverseImage)
                        .placeholder(shimmerDrawable)
                        .into(imvcoin)
                    tvname.text = item.name
                }

                root.setOnClickListener {
                    if (!showShimmerForAll) {
                        item.name?.let { it1 -> item.obverseImage?.let { it2 ->
                            Const.Title=item.name
                            Const.id=item.id
                            Const.url=item.obverseImage
                            Const.url2= item.reverseImage.toString()
                            item.reverseImage?.let { it3 ->
                                listener.isClick(item.id, it1,it2,
                                    it3
                                )
                            }
                        } }
                    }
                }
            }
            binding.tvname.isSelected = true
        }

    }
    fun showShimmerEffect(show: Boolean) {
        showShimmerForAll = show
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCoinBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
    fun getCurrentItems(): List<Coin> {
        return items
    }

}

