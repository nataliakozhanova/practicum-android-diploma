package ru.practicum.android.diploma.search.data

enum class HhQueryOptions(val key: String, val value: String) {
    TEXT("text", ""),
    PAGE("page", ""),
    PER_PAGE("per_page", ""),
    SEARCH_FIELD("search_field", "name"),
    VACANCY_SEARCH_ORDER("vacancy_search_order", "publication_time"),
    SALARY("salary", ""),
    ONLY_WITH_SALARY("only_with_salary", ""),
}
