/**
 *  CasaEnergeticamenteEficiente-v2
 *
 *  Copyright 2016 Monica Pinto
 *
 *
 */
definition(
    name: "CasaEnergeticamenteEficiente-v2",
    namespace: "cursoIoTApplications",
    author: "Monica Pinto",
    description: "Usando planificacion de tareas y la variable static; nos aseguramos de apagar las luces en caso de que haya m\u00E1s de una encendida y dejar solo una. Dejamos la \u00FAltima en encenderse",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Title") {
        input "luces", "capability.switch", multiple:true
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"

	initialize()
}

def updated() {
	log.debug "Updated with settings: ${settings}"

	unsubscribe()
	initialize()
}

def initialize() {
    runEvery30Minutes(comprobarLuces)
}

def comprobarLuces(){

    def numLucesOn = luces.count {it?.latestValue("switch") == 'on'}
    log.debug "numLuces = $numLucesOn"
	
    if (numLucesOn > 1) {
    	log.debug "Apaga alguna luz"
        def luzOn = luces.findAll {it?.latestValue("switch") == 'on' }
        def orden = luzOn.sort {it.latestState?.date}
        def times = orden*.latestState.date
        log.debug "times: $orden orden:$times"
        def i = luzOn.size, j = 0
        while(i > 1 ){
        	orden[j].off()
            i = i - 1
            j = j + 1
        }
    }
}