package ru.practicum.android.diploma.search.presentation.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.BadRequestError
import ru.practicum.android.diploma.common.domain.NoInternetError
import ru.practicum.android.diploma.common.domain.ServerInternalError
import ru.practicum.android.diploma.common.domain.VacancyBase
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.presentation.models.SearchState
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchViewModel
import ru.practicum.android.diploma.util.getCountableVacancies
import ru.practicum.android.diploma.vacancydetails.presentation.view.VacancyDetailsFragment

class SearchFragment : Fragment() {

    companion object {
        private const val EMPTY = "empty"
        private const val SERVER_ERROR = "server_error"
        private const val NO_INTERNET = "no_internet"
        private const val SEARCH_MASK = ""
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()

    private val vacancySearchAdapter = VacancySearchAdapter { vacancy -> openVacancy(vacancy) }

    private var currentPage = 0
    private var searchMask = SEARCH_MASK

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchResultsRV.adapter = vacancySearchAdapter

        showEmptySearch()

        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchState.Content -> {
                    showContent(state.vacancies)
                }

                is SearchState.Loading -> {
                    showLoading()
                }

                is SearchState.Empty -> {
                    showErrorOrEmptySearch(EMPTY)
                }

                is SearchState.Error -> {
                    when (state.errorType) {
                        is ServerInternalError -> {
                            showErrorOrEmptySearch(SERVER_ERROR)
                        }

                        is BadRequestError -> {
                            showErrorOrEmptySearch(SERVER_ERROR)
                        }

                        is NoInternetError -> {
                            showErrorOrEmptySearch(NO_INTERNET)
                        }
                    }
                }

                else -> {}
            }
        }

        binding.editTextSearch.doOnTextChanged { text, start, before, count ->
            searchMask = text.toString()
            if (searchMask.isNotEmpty()) {
                changeDrawableClearText(binding.editTextSearch)
                viewModel.searchDebounce(searchMask, currentPage)
            }
        }

        binding.editTextSearch.setOnTouchListener { v, event ->
            v.performClick()
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.editTextSearch.right - binding.editTextSearch.compoundDrawables[2].bounds.width())) {
                    changeDrawableSearchIcon(binding.editTextSearch)
                    binding.editTextSearch.setText(SEARCH_MASK)
                    showEmptySearch()
                    return@setOnTouchListener true
                }
            }
            false
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading() {
        with(binding) {
            searchProgressBar.isVisible = true
            placeHolderImage.isVisible = false
            placeHolderText.isVisible = false
            searchResultsRV.isVisible = false
            vacanciesCountText.isVisible = false
        }
    }

    private fun showEmptySearch() {
        with(binding) {
            placeHolderImage.isVisible = true
            placeHolderImage.setImageResource(R.drawable.image_search_empty)
            placeHolderText.isVisible = false
            searchResultsRV.isVisible = false
            vacanciesCountText.isVisible = false
        }
        vacancySearchAdapter.vacancies.clear()
    }

    private fun showContent(vacancies: MutableList<VacancyBase>) {
        with(binding) {
            searchProgressBar.isVisible = false
            vacanciesCountText.isVisible = true
            vacanciesCountText.text = getCountableVacancies(vacancies.size, resources)
            placeHolderImage.isVisible = false
            placeHolderText.isVisible = false
            searchResultsRV.isVisible = true
        }
        vacancySearchAdapter.vacancies.clear()
        vacancySearchAdapter.vacancies.addAll(vacancies)
        vacancySearchAdapter.notifyDataSetChanged()
    }

    private fun showErrorOrEmptySearch(type: String) {
        with(binding) {
            searchResultsRV.isVisible = false
            searchProgressBar.isVisible = false
            placeHolderImage.isVisible = true
            placeHolderText.isVisible = true
        }
        when (type) {
            EMPTY -> {
                with(binding) {
                    vacanciesCountText.isVisible = true
                    vacanciesCountText.text = getString(R.string.no_vacancies)
                    placeHolderImage.setImageResource(R.drawable.image_nothing_found)
                    placeHolderText.text = getString(R.string.failed_to_get_vacancies)
                }
            }

            SERVER_ERROR -> {
                with(binding) {
                    vacanciesCountText.isVisible = false
                    placeHolderImage.setImageResource(R.drawable.image_search_server_error)
                    placeHolderText.text = getString(R.string.server_error)
                }
            }

            NO_INTERNET -> {
                with(binding) {
                    vacanciesCountText.isVisible = false
                    placeHolderImage.setImageResource(R.drawable.image_no_internet_error)
                    placeHolderText.text = getString(R.string.no_internet)
                }
            }

            else -> {}
        }

    }

    private fun changeDrawableClearText(editText: EditText) {
        // Меняем иконку на другую
        val newIcon = ContextCompat.getDrawable(requireContext(), R.drawable.clear_24px_input_edittext_button)
        newIcon?.setBounds(0, 0, newIcon.intrinsicWidth, newIcon.intrinsicHeight)
        editText.setCompoundDrawables(null, null, newIcon, null) // Устанавливаем новую иконку справа
    }

    private fun changeDrawableSearchIcon(editText: EditText) {
        // Меняем иконку на другую
        val newIcon = ContextCompat.getDrawable(requireContext(), R.drawable.search_24px_input_edittext_icon)
        newIcon?.setBounds(0, 0, newIcon.intrinsicWidth, newIcon.intrinsicHeight)
        editText.setCompoundDrawables(null, null, newIcon, null) // Устанавливаем новую иконку справа
    }

    private fun openVacancy(vacancy: VacancyBase) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyDetailsFragment,
            VacancyDetailsFragment.createArgs(vacancy.hhID)
        )
    }

}
