package com.krivykh.animeapp.data.local

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FilterBadgeCache {
    private val _isBadgeVisible = MutableStateFlow(false)
    val isBadgeVisible = _isBadgeVisible.asStateFlow()

    fun updateBadgeVisibility(hasActiveFilters: Boolean) {
        _isBadgeVisible.value = hasActiveFilters
    }
}