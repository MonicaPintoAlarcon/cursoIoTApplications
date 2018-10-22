/**
 *  PruebaWebApp-v3
 *
 *  Copyright 2017 Monica Pinto
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
    name: "PruebaWebApp-v3",
    namespace: "MonicaPintoAlarcon",
    author: "Monica Pinto",
    description: "prueba web",
    category: "Green Living",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
	section("Title") {
		// TODO: put inputs here
        input "switches", "capability.switch", multiple:true, required:true
	}
}

mappings{
	path("/switches"){
    	action: [ GET: "listSwitches" ]
    }
    path("/switches/:command"){
    	action: [PUT: "updateSwitches"]
    }
}

def listSwitches(){
	def resp = [] //Creamos mapa vacio
    switches.each{
    	resp << [name: it.displayName, value: it.currentValue("switch")]
    }
    log.debug resp
    return resp
}

def updateSwitches(){
	def command = params.command
    switch(command){
    	case "on": switches.on(); break
        case "off": switches.off(); break
        default: httpError(400, "$command is not a valid command for the specified switches")
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