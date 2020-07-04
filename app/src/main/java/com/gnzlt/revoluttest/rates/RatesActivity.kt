package com.gnzlt.revoluttest.rates

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.gnzlt.revoluttest.R
import com.gnzlt.revoluttest.RevolutApp
import com.gnzlt.revoluttest.data.model.RateItem
import com.gnzlt.revoluttest.databinding.ActivityRatesBinding
import com.gnzlt.revoluttest.util.LifecycleAwareTimer
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class RatesActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var binding: ActivityRatesBinding

    private val ratesViewModel: RatesViewModel by viewModels {
        viewModelFactory
    }
    private val timer: LifecycleAwareTimer by lazy {
        LifecycleAwareTimer(::onTimerTick)
    }
    private val ratesAdapter: RatesAdapter by lazy {
        RatesAdapter(::onItemEdited, ::onItemClicked)
    }
    private val adapterObserver: RecyclerView.AdapterDataObserver by lazy {
        getAdapterDataObserver()
    }
    private val errorSnackbar: Snackbar by lazy {
        Snackbar.make(
            binding.root,
            "There was a problem loading rates...",
            Snackbar.LENGTH_INDEFINITE
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as RevolutApp).appComponent.inject(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_rates)

        setupView()
        setupObservers()
    }

    private fun setupView() {
        binding.listRates.apply {
            setHasFixedSize(true)
            (itemAnimator as SimpleItemAnimator?)?.supportsChangeAnimations = false
            layoutManager = LinearLayoutManager(this@RatesActivity).apply {
                reverseLayout = true
            }
            adapter = ratesAdapter.apply {
                registerAdapterDataObserver(adapterObserver)
            }
        }
    }

    private fun setupObservers() {
        lifecycle.addObserver(timer)
        ratesViewModel.onRates().observe(this, ::onRates)
        ratesViewModel.onError().observe(this, ::onError)
    }

    override fun onDestroy() {
        lifecycle.removeObserver(timer)
        ratesAdapter.unregisterAdapterDataObserver(adapterObserver)
        super.onDestroy()
    }

    private fun onTimerTick() {
        ratesViewModel.updateRates()
    }

    private fun onRates(list: List<RateItem>) {
        ratesAdapter.submitList(list)
    }

    private fun onError(showError: Boolean) {
        if (showError) {
            if (!errorSnackbar.isShownOrQueued) {
                errorSnackbar.show()
            }
        } else {
            if (errorSnackbar.isShownOrQueued) {
                errorSnackbar.dismiss()
            }
        }
    }

    private fun onItemEdited(item: RateItem) {
        ratesViewModel.setBaseValue(item.value)
    }

    private fun onItemClicked(item: RateItem) {
        ratesViewModel.setBaseRate(item)
    }

    private fun getAdapterDataObserver(): RecyclerView.AdapterDataObserver =
        object : RecyclerView.AdapterDataObserver() {

            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                if (positionStart == 0 && itemCount > 0) {
                    binding.listRates.scrollToPosition(itemCount - 1)
                }
                super.onItemRangeInserted(positionStart, itemCount)
            }

            override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
                val lastPosition = ratesAdapter.itemCount - 1
                if (fromPosition == lastPosition || toPosition == lastPosition) {
                    binding.listRates.smoothScrollToPosition(lastPosition)
                }
                super.onItemRangeMoved(fromPosition, toPosition, itemCount)
            }
        }
}
