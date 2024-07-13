package ru.practicum.android.diploma.vacancydetails.presentation.models

enum class CurrencyDetails(val abbr: String, val curName: String) {
    AZN("₼", "Манаты"),
    BYR("Br", "Белорусские рубли"),
    EUR("€", "Евро"),
    GEL("₾", "Грузинский лари"),
    KGS("сом", "Кыргызский сом"),
    KZT("₸", "Тенге"),
    RUR("₽", "Рубли"),
    UAH("₴", "Гривны"),
    USD("$", "Доллары"),
    UZS("so'm", "Узбекский сум"),
}

// пока сделал дубль того, что в Search, но это нужно будет перенести в common
