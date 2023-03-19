package hu.bme.aut.android.cryptotracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.cryptotracker.adapter.PurchaseAdapter
import hu.bme.aut.android.cryptotracker.data.CryptoListDatabase
import hu.bme.aut.android.cryptotracker.databinding.FragmentThirdBinding
import hu.bme.aut.android.cryptotracker.model.CryptoCurrency
import hu.bme.aut.android.cryptotracker.network.CryptoApi
import hu.bme.aut.android.cryptotracker.network.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.concurrent.thread

class ThirdFragment : Fragment() {

    private lateinit var binding: FragmentThirdBinding
    private lateinit var adapter: PurchaseAdapter
    private lateinit var list: List<CryptoCurrency>

    private lateinit var database: CryptoListDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentThirdBinding.inflate(layoutInflater)
        list = listOf()
        database = CryptoListDatabase.getDatabase(requireActivity().applicationContext)


        lifecycleScope.launch(Dispatchers.IO){

            val res = NetworkManager.getInstance().create(CryptoApi::class.java).getData()
            if(res?.body() != null){
                withContext(Dispatchers.Main){
                    list = res.body()!!.data.cryptoCurrencyList
                    initRecyclerView()
                }
            }
        }
        return binding.root
    }

    private fun initRecyclerView() {
        adapter = PurchaseAdapter(requireContext(), list)
        binding.purchasedRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.purchasedRecyclerView.adapter = adapter
        loadItemsInBackground()
    }

    private fun loadItemsInBackground() {
        thread {
            val items = database.cryptoItemDao().getAll()
            requireActivity().runOnUiThread() {
                adapter.update(items)
            }
        }
    }
}