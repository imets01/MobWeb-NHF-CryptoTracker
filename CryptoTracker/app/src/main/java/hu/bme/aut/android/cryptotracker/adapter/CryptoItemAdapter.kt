package hu.bme.aut.android.cryptotracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hu.bme.aut.android.cryptotracker.R
import hu.bme.aut.android.cryptotracker.data.CryptoItem
import hu.bme.aut.android.cryptotracker.databinding.CryptoItemBinding
import hu.bme.aut.android.cryptotracker.fragments.HomeFragmentDirections
import hu.bme.aut.android.cryptotracker.model.CryptoCurrency

class CryptoItemAdapter(var context: Context, var list: List<CryptoCurrency>) :
    RecyclerView.Adapter<CryptoItemAdapter.CryptoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.crypto_item, parent, false)
        return CryptoViewHolder(view)
    }

    interface OnCryptoSelectedListener {
        fun onCryptoSelected(crypto: String?)
    }

    fun updateData(dataItem : List<CryptoCurrency>){
        list = dataItem
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val item = list[position]
        //holder.bind(item.symbol)
        holder.binding.currencyNameTextView.text = item.name
        holder.binding.currencySymbolTextView.text = item.symbol

        Glide.with(context)
            .load("https://s2.coinmarketcap.com/static/img/coins/64x64/"+item.id +".png")
            .into(holder.binding.currencyImageView)

/*        Glide.with(context)
            .load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/"+item.id +".png")
            .into(holder.binding.currencyChartImageView)*/

        holder.binding.currencyPriceTextView.text = "${String.format("$%.02f", item.quotes[0].price)}"

        if(item.quotes[0].percentChange24h > 0){
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.green))
            holder.binding.currencyChangeTextView.text = "+${String.format("%.05f", item.quotes[0].percentChange24h)} %"
        }
        else{
            holder.binding.currencyChangeTextView.setTextColor(context.resources.getColor(R.color.red))
            holder.binding.currencyChangeTextView.text = " ${String.format("%.05f", item.quotes[0].percentChange24h)} %"
        }

        holder.itemView.setOnClickListener{
            findNavController(it).navigate(
                HomeFragmentDirections.actionHomeFragmentToDetailFragment(item)
            )
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface CryptoItemClickListener{
        fun onItemChanged(item: CryptoItem)
    }


    inner class CryptoViewHolder(private val view: View) : RecyclerView.ViewHolder(view){
        var binding = CryptoItemBinding.bind(view)
    }

}