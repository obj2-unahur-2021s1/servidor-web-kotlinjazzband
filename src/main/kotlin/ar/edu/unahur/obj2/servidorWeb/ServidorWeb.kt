package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime

class Pedido(val ip: String, val url: String, val fechaHora: LocalDateTime){
  fun protocolo(): String = url.substringBefore(':')
  fun ruta(): String = url.substringAfter("com.ar","No Encontrado")
  fun extension(): String = url.substringAfterLast('.',"No Encontrado")
}

object ServidorWeb{
  val modulosHabilitados: MutableList<Modulo> = mutableListOf()
  val ipSospechosas:MutableList<String> = mutableListOf()

  fun agregarIpSospechosa(string: String) = ipSospechosas.add(string)
  fun esProtocoloHabilitado(pedido: Pedido) = if (pedido.protocolo() == "http") 200 else 501
  // El servidor debe delegar al modulo si puede responder
  fun serverPuedeResponderPedido(pedido: Pedido): Boolean = modulosHabilitados.any{ m -> m.moduloPuedeProcesarPedido(pedido) }

  // OBVIAMENTE NO PUEDE SER ASI, PERO ES LO QUE CREO QUE TENDRÍA QUE PASAR ()
  fun rtaPtcl(pedido: Pedido, modulo: Modulo){
    if ( pedido.protocolo() == "http" && modulo.moduloPuedeProcesarPedido(pedido) ){
      fun refPedido() = pedido
      fun codigo() = CodigoHttp.OK
      fun body(): String = "Servicio Implementado"
      fun tiempo(): Int = 22 // OJO LO DEBE CALCULAR EL MODULO
    } else {
      fun refPedido() = pedido
      fun codigo() = CodigoHttp.NOT_FOUND
      fun body(): String = ""
      fun tiempo(): Int = 10
    }
  }
  fun rtaModulo(pedido: Pedido){
    if (serverPuedeResponderPedido(pedido)){
      fun refPedido() = pedido
      fun codigo() = CodigoHttp.OK
      fun body(): String = "Servicio Implementado"
      fun tiempo(): Int = 45
    } else {
      fun refPedido() = pedido
      fun codigo() = CodigoHttp.NOT_IMPLEMENTED
      fun body(): String = ""
      fun tiempo(): Int = 10
    }
  }

}

interface Modulo{
  val extensionesSoportadas: MutableList<String>
  val body: String
  val tiempo: Int
  fun agregarExtension(extension: String) = extensionesSoportadas.add(extension)
  fun puedeSoportarExtension(extension: String): Boolean = extensionesSoportadas.contains(extension)
  fun moduloPuedeProcesarPedido(pedido:Pedido) = extensionesSoportadas.contains(pedido.extension())
  //fun moduloRespondeAlPedido(pedido: Pedido) = (moduloPuedeProcesarPedido(pedido))

  }
class ModuloImagen: Modulo{
  override val extensionesSoportadas: MutableList<String> = mutableListOf("tiff","psd","bmp")
  override val body: String = "Esta es una imagen"
  override val tiempo: Int = 5
}
class ModuloVideo: Modulo{
  override val extensionesSoportadas: MutableList<String> = mutableListOf("mkv","mov","wmv")
  override val body: String = "Este es un video"
  override val tiempo: Int = 15
}
class ModuloTexto: Modulo{
  override val extensionesSoportadas: MutableList<String> = mutableListOf("pdf","log","idx","dic")
  override val body: String = "Este es un texto"
  override val tiempo: Int = 2
}

class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido, respuesta: MensajeDeRespuesta){
//  fun CodigoHttp() = this.codigo
//  fun body() = this.body
//  fun tiempo() = this.tiempo
//  fun pedido() = this.pedido
}


interface MensajeDeRespuesta {
  fun codigo():CodigoHttp
  fun body(): String
  fun tiempo(): Int
}
object RtaOK: MensajeDeRespuesta{
  override fun codigo() = CodigoHttp.OK
  override fun body(): String = "Servicio Implementado"
  override fun tiempo(): Int = 45
}
object RtaNoEncontrado: MensajeDeRespuesta{
  override fun codigo() = CodigoHttp.NOT_IMPLEMENTED
  override fun body(): String = " "
  override fun tiempo(): Int = 10
}
object RtaError: MensajeDeRespuesta{
  override fun codigo() = CodigoHttp.NOT_FOUND
  override fun body(): String = " "
  override fun tiempo(): Int = 10
}


//fun respuestaAlPedido(pedido: Pedido) = if (! moduloPuedeProcesarPedido(pedido)) CodigoHttp.NOT_FOUND else CodigoHttp.OK
//fun serverAceptaPedido(pedido: Pedido): Boolean = ServidorWeb.modulosHabilitados.any{ modulo -> modulo.extensionesSoportadas.any{ ext -> ext == pedido.extension()} }
/*
  if(moduloAceptaPedido(pedido)){
    refPedido = pedido
    codigo = CodigoHttp.OK
    body = "Servicio Implementado"
    tiempo = 15
  }
  var codigo: CodigoHttp = CodigoHttp.NOT_IMPLEMENTED
  var body: String = "Servicio No Implementado"
  var tiempo: Int = 10
  var refPedido: Pedido = pedido
 */
