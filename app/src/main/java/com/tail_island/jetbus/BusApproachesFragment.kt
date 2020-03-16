package com.tail_island.jetbus

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.tail_island.jetbus.databinding.FragmentBusApproachesBinding
import com.tail_island.jetbus.view_model.BusApproachesViewModel
import javax.inject.Inject
import kotlin.concurrent.thread

class BusApproachesFragment: Fragment() {
    @Inject lateinit var viewModelProviderFactory: AppViewModelProvideFactory

    private val viewModel by viewModels<BusApproachesViewModel> { viewModelProviderFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity().application as App).component.inject(this)

        return FragmentBusApproachesBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel      = this@BusApproachesFragment.viewModel

            thread {
                Thread.sleep(5000)

                this@BusApproachesFragment.viewModel.departureBusStopName.postValue("日本ユニシス本社前")
                Log.d("BusApproachesFragment", "MutableLiveData.postValue()")

                Thread.sleep(5000)

                this@BusApproachesFragment.viewModel.departureBusStopName.postValue("深川第八中学校前")
            }
        }.root
    }
}
