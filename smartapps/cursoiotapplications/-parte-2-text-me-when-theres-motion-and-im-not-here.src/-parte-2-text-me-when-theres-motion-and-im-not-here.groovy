/**
 *  Copyright 2015 SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  Text Me When There's Motion and I'm Not Here
 *
 *  Author: SmartThings
 */

/* Desarrollar una aplicación que:

- Notifique al usuario cuando se detecte movimiento en su casa y él no se encuentre (sensor de movimiento y sensor de presencia)
- Evitar enviar una secuencia seguida de mensajes (si ya se ha enviado uno en los últimos 10 segundos entonces no se vuelve a enviar)
	* Obtener los eventos de tipo "active" se han producido en los últimos 10 segundos
    
    	def deltaSeconds = 10
        def timeAgo = new Date(now() - (1000 * deltaSeconds))  //10 segundos -> 10000 milisegundos
		def recentEvents = motion1.eventsSince(timeAgo)
        
    * Contar cuantos eventos de tipo "active" se han producido
    
    	def count = recentEvents.count { it.value && it.value == "active" } > 1

    * Si se han producido más de 1 entonces no se hace nada (ya se habrá notificado cuando se produjo el primero)
    * Si se han producido solo 1, entonces si se notifica (es el primero que se produce en los últimos 10 segundos)
*/

definition(
    name: "(Parte 2) Text Me When There's Motion and I'm Not Here",
    namespace: "cursoIoTApplications",
    author: "SmartThings",
    description: "Send a text message when there is motion while you are away.",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/intruder_motion-presence.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/intruder_motion-presence@2x.png"
)

preferences {
	section("When there's movement...") {
		input "motion1", "capability.motionSensor", title: "Where?"
	}
	section("While I'm out...") {
		input "presence1", "capability.presenceSensor", title: "Who?"
	}
	section("Text me at...") {
        input("recipients", "contact", title: "Send notifications to") {
            input "phone1", "phone", title: "Phone number?"
        }
	}
}

def installed() {
	subscribe(motion1, "motion.active", motionActiveHandler)
}

def updated() {
	unsubscribe()
	subscribe(motion1, "motion.active", motionActiveHandler)
}

def motionActiveHandler(evt) {
	log.trace "$evt.value: $evt, $settings"
	
	if (presence1.latestValue("presence") == "not present") {
		// Don't send a continuous stream of text messages
		def deltaSeconds = 10
        log.debug "${now()-(1000 * deltaSeconds)}"
        // Se pasa como parámetro un valor long que representa un número de milisegundos a contar desde un instante de tiempo estándar que es 1 de Enero de 1970 GMT
        // now() devuelve el instante actual como un número de milisegundos a contar desde ese mismo instante de tiempo estándar
        def timeAgo = new Date(now() - (1000 * deltaSeconds))  //10 segundos -> 10000 milisegundos
        // Lista con todos los eventos que han ocurrido 
		def recentEvents = motion1.eventsSince(timeAgo)
		log.debug "Found ${recentEvents?.size() ?: 0} events in the last $deltaSeconds seconds"
        // Cuenta el número de eventos de tipo active (movimiento) que ha habido en el periodo de tiempo establecido
		def alreadySentSms = recentEvents.count { it.value && it.value == "active" } > 1

		//Si en ese periodo han ocurrido más de un evento, en el primero se habrá mandado la notificación y ahora no se repite
		if (alreadySentSms) {
			log.debug "SMS already sent to $phone1 within the last $deltaSeconds seconds"
		} else {
        	//Si en los 10 segundos solo ha habido una detección de movimientos entonces se hace la notificación 
            //Se notifica usando la agenda de contactos o no, según si hay agenda
            if (location.contactBookEnabled && recipients) {
                log.debug "$motion1 has moved while you were out, sending notifications to: ${recipients?.size()}"
                sendNotificationToContacts("${motion1.label} ${motion1.name} moved while you were out", recipients)
            }
            else {
                log.debug "$motion1 has moved while you were out, texting $phone1"
                sendSms(phone1, "${motion1.label} ${motion1.name} moved while you were out")
            }
		}
	} else {
		log.debug "Motion detected, but presence sensor indicates you are present"
	}
}