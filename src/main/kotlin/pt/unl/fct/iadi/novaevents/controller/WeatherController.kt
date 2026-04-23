package pt.unl.fct.iadi.novaevents.controller

import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import pt.unl.fct.iadi.novaevents.service.WeatherService

@Controller
@RequestMapping("/api")
class WeatherController(
    private val weatherService: WeatherService
) {

    @GetMapping("/weather", produces = [MediaType.APPLICATION_JSON_VALUE])
    @ResponseBody
    fun checkWeatherJson(@RequestParam location: String?): Map<String, Boolean?> {
        val normalizedLocation = location?.trim().orEmpty()
        val raining = if (normalizedLocation.isBlank()) null else weatherService.isRaining(normalizedLocation)
        return mapOf("raining" to raining)
    }

    @GetMapping("/weather", produces = [MediaType.TEXT_HTML_VALUE])
    fun checkWeatherHtml(@RequestParam location: String?, model: ModelMap): String {
        val normalizedLocation = location?.trim().orEmpty()
        model["raining"] = if (normalizedLocation.isBlank()) null else weatherService.isRaining(normalizedLocation)
        return "fragments/weather :: result"
    }
}

