/**
 *  PruebaConsultaTiempoWeb
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
    name: "PruebaConsultaTiempoWeb",
    namespace: "cursoIoTApplications",
    author: "Monica Pinto",
    description: "Prueba de acceso a servicio web externo",
    category: "Convenience",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Title") {
		// TODO: put inputs here
        input "termostato","capability.thermostat",required:true
        input "interruptor","capability.switch",required:true
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
    runEvery30Minutes(consultarTiempoJSON)
}

def consultarTiempoJSON(){
	def params = [
        uri:  'http://api.openweathermap.org/data/2.5/',
        path: 'weather',
        contentType: 'application/json',
        query: [lat:'36.572344', lon:'-4.602985', units: 'metric', APPID: 'b811614ad352ec866712a6e9439e3462']
        //q:'Malaga' para buscar por Ciudad
    ]
    try {
        httpGet(params) {resp ->
            log.debug "resp data: ${resp.data}"
            def temp = resp.data.main.temp
            log.debug "temp: ${temp}"
        }
        def estado = termostato.latestState("thermostatMode")
        if (estado != 'cool' && temp > 27){
        	termostato.cool()
        }
        if (interruptor.latestState("switch") == 'off' && temp > 27){
            interruptor.on()
        }
    } catch (e) {
        log.error "error: $e"
    }
}