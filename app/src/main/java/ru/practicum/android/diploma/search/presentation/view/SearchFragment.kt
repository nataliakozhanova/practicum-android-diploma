package ru.practicum.android.diploma.search.presentation.view

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
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.common.data.NoInternetError
import ru.practicum.android.diploma.common.domain.VacancyBase
import ru.practicum.android.diploma.common.presentation.EditTextSearchIcon
import ru.practicum.android.diploma.databinding.FragmentSearchBinding
import ru.practicum.android.diploma.search.domain.models.VacanciesNotFoundType
import ru.practicum.android.diploma.search.presentation.models.SearchState
import ru.practicum.android.diploma.search.presentation.viewmodel.SearchViewModel
import ru.practicum.android.diploma.util.debounce
import ru.practicum.android.diploma.util.getCountableVacancies
import ru.practicum.android.diploma.vacancydetails.presentation.view.VacancyDetailsFragment

class SearchFragment : Fragment() {

    companion object {
        private const val SEARCH_MASK = ""
        private const val CLICK_DEBOUNCE_DELAY_MILLIS = 200L
        private const val TOAST_DEBOUNCE_DELAY_MILLIS = 1000L
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<SearchViewModel>()
    private var nextPageRequestSending = false
    private var showToastAllowed = true
    private var onVacancyClickDebounce: (VacancyBase) -> Unit = { _ -> }
    private val vacancySearchAdapter = VacancySearchAdapter { vacancy -> onVacancyClickDebounce(vacancy) }
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
    }

    // обработка состояний поиска первой страницы
    private fun searchStateCheck(state: SearchState) {
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
        when (state) {
            SearchState.Default -> {
                nextPagePreloaderToggle(false)
                nextPageRequestSending = false
            }

            is SearchState.AtBottom -> {
                nextPagePreloaderToggle(false, getString(R.string.bottom_of_list))
                // чтобы снова не сработало событие "прокрутка до конца" уменьшим скролл
                binding.idNestedSV.scrollTo(0, binding.idNestedSV.scrollY - 1)
            }

            is SearchState.Loading -> {}

            is SearchState.Content -> {
                nextPagePreloaderToggle(false)
                loadVacancies(state.vacancies)
                nextPageRequestSending = false
            }

            is SearchState.Empty -> {
                nextPagePreloaderToggle(false)
                showErrorOrEmptySearch(VacanciesNotFoundType())
                nextPageRequestSending = false
            }

            is SearchState.Error -> {
                val errorMessage = getErrorMessage(state.errorType)
                nextPagePreloaderToggle(false, errorMessage)
                nextPageRequestSending = false
            }
        }
    }

    // изменения в поле поиска
    private fun bindEditSearch() {
        // следим за изменением в поисковой строке
        binding.tilSearchTracksField.editText?.doOnTextChanged { text, _, _, _ ->
            searchMask = text.toString().trim()
            // иконка в поле поиска
            binding.tilSearchTracksField.endIconDrawable = ContextCompat.getDrawable(
                requireContext(),
                if (searchMask.isEmpty()) {
                    EditTextSearchIcon.SEARCH_ICON.drawableId
                } else {
                    EditTextSearchIcon.CLEAR_ICON.drawableId
                }
            )
            if (searchMask.trim().isEmpty()) {
                viewModel.clearSearch()
            } else if (binding.tilSearchTracksField.hasFocus()) {
                viewModel.setSearchMask(searchMask)
                // запуск поискового запроса
                viewModel.searchDebounce(searchMask)
            }
        }

        // очистка поискового запроса кнопкой
        binding.tilSearchTracksField.setEndIconOnClickListener {
            searchMask = ""
            // иконка в поле поиска
            binding.tilSearchTracksField.endIconDrawable =
                ContextCompat.getDrawable(requireContext(), EditTextSearchIcon.SEARCH_ICON.drawableId)
            binding.tilSearchTracksField.editText?.setText(searchMask)
            viewModel.clearSearch()
        }

        binding.tilSearchTracksField.editText?.setOnEditorActionListener { _, actionId, _ ->
            // поиск по нажатию Done на клавиатуре
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                viewModel.searchByClick(binding.tietSearchMask.text.toString())
            }
            false
        }
    }

    // настроим слежку за изменениями во фрагменте
    @SuppressLint("ClickableViewAccessibility")
    private fun setBindings() {
        // обработка изменений в строке поиска
        bindEditSearch()
        // мониторим скроллинг списка вакансий для загрузки новой страницы
        binding.idNestedSV.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
                // если вернулись с другого фрагмента кнопкой назад NestedView скроллится на ту же позицию
                val scrolledTo = v.getChildAt(0).measuredHeight - v.measuredHeight
                val deltaScroll = scrollY - oldScrollY
                // поэтому если сразу проскроллилось от самого верха до низа (дельта скроллинга = всей высоте элемента), то подгружать не надо
                if (deltaScroll < scrollY && scrolledTo == scrollY) {
                    loadNextPage()
                }
            }
        )
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
    private fun nextPagePreloaderToggle(show: Boolean, message: String? = null) {
        if (binding.searchNewItemsProgressBar.isVisible != show) {
            binding.searchNewItemsProgressBar.isVisible = show
            if (message != null) {
                showToast(message)
            }
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
            nextPagePreloaderToggle(true)
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
