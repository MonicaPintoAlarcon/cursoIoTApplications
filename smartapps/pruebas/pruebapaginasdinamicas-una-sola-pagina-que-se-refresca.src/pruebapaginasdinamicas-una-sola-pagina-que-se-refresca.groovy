/**
 *  pruebaPaginasDinamicas(una sola pagina que se refresca)
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
    name: "pruebaPaginasDinamicas(una sola pagina que se refresca)",
    namespace: "pruebas",
    author: "Monica Pinto",
    description: "Una sola p\u00E1gina din\u00E1mica que se refresca",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	page(name:"paginaDin", title:"Una sola pagina dinamica", uninstall:true)
}

def paginaDin(){
	dynamicPage(name:"paginaDin"){
	    section() {
			input("tipoNotificacion","enum",options: ["push","sms","ambos"],title:"Tipo?",submitOnChange:true)
		}

		if (tipoNotificacion == "sms" || tipoNotificacion == "ambos"){
        	section (){
            	input ("receptores", "contact", title:"Selecciona los contactos")
            }
        }
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
	// TODO: subscribe to attributes, devices, locations, etc.
}

// TODO: implement event handlers