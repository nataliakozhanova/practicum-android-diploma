package ru.practicum.android.diploma.filters.settingsfilters.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.FiltersAll
import ru.practicum.android.diploma.common.presentation.FilterArrow
import ru.practicum.android.diploma.databinding.FragmentFiltersSettingsBinding
import ru.practicum.android.diploma.filters.settingsfilters.domain.models.SalaryFilters
import ru.practicum.android.diploma.filters.settingsfilters.presentation.viewmodel.SettingsFiltersViewModel
import ru.practicum.android.diploma.search.ui.SearchFragment

class SettingsFiltersFragment : Fragment() {

    private var _binding: FragmentFiltersSettingsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SettingsFiltersViewModel>()
    private var originalSalaryFilters: SalaryFilters? = null
    private var originalFilters: FiltersAll? = null
    private var lastSearchMask: String? = null

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            myOnBackPressed()
        }
    }

    companion object {
        private const val LAST_SEARCH_MASK = "lastSearchMask"
        fun createArgs(lastSearchMask: String?): Bundle = bundleOf(LAST_SEARCH_MASK to lastSearchMask)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        _binding = FragmentFiltersSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setBindings()
        renderSavedAreaSettings()
        renderSavedSalarySettings()
        renderSavedIndustrySettings()
        originalSalaryFilters = viewModel.getSalaryFilters()
        originalFilters = viewModel.getOriginalFilters()
        lastSearchMask = arguments?.getString(LAST_SEARCH_MASK)
        updateButtonsVisibility()
    }

    private fun openAreaSettings() {
        findNavController().navigate(
            R.id.action_filterFragment_to_chooseAreaFragment,
        )
    }

    private fun openIndustrySettings() {
        findNavController().navigate(
            R.id.action_filterFragment_to_chooseIndustryFragment,
        )
    }

    private fun onClickFilterArrows() {
        binding.filterArrowForward1.setOnClickListener {
            if (binding.filterArrowForward1.tag == FilterArrow.FORWARD.drawableId) {
                openAreaSettings()
            } else {
                clearAreaSettings()
            }
        }
        binding.filterArrowForward2.setOnClickListener {
            if (binding.filterArrowForward2.tag == FilterArrow.FORWARD.drawableId) {
                openIndustrySettings()
            } else {
                clearIndustrySettings()
            }
        }
    }

    private fun setBindings() {
        binding.placeToWorkCl.setOnClickListener {
            openAreaSettings()
        }
        binding.constraintIndustry.setOnClickListener {
            openIndustrySettings()
        }

        binding.noSalaryCheckbox.setOnClickListener {
            viewModel.setOnlyWithSalary(binding.noSalaryCheckbox.isChecked)
            updateButtonsVisibility()
        }

        onClickFilterArrows()

        // How about this, Nikita?
        binding.salaryLayout.editText?.doOnTextChanged { text, _, _, _ ->
            updateButtonsVisibility()
            changeTextInputLayoutEndIconMode(text)
        }

        binding.salaryLayout.setEndIconOnClickListener {
            binding.salaryTextInput.text?.clear()
        }
        binding.applyButton.setOnClickListener {
            applyFiltersAndNavigate()
        }

        binding.resetButton.setOnClickListener {
            resetFilters()
        }
        updateButtonsVisibility()
        myOnBackArrowPressed()
    }

    private fun myOnBackArrowPressed() {
        binding.arrowBackIv.setOnClickListener {
            viewModel.savePreviousFilters()
            findNavController().popBackStack()
            //findNavController().navigateUp()
        }
    }

    private fun myOnBackPressed() {
        viewModel.savePreviousFilters()
        callback.isEnabled = false
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    private fun updateButtonsVisibility() {
        // показать кнопку сброс, если что-то заполнено
        val isSalaryEntered = binding.salaryTextInput.text?.isNotEmpty() == true
        val isNoSalaryChecked = binding.noSalaryCheckbox.isChecked
        val isAreaSet = viewModel.getAreaSettings() != null
        val isIndustrySet = viewModel.getIndustrySettings() != null
        binding.resetButton.isVisible = isSalaryEntered || isNoSalaryChecked || isAreaSet || isIndustrySet
        // есть предыдущие фильтры
        val hasPreviousFilters = viewModel.hasPreviousFilters()
        // показать кнопку применить если что-то поменялось (или есть предыдущие фильтры)
        val salaryInputValue = binding.salaryTextInput.text.toString()
        val originalSalaryValue = originalFilters?.salary?.salary ?: ""
        val isSalaryChanged = salaryInputValue != originalSalaryValue
        val isNoSalaryChanged = binding.noSalaryCheckbox.isChecked != originalFilters?.salary?.checkbox
        val isAreaChanged = viewModel.getAreaSettings()?.id != originalFilters?.area?.id
        val isIndustryChanged = viewModel.getIndustrySettings()?.id != originalFilters?.industry?.id
        binding.applyButton.isVisible = isSalaryChanged || isNoSalaryChanged || isAreaChanged
            || isIndustryChanged || hasPreviousFilters
    }

    private fun changeTextInputLayoutEndIconMode(text: CharSequence?) {
        with(binding) {
            if (text.isNullOrEmpty()) {
                viewModel.clearSalary()
                salaryLayout.isEndIconVisible = false
            } else {
                viewModel.saveSalary(text.toString())
                binding.salaryLayout.endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                binding.salaryLayout.endIconDrawable =
                    AppCompatResources.getDrawable(requireContext(), FilterArrow.CLEAR.drawableId)
                salaryLayout.isEndIconVisible = true
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
                filterArrowForward1.setImageResource(FilterArrow.CLEAR.drawableId)
                // чтобы определить потом реакцию на нажатие этого imageView
                filterArrowForward1.tag = FilterArrow.CLEAR.drawableId
            }
        } else {
            with(binding) {
                placeToWork.isVisible = true
                placeToWorkDark.isVisible = false
                placeToWorkTv.isVisible = false
                filterArrowForward1.setImageResource(FilterArrow.FORWARD.drawableId)
                // чтобы определить потом реакцию на нажатие этого imageView
                filterArrowForward1.tag = FilterArrow.FORWARD.drawableId
            }
        }
    }

    private fun renderSavedIndustrySettings() {
        val industrySettings = viewModel.getIndustrySettings()
        if (industrySettings != null) {
            with(binding) {
                industryFilterSetting.isVisible = false
                industryDark.isVisible = true
                industryBigDark.isVisible = true
                industryBigDark.text = industrySettings.name
                filterArrowForward2.setImageResource(FilterArrow.CLEAR.drawableId)
                // чтобы определить потом реакцию на нажатие этого imageView
                filterArrowForward2.tag = FilterArrow.CLEAR.drawableId
            }
        } else {
            with(binding) {
                industryFilterSetting.isVisible = true
                industryDark.isVisible = false
                industryBigDark.isVisible = false
                filterArrowForward2.setImageResource(FilterArrow.FORWARD.drawableId)
                // чтобы определить потом реакцию на нажатие этого imageView
                filterArrowForward2.tag = FilterArrow.FORWARD.drawableId
            }
        }
    }

    private fun renderSavedSalarySettings() {
        val salaryFilters = viewModel.getSalaryFilters()
        if (salaryFilters != null) {
            binding.salaryTextInput.setText(salaryFilters.salary)
            binding.salaryLayout.editText?.requestFocus()
            binding.noSalaryCheckbox.isChecked = salaryFilters.checkbox
        }
    }

    private fun clearAreaSettings() {
        viewModel.clearAreaSettings()
        renderSavedAreaSettings()
        updateButtonsVisibility()
    }

    private fun clearIndustrySettings() {
        viewModel.clearIndustrySettings()
        renderSavedIndustrySettings()
        updateButtonsVisibility()
    }

    private fun applyFiltersAndNavigate() {
        // Сохранение всех текущих настроек фильтра
        viewModel.applyFilters()
        // Обновление оригинальных фильтров
        originalSalaryFilters = viewModel.getSalaryFilters()
        updateButtonsVisibility()
        // стираем предыдущие фильтры, чтобы применились новые
        viewModel.deletePreviousFilters()
        // Переход на SearchFragment с примененными фильтрами
        findNavController().navigate(
            R.id.action_filterFragment_to_searchFragment,
            SearchFragment.createArgs(true, lastSearchMask)
        )
    }

    private fun resetFilters() {
        // Сброс всех фильтров
        viewModel.resetFilters()
        viewModel.deletePreviousFilters()
        renderSavedAreaSettings()
        renderSavedIndustrySettings()
        renderSavedSalarySettings()
        binding.salaryTextInput.text?.clear()
        binding.noSalaryCheckbox.isChecked = false
        updateButtonsVisibility()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
