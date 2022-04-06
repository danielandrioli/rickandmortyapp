package com.dboy.rickandmortyapp.util

import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

fun ChipGroup.getTextFromCheckedId(): String {
    return if (checkedChipId != -1) findViewById<Chip>(checkedChipId).text.toString() else ""
}