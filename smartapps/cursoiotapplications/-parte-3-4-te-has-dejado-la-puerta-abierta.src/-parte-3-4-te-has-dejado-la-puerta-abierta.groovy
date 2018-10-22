/**
 *  Copyright 2015 SmartThings
 *
 *  Author: Monica Pinto
 *  Date: 2016-07-06
 */
definition(
    name: "(Parte 3) 4. Te has dejado la puerta abierta",
    namespace: "cursoIoTApplications",
    author: "SmartThings",
    description: "Notifies you when you have left a door or window open longer that a specified amount of time.",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/bon-voyage.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/ModeMagic/bon-voyage%402x.png"
)

preferences {

	section("Monitor this door or window") {
		input "contact", "capability.contactSensor", multiple:true
	}
	section("And notify me if it's open for more than this many minutes (default 10)") {
		input "openThreshold", "number", description: "Number of minutes", required: false
	}
	section("Via text message at this number (or via push notification if not specified") {
        input("recipients", "contact", title: "Send notifications to") {
            input "phone", "phone", title: "Phone number (optional)", required: false
        }
	}
}

def installed() {
	log.trace "installed()"
	initialize()
}

def updated() {
	log.trace "updated()"
	unsubscribe()
	initialize()
}

def initialize() {
	subscribe(contact, "contact.open", doorOpen)
	subscribe(contact, "contact.closed", doorClosed)
}

def doorOpen(evt)
{
	log.trace "doorOpen($evt.name: $evt.value)"
	
    def t0 = now()
    //El retraso se expresa en segundos
    //El valor por defecto es 10 (10 minutos * 60 = 600 segundos)
	def delay = (openThreshold != null && openThreshold != "") ? openThreshold * 60 : 600
	runIn(delay, doorOpenTooLong)
	
    log.debug "scheduled doorOpenTooLong in ${now() - t0} msec"
}

def doorClosed(evt)
{
	log.trace "doorClosed($evt.name: $evt.value)"
}

def doorOpenTooLong() {
	//def contactState = contact.currentState("contact")
    def contactState = contact.contactState
  
	//if (contactState.value == "open") { //VALIDO SI SOLO HAY UNA PUERTA/VENTANA
    if (contactState.any {it.value == "open"}){
    	//Devuelve el tiempo en milisegundos
		def elapsed = now() - contactState.rawDateCreated.time
        log.debug "rawDateCreated.time = $contactState.rawDateCreated.time"
        log.debug "date.time = $contactState.date.time"
        log.debug "elapsed time = $elapsed"
        //threshold se expresa en milisegundos, para poder compararlo con elapsed
		def threshold = ((openThreshold != null && openThreshold != "") ? openThreshold * 60000 : 600000) - 1000
        log.debug "threshold = $threshold"
		if (elapsed >= threshold) {
			log.debug "Contact has stayed open long enough since last check ($elapsed ms):  calling sendMessage()"
			sendMessage()
		} else {
			log.debug "Contact has not stayed open long enough since last check ($elapsed ms):  doing nothing"
		}
	} else {
		log.warn "doorOpenTooLong() called but contact is closed:  doing nothing"
	}
}

void sendMessage()
{
	def minutes = (openThreshold != null && openThreshold != "") ? openThreshold : 10
	def msg = "${contact.displayName} has been left open for ${minutes} seconds."
	log.info msg
    if (location.contactBookEnabled && recipients) {
        sendNotificationToContacts(msg, recipients)
    }
    else {
        if (phone) {
            sendSms phone, msg
        } else {
            sendPush msg
        }
    }
}