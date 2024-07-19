package ru.practicum.android.diploma.filters.choosearea.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentChoosingPlaceWorkBinding

class ChooseAreaFragment : Fragment() {

    private var _binding: FragmentChoosingPlaceWorkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentChoosingPlaceWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.countryName.setEndIconOnClickListener {
            findNavController().navigate(
                R.id.action_chooseAreaFragment_to_chooseCountryFragment
            )
        }
        binding.areaName.setEndIconOnClickListener {
            findNavController().navigate(
                R.id.action_chooseAreaFragment_to_chooseRegionFragment
            )
        }
        binding.arrowBackIv.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
