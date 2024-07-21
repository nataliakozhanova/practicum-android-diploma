package ru.practicum.android.diploma.filters.choosearea.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentChoosingCountryBinding
import ru.practicum.android.diploma.filters.choosearea.presentation.models.CountriesState
import ru.practicum.android.diploma.filters.choosearea.presentation.viewmodel.ChooseCountryViewModel

class ChooseCountryFragment : Fragment() {

    private var _binding: FragmentChoosingCountryBinding? = null
    private val binding get() = _binding!!
    private val viewModelChooseCountry: ChooseCountryViewModel by viewModel()
    private val countriesAdapter = CountriesAdapter { country ->
        viewModelChooseCountry.saveCountrySettings(country)
        findNavController().navigateUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentChoosingCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.countryRv.adapter = countriesAdapter

        viewModelChooseCountry.observeCountriesState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is CountriesState.Content -> {
                    binding.countriesProgressBar.isVisible = false
                    binding.errorPlaceholderIv.isVisible = false
                    binding.errorPlaceholderTv.isVisible = false
                    binding.countryRv.isVisible = true

                    countriesAdapter.countries.clear()
                    countriesAdapter.countries.addAll(state.areas)
                    countriesAdapter.notifyDataSetChanged()
                }

                is CountriesState.Error -> {
                    // showTypeErrorOrEmpty(state.errorType) - дописать
                }

                is CountriesState.Empty -> {
                    // showTypeErrorOrEmpty(AreasNotFoundType()) - дописать
                }

                is CountriesState.Loading -> {
                    binding.countriesProgressBar.isVisible = true
                }

            }

        }

        viewModelChooseCountry.chooseCountry()

        binding.arrowBackIv.setOnClickListener{
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
