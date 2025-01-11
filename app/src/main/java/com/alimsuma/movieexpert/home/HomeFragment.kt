package com.alimsuma.movieexpert.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.alimsuma.core.core.data.source.Resource
import com.alimsuma.core.core.ui.MovieAdapter
import com.alimsuma.movieexpert.databinding.FragmentHomeBinding
import com.alimsuma.movieexpert.detail.DetailMovieActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var searchEditTextListener: TextView.OnEditorActionListener? = null
    private var tourismAdapter: MovieAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activity != null) {

            tourismAdapter = MovieAdapter().apply {
                onItemClick = { selectedData ->
                    val intent = Intent(requireActivity(), DetailMovieActivity::class.java)
                    intent.putExtra(DetailMovieActivity.EXTRA_DATA, selectedData)
                    startActivity(intent)
                }
            }

            homeViewModel.movies.observe(viewLifecycleOwner) { movie ->
                if (movie != null) {
                    when (movie) {
                        is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE
                        is Resource.Success -> {
                            binding.progressBar.visibility = View.GONE

                            tourismAdapter?.submitList(movie.data)
                        }
                        is Resource.Error -> {
                            binding.progressBar.visibility = View.GONE
                            binding.viewError.root.visibility = View.VISIBLE
                            binding.viewError.tvError.text =
                                movie.message ?: "Eror Brok"
                        }
                    }
                }
            }

            searchEditTextListener = TextView.OnEditorActionListener { _, _, _ ->
                val query = binding.search.searchView.text
                binding.search.searchBar.setText(query)
                homeViewModel.setSearchQuery(query.toString())
                binding.search.searchView.hide()
                false
            }

            binding.search.searchView.setupWithSearchBar(binding.search.searchBar)
            binding.search.searchView.editText.setOnEditorActionListener(searchEditTextListener)

            with(binding.rvTourism) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = tourismAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvTourism.adapter = null
        tourismAdapter?.onItemClick = null
        tourismAdapter = null
        binding.search.searchView.editText.setOnEditorActionListener(null)
        searchEditTextListener = null

        _binding = null
    }
}
