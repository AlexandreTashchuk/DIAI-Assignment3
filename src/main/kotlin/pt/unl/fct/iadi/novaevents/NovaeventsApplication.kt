package pt.unl.fct.iadi.novaevents

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NovaeventsApplication

//model: ModelMap
fun main(args: Array<String>) {
    runApplication<NovaeventsApplication>(*args)
}
