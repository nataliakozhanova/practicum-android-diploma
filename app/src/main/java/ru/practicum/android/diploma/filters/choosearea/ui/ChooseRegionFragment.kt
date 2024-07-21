package ru.practicum.android.diploma.filters.choosearea.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentChooosingRegionBinding
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo
import ru.practicum.android.diploma.filters.choosearea.presentation.models.AreasByParentIdState
import ru.practicum.android.diploma.filters.choosearea.presentation.models.AreasWithCountriesState
import ru.practicum.android.diploma.filters.choosearea.presentation.viewmodel.ChooseRegionViewModel
import ru.practicum.android.diploma.util.debounce

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

    private val searchDebounce by lazy {
        debounce<String>(
            delayMillis = 300L,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = true
        ) { query ->
            viewModelChooseRegion.searchAreas(query)
        }
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

                is AreasWithCountriesState.Loading -> {
                    showLoading()
                }

                is AreasWithCountriesState.Error -> {
                    showErrorPlaceholder(R.drawable.image_empty_content, getString(R.string.failed_to_get_list))
                }

                is AreasWithCountriesState.Empty -> {
                    showErrorPlaceholder(R.drawable.image_nothing_found, getString(R.string.no_region))
                }
            }
        }

        viewModelChooseRegion.observeAreasBiIdState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is AreasByParentIdState.Content -> {
                    showContent(state.areas)
                }

                is AreasByParentIdState.Loading -> {
                    showLoading()
                }

                is AreasByParentIdState.Error -> {
                    showErrorPlaceholder(R.drawable.image_empty_content, getString(R.string.failed_to_get_list))
                }

                is AreasByParentIdState.Empty -> {
                    showErrorPlaceholder(R.drawable.image_nothing_found, getString(R.string.no_region))
                }
            }
        }

        countryID = requireArguments().getString(ARGS_COUNTRY_ID)
        if (countryID != null) {
            viewModelChooseRegion.chooseAreasByParentId(countryID!!)
        } else {
            viewModelChooseRegion.chooseOnlyArea()
        }

        binding.arrowBackIv.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tietSearchMask.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // for fu**ing detekt
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce(s.toString())
                if (s.isNullOrEmpty()) {
                    binding.editTextRegion.endIconDrawable =
                        ContextCompat.getDrawable(requireContext(), R.drawable.search_24px_input_edittext_icon)
                } else {
                    binding.editTextRegion.endIconDrawable =
                        ContextCompat.getDrawable(requireContext(), R.drawable.clear_24px_input_edittext_button)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // for fu**ing detekt
            }
        })

        binding.editTextRegion.setEndIconOnClickListener {
            if (!binding.tietSearchMask.text.isNullOrEmpty()) {
                binding.tietSearchMask.text?.clear()
                viewModelChooseRegion.searchAreas("")
            }
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

    private fun showLoading() {
        binding.regionProgressBar.isVisible = true
        binding.errorPlaceholderIv.isVisible = false
        binding.errorPlaceholderTv.isVisible = false
        binding.regionRv.isVisible = false
    }

    private fun showErrorPlaceholder(imageResId: Int, message: String) {
        binding.regionProgressBar.isVisible = false
        binding.errorPlaceholderIv.isVisible = true
        binding.errorPlaceholderTv.isVisible = true
        binding.regionRv.isVisible = false
        binding.errorPlaceholderIv.setImageResource(imageResId)
        binding.errorPlaceholderTv.text = message
    }
}
