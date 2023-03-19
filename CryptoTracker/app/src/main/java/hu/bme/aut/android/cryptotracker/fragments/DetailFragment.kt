package hu.bme.aut.android.cryptotracker.fragments

import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hu.bme.aut.android.cryptotracker.R
import hu.bme.aut.android.cryptotracker.data.CryptoItem
import hu.bme.aut.android.cryptotracker.data.CryptoListDatabase
import hu.bme.aut.android.cryptotracker.databinding.FragmentDetailBinding
import hu.bme.aut.android.cryptotracker.model.CryptoCurrency
import kotlin.concurrent.thread

class DetailFragment: Fragment(), NewPurchaseDialogFragment.NewPurchaseItemDialogListener{


   lateinit var binding: FragmentDetailBinding
    private val item: DetailFragmentArgs by navArgs()

    private lateinit var database: CryptoListDatabase

    companion object {
        private const val TAG = "DetailFragment"
        const val EXTRA_CRYPTO_NAME = "extra.crypto_name"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentDetailBinding.inflate(layoutInflater)
        val crypto : CryptoCurrency = item.data!!

        database = CryptoListDatabase.getDatabase(requireContext())

        binding.extendedFab.setOnClickListener{
            NewPurchaseDialogFragment(this).show(
                requireActivity().supportFragmentManager,
                NewPurchaseDialogFragment.TAG
            )
        }

        setCryptoData(crypto)
        addToWatchListCheck(crypto)
        return binding.root
    }

    override fun onPurchaseItemCreated(amount: Double) {
        val crypto : CryptoCurrency = item.data!!
        var purchaseItem = CryptoItem(
            name = crypto.name,
            symbol = crypto.symbol,
            priceWhenBought = crypto.quotes[0].price,
            amount = amount
        )

        thread {
            val insertId = database.cryptoItemDao().insert(purchaseItem)
            purchaseItem.id = insertId
        }
    }

    var watchList : ArrayList<String>? = null
    var watchListIsChecked = false

    private fun setCryptoData(crypto: CryptoCurrency){
        binding.detailSymbolTextView.text = crypto.symbol
        binding.detailPriceTextView.text = "${String.format("$%.4f", crypto.quotes[0].price)}"

        Glide.with(requireContext())
            .load("https://s2.coinmarketcap.com/static/img/coins/64x64/"+crypto.id +".png")
            .into(binding.detailImageView)

        if(crypto.quotes[0].percentChange24h > 0){
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.green))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_up_arrow)
            binding.detailChangeTextView.text = "+${String.format("%.05f", crypto.quotes[0].percentChange24h)} %"
        }
        else{
            binding.detailChangeImageView.setImageResource(R.drawable.ic_down_arrow)
            binding.detailChangeTextView.setTextColor(requireContext().resources.getColor(R.color.red))
            binding.detailChangeTextView.text = " ${String.format("%.05f", crypto.quotes[0].percentChange24h)} %"
        }
        binding.tvDetailTitle.text = crypto.name
        binding.volumeTv.text = crypto.quotes[0].volume24h.toString()
        binding.percentage1hTv.text = crypto.quotes[0].percentChange1h.toString()
        binding.percentage24hTv.text = crypto.quotes[0].percentChange24h.toString()
        binding.percentage7dTv.text = crypto.quotes[0].percentChange7d.toString()
        binding.percentage30dTv.text = crypto.quotes[0].percentChange30d.toString()
        binding.marketCapTv.text = crypto.quotes[0].marketCap.toString()
        binding.marketCapByTotalSupplyTv.text = crypto.quotes[0].marketCapByTotalSupply.toString()
    }

    private fun addToWatchListCheck(crypto: CryptoCurrency){
        loadWatchList()
        watchListIsChecked =
            if(watchList!!.contains(crypto.symbol)){
                binding.addWatchlistButton.setImageResource(R.drawable.ic_checked)
                true
            }
            else{
                binding.addWatchlistButton.setImageResource(R.drawable.ic_unchecked)
                false
            }
        binding.addWatchlistButton.setOnClickListener{
            watchListIsChecked =
                if(!watchListIsChecked){
                    if(!watchList!!.contains(crypto.symbol)){
                        watchList!!.add(crypto.symbol)
                    }
                    saveWatchList()
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_checked)
                    Snackbar.make(binding.root, "Added to watchlist", Snackbar.LENGTH_LONG).show()
                    true
                }else{
                    binding.addWatchlistButton.setImageResource(R.drawable.ic_unchecked)
                    watchList!!.remove(crypto.symbol)
                    saveWatchList()
                    Snackbar.make(binding.root, "Removed from watchlist", Snackbar.LENGTH_LONG).show()
                    false
                }
        }
    }

    private fun saveWatchList(){
        val sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val json = gson.toJson(watchList)
        editor.putString("watchlist", json)
        editor.apply()
    }

    private fun loadWatchList(){
        val sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("watchlist", ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>(){}.type
        watchList = gson.fromJson(json, type)
    }
}

