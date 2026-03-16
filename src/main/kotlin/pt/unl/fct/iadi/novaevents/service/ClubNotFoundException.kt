package pt.unl.fct.iadi.novaevents.service

class ClubNotFoundException(id: Long) :
    RuntimeException("Club with id $id not found")