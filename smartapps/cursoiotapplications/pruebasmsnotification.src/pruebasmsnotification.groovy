/**
 *  PruebaSmsNotification
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
    name: "PruebaSmsNotification",
    namespace: "cursoIoTApplications",
    author: "Monica Pinto",
    description: "Aplicaci\u00F3n para probar las notificaciones mediante SMS",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Configuracion de los dispositivos") {
		input "movimiento", "capability.motionSensor", title: "Selecciona el sensor de movimiento", required:true
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

// TODO: implement event handlers
def manejadorMovimiento(evt){
	log.debug "Manejador de eventos. Evento: $evt"
    
    if (evt.value == "active"){
    	sendSms("+34609351275","Hay movimiento extraño en casa")
    }
}