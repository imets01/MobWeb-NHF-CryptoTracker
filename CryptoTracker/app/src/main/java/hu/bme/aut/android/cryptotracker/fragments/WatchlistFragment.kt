package hu.bme.aut.android.cryptotracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import hu.bme.aut.android.cryptotracker.adapter.CryptoItemAdapter
import hu.bme.aut.android.cryptotracker.databinding.FragmentWatchlistBinding
import hu.bme.aut.android.cryptotracker.model.CryptoCurrency
import hu.bme.aut.android.cryptotracker.network.CryptoApi
import hu.bme.aut.android.cryptotracker.network.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class WatchlistFragment : Fragment(){

    private lateinit var binding : FragmentWatchlistBinding
    private lateinit var watchList : ArrayList<String>
    private lateinit var watchListItem : ArrayList<CryptoCurrency>
    private lateinit var adapter: CryptoItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWatchlistBinding.inflate(layoutInflater)
        readData()

        lifecycleScope.launch(Dispatchers.IO){
            val res = NetworkManager.getInstance().create(CryptoApi::class.java)
                .getData()
            if(res.body() != null){
                withContext(Dispatchers.Main){
                    watchListItem = ArrayList()
                    watchListItem.clear()

                    for(watchData in watchList){
                        for(item in res.body()!!.data.cryptoCurrencyList){
                            if(watchData == item.symbol){
                                watchListItem.add(item)
                            }
                        }
                    }
                    adapter = CryptoItemAdapter(requireContext(), watchListItem)
                    binding.watchlistRecyclerView.layoutManager = LinearLayoutManager(context)
                    binding.watchlistRecyclerView.adapter = adapter
                }
            }
        }

        return binding.root
    }

    private fun readData() {
        val sharedPreferences = requireContext().getSharedPreferences("watchlist", Context.MODE_PRIVATE)
        val gson = Gson()
        val json = sharedPreferences.getString("watchlist", ArrayList<String>().toString())
        val type = object : TypeToken<ArrayList<String>>(){}.type
        watchList = gson.fromJson(json, type)
    }
}