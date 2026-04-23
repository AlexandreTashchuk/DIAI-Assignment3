package pt.unl.fct.iadi.novaevents.service

class OutdoorEventLocationRequiredException(
    message: String = "Location is required for outdoor events"
) : RuntimeException(message)

