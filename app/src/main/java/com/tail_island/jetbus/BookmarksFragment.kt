package com.tail_island.jetbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.tail_island.jetbus.databinding.FragmentBookmarksBinding

class BookmarksFragment: Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return FragmentBookmarksBinding.inflate(inflater, container, false).apply {
            departureBusStopButton.setOnClickListener {
                findNavController().navigate(BookmarksFragmentDirections.bookmarksFragmentToDepartureBusStopFragment())
            }

            busApproachesButton.setOnClickListener {
                findNavController().navigate(BookmarksFragmentDirections.bookmarksFragmentToBusApproachesFragment("日本ユニシス本社前", "深川第八中学校前"))
            }
        }.root
    }
}
