/**
 *  PruebaPreferencias
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
    name: "PruebaPreferencias",
    namespace: "cursoIoTApplications",
    author: "Monica Pinto",
    description: "Vamos a probar las distintas opciones de la secci\u00F3n de preferencias",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
    /*Prueba 1. Uso del map settings */
      //section([mobileOnly:true],"Titulo de la seccion2") //{ Prueba 3: opciones de las secciones 
      section(){
        input "someSwitch", "capability.switch" 
        input "someText", "text"
        input "someTime", "time"
    }
    
    /*Prueba 2. Opción multi-páginas 
    page(name:"Interruptor", title:"Selecciona el interruptor", 
         nextPage: "TextoTiempo"){
    	section(){
        	input "someSwitch", "capability.switch" 
        }
    }
    
    page(name:"TextoTiempo", title:"Indica el resto de parametros", 
        install:true, uninstall:true){
    	section(){
        	input "someText", "text"
        }
        section(){
            input "someTime", "time"
        }
    }*/
    
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
	log.debug "someSwitch: ${someSwitch}"
    log.debug "someText: ${someText}"
    log.debug "someTime: ${someTime}"
    
    log.debug "someSwitch: ${settings.someSwitch}"
    log.debug "someText: ${settings.someText}"
    log.debug "someTime: ${settings.someTime}"
}

// TODO: implement event handlers