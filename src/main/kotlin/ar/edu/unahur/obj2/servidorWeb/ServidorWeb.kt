package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime
import kotlin.math.roundToInt

class Pedido(val ip: String, val url: String, val fechaHora: LocalDateTime){
  fun protocolo(): String = url.substringBefore(':')
  fun ruta(): String = url.substringAfter("com.ar","No Encontrado")
  fun extension(): String = url.substringAfterLast('.',"No Encontrado")
}

class ServidorWeb {
  val modulosHabilitados: MutableList<Modulo> = mutableListOf()
  val analizadoresAsignados: MutableList<Analizador> = mutableListOf()
  val pedidoDeIPSospechosa:MutableList<Pedido> = mutableListOf()

  fun esProtocoloHabilitado(pedido: Pedido) = if (pedido.protocolo() == "http") 200 else 501
  // El servidor debe delegar al modulo si puede responder
  fun serverPuedeResponderPedido(pedido: Pedido): Boolean = moduloAptoResponderPedido(pedido).isNotEmpty()
  fun moduloAptoResponderPedido(pedido: Pedido): List<Modulo> = modulosHabilitados.filter{ a -> a.moduloPuedeProcesarPedido(pedido) }

  // ANALIZADOR - HAY QUE ENVIAR RESPUESTAS E IP A LOS ANALIZADORES - Tomar de las listas
  fun agregarPedidoIPSosp(pedido:Pedido) = pedidoDeIPSospechosa.add(pedido)

 fun respuestaOk(pedido:Pedido): Respuesta{
   val respuestaOK = Respuesta(CodigoHttp.OK,moduloAptoResponderPedido(pedido).first().body,moduloAptoResponderPedido(pedido).first().tiempo,pedido)
   analizadoresAsignados.forEach{ a -> a.respuestasAPedidos.add(respuestaOK)}
   return respuestaOK
 }
  fun respuestaFail(pedido:Pedido): Respuesta{
    val respuestaFail = Respuesta(CodigoHttp.NOT_FOUND, "", 10, pedido)
    analizadoresAsignados.forEach{ a -> a.respuestasAPedidos.add(respuestaFail)}
    return respuestaFail
  }
  fun atenderPedido(pedido: Pedido) = if(this.serverPuedeResponderPedido(pedido)){respuestaOk(pedido)}else{respuestaFail(pedido)}
}

class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido){}

interface Modulo{
  val extensionesSoportadas: MutableList<String>
  val body: String
  val tiempo: Int
  fun agregarExtension(extension: String) = extensionesSoportadas.add(extension)
  fun puedeSoportarExtension(extension: String): Boolean = extensionesSoportadas.contains(extension)
  fun moduloPuedeProcesarPedido(pedido:Pedido): Boolean = extensionesSoportadas.contains(pedido.extension())
  }

object ModuloImagen: Modulo{
  override val extensionesSoportadas: MutableList<String> = mutableListOf("tiff","psd","bmp")
  override val body: String = "Esta es una imagen"
  override val tiempo: Int = 5
}
object ModuloVideo: Modulo{
  override val extensionesSoportadas: MutableList<String> = mutableListOf("mkv","mov","wmv")
  override val body: String = "Este es un video"
  override val tiempo: Int = 15
}
object ModuloTexto: Modulo{
  override val extensionesSoportadas: MutableList<String> = mutableListOf("pdf","log","idx","dic")
  override val body: String = "Este es un texto"
  override val tiempo: Int = 2
}

interface Analizador{
  val respuestasAPedidos:MutableList<Respuesta>
}
object AnalizadorDeDemora: Analizador{
  override val respuestasAPedidos:MutableList<Respuesta> = mutableListOf()
  val tiempoMinimo = 5
  fun cantidadDeRespuestasDemoradas() = respuestasAPedidos.filter { r->r.tiempo > tiempoMinimo }.size
}
object AnalizadorDeEstadisticas: Analizador{
  override val respuestasAPedidos:MutableList<Respuesta> = mutableListOf()

  fun tiempoRespuestaPromedio() = respuestasAPedidos.map{ r->r.tiempo}.average().roundToInt()

  fun cantidadDePedidosEntreFechas(fechaHoraDesde: LocalDateTime, fechaHoraHasta: LocalDateTime): Int{
    val rango= fechaHoraDesde..fechaHoraHasta
    return respuestasAPedidos.filter { a ->a.pedido.fechaHora in rango }.size
  }
   fun cantidadDeRespuestasConDeterminadoBody(body: String): Int{
     return respuestasAPedidos.filter { a ->a.body == body }.size
   }

  fun porcentajeDeRespuestaExitosa() = cantidadPedidosOk()*100/cantidadDePedidos()
  private fun cantidadDePedidos(): Int = respuestasAPedidos.size
  private fun cantidadPedidosOk(): Int = respuestasAPedidos.filter{ r->r.codigo == CodigoHttp.OK }.size
}

object AnalizadorDeIPSospechosa: Analizador{
  override val respuestasAPedidos:MutableList<Respuesta> = mutableListOf()

  // cuántos pedidos realizó una cierta IP sospechosa
  fun pedidosIpRara(servidor: ServidorWeb, ipRara:String) = servidor.pedidoDeIPSospechosa.filter { p->p.ip == ipRara }
  fun cantidadPedidosIPSospechosas(servidor: ServidorWeb, ipRara:String) = pedidosIpRara(servidor, ipRara).size

  // cuál fue el módulo más consultado por todas las IPs sospechosas

  fun moduloMasConsultados(){
    //return servidor.modulosHabilitados.filter
  }

  // el conjunto de IPs sospechosas que requirieron una cierta ruta.
  fun pedidosQueBuscaronRuta(servidor: ServidorWeb, ruta:String):List<Pedido> = servidor.pedidoDeIPSospechosa.filter { p->p.ruta() == ruta }
  fun ipsRequirieronRuta(servidor: ServidorWeb, ruta:String): Set<String> = pedidosQueBuscaronRuta(servidor, ruta).map{ p->p.ip }.toSet()
}
