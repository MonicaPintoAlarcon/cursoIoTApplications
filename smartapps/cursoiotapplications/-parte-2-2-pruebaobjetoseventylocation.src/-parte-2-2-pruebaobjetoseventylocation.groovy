/**
 *  PruebaObjetosEventYLocation
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
    name: "(Parte 2) 2. PruebaObjetosEventYLocation",
    namespace: "cursoIoTApplications",
    author: "Monica Pinto",
    description: "Esta aplicaci\u00F3n nos permite probar la informaci\u00F3n a la que podemos acceder a trav\u00E9s de los objetos Event y Location ofrecidos por SmarThings",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Title") {
		input("alarma","capability.alarm")
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
	subscribe(alarma,"alarm",manejadorEventos)
}

def manejadorEventos(evt){
	log.debug "Nombre del evento: ${evt.name}"
    log.debug "Valor del evento: ${evt.value}"
    log.debug "Descripción del evento: ${evt.descriptionText}"
    if (evt.isStateChange())
    	log.debug "El estado del evento ${evt.name} ha cambiado"
    log.debug "Localizacion desde la que se ha producido el evento: ${evt.location}"
    
    log.debug "Localizacion: $location"
    log.debug "Coordenadas de la localizacion: ${location.latitude},${location.longitude}"
    log.debug "Zona horaria de la localizacion: ${location.timeZone}"
}