package com.alimsuma.movieexpert.favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.alimsuma.core.core.ui.MovieAdapter
import com.alimsuma.movieexpert.detail.DetailMovieActivity
import com.alimsuma.movieexpert.di.FavoriteComponentDependencies
import com.alimsuma.movieexpert.favorite.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var favoriteViewModel: FavoriteViewModel
    private var searchEditTextListener: TextView.OnEditorActionListener? = null
    private var movieAdapter: MovieAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dependencies = DynamicFeatureModule.getDependencies(
            requireContext(),
            FavoriteComponentDependencies::class.java
        )

        favoriteViewModel = ViewModelProvider(
            this,
            FavoriteViewModel.provideFactory(dependencies.movieUseCase())
        )[FavoriteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (activity != null) {
            movieAdapter = MovieAdapter().apply {
                onItemClick = { selectedData ->
                    val intent = Intent(requireActivity(), DetailMovieActivity::class.java)
                    intent.putExtra(DetailMovieActivity.EXTRA_DATA, selectedData)
                    startActivity(intent)
                }
            }

            favoriteViewModel.favoriteTourism.observe(viewLifecycleOwner) { dataMovie ->
                movieAdapter?.submitList(dataMovie)
                binding.viewEmpty.root.visibility =
                    if (dataMovie.isNotEmpty()) View.GONE else View.VISIBLE
            }

            searchEditTextListener = TextView.OnEditorActionListener { _, _, _ ->
                val query = binding.search.searchView.text
                binding.search.searchBar.setText(query)
                favoriteViewModel.setSearchQuery(query.toString())
                binding.search.searchView.hide()
                false
            }

            binding.search.searchView.setupWithSearchBar(binding.search.searchBar)
            binding.search.searchView.editText.setOnEditorActionListener(searchEditTextListener)

            with(binding.rvMovie) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = movieAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rvMovie.adapter = null
        movieAdapter?.onItemClick = null
        movieAdapter = null
        binding.search.searchView.editText.setOnEditorActionListener(null)
        searchEditTextListener = null

        _binding = null
    }
}