package com.tail_island.jetbus

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tail_island.jetbus.adapter.BookmarkAdapter
import com.tail_island.jetbus.databinding.FragmentBookmarksBinding
import com.tail_island.jetbus.view_model.BookmarksViewModel
import javax.inject.Inject

class BookmarksFragment: Fragment() {
    @Inject lateinit var viewModelProviderFactory: AppViewModelProvideFactory

    private val viewModel by viewModels<BookmarksViewModel> { viewModelProviderFactory }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        (requireActivity().application as App).component.inject(this)

        return FragmentBookmarksBinding.inflate(inflater, container, false).apply {
            addButton.setOnClickListener {
                findNavController().navigate(BookmarksFragmentDirections.bookmarksFragmentToDepartureBusStopFragment())
            }

            recyclerView.adapter = BookmarkAdapter().apply {
                viewModel.bookmarks.observe(viewLifecycleOwner, Observer {
                    submitList(it)
                })

                onBookmarkClick = {
                    findNavController().navigate(BookmarksFragmentDirections.bookmarksFragmentToBusApproachesFragment(it.departureBusStopName, it.arrivalBusStopName))
                }
            }
        }.root
    }
}
