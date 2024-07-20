package ru.practicum.android.diploma.filters.settingsfilters.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentFiltersSettingsBinding
import ru.practicum.android.diploma.filters.settingsfilters.domain.models.SalaryFilters
import ru.practicum.android.diploma.filters.settingsfilters.presentation.viewmodel.SettingsFiltersViewModel

class SettingsFiltersFragment : Fragment() {

    private var _binding: FragmentFiltersSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SettingsFiltersViewModel>()
    private var originalFilters: SalaryFilters? = null

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
        renderSavedAreaSettings()
        /*renderSavedIndustrySettings()*/
        changeTextInputLayoutEndIconMode(binding.industryTextInput.text)
        originalFilters = viewModel.getSalaryFilters()
        updateButtonsVisibility()
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
        binding.constraintIndustry.setOnClickListener {
            findNavController().navigate(
                R.id.action_filterFragment_to_chooseIndustryFragment,
            )
        }
        binding.noSalaryCheckbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setOnlyWithSalary(isChecked)
        }

        binding.filterArrowForward1.setOnClickListener {
            clearAreaSettings()
        }

        /*binding.filterArrowForward2.setOnClickListener {
            clearIndustrySettings()
        }*/

        binding.industryTextInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                updateButtonsVisibility()
                changeTextInputLayoutEndIconMode(s)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.industryLayout.setEndIconOnClickListener {
            binding.industryTextInput.text?.clear()
            binding.industryLayout.endIconDrawable =
                ContextCompat.getDrawable(requireContext(), R.drawable.clear_24px_input_edittext_button)
        }
        binding.applyButton.setOnClickListener {
            applyFiltersAndNavigate()
        }

        binding.resetButton.setOnClickListener {
            resetFilters()
        }
        updateButtonsVisibility()
    }

    private fun updateButtonsVisibility() {
        val isSalaryEntered = binding.industryTextInput.text?.isNotEmpty() == true
        val isNoSalaryChecked = binding.noSalaryCheckbox.isChecked
        val isAreaSet = viewModel.getAreaSettings() != null
        // val isIndustrySet = viewModel.getIndustrySettings() != null

        binding.applyButton.isVisible = isSalaryEntered || isNoSalaryChecked || isAreaSet
        binding.resetButton.isVisible = isSalaryEntered || isNoSalaryChecked || isAreaSet
    }

    private fun changeTextInputLayoutEndIconMode(text: CharSequence?) {
        with(binding) {
            if (text.isNullOrEmpty()) {
                viewModel.clearSalary()
                industryLayout.endIconMode = TextInputLayout.END_ICON_NONE
            } else {
                viewModel.saveSalary(text.toString())
                industryLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                industryLayout.endIconDrawable =
                    AppCompatResources.getDrawable(requireContext(), R.drawable.clear_24px_input_edittext_button)
            }
        }
    }

    private fun renderSavedAreaSettings() {
        val areaSettings = viewModel.getAreaSettings()
        if (areaSettings != null) {
            with(binding) {
                placeToWork.isVisible = false
                placeToWorkDark.isVisible = true
                placeToWorkTv.isVisible = true
                placeToWorkTv.text = if (areaSettings.name.isNotEmpty()) {
                    "${areaSettings.countryInfo.name}, ${areaSettings.name}"
                } else {
                    areaSettings.countryInfo.name
                }
                filterArrowForward1.setImageResource(R.drawable.clear_24px_input_edittext_button)
            }
        } else {
            with(binding) {
                placeToWork.isVisible = true
                placeToWorkDark.isVisible = false
                placeToWorkTv.isVisible = false
                filterArrowForward1.setImageResource(R.drawable.arrow_forward_24px_button)
            }
        }
    }

    /*private fun renderSavedIndustrySettings() {
        val industrySettings = viewModel.getIndustrySettings()
        if (industrySettings != null) {
            with(binding) {
                industryFilterSetting.isVisible = false
                industryDark.isVisible = true
                industryBigDark.isVisible = true
                industryBigDark.text = industrySettings.name
                filterArrowForward2.setImageResource(R.drawable.clear_24px_input_edittext_button)
            }
        } else {
            with(binding) {
                industryFilterSetting.isVisible = true
                industryDark.isVisible = false
                industryBigDark.isVisible = false
                filterArrowForward2.setImageResource(R.drawable.arrow_forward_24px_button)
            }
        }
    }*/

    private fun clearAreaSettings() {
        viewModel.clearAreaSettings()
        renderSavedAreaSettings()
        updateButtonsVisibility()
    }

    /*private fun clearIndustrySettings() {
        viewModel.clearIndustrySettings()
        renderSavedIndustrySettings()
    }*/

    private fun applyFiltersAndNavigate() {
        // Сохранение всех текущих настроек фильтра
        viewModel.applyFilters()
        // Обновление оригинальных фильтров
        originalFilters = viewModel.getSalaryFilters()
        updateButtonsVisibility()

        // Переход на SearchFragment с примененными фильтрами
        findNavController().navigate(R.id.action_filterFragment_to_searchFragment)
    }

    private fun resetFilters() {
        // Сброс всех фильтров
        viewModel.resetFilters()
        renderSavedAreaSettings()
        // renderSavedIndustrySettings()
        binding.industryTextInput.text?.clear()
        binding.noSalaryCheckbox.isChecked = false
        updateButtonsVisibility()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
