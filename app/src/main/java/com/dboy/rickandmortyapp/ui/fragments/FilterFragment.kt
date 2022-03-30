package com.dboy.rickandmortyapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dboy.rickandmortyapp.R
import com.dboy.rickandmortyapp.databinding.FragmentFilterBinding
import com.dboy.rickandmortyapp.ui.RmViewModel
import com.dboy.rickandmortyapp.util.getTextFromCheckedId
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip

class FilterFragment : BottomSheetDialogFragment() {
    private var binding: FragmentFilterBinding? = null
    private val rmViewModel: RmViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFilterBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.apply {
            rmViewModel.filterStatus.observe(viewLifecycleOwner) {
                cgStatus.check(
                    when (rmViewModel.filterStatus.value) {
                        "Alive" -> R.id.chipStatusAlive
                        "Dead" -> R.id.chipStatusDead
                        "Unknown" -> R.id.chipStatusUnknown
                        else -> {
                            cgStatus.checkedChipId
                        }
                    }
                )
            }

            rmViewModel.filterGender.observe(viewLifecycleOwner) {
                cgGender.check(
                    when (rmViewModel.filterGender.value) {
                        "Female" -> R.id.chipGenderFemale
                        "Male" -> R.id.chipGenderMale
                        "Genderless" -> R.id.chipGenderGenderless
                        "Unknown" -> R.id.chipGenderUnknown
                        else -> {
                            cgGender.checkedChipId
                        }
                    }
                )
            }

            btnApply.setOnClickListener {
                Log.i("FilterFragment", cgStatus.getTextFromCheckedId())
//                if (cgStatus.getTextFromCheckedId().isNotEmpty() || cgGender.getTextFromCheckedId()
//                        .isNotEmpty()
//                ) {
//                }
                rmViewModel.makeFilteredQuery(
                    status = cgStatus.getTextFromCheckedId(),
                    gender = cgGender.getTextFromCheckedId()
                )
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}