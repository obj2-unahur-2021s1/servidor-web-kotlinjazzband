package ar.edu.unahur.obj2.servidorWeb

import java.time.LocalDateTime

object ServidorWeb{
  val modulosHabilitados: MutableList<Modulo> = mutableListOf()
  fun agregarModulo(modulo: Modulo) = modulosHabilitados.add(modulo)
  fun quitarModulo(modulo: Modulo) = modulosHabilitados.remove(modulo)
}

class Pedido(val ip: String, val url: String, val fechaHora: LocalDateTime){

  fun protocolo(): String = url.substringBefore(':')
  fun ruta(): String = url.substringAfter("com.ar","No Encontrado")
  fun extension(): String = url.substringAfterLast('.',"No Encontrado")

}

class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido){

  fun verificacionDelProtocolo() =  if (pedido.protocolo() == "http") 200 else 501
  fun moduloAceptaPedido(): Boolean = ServidorWeb.modulosHabilitados.any {  modulo -> modulo.extensioneSoportadas.any{ext -> ext.toString() == pedido.extension()}  }

/*
  fun comparaExtension(): Boolean{
    val extensionSimilar = pedido.extension() == this.toString()
    return extensionSimilar
  }*/

}

class Modulo(var extensioneSoportadas: MutableList<Extension>,val texto: String,val tiempo: Int){
  fun puedeSoportarExtension(extension: Extension): Boolean{
    return extensioneSoportadas.contains(extension)
  }
}
class Extension{
   //Agregar futuros metodos
}