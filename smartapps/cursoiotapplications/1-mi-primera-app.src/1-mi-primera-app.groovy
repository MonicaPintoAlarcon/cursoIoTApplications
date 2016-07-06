/**
 *  Mi Primera App
 *
 *  Copyright 2016 Monica Pinto
 *
 *
 */
definition(
    name: "1. Mi Primera App",
    namespace: "cursoIoTApplications",
    author: "Monica Pinto",
    description: "Aplicacion de prueba para demostrar las caracteristicas basicas de SmartThing",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Selecciona el sensor de movimiento:") {
        input "elSensorMov", "capability.motionSensor", required: true, title: "Sensor de movimiento?"
    }
    section("Selecciona la luz a encender/apagar") {
        input "elInterruptor", "capability.switch", required: true, multiple: true, title: "Interruptor?"
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
	// Nos suscribimos al sensor de movimiento
    subscribe(elSensorMov,"motion.active",movimientoDetectadoManejador)
}

// Implementamos el manejador de eventos
def movimientoDetectadoManejador(evt){
	log.debug "movimientoDetectadoManejador called: $evt"
    elInterruptor.on()
    /** De forma alternativa, si hay más de un interruptor podemos interactuar con ellos de forma separada
     *  "elInterruptor" en caso de haber más de uno sería una lista a la que podemos acceder por posición
    
     *elInterruptor[0].on()
     *elInterruptor[2].on()
    */
}
