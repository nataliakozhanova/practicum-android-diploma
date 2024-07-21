package ru.practicum.android.diploma.filters.chooseindustry.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentChoosingIndustryBinding
import ru.practicum.android.diploma.filters.chooseindustry.presentation.models.IndustriesStates
import ru.practicum.android.diploma.filters.chooseindustry.presentation.viewmodel.ChooseIndustryViewModel
import ru.practicum.android.diploma.filters.chooseindustry.domain.model.IndustriesModel

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
                    binding.errorPlaceholderIv.isVisible = false
                    binding.errorPlaceholderTv.isVisible = false
                    binding.recyclerView.isVisible = true

                    industriesAdapter.industries.clear()
                    industriesAdapter.industries.addAll(state.industries)
                    industriesAdapter.notifyDataSetChanged()
                }

                is IndustriesStates.Error -> {
                    binding.errorIndustryCl.isVisible = true
                    binding.errorPlaceholderIv.isVisible = true
                    binding.errorPlaceholderTv.isVisible = true
                }

                is IndustriesStates.Empty -> {
                    binding.errorIndustryCl.isVisible = true
                    binding.errorPlaceholderIv.isVisible = true
                    binding.errorPlaceholderTv.isVisible = true
                    binding.errorPlaceholderTv.text = "пустой список"
                }

                is IndustriesStates.Loading -> {
                    binding.industryProgressBar.isVisible = true
                }
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
