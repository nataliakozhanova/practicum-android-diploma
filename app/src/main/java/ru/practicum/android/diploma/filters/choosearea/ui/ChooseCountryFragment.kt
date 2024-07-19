package ru.practicum.android.diploma.filters.choosearea.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentChoosingCountryBinding
import ru.practicum.android.diploma.filters.choosearea.presentation.models.CountriesState
import ru.practicum.android.diploma.filters.choosearea.presentation.viewmodel.ChooseAreaViewModel

class ChooseCountryFragment : Fragment() {

    private var _binding: FragmentChoosingCountryBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<ChooseAreaViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChoosingCountryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.chooseCountry()

        viewModel.observeCountriesState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is CountriesState.Content -> {
                    for (c in state.areas) {
                        binding.debugCountries.append("${c.name} (${c.id})\n")
                    }
                }
                else -> {}
            }
        }

        binding.arrowBackIv.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
