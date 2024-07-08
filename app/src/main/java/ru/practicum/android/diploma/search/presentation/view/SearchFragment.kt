package ru.practicum.android.diploma.search.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()

    private val vacancySearchAdapter = VacancySearchAdapter() { vacancy -> openVacancy(vacancy) }

    private var currentPage = 0
    private var searchMask: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

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
                    showErrorOrEmptySearch(1)
                }

                is SearchState.Error -> {
                    when (state.errorType) {
                        is ServerInternalError -> {
                            showErrorOrEmptySearch(2)
                        }

                        is BadRequestError -> {
                            showErrorOrEmptySearch(2)
                        }

                        is NoInternetError -> {
                            showErrorOrEmptySearch(3)
                        }
                    }
                }

                else -> {}
            }
        }

        binding.editTextSearch.doOnTextChanged { text, start, before, count ->
            searchMask = text.toString()
            if (searchMask.isNotEmpty()) {
                viewModel.searchDebounce(searchMask, currentPage)
            }
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

    private fun showErrorOrEmptySearch(type: Int) {
        with(binding) {
            searchResultsRV.isVisible = false
            searchProgressBar.isVisible = false
            placeHolderImage.isVisible = true
            placeHolderText.isVisible = true
        }
        when (type) {
            1 -> {
                with(binding) {
                    vacanciesCountText.isVisible = true
                    vacanciesCountText.text = getString(R.string.no_vacancies)
                    placeHolderImage.setImageResource(R.drawable.image_nothing_found)
                    placeHolderText.text = getString(R.string.failed_to_get_vacancies)
                }
            }
            2 -> {
                with(binding) {
                    vacanciesCountText.isVisible = false
                    placeHolderImage.setImageResource(R.drawable.image_search_server_error)
                    placeHolderText.text = getString(R.string.server_error)
                }
            }
            3 -> {
                with(binding) {
                    vacanciesCountText.isVisible = false
                    placeHolderImage.setImageResource(R.drawable.image_no_internet_error)
                    placeHolderText.text = getString(R.string.no_internet)
                }
            }
            else ->{}
        }

    }

    private fun openVacancy(vacancy: VacancyBase) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyDetailsFragment,
            VacancyDetailsFragment.createArgs(vacancy.hhID)
        )
    }

}
