/**
 *  Avisame cuando ...
 *
 *  Author: Mónica Pinto
 *
 */
definition(
		name: "(Parte 2) Avisame cuando ...",
		namespace: "cursoIoTApplications",
		author: "Monica Pinto",
		description: "Recibe notifications cuando alguien llega/sale de una localización",
		category: "My Apps",
		iconUrl: "https://s3.amazonaws.com/smartapp-icons/Meta/window_contact.png",
		iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Meta/window_contact@2x.png"
)

preferences {
	page(name:"pagina1", title:"", install:false, uninstall:true, nextPage:"pagina2"){
    	section("Elige el sensor de presencia..."){
			input "presencia", "capability.presenceSensor", title: "Llegada/Marcha de", required: true, multiple: false 
		}
		section("Envia este mensaje cuando alguien llega (opcional, envia un mensaje estandar si este no se especifica)"){
			input "textoMensajeLlegada", "text", title: "Texto del Mensaje", required: false
		}
        section("Envia este mensaje cuando alguien se marcha (opcional, envia un mensaje estandar si este no se especifica)"){
			input "textoMensajeMarcha", "text", title: "Texto del Mensaje", required: false
		}
    }
    
	page(name:"pagina2")
}

def pagina2(){
	dynamicPage(name:"pagina2",title:"",install:true, uninstall:true){
    	
    	section(){
    		input "pushYTelefono", "enum", title: "Tanto Push como SMS?", required: true, submitOnChange: true, options: ["Solo Push", "Solo SMS", "Push y SMS"]
    	}
    
   		if (pushYTelefono == "1" || pushYTelefono == "2"){ //En el simulador no funciona con las cadenas de caracteres
			section("Telefonos para mensaje SMS"){
				input("receptores", "contact", title: "Enviar notifications a") {
                	paragraph "Si esta fuera de US indique el codigo del Pais"
					input "telefono", "phone", title: "Numero de Telefono (para SMS, opcional)", required: false
				}
			}
    	}
    }
}

def installed() {
	log.debug "Installed with settings: ${settings}"
	subscribeToEvents()
}

def updated() {
	log.debug "Updated with settings: ${settings}"
	unsubscribe()
	subscribeToEvents()
}

def subscribeToEvents() {
	subscribe(presencia, "presence", manejadorEventos)
}

def manejadorEventos(evt) {
	log.debug "Recibida notificacion. Evento ${evt}"
	sendMessage(evt)
}

//Método privado
private sendMessage(evt) {
	String msg
    
    if (evt.value == "present" && textoMensaje){
    	msg = textoMensajeLlegada
    }else if (evt.value == "present" && !textoMensaje){
    	msg = "Alguien ha llegado a $location"
    }else if (evt.value == "not present" && textoMensaje){
    	msg = textoMensajeSalida
    }else
		msg = "Alguien se ha marchado de $location"

	log.debug "$evt.name:$evt.value, pushAndPhone:$pushAndPhone, '$msg'"

	if (pushYTelefono == "Solo Push"){
    	log.debug 'enviando Push'
    	sendPush(msg)
    }else{
    	if (location.contactBookEnabled){
        	log.debug 'enviando a la lista de contactos'
        	sendNotificationToContacts(msg,receptores)
        }else{
        	if (telefono){
            	log.debug 'enviando a movil'
            	sendSms(telefono,msg)
            }
        }
        if (pushAndPhone == "Push y SMS"){
        	log.debug 'enviando Push'
        	sendPush(msg)
        }
    }
}