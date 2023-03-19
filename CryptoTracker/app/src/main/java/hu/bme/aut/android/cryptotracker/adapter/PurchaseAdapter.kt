package hu.bme.aut.android.cryptotracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import hu.bme.aut.android.cryptotracker.R
import hu.bme.aut.android.cryptotracker.data.CryptoItem
import hu.bme.aut.android.cryptotracker.databinding.PurchaseItemBinding
import hu.bme.aut.android.cryptotracker.model.CryptoCurrency

class PurchaseAdapter(var context: Context, var list: List<CryptoCurrency>) :
    RecyclerView.Adapter<PurchaseAdapter.CryptoViewHolder>() {

    private val purchase = mutableListOf<CryptoItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CryptoViewHolder(
            PurchaseItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    /*override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CryptoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.crypto_item, parent, false)
        return CryptoViewHolder(view)
    }*/

    override fun onBindViewHolder(holder: CryptoViewHolder, position: Int) {
        val purchaseItem = purchase[position]
        var price = 0.0
        var id = 0

        for(freshData in list){
            if(purchaseItem.symbol == freshData.symbol){
                price = freshData.quotes[0].price
                id = freshData.id
            }
        }

        holder.binding.currencyNameTextView.text = purchaseItem.name
        holder.binding.currencySymbolTextView.text = purchaseItem.symbol

        Glide.with(context)
            .load("https://s2.coinmarketcap.com/static/img/coins/64x64/$id.png")
            .into(holder.binding.currencyImageView)


        val priceNow = (price / purchaseItem.priceWhenBought) * purchaseItem.amount
        holder.binding.newPriceTextView.text = "${String.format("$%.02f", priceNow)}"
        if(priceNow > purchaseItem.amount){
            holder.binding.newPriceTextView.setTextColor(context.resources.getColor(R.color.green))
        }
        else {
            holder.binding.newPriceTextView.setTextColor(context.resources.getColor(R.color.red))
        }
        holder.binding.oldPriceTextView.text = "${String.format("$%.02f", purchaseItem.amount)}"
    }

    interface OnCryptoSelectedListener {
        fun onCryptoSelected(crypto: String?)
    }

    fun updateData(dataItem : List<CryptoCurrency>){
        list = dataItem
        notifyDataSetChanged()
    }

    fun update(purchasedItems: List<CryptoItem>) {
        purchase.clear()
        purchase.addAll(purchasedItems)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = purchase.size

    interface CryptoItemClickListener{
        fun onItemChanged(item: CryptoItem)
    }

    inner class CryptoViewHolder(val binding: PurchaseItemBinding) : RecyclerView.ViewHolder(binding.root)

}