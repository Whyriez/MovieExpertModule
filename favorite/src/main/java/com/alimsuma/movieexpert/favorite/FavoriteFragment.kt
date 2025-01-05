package com.alimsuma.movieexpert.favorite

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
            val movieAdapter = MovieAdapter()
            movieAdapter.onItemClick = { selectedData ->
                val intent = Intent(activity, DetailMovieActivity::class.java)
                intent.putExtra(DetailMovieActivity.EXTRA_DATA, selectedData)
                startActivity(intent)
            }

            favoriteViewModel.favoriteTourism.observe(viewLifecycleOwner) { dataMovie ->
                movieAdapter.submitList(dataMovie)
                binding.viewEmpty.root.visibility =
                    if (dataMovie.isNotEmpty()) View.GONE else View.VISIBLE
            }

            binding.search.searchView.setupWithSearchBar(binding.search.searchBar)
            binding.search.searchView.editText.setOnEditorActionListener { _, _, _ ->
                val query = binding.search.searchView.text
                binding.search.searchBar.setText(query)
                favoriteViewModel.setSearchQuery(query.toString())
                binding.search.searchView.hide()
                false
            }

            with(binding.rvMovie) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = movieAdapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}