package hu.bme.aut.android.cryptotracker.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import hu.bme.aut.android.cryptotracker.databinding.DialogPurchaseBinding

class NewPurchaseDialogFragment(private var listener: NewPurchaseItemDialogListener) : DialogFragment(){
    interface NewPurchaseItemDialogListener {
        fun onPurchaseItemCreated(newItem: Double)
    }

    private lateinit var binding: DialogPurchaseBinding

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogPurchaseBinding.inflate(LayoutInflater.from(context))

        return AlertDialog.Builder(requireContext())
            .setTitle("the purchased amount (currency: USD)")
            .setView(binding.root)
            .setPositiveButton("Ok") { _, _ ->
                if (isValid()) {
                    listener.onPurchaseItemCreated(getPurchaseItem())
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
    }

    companion object {
        const val TAG = "NewPurchaseItemDialogFragment"
    }

    private fun isValid() = binding.amountText.text.isNotEmpty()

    private fun getPurchaseItem() :Double{
        return binding.amountText.text.toString().toDouble()
    }

}