package com.metalsensor.gold.detector.finder.ui.frament

import android.app.Fragment
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.metalsensor.gold.detector.finder.adapter.CoinAdapter
import com.metalsensor.gold.detector.finder.base.BaseFragment
import com.metalsensor.gold.detector.finder.databinding.FragmentCoinlibraryBinding
import com.metalsensor.gold.detector.finder.dialog.dialogmang
import com.metalsensor.gold.detector.finder.interfaces.ICoinClick
import com.metalsensor.gold.detector.finder.ui.AboutCoinActivity
import com.metalsensor.gold.detector.finder.utils.AboutCoinViewmodel
import com.metalsensor.gold.detector.finder.utils.CoinViewModel
import com.metalsensor.gold.detector.finder.utils.Const.countries
import com.metalsensor.gold.detector.finder.utils.InternetConnectionChecker
import com.metalsensor.gold.detector.finder.utils.gone
import com.metalsensor.gold.detector.finder.utils.visible
import kotlinx.coroutines.launch


class CoinlibraryFragment : BaseFragment<FragmentCoinlibraryBinding>(), ICoinClick {
    private lateinit var intent: Intent
    private var isClick = true
    private val handel by lazy {
        Handler(Looper.getMainLooper())
    }
    private var a=0
    private lateinit var adapter: CoinAdapter
    private val viewModel: CoinViewModel by viewModels()
    private val aboutcoinViewModel: AboutCoinViewmodel by viewModels()
    private val layoutManager by lazy {
        LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
    private lateinit var internetChecker: InternetConnectionChecker

    private val loadingDialog by lazy {
        dialogmang(requireContext())
    }
    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentCoinlibraryBinding {
        return FragmentCoinlibraryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        internetChecker = InternetConnectionChecker(requireContext())
        // Initialize the adapter
        adapter = CoinAdapter(requireContext(), this)

        // Set up RecyclerView
        binding.rcvCoin.apply {
            adapter = this@CoinlibraryFragment.adapter
            layoutManager = this@CoinlibraryFragment.layoutManager
        }

        observeViewModel()

//        if (viewModel.coinList.value.isNullOrEmpty()) {
//            viewModel.fetchCoinsForCountries(countries)
//        }
        val size = viewModel.coinList.value?.size ?: 0
        Log.d(TAG, "onViewCreated: "+size)
        binding.icsearch.setOnClickListener {
            hideKeyboard()
            val query = binding.edtsearch.text.toString().trim()
            adapter.filterItems(query)
            if (adapter.itemCount > 0) {
                binding.noitem.gone()
                binding.rcvCoin.visible()
            } else {
                binding.rcvCoin.gone()
                binding.noitem.visible()
            }
        }

        binding.edtsearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = charSequence.toString()

            }

            override fun afterTextChanged(editable: Editable?) {
                val query = binding.edtsearch.text.toString().trim()
                adapter.filterItems(query)
                if (adapter.itemCount > 0) {
                    binding.noitem.gone()
                    binding.rcvCoin.visible()
                } else {
                    binding.rcvCoin.gone()
                    binding.noitem.visible()
                }
            }
        })
    }
    private fun observeViewModel() {
        viewModel.coinList.observe(viewLifecycleOwner) { fetchedCoinList ->
            if (!fetchedCoinList.isNullOrEmpty()) {
                adapter.setItems(fetchedCoinList)
            } else {
                lifecycleScope.launch {
                    viewModel.fetchCoinsForCountries(countries)
                    viewModel.coinList.observe(viewLifecycleOwner) { updatedCoinList ->
                        if (!updatedCoinList.isNullOrEmpty()) {
                            adapter.setItems(updatedCoinList)
                        }
                    }
                }
            }
        }
    }

    override fun initView() {

    }

    private fun about() {
        check()
        if (a == 0) {
            isClick = true
            intent = Intent(requireContext(), AboutCoinActivity::class.java)
            startActivity(intent)
            handel.postDelayed({ isClick = true }, 500)
        }else{
            loadingDialog.show()
        }
    }

    private fun check() {
        lifecycleScope.launch {
            internetChecker.connectionState.collect { state ->
                when (state) {
                    is InternetConnectionChecker.ConnectionState.Unknown -> {
                        a = 1
                    }

                    is InternetConnectionChecker.ConnectionState.NoConnection -> {
                        a = 1
                    }

                    is InternetConnectionChecker.ConnectionState.HasConnection -> {
                        a = 1
                    }

                    is InternetConnectionChecker.ConnectionState.HasInternet -> {
                        a = 0
                    }
                    else -> {}
                }
                Log.d("TAG", "check: "+a)
            }
        }

        internetChecker.startPeriodicCheck()
    }
    fun Fragment.hideKeyboard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val view = view ?: requireActivity().currentFocus ?: View(requireContext())
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun isClick(id: Int, name: String, obverse: String, re: String) {
        lifecycleScope.launch {
            aboutcoinViewModel.fetchCoinDetails()
        }
        about()
    }

}
