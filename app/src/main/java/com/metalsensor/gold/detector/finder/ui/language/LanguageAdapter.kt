package com.metalsensor.gold.detector.finder.ui.language

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.metalsensor.gold.detector.finder.R
import com.metalsensor.gold.detector.finder.databinding.ItemLanguageBinding
import com.metalsensor.gold.detector.finder.model.LanguageModel
import com.metalsensor.gold.detector.finder.utils.Const.positionLanguageOld

class LanguageAdapter(var context : Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var onClick:((position : Int)->Unit)? = null
    var data = listOf<LanguageModel>()
    fun getData(mdata: List<LanguageModel>) {
        data = mdata
    }
var a=false
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var binding =
            ItemLanguageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(position)
            holder.binding.ctlItemLanguage.setOnClickListener {
                a=false
                onClick!!.invoke(position)
                if (data[0].active) {
                    data[0].active = false
                    notifyItemChanged(0)
                }
                data[positionLanguageOld].active = false
                notifyItemChanged(positionLanguageOld)
                positionLanguageOld = position
                data[position].active = true
                notifyItemChanged(position)
                notifyDataSetChanged()
            }

        }
    }
    inner class ViewHolder(val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val textColor = ContextCompat.getColor(context, R.color.black)
            val textColor_2 = ContextCompat.getColor(context, R.color.black)

            if (a){
                Log.d("TAG", "bind: ")
                if(position==3){
                    binding.ctlItemLanguage.setBackgroundResource(R.drawable.iclanguage)
                    binding.tvLanguage.setTextColor(textColor_2)
                    binding.imvPickOn.visibility = View.VISIBLE
                    binding.imvPickOff.visibility = View.GONE
                }else{
                    binding.tvLanguage.setTextColor(textColor)
                    binding.tvLanguage.setTypeface(null, Typeface.NORMAL)
                    binding.ctlItemLanguage.setBackgroundResource(R.drawable.bg_language_8dp)
                    binding.imvPickOn.visibility = View.GONE
                    binding.imvPickOff.visibility = View.VISIBLE
                }
            }else{
                if(data[position].active){
                    binding.ctlItemLanguage.setBackgroundResource(R.drawable.iclanguage)
                    binding.tvLanguage.setTextColor(textColor_2)
                    binding.imvPickOn.visibility = View.VISIBLE
                    binding.imvPickOff.visibility = View.GONE
                }else{
                    binding.tvLanguage.setTextColor(textColor)
                    binding.tvLanguage.setTypeface(null, Typeface.NORMAL)
                    binding.ctlItemLanguage.setBackgroundResource(R.drawable.bg_language_8dp)
                    binding.imvPickOn.visibility = View.GONE
                    binding.imvPickOff.visibility = View.VISIBLE
                }
            }
            binding.tvLanguage.text = data[position].name
            Glide.with(binding.imvIconFlag).load(data[position].icon)
                .into(binding.imvIconFlag)
        }
    }
}