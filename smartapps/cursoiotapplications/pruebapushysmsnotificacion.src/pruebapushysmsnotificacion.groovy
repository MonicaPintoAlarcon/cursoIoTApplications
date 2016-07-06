/**
 *  PruebaPushYSMSNotificacion
 *
 *  Copyright 2016 Monica Pinto
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
 */
definition(
    name: "PruebaPushYSMSNotificacion",
    namespace: "cursoIoTApplications",
    author: "Monica Pinto",
    description: "Usa el m\u00E9todo que notifica tanto mediante Push como mediante SMS",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")

preferences {
	section("Configuracion de los dispositivos") {
		input "movimiento", "capability.motionSensor", title: "Selecciona el sensor de movimiento", required:true
	}
    section("Moviles para las notificaciones"){
    	input "movilPrincipal", "phone", title: "Notificar a: ", required:true
        input "movil2", "phone", title: "Notificar a: ", required:false
        input "movil3", "phone", title: "Notificar a: ", required:false
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
	subscribe(movimiento,"motion",manejadorMovimiento)
}

def manejadorMovimiento(evt){
	log.debug "Manejador de eventos. Evento: $evt"
    
    if (evt.value == "active"){
    	sendNotification("Hay movimiento extraño en casa",[method:"both", phone: movilPrincipal])
        if (movil2)
        	sendNotification("Hay movimiento extraño en casa",[method:"phone", phone: movil2])
        if (movil3)
        	sendNotification("Hay movimiento extraño en casa",[method:"phone", phone: movil3])
    }
}