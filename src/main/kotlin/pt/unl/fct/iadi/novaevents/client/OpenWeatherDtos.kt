package pt.unl.fct.iadi.novaevents.client

data class OpenWeatherResponse(
    val weather: List<OpenWeatherCondition> = emptyList()
)

data class OpenWeatherCondition(
    val main: String? = null,
    val description: String? = null
)

