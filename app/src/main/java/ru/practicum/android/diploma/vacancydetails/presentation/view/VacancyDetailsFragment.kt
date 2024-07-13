package ru.practicum.android.diploma.vacancydetails.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.vacancydetails.presentation.models.DetailsState
import ru.practicum.android.diploma.vacancydetails.presentation.viewmodel.DetailsViewModel

class VacancyDetailsFragment : Fragment() {

    private var _binding: FragmentVacancyDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<DetailsViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentVacancyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vacancyId = requireArguments().getString(ARGS_VACANCY_ID)
        if (vacancyId != null) {
            viewModel.getVacancy(vacancyId)
        }
        viewModel.observeVacancyState().observe(viewLifecycleOwner) {state->
            when (state) {
                is DetailsState.Content -> {
                    // showContent
                }
                else -> {}
            }

        }
        // showToast("vacancyId=$vacancyId")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(additionalMessage: String) {
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ARGS_VACANCY_ID = "vacancyID"

        fun createArgs(vacancyID: String): Bundle =
            bundleOf(ARGS_VACANCY_ID to vacancyID)
    }
}
