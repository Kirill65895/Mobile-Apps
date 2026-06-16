package com.example.fitnesstracker.feature.about.ui

/** Данные о (вымышленной) компании и адресе офиса для раздела «О нас». */
data class OfficeInfo(
    val companyName: String,
    val description: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
) {
    companion object {
        val DEMO = OfficeInfo(
            companyName = "ООО «ФитТрек»",
            description = "Мы создаём приложения для здорового образа жизни и помогаем " +
                "людям отслеживать тренировки и достигать спортивных целей.",
            address = "г. Москва, Пресненская наб., 12 (башня «Федерация»)",
            latitude = 55.749792,
            longitude = 37.537034,
        )
    }
}
