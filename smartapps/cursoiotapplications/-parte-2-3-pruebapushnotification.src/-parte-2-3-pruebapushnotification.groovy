/**
 *  PruebaPushNotification
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
    name: "(Parte 2) 3. PruebaPushNotification",
    namespace: "cursoIoTApplications",
    author: "Monica Pinto",
    description: "Aplicaci\u00F3n para probar el uso de la notificaci\u00F3n de tipo \"Push\"",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Configuración de la aplicación") {
		input "alarma", "capability.alarm", title: "Selecciona la alarma", required:true 
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
	subscribe(alarma, "alarm", manejadorAlarma)
}

def manejadorAlarma(evt){
	log.debug "Llamada a manejadorAlarma, con evento: $evt"
    
    log.debug "Mode : $location.mode"
    if (location.mode != "Home"){
    	if (evt.value == "strobe")
    		sendPush("The ${alarma.displayName} is strobing!")
    	else if (evt.value == "siren")
    		sendPush("The ${alarma.displayName} is sirening!")
    	else if (evt.value == "off")
    		sendPushMessage("The ${alarma.displayName} is off!")
    }
}