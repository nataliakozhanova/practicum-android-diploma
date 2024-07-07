package ru.practicum.android.diploma.search.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.common.domain.BadRequestError
import ru.practicum.android.diploma.common.domain.NoInternetError
import ru.practicum.android.diploma.common.domain.ServerInternalError
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()
    // private var currentPage = 0
    // private var searchMask: String = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.observeToast().observe(viewLifecycleOwner) {
            showToast(it)
        }

        /*viewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchState.Content -> {
                    showContent(state.vacancies)
                }

                is SearchState.Loading -> {
                    showLoading()
                }

                is SearchState.Empty -> {}
                is SearchState.Error -> {
                    when (state.errorType) {
                        is ServerInternalError -> {
                            TODO("Дописать визуалку Ошибка сервера")
                        }

                        is BadRequestError -> {
                            TODO("Дописать визуалку Ошибка сервера")
                        }

                        is NoInternetError -> {
                            TODO("Дописать визуалку Нет интернета")
                        }
                    }
                }
            }
        }*/

        /*binding.etSearchExpression.doOnTextChanged { text, start, before, count ->
            searchMask = text.toString()
            if (searchMask.isNotEmpty()) {
                viewModel.searchDebounce(searchMask, currentPage)
            }
        }*/
    }

    /*private fun showLoading() {
        binding.tvSearchResults.isVisible = false
        binding.searchVacancyView.isVisible = true
    }*/

    /*private fun showContent(vacancies: List<Vacancy>) {
        binding.tvSearchResults.isVisible = true
        binding.searchVacancyView.isVisible = false
        binding.tvSearchResults.text = "${objectToStringWithLineBreaks(vacancies[0])}"
    }*/

    /*private fun objectToStringWithLineBreaks(obj: Any): String {
        val stringBuilder = StringBuilder()

        obj.javaClass.declaredFields.forEach { field ->
            field.isAccessible = true
            val value = field.get(obj)
            stringBuilder.append("${field.name}: $value\n")
        }

        return stringBuilder.toString()
    }*/
    private fun showToast(additionalMessage: String) {
        Toast.makeText(requireContext(), additionalMessage, Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
