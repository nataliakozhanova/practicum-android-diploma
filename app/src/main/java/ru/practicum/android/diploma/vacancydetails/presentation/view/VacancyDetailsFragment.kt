package ru.practicum.android.diploma.vacancydetails.presentation.view

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.common.data.BadRequestError
import ru.practicum.android.diploma.common.data.ErrorType
import ru.practicum.android.diploma.common.data.NoInternetError
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.databinding.ItemVacancyDetailsViewBinding
import ru.practicum.android.diploma.util.Formatter
import ru.practicum.android.diploma.vacancydetails.domain.models.DetailsNotFoundType
import ru.practicum.android.diploma.vacancydetails.domain.models.VacancyDetails
import ru.practicum.android.diploma.vacancydetails.presentation.models.DetailsState
import ru.practicum.android.diploma.vacancydetails.presentation.viewmodel.DetailsViewModel

class VacancyDetailsFragment : Fragment() {

    private var _binding: FragmentVacancyDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<DetailsViewModel>()
    private var vacancy: VacancyDetails? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentVacancyDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val vacancyId = requireArguments().getString(ARGS_VACANCY_ID)
        if (vacancyId != null) {
            viewModel.getVacancy(vacancyId)
            viewModel.isFavourite(vacancyId)
        }

        viewModel.observeVacancyState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is DetailsState.Content -> {
                    vacancy = state.vacancy
                    hideErrorsAndLoading()
                    showVacancyContent(state)
                    binding.itemVacancyDetails.itemVacancyDetailsView.isVisible = true
                }

                is DetailsState.Error -> {
                    showTypeErrorOrEmpty(state.errorType)
                }

                is DetailsState.Empty -> {
                    showTypeErrorOrEmpty(DetailsNotFoundType())
                }

                is DetailsState.Loading -> {
                    showLoading()
                }

                is DetailsState.isFavorite -> checkFavouriteIcon(state.isFav)
                else -> {}
            }

        }

        binding.favoriteVacansyIv.setOnClickListener {
            checkIsFavourite(viewModel.getFavouriteState())
        }

        binding.shareVacansyIv.setOnClickListener {
            val intent = viewModel.getSharingIntent()
            if(intent != null) {
                startActivity(intent)
            }
        }

        binding.arrowBackIv.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun checkFavouriteIcon(isFavorite: Boolean) {
        if (isFavorite) {
            binding.favoriteVacansyIv.setImageResource(R.drawable.favorites_on_24px_button)
        } else {
            binding.favoriteVacansyIv.setImageResource(R.drawable.favorites_off_24px_button)
        }
    }

    private fun checkIsFavourite(favouriteState: Boolean) {
        if (favouriteState) {
            vacancy!!.hhID.let { id -> viewModel.deleteFavouriteVacancy(id) }
        } else {
            viewModel.addToFavById(vacancy!!)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun hideErrorsAndLoading() {
        binding.detailsProgressBar.isVisible = false
        binding.errorPlaceholderIv.isVisible = false
        binding.errorPlaceholderTv.isVisible = false
    }

    private fun showTypeErrorOrEmpty(type: ErrorType) {
        binding.detailsProgressBar.isVisible = false
        binding.errorPlaceholderIv.isVisible = true
        binding.errorPlaceholderTv.isVisible = true
        binding.itemVacancyDetails.itemVacancyDetailsView.isVisible = false
        when (type) {
            is BadRequestError -> {
                binding.errorPlaceholderTv.text = getString(R.string.server_error)
                binding.errorPlaceholderIv.setImageResource(R.drawable.image_vacancy_server_error)
            }

            is NoInternetError -> {
                binding.errorPlaceholderTv.text = getString(R.string.no_internet)
                binding.errorPlaceholderIv.setImageResource(R.drawable.image_no_internet_error)
            }

            else -> {
                binding.errorPlaceholderTv.text = getString(R.string.not_found_or_deleted_vacancy)
                binding.errorPlaceholderIv.setImageResource(R.drawable.image_vacancy_not_found_error)
            }
        }
    }

    private fun showLoading() {
        binding.itemVacancyDetails.itemVacancyDetailsView.isVisible = false
        binding.detailsProgressBar.isVisible = true
        binding.errorPlaceholderIv.isVisible = false
        binding.errorPlaceholderTv.isVisible = false
    }

    private fun showVacancyContent(state: DetailsState.Content) {
        val vacancyDetailsBinding = ItemVacancyDetailsViewBinding.bind(binding.root)

        setVacancyTitle(vacancyDetailsBinding, state)
        setCompanyDetails(vacancyDetailsBinding, state)
        setAddress(vacancyDetailsBinding, state)
        setSalary(vacancyDetailsBinding, state)
        setKeySkills(vacancyDetailsBinding, state)
        setDescription(vacancyDetailsBinding, state)
        setExperience(vacancyDetailsBinding, state)
        setEmployment(vacancyDetailsBinding, state)
        setCompanyLogo(vacancyDetailsBinding, state)
    }

    private fun setVacancyTitle(binding: ItemVacancyDetailsViewBinding, state: DetailsState.Content) {
        binding.nameVacancyTv.text = "${state.vacancy.name}, ${state.vacancy.employerInfo.areaName}"
    }

    private fun setCompanyDetails(binding: ItemVacancyDetailsViewBinding, state: DetailsState.Content) {
        binding.nameCompanyTv.text = state.vacancy.employerInfo.employerName
    }

    private fun setAddress(binding: ItemVacancyDetailsViewBinding, state: DetailsState.Content) {
        val address = state.vacancy.details.address
        if (address != null) {
            val addressParts = listOfNotNull(address.street, address.building, address.city).filter { it.isNotBlank() }
            val addressText = addressParts.joinToString(", ")
            binding.adressCompanyTv.text =
                if (addressText.isNotBlank()) addressText else state.vacancy.employerInfo.areaName
        } else {
            binding.adressCompanyTv.text = state.vacancy.employerInfo.areaName
        }
    }

    private fun setSalary(binding: ItemVacancyDetailsViewBinding, state: DetailsState.Content) {
        binding.vacancySalaryTv.text =
            Formatter.formatSalary(requireContext(), state.vacancy.salaryInfo)
    }

    private fun setKeySkills(binding: ItemVacancyDetailsViewBinding, state: DetailsState.Content) {
        val keySkills = state.vacancy.details.keySkills
        if (keySkills.isNullOrEmpty()) {
            binding.keySkills.visibility = View.GONE
            binding.vacancyKeySkillsTv.visibility = View.GONE
        } else {
            binding.keySkills.visibility = View.VISIBLE
            binding.vacancyKeySkillsTv.visibility = View.VISIBLE
            val skillsText = keySkills.joinToString(separator = "\n") { "•  ${it.name}" }
            binding.vacancyKeySkillsTv.text = skillsText
        }
    }

    private fun setDescription(binding: ItemVacancyDetailsViewBinding, state: DetailsState.Content) {
        binding.descriptionVacancy.text = Html.fromHtml(
            state.vacancy.details.description,
            Html.FROM_HTML_MODE_LEGACY
        )
    }

    private fun setExperience(binding: ItemVacancyDetailsViewBinding, state: DetailsState.Content) {
        binding.valueExperienceTv.text = state.vacancy.details.experience?.name
    }

    private fun setEmployment(binding: ItemVacancyDetailsViewBinding, state: DetailsState.Content) {
        binding.formatWorkTv.text = state.vacancy.details.employment?.name
    }

    private fun setCompanyLogo(binding: ItemVacancyDetailsViewBinding, state: DetailsState.Content) {
        val placeHolderCornerRadius: Int = resources.getDimensionPixelSize(R.dimen.logo_corner_radius)
        Glide.with(binding.root)
            .load(state.vacancy.employerInfo.employerLogoUrl)
            .placeholder(R.drawable.logo_placeholder_image)
            .transform(CenterCrop(), RoundedCorners(placeHolderCornerRadius))
            .into(binding.logoCompanyIv)
    }

    companion object {
        private const val ARGS_VACANCY_ID = "vacancyID"

        fun createArgs(vacancyID: String): Bundle =
            bundleOf(ARGS_VACANCY_ID to vacancyID)
    }
}
