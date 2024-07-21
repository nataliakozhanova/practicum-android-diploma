package ru.practicum.android.diploma.filters.choosearea.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentChoosingPlaceWorkBinding
import ru.practicum.android.diploma.filters.choosearea.domain.models.AreaInfo
import ru.practicum.android.diploma.filters.choosearea.presentation.viewmodel.ChooseAreaViewModel

class ChooseAreaFragment : Fragment() {

    private var _binding: FragmentChoosingPlaceWorkBinding? = null
    private val binding get() = _binding!!
    private val viewModelChooseArea: ChooseAreaViewModel by viewModel()
    private var areaSettings: AreaInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentChoosingPlaceWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelChooseArea.areaSettings.observe(viewLifecycleOwner) { area ->
            renderAreaSettings()
        }

        binding.countryEd.setOnClickListener {
            findNavController().navigate(
                R.id.action_chooseAreaFragment_to_chooseCountryFragment
            )
        }

        binding.regionEd.setOnClickListener {
            findNavController().navigate(
                R.id.action_chooseAreaFragment_to_chooseRegionFragment,
                ChooseRegionFragment.createArgs(areaSettings?.countryInfo?.id)
            )
        }

        binding.countryArrowAndCleanIv.setOnClickListener {
            viewModelChooseArea.deleteCountrySettings()
            with(binding) {
                areaSettings = null
                countryArrowAndCleanIv.setImageResource(R.drawable.arrow_forward_24px_button)
                regionArrowAndCleanIv.setImageResource(R.drawable.arrow_forward_24px_button)
                countryEd.text = null
                regionEd.text = null
                applyBt.isVisible = false
            }
        }
        binding.regionArrowAndCleanIv.setOnClickListener {
            viewModelChooseArea.saveAreaSettings(AreaInfo("", "", areaSettings!!.countryInfo))
            with(binding) {
                regionArrowAndCleanIv.setImageResource(R.drawable.arrow_forward_24px_button)
                regionEd.text = null
            }
        }

        binding.applyBt.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.arrowBackIv.setOnClickListener {
            viewModelChooseArea.deleteCountrySettings()
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun renderAreaSettings() {
        areaSettings = viewModelChooseArea.getAreaSettings()
        if (areaSettings != null) {
            with(binding) {
                countryEd.setText(areaSettings!!.countryInfo.name)
                applyBt.isVisible = true
                countryArrowAndCleanIv.isVisible = true
                countryArrowAndCleanIv.setImageResource(R.drawable.clear_24px_input_edittext_button)
            }
            if (areaSettings!!.name.isNotEmpty()) {
                with(binding) {
                    regionEd.setText(areaSettings!!.name)
                    regionArrowAndCleanIv.isVisible = true
                    regionArrowAndCleanIv.setImageResource(R.drawable.clear_24px_input_edittext_button)
                }

            }
        }
    }
}
