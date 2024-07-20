package ru.practicum.android.diploma.filters.choosearea.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentChooosingRegionBinding
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo
import ru.practicum.android.diploma.filters.choosearea.presentation.models.AreasByParentIdState
import ru.practicum.android.diploma.filters.choosearea.presentation.models.AreasWithCountriesState
import ru.practicum.android.diploma.filters.choosearea.presentation.viewmodel.ChooseRegionViewModel

class ChooseRegionFragment : Fragment() {

    companion object {
        private const val ARGS_COUNTRY_ID = "countryID"

        fun createArgs(countryID: String?): Bundle =
            bundleOf(ARGS_COUNTRY_ID to countryID)
    }

    private var _binding: FragmentChooosingRegionBinding? = null
    private val binding get() = _binding!!
    private val viewModelChooseRegion: ChooseRegionViewModel by viewModel()
    private var countryID: String? = null
    private val regionsAdapter = RegionsAdapter { region ->
        viewModelChooseRegion.saveAreaWithCountrySettings(region)
        findNavController().navigateUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentChooosingRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.regionRv.adapter = regionsAdapter

        viewModelChooseRegion.observeAreasWithCountriesState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is AreasWithCountriesState.Content -> {
                    showContent(state.areasWithCountry)
                }

                is AreasWithCountriesState.Error -> {
                    // showTypeErrorOrEmpty(state.errorType) - дописать
                }

                is AreasWithCountriesState.Empty -> {
                    // showTypeErrorOrEmpty(AreasNotFoundType()) - дописать
                }

                is AreasWithCountriesState.Loading -> {
                    binding.regionProgressBar.isVisible = true
                }
            }
        }

        viewModelChooseRegion.observeAreasBiIdState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is AreasByParentIdState.Content -> {
                    showContent(state.areas)
                }

                is AreasByParentIdState.Error -> {
                    // showTypeErrorOrEmpty(state.errorType) - дописать
                }

                is AreasByParentIdState.Empty -> {
                    // showTypeErrorOrEmpty(AreasNotFoundType()) - дописать
                }

                is AreasByParentIdState.Loading -> {
                    binding.regionProgressBar.isVisible = true
                }

            }

        }

        countryID = requireArguments().getString(ARGS_COUNTRY_ID)
        if (countryID != null) {
            viewModelChooseRegion.chooseAreasByParentId(countryID!!)
        } else {
            viewModelChooseRegion.chooseOnlyArea()

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showContent(areas: List<AreaInfo>) {
        binding.regionProgressBar.isVisible = false
        binding.errorPlaceholderIv.isVisible = false
        binding.errorPlaceholderTv.isVisible = false
        binding.regionRv.isVisible = true
        regionsAdapter.areas.clear()
        regionsAdapter.areas.addAll(areas)
        regionsAdapter.notifyDataSetChanged()
    }
}
