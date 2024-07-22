package ru.practicum.android.diploma.filters.choosearea.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
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

        renderAreaSettings()

        binding.countryCl.setOnClickListener {
            findNavController().navigate(
                R.id.action_chooseAreaFragment_to_chooseCountryFragment
            )
        }

        binding.regionCl.setOnClickListener {
            findNavController().navigate(
                R.id.action_chooseAreaFragment_to_chooseRegionFragment,
                ChooseRegionFragment.createArgs(areaSettings?.countryInfo?.id)
            )
        }

        binding.countryArrowAndCleanIv.setOnClickListener {
            if (checkIvImage(binding.countryArrowAndCleanIv, R.drawable.clear_24px_input_edittext_button)) {
                viewModelChooseArea.deleteCountrySettings()
                areaSettings = null
                hideRegion()
                hideCountry()
            } else {
                findNavController().navigate(
                    R.id.action_chooseAreaFragment_to_chooseCountryFragment
                )
            }
        }

        binding.regionArrowAndCleanIv.setOnClickListener {
            if (checkIvImage(binding.regionArrowAndCleanIv, R.drawable.clear_24px_input_edittext_button)) {
                viewModelChooseArea.saveAreaSettings(AreaInfo("", "", areaSettings!!.countryInfo))
                hideRegion()
            } else {
                findNavController().navigate(
                    R.id.action_chooseAreaFragment_to_chooseRegionFragment,
                    ChooseRegionFragment.createArgs(areaSettings?.countryInfo?.id)
                )
            }
        }

        binding.applyBt.setOnClickListener {
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

    private fun renderAreaSettings() {
        areaSettings = viewModelChooseArea.getAreaSettings()
        if (areaSettings != null) {
            showCountry()
            if (areaSettings!!.name.isNotEmpty()) {
                showRegion()
            } else {
                hideRegion()
            }
        } else {
            hideCountry()
        }
    }

    private fun showCountry() {
        with(binding) {
            countryTil.isVisible = true
            countryTv.isVisible = false
            countryDarkTv.isVisible = true
            countryDarkTv.text = areaSettings!!.countryInfo.name
            applyBt.isVisible = true
            countryArrowAndCleanIv.setImageResource(R.drawable.clear_24px_input_edittext_button)
        }
    }

    private fun hideCountry() {
        with(binding) {
            countryTv.isVisible = true
            countryDarkTv.isVisible = false
            countryTil.isVisible = false
            applyBt.isVisible = false
            countryArrowAndCleanIv.setImageResource(R.drawable.arrow_forward_24px_button)
        }
    }

    private fun showRegion() {
        with(binding) {
            regionTil.isVisible = true
            regionTv.isVisible = false
            regionDarkTv.isVisible = true
            regionDarkTv.text = areaSettings!!.name
            regionArrowAndCleanIv.setImageResource(R.drawable.clear_24px_input_edittext_button)
        }
    }

    private fun hideRegion() {
        with(binding) {
            regionTv.isVisible = true
            regionDarkTv.isVisible = false
            regionTil.isVisible = false
            regionArrowAndCleanIv.setImageResource(R.drawable.arrow_forward_24px_button)
        }
    }

    private fun checkIvImage(imageView: ImageView, imageResourceId: Int): Boolean {
        return imageView.drawable?.let {
            return (it.constantState == ContextCompat.getDrawable(imageView.context, imageResourceId)?.constantState)
        } ?: false
    }
}
