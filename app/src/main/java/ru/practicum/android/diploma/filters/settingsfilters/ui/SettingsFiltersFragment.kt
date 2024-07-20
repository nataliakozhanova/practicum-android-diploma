package ru.practicum.android.diploma.filters.settingsfilters.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltersSettingsBinding
import ru.practicum.android.diploma.filters.settingsfilters.presentation.viewmodel.SettingsFiltersViewModel

class SettingsFiltersFragment : Fragment() {

    private var _binding: FragmentFiltersSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SettingsFiltersViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFiltersSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBindings()
        setObserves()
    }

    private fun setObserves() {
        viewModel.observeFilters().observe(viewLifecycleOwner) { state ->
            binding.noSalaryCheckbox.isChecked = state?.checkbox ?: false
        }
    }

    private fun setBindings() {
        binding.placeToWorkCl.setOnClickListener {
            findNavController().navigate(
                R.id.action_filterFragment_to_chooseAreaFragment,
            )
        }
        binding.constraintIndustry.setOnClickListener{
            findNavController().navigate(
                R.id.action_filterFragment_to_chooseIndustryFragment
            )
        }
        binding.noSalaryCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setOnlyWithSalary(isChecked)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
