package ru.practicum.android.diploma.filters.chooseindustry.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.BadRequestError
import ru.practicum.android.diploma.common.domain.ErrorType
import ru.practicum.android.diploma.common.domain.NoInternetError
import ru.practicum.android.diploma.common.domain.ServerInternalError
import ru.practicum.android.diploma.databinding.FragmentChoosingIndustryBinding
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustryNotFoundType
import ru.practicum.android.diploma.filters.chooseindustry.presentation.models.ChosenStates
import ru.practicum.android.diploma.filters.chooseindustry.presentation.models.IndustriesStates
import ru.practicum.android.diploma.filters.chooseindustry.presentation.viewmodel.ChooseIndustryViewModel
import ru.practicum.android.diploma.util.debounce

class ChooseIndustryFragment : Fragment() {
    private var _binding: FragmentChoosingIndustryBinding? = null
    private val binding get() = _binding!!
    private val viewModelChooseIndustry: ChooseIndustryViewModel by viewModel()
    private val industriesAdapter: ChooseIndustryAdapter by lazy {
        ChooseIndustryAdapter { industry: IndustriesModel ->
            viewModelChooseIndustry.selectIndustry(industry)
            industriesAdapter.selectIndustry(industry)
        }
    }

    private val searchDebounce by lazy {
        debounce<String>(
            delayMillis = 300L,
            coroutineScope = viewLifecycleOwner.lifecycleScope,
            useLastParam = true
        ) { query ->
            viewModelChooseIndustry.searchIndustries(query)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChoosingIndustryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = industriesAdapter

        viewModelChooseIndustry.observeIndustryState().observe(viewLifecycleOwner) { state: IndustriesStates ->
            when (state) {
                is IndustriesStates.Content -> {
                    binding.industryProgressBar.isVisible = false
                    binding.errorIndustryCl.isVisible = false
                    binding.errorPlaceholderIv.isVisible = false
                    binding.errorPlaceholderTv.isVisible = false
                    binding.recyclerView.isVisible = true
                    val selectedIndustry = viewModelChooseIndustry.getSelectedIndustry()
                    industriesAdapter.setItems(state.industries, selectedIndustry)
                }

                is IndustriesStates.Error -> {
                    showTypeErrorOrEmpty(state.errorType)
                }

                is IndustriesStates.Empty -> {
                    showTypeErrorOrEmpty(IndustryNotFoundType())
                }

                is IndustriesStates.Loading -> {
                    binding.industryProgressBar.isVisible = true
                    binding.errorPlaceholderIv.isVisible = false
                    binding.errorPlaceholderTv.isVisible = false
                    binding.errorIndustryCl.isVisible = false
                    binding.recyclerView.isVisible = false
                }
            }
        }

        viewModelChooseIndustry.observeIndustryStateChosen().observe(viewLifecycleOwner) { state: ChosenStates ->
            when (state) {
                is ChosenStates.Chosen -> binding.applyBt.isVisible = true
                is ChosenStates.NotChosen -> binding.applyBt.isVisible = false
            }
        }

        viewModelChooseIndustry.getIndustry()

        binding.applyBt.setOnClickListener {
            viewModelChooseIndustry.saveSelectedIndustry()
            findNavController().navigateUp()
        }

        binding.arrowBackIv.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.tietSearchMask.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // не используется
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
                // не используется
            }
        })

        binding.editTextRegion.setEndIconOnClickListener {
            if (!binding.tietSearchMask.text.isNullOrEmpty()) {
                binding.tietSearchMask.text?.clear()
                viewModelChooseIndustry.searchIndustries("")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showTypeErrorOrEmpty(errorType: ErrorType) {
        binding.recyclerView.isVisible = false
        binding.industryProgressBar.isVisible = false
        binding.errorIndustryCl.isVisible = true
        binding.errorPlaceholderIv.isVisible = true
        binding.errorPlaceholderTv.isVisible = true
        when (errorType) {
            is ServerInternalError -> {
                binding.errorPlaceholderIv.setImageResource(R.drawable.image_search_server_error)
                binding.errorPlaceholderTv.setText(R.string.server_error)
            }

            is NoInternetError -> {
                binding.errorPlaceholderIv.setImageResource(R.drawable.image_no_internet_error)
                binding.errorPlaceholderTv.setText(R.string.no_internet)
            }

            is IndustryNotFoundType, is BadRequestError -> {
                binding.errorPlaceholderIv.setImageResource(R.drawable.image_empty_content)
                binding.errorPlaceholderTv.setText(R.string.failed_to_get_list)
            }

            else -> {}
        }
    }
}
