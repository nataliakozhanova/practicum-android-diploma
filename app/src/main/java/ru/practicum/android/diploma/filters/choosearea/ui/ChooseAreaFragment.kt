package ru.practicum.android.diploma.filters.choosearea.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentChoosingPlaceWorkBinding
import ru.practicum.android.diploma.databinding.FragmentFiltersSettingsBinding

class ChooseAreaFragment : Fragment() {

    private var _binding: FragmentChoosingPlaceWorkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentChoosingPlaceWorkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.countryTv.setOnClickListener {
            findNavController().navigate(
                R.id.action_chooseAreaFragment_to_chooseCountryFragment,
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
