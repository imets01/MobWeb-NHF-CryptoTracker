package hu.bme.aut.android.cryptotracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.android.cryptotracker.adapter.CryptoItemAdapter
import hu.bme.aut.android.cryptotracker.data.CryptoDataHolder
import hu.bme.aut.android.cryptotracker.databinding.FragmentHomeBinding
import hu.bme.aut.android.cryptotracker.model.CryptoCurrency
import hu.bme.aut.android.cryptotracker.model.Data
import hu.bme.aut.android.cryptotracker.network.CryptoApi
import hu.bme.aut.android.cryptotracker.network.NetworkManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment() : Fragment(), CryptoDataHolder, CryptoItemAdapter.OnCryptoSelectedListener{

    private lateinit var binding: FragmentHomeBinding
    private lateinit var list: List<CryptoCurrency>
    private lateinit var adapter: CryptoItemAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        list = listOf()
        initRecyclerView()

        lifecycleScope.launch(Dispatchers.IO){

            val res = NetworkManager.getInstance().create(CryptoApi::class.java).getData()
            if(res?.body() != null){
                withContext(Dispatchers.Main){
                    list = res.body()!!.data.cryptoCurrencyList
                    adapter.updateData(list)
                }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun initRecyclerView() {
        adapter = CryptoItemAdapter(requireContext(), list)
        binding.mainRecyclerView.layoutManager = LinearLayoutManager(context)
        binding.mainRecyclerView.adapter = adapter

    }

    override fun getCryptoData(): Data? {
        return getCryptoData()
    }

    override fun onCryptoSelected(crypto: String?) {
    }
}
