package ar.edu.unahur.obj2.servidorWeb

import java.security.cert.Extension
import java.time.LocalDateTime


class Pedido(val ip: String, val url: String, val fechaHora: LocalDateTime){

  fun protocolo(): String = url.substringBefore(':')
  fun ruta(): String = url.substringAfter("com.ar","No Encontrado")
  fun extension(): String = url.substringAfterLast('.',"No Encontrado")

}

class Respuesta(val codigo: CodigoHttp, val body: String, val tiempo: Int, val pedido: Pedido){

  fun verificacionDelProtocolo() =  if (pedido.protocolo() == "http") 200 else 501

}

