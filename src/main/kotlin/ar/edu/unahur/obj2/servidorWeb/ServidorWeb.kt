package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime

// Para no tener los códigos "tirados por ahí", usamos un enum que le da el nombre que corresponde a cada código
// La idea de las clases enumeradas es usar directamente sus objetos: CodigoHTTP.OK, CodigoHTTP.NOT_IMPLEMENTED, etc
enum class CodigoHttp(val codigo: Int) {
  OK(200),
  NOT_IMPLEMENTED(501),
  NOT_FOUND(404),
}
//object CodigoHTTP.OK{}
//object CodigoHTTP.NOT_IMPLEMENTED{}
//object CodigoHTTP.NOT_FOUND{}

class Pedido(val ip: String, val url: String, val fechaHora: LocalDateTime){

  fun protocolo(): String = url.substringBefore(':')
  fun ruta(): String = url.substringAfter("com.ar","No Encontrado")
  fun extension(): String = url.substringAfterLast('.',"No Encontrado")

}

class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido){
  fun tiempoDeRespuesta(){
    // tiempo de respuesta en milisegundos
  }
  fun codigoDeRespuesta(){
    // un código de respuesta(Ok, No implementado o no encontrado)
    //Si el protocolo es distinto a "http" retorna código de respuesta 501 (servicio no implementado)
  }
  fun body(){
    // un body vacio o contenido que será un String
  }
  fun referencia(){
    //una referencia al pedido que la generó.
  }
}




