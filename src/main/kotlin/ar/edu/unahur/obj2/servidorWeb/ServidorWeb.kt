package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime

object ServidorWeb{
  val modulosHabilitados: MutableList<Modulo> = mutableListOf()
  fun agregarModulo(modulo: Modulo) = modulosHabilitados.add(modulo)
  fun quitarModulo(modulo: Modulo) = modulosHabilitados.remove(modulo)
  fun verificacionDelProtocolo(pedido: Pedido) =  if (pedido.protocolo() == "http") 200 else 501
  fun serverAceptaPedido(pedido: Pedido): Boolean = ServidorWeb.modulosHabilitados.any{ modulo -> modulo.extensionesSoportadas.any{ ext -> ext == pedido.extension()} }

}

class Pedido(val ip: String, val url: String, val fechaHora: LocalDateTime){
  fun protocolo(): String = url.substringBefore(':')
  fun ruta(): String = url.substringAfter("com.ar","No Encontrado")
  fun extension(): String = url.substringAfterLast('.',"No Encontrado")
}

class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido){
  fun CodigoHttp() = this.codigo
  fun body() = this.body
  fun tiempo() = this.tiempo
  fun pedido() = this.pedido
}

abstract class Modulo(pedido: Pedido) {

  var mensaje: Mensajes = RtaError

  val extensionesSoportadas: MutableList<String> = mutableListOf()
  fun agregarExtension(extension: String) = extensionesSoportadas.add(extension)
  fun puedeSoportarExtension(extension: String): Boolean = extensionesSoportadas.contains(extension)

  fun rtaParaPedido(pedido:Pedido){
    if(ServidorWeb.serverAceptaPedido(pedido)) mensaje = RtaOK
  }

}

class Image(pedido: Pedido):Modulo(pedido){}
class Texto(pedido: Pedido):Modulo(pedido){}
class Video(pedido: Pedido):Modulo(pedido){}


interface Mensajes {
  fun codigo():CodigoHttp
  fun body(): String
  fun tiempo(): Int
}
object RtaOK: Mensajes{
  override fun codigo() = CodigoHttp.OK
  override fun body(): String = "Servicio Implementado"
  override fun tiempo(): Int = 45
}
object RtaNoEncontrado: Mensajes{
  override fun codigo() = CodigoHttp.NOT_IMPLEMENTED
  override fun body(): String = " "
  override fun tiempo(): Int = 10
}
object RtaError: Mensajes{
  override fun codigo() = CodigoHttp.NOT_FOUND
  override fun body(): String = " "
  override fun tiempo(): Int = 10
}




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
