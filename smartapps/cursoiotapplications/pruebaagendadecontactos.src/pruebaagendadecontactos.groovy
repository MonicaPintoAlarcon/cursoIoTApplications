/**
 *  PruebaAgendaDeContactos
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
    name: "PruebaAgendaDeContactos",
    namespace: "cursoIoTApplications",
    author: "Monica Pinto",
    description: "Ejemplo de uso del libro de contactos",
    category: "My Apps",
    iconUrl: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience.png",
    iconX2Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png",
    iconX3Url: "https://s3.amazonaws.com/smartapp-icons/Convenience/Cat-Convenience@2x.png")


preferences {
    section("Which Door?") {
        input "door", "capability.contactSensor", required: true,
              title: "Which Door?"
    }

    section("Enviar Notificaciones?") {
        input("receptores", "contact", title: "Enviar notifiaciones a...") {
            input "telefono", "phone", title: "Avisar con mensaje de texto (opcional)",
                description: "Numero Telefono", required: false
        }
    }
}

def installed() {
    initialize()
}

def updated() {
    initialize()
}

def initialize() {
    subscribe(door, "contact.open", doorOpenHandler)
}

def doorOpenHandler(evt) {
    log.debug "receptores configured: $receptores"

    def mensaje = "La ${door.displayName} esta abierta!"
    if (location.contactBookEnabled && receptores) {
        log.debug "agenda de contactos disponible!"
        sendNotificationToContacts(mensaje, receptores)
    } else {
        log.debug "agenda de contactos no disponible"
        if (telefono) {
            sendSms(telefono, mensaje)
        }
    }
}