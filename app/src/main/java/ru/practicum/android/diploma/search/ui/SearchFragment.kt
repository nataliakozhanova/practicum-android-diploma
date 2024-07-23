package ru.practicum.android.diploma.search.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.domain.ErrorType
import ru.practicum.android.diploma.common.domain.NoInternetError
import ru.practicum.android.diploma.common.domain.VacancyBase
import ru.practicum.android.diploma.common.presentation.EditTextSearchIcon
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.filters.settingsfilters.ui.SettingsFiltersFragment
import ru.practicum.android.diploma.search.domain.models.VacanciesNotFoundType
import ru.practicum.android.diploma.search.presentation.models.SearchState
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchViewModel
import ru.practicum.android.diploma.util.debounce
import ru.practicum.android.diploma.util.getCountableVacancies
import ru.practicum.android.diploma.vacancydetails.ui.VacancyDetailsFragment

class SearchFragment : Fragment() {

    companion object {
        private const val SEARCH_MASK = ""
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 200L
        private const val TOAST_DEBOUNCE_DELAY_MILLIS = 1000L

        private const val RESTART_FLAG = "restartLastSearch"
        private const val SET_SEARCH_MASK = "setSearchMask"

        fun createArgs(restartLastSearch: Boolean, searchMask: String?): Bundle =
            bundleOf(RESTART_FLAG to restartLastSearch, SET_SEARCH_MASK to searchMask)
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()
    private var nextPageRequestSending = false
    private var showToastAllowed = true
    private var onVacancyClickDebounce: (VacancyBase) -> Unit = { _ -> }
    private val vacancySearchAdapter = VacancyAdapter { vacancy -> onVacancyClickDebounce(vacancy) }
    private var searchMask = SEARCH_MASK

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchResultsRV.adapter = vacancySearchAdapter
        onVacancyClickDebounce = debounce<VacancyBase>(
            CLICK_DEBOUNCE_DELAY_MILLIS,
            viewLifecycleOwner.lifecycleScope,
            false
        ) { track ->
            openVacancy(track)
        }

        // подпишемся на результаты поиска первой страницы
        viewModel.observeState().observe(viewLifecycleOwner) { state ->
            searchStateCheck(state)
        }
        // подпишемся на результаты поиска следующих страниц
        viewModel.observeNextPageState().observe(viewLifecycleOwner) { state ->
            nextPageStateCheck(state)
        }
        // подпишемся на тост
        viewModel.observeToast().observe(viewLifecycleOwner) {
            showToast(it)
        }
        // настроим слежение за объектами фрагмента
        setBindings()

        // маска может прийти аргументом
        val setSearchMask = arguments?.getString(SET_SEARCH_MASK)
        if (setSearchMask != null) {
            searchMask = setSearchMask
            binding.editTextSearchLayout.editText?.setText(searchMask)
        }

        // перезапускаем поиск, если пришел флаг
        val restartLastSearch = arguments?.getBoolean(RESTART_FLAG) ?: false
        if (restartLastSearch && searchMask.isNotEmpty()) {
            viewModel.searchByClick(searchMask)
        }
    }

    // обработка состояний поиска первой страницы
    private fun searchStateCheck(state: SearchState) {
        // Log.d("mine", "State = ${state.javaClass}")
        when (state) {
            SearchState.Default -> showStartPage()

            is SearchState.Loading -> {
                showLoading()
            }

            is SearchState.Content -> {
                showContent(state)
                nextPageRequestSending = false
            }

            is SearchState.Empty -> {
                showErrorOrEmptySearch(VacanciesNotFoundType())
                nextPageRequestSending = false
            }

            is SearchState.Error -> {
                showErrorOrEmptySearch(state.errorType)
                nextPageRequestSending = false
            }

            else -> {}
        }
    }

    // обработка состояний поиска следующих страниц
    private fun nextPageStateCheck(state: SearchState) {
        // Log.d("mine", "nextState = ${state.javaClass}")
        when (state) {
            is SearchState.Default -> {
                showNextPagePreloader(false)
                nextPageRequestSending = false
            }

            is SearchState.AtBottom -> {
                showNextPagePreloader(false, getString(R.string.bottom_of_list))
            }

            is SearchState.Loading -> {}

            is SearchState.Content -> {
                showNextPagePreloader(false)
                loadVacancies(state.vacancies)
                nextPageRequestSending = false
            }

            is SearchState.Empty -> {
                showNextPagePreloader(false)
                showErrorOrEmptySearch(VacanciesNotFoundType())
                nextPageRequestSending = false
            }

            is SearchState.Error -> {
                val errorMessage = getErrorMessage(state.errorType)
                showNextPagePreloader(false, errorMessage)
                nextPageRequestSending = false
            }
        }
    }

    // изменения в поле поиска
    private fun bindEditSearch() {
        // следим за изменением в поисковой строке
        binding.editTextSearchLayout.editText?.doOnTextChanged { text, _, _, _ ->
            searchMask = text.toString().trim()
            // иконка в поле поиска
            setEditEndIcon()
            if (searchMask.trim().isEmpty()) {
                viewModel.clearSearch()
                showStartPage()
                showNextPagePreloader(false)
                nextPageRequestSending = true
            } // без условия hasFocus срабатывает при возврате на фрагмент
            else if (binding.editTextSearchLayout.hasFocus()) {
                viewModel.initSearch()
                // запуск поискового запроса
                viewModel.searchDebounce(searchMask)
            }
        }

        // очистка поискового запроса кнопкой
        binding.editTextSearchLayout.setEndIconOnClickListener {
            searchMask = ""
            // иконка в поле поиска
            binding.editTextSearchLayout.endIconDrawable =
                ContextCompat.getDrawable(requireContext(), EditTextSearchIcon.SEARCH_ICON.drawableId)
            binding.editTextSearchLayout.editText?.setText(searchMask)
            viewModel.clearSearch()
            showNextPagePreloader(false)
            nextPageRequestSending = true
        }

        binding.editTextSearchLayout.editText?.setOnEditorActionListener { _, actionId, _ ->
            // поиск по нажатию Done на клавиатуре
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.deletePreviousFilters()
                viewModel.searchByClick(binding.tietSearchMask.text.toString())
            }
            false
        }
    }

    private fun setEditEndIcon() {
        binding.editTextSearchLayout.endIconDrawable = ContextCompat.getDrawable(
            requireContext(),
            if (searchMask.isEmpty()) {
                EditTextSearchIcon.SEARCH_ICON.drawableId
            } else {
                EditTextSearchIcon.CLEAR_ICON.drawableId
            }
        )
    }

    // настроим слежку за изменениями во фрагменте
    @SuppressLint("ClickableViewAccessibility")
    private fun setBindings() {
        // обработка изменений в строке поиска
        bindEditSearch()
        // мониторим скроллинг списка вакансий для загрузки новой страницы
        binding.searchResultsRV.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && !nextPageRequestSending) {
                    // отправим запрос следующей страницы
                    loadNextPage()
                    // прокрутим адаптер вниз, иначе лоадер закрывает последнюю вакансию текущей страницы
                    binding.searchResultsRV.smoothScrollToPosition(vacancySearchAdapter.vacancies.size)
                }
            }
        })
        // иконка кнопки фильтров
        binding.buttonFilters.setImageResource(viewModel.filtersOn().drawableId)
        // переход в фильтры
        bindOpenFilters()
    }

    private fun bindOpenFilters() {
        binding.buttonFilters.setOnClickListener {
            findNavController().navigate(
                R.id.action_searchFragment_to_filterFragment,
                SettingsFiltersFragment.createArgs(searchMask)
            )
        }
    }

    private fun loadVacancies(vacancies: MutableList<VacancyBase>) {
        vacancySearchAdapter.vacancies.clear()
        vacancySearchAdapter.vacancies.addAll(vacancies)
        vacancySearchAdapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // переключить видимость прелоадера следующей страницы и может показать тост
    private fun showNextPagePreloader(show: Boolean, message: String? = null) {
        binding.searchNewItemsProgressBar.isVisible = show
        if (show) {
            binding.searchResultsRV.scrollTo(0, binding.searchResultsRV.bottom)
        }
        if (message != null) {
            showToast(message)
        }
    }

    // показать прелоадер первой страницы
    private fun showLoading() {
        with(binding) {
            searchProgressBar.isVisible = true
            placeHolderImage.isVisible = false
            placeHolderText.isVisible = false
            searchResultsRV.isVisible = false
            vacanciesCountText.isVisible = false
        }
        hideKeyboard()
    }

    private fun showStartPage() {
        with(binding) {
            placeHolderImage.isVisible = true
            placeHolderImage.setImageResource(R.drawable.image_search_empty)
            searchProgressBar.isVisible = false
            placeHolderText.isVisible = false
            searchResultsRV.isVisible = false
            vacanciesCountText.isVisible = false
        }
    }

    private fun showContent(state: SearchState.Content) {
        with(binding) {
            searchProgressBar.isVisible = false
            vacanciesCountText.isVisible = true
            vacanciesCountText.text = getCountableVacancies(state.found, resources)
            placeHolderImage.isVisible = false
            placeHolderText.isVisible = false
            searchResultsRV.isVisible = true
        }
        loadVacancies(state.vacancies)
    }

    private fun loadNextPage() {
        if (!nextPageRequestSending) {
            showNextPagePreloader(true)
            nextPageRequestSending = true
            viewModel.nextPageSearch()
        }
    }

    private fun getErrorMessage(type: ErrorType, isNextPage: Boolean = false): String {
        return if (isNextPage) {
            when (type) {
                is NoInternetError -> getString(R.string.no_internet_next_page)
                else -> getString(R.string.server_error_next_page)
            }
        } else {
            when (type) {
                is VacanciesNotFoundType -> getString(R.string.no_vacancies)
                is NoInternetError -> getString(R.string.no_internet)
                else -> getString(R.string.server_error)
            }
        }
    }

    private fun showErrorOrEmptySearch(type: ErrorType) {
        hideKeyboard()
        when (type) {
            is VacanciesNotFoundType -> {
                with(binding) {
                    vacanciesCountText.isVisible = true
                    vacanciesCountText.text = getString(R.string.no_vacancies)
                    placeHolderImage.setImageResource(R.drawable.image_nothing_found)
                }
            }

            is NoInternetError -> {
                with(binding) {
                    vacanciesCountText.isVisible = false
                    placeHolderImage.setImageResource(R.drawable.image_no_internet_error)
                }
            }

            else -> {
                with(binding) {
                    vacanciesCountText.isVisible = false
                    placeHolderImage.setImageResource(R.drawable.image_search_server_error)
                }
            }
        }
        with(binding) {
            placeHolderText.text = getErrorMessage(type)
            searchResultsRV.isVisible = false
            searchProgressBar.isVisible = false
            placeHolderImage.isVisible = true
            placeHolderText.isVisible = true
        }
    }

    override fun onResume() {
        super.onResume()
        // Log.d("mine", "Resume ($searchMask)")
    }

    private fun openVacancy(vacancy: VacancyBase) {
        findNavController().navigate(
            R.id.action_searchFragment_to_vacancyDetailsFragment,
            VacancyDetailsFragment.createArgs(vacancy.hhID)
        )
    }

    private fun showToastDebounce(): Boolean {
        val current = showToastAllowed
        if (showToastAllowed) {
            showToastAllowed = false
            lifecycleScope.launch {
                delay(TOAST_DEBOUNCE_DELAY_MILLIS)
                showToastAllowed = true
            }
        }
        return current
    }

    private fun showToast(tostMessage: String) {
        if (showToastDebounce()) {
            Toast.makeText(requireContext(), tostMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun hideKeyboard() {
        val view: View? = activity?.currentFocus
        if (view != null) {
            val inputMethodManager =
                activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}
