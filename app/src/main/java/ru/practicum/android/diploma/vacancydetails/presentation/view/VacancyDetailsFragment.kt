package ru.practicum.android.diploma.vacancydetails.presentation.view

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.FragmentVacancyDetailsBinding
import ru.practicum.android.diploma.databinding.ItemVacancyDetailsViewBinding
import ru.practicum.android.diploma.util.Formatter
import ru.practicum.android.diploma.vacancydetails.domain.models.Contacts
import ru.practicum.android.diploma.vacancydetails.domain.models.Phone
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
        viewModel.observeVacancyState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is DetailsState.Content -> showVacancyContent(state)
                else -> {}
            }
        }

        binding.favoriteVacansyIv.setOnClickListener {}
        binding.arrowBackIv.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        setContacts(vacancyDetailsBinding, state)
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
        binding.vacancySalaryTv.text = Html.fromHtml(
            Formatter.formatSalary(requireContext(), state.vacancy.salaryInfo),
            Html.FROM_HTML_MODE_LEGACY
        )
    }

    private fun setKeySkills(binding: ItemVacancyDetailsViewBinding, state: DetailsState.Content) {
        binding.vacancyKeySkillsTv.text = state.vacancy.details.keySkill.toString()
        if (state.vacancy.details.keySkill.isNullOrEmpty()) {
            binding.keySkills.visibility = View.GONE
            binding.vacancyKeySkillsTv.visibility = View.GONE
        } else {
            binding.keySkills.visibility = View.VISIBLE
            binding.vacancyKeySkillsTv.visibility = View.VISIBLE
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

    private fun setContacts(binding: ItemVacancyDetailsViewBinding, state: DetailsState.Content) {
        val contacts = state.vacancy.details.contacts
        if (contacts == null || (contacts.email.isNullOrEmpty() && contacts.name.isNullOrEmpty() && contacts.phone.isNullOrEmpty())) {
            hideAllContactViews(binding)
        } else {
            showContactViews(binding, contacts)
        }
    }

    private fun hideAllContactViews(binding: ItemVacancyDetailsViewBinding) {
        binding.contacts.visibility = View.GONE
        binding.vacancyContactsTv.visibility = View.GONE
        binding.vacancyContactsCommentTv.visibility = View.GONE
        binding.emailVD.visibility = View.GONE
        binding.phoneVD.visibility = View.GONE
    }

    private fun showContactViews(binding: ItemVacancyDetailsViewBinding, contacts: Contacts) {
        binding.contacts.visibility = View.VISIBLE
        setName(binding, contacts.name)
        setEmail(binding, contacts.email)
        setPhone(binding, contacts.phone)
    }

    private fun setName(binding: ItemVacancyDetailsViewBinding, name: String?) {
        if (name.isNullOrEmpty()) {
            binding.vacancyContactsTv.visibility = View.GONE
        } else {
            binding.vacancyContactsTv.visibility = View.VISIBLE
            binding.vacancyContactsTv.text = name
        }
    }

    private fun setEmail(binding: ItemVacancyDetailsViewBinding, email: String?) {
        if (email.isNullOrEmpty()) {
            binding.emailVD.visibility = View.GONE
        } else {
            binding.emailVD.visibility = View.VISIBLE
            binding.emailVD.text = email
            binding.emailVD.setOnClickListener {
                // Слушатель нажатия на почту
            }
        }
    }

    private fun setPhone(binding: ItemVacancyDetailsViewBinding, phoneList: List<Phone>?) {
        if (phoneList.isNullOrEmpty()) {
            binding.phoneVD.visibility = View.GONE
        } else {
            binding.phoneVD.visibility = View.VISIBLE
            val phoneInfo = phoneList.joinToString("\n") { phone ->
                "${phone.formatted} (${phone.comment ?: "Нет комментария"})"
            }
            binding.phoneVD.text = phoneInfo
            binding.phoneVD.setOnClickListener {
                // Слушатель нажатия на телефон
            }
        }
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
