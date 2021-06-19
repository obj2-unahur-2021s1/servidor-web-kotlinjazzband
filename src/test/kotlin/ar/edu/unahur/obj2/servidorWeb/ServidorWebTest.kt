package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({

  val fecha1 = LocalDateTime.of(2021, 6, 10, 14, 17, 22)
  val order1 = Pedido("207.46.13.5","http://pepito.com.ar/documentos/doc1.html", fecha1)
  val order2 = Pedido("207.46.130.7","https://pepito.com.ar/documentos/doc1.html", fecha1)

  describe("Pedido y Respuesta al servidor sin Módulos") {

    describe(" Datos de la URL"){
      it("El protocolo de la URL es: "){
        order1.protocolo().shouldBe("http")
      }
      it("La ruta de la URL es:"){
        order1.ruta().shouldBe("/documentos/doc1.html")
      }
      it("La extension de la URL del pedido al servidor es"){
        order1.extension().shouldBe("html")
      }
      it("El año del pedido al servidor es"){
        order1.fechaHora.year.shouldBe(2021)
      }
    }
    describe("Verificacion de protocolo de ingreso de un pedido"){
      it("reply1 respuesta del servidor"){
        ServidorWeb.esProtocoloHabilitado(order1).shouldBe(200)
      }
      it("reply2 respuesta del servidor"){
        ServidorWeb.esProtocoloHabilitado(order2).shouldBe(501)
      }
    }
  }

  val fecha2 = LocalDateTime.of(2021, 6, 15, 14, 17, 22)
  val order3 = Pedido("207.46.13.8","http://pepito.com.ar/documentos/doc1.jpg", fecha2)
  val order4 = Pedido("207.46.130.9","http://pepito.com.ar/documentos/doc1.html", fecha2) // era https
  val order5 = Pedido("207.46.13.5","http://pepito.com.ar/documentos/doc1.avi", fecha1)
  val order6 = Pedido("207.46.130.9","http://pepito.com.ar/documentos/doc1.css", fecha2)
  val order7 = Pedido("207.46.13.5","http://pepito.com.ar/documentos/doc1.json", fecha1)

  val unModuloImagen = Image(order3)
  unModuloImagen.agregarExtension("jpg")
  unModuloImagen.agregarExtension("png")
  unModuloImagen.agregarExtension("gif")

  val unModuloTexto = Texto(order4)
  unModuloTexto.agregarExtension("docx")
  unModuloTexto.agregarExtension("odt")
  unModuloTexto.agregarExtension("txt")
  unModuloTexto.agregarExtension("html")
  val unModuloVideo = Video(order5)
  unModuloVideo.agregarExtension("mpg")
  unModuloVideo.agregarExtension("avi")
  unModuloVideo.agregarExtension("mpeg")

  //Agregamos a modulo habilitados en ServidorWeb
  ServidorWeb.modulosHabilitados.add(unModuloImagen)
  ServidorWeb.modulosHabilitados.add(unModuloTexto)
  ServidorWeb.modulosHabilitados.add(unModuloVideo) // ** No agregamos modulo video para testear false

  describe("Pedido y Respuesta al servidor con Módulos") {
    it("El modulo unModuloImagen agregado puede soportar extensiones jpg gif y png"){
      unModuloImagen.puedeSoportarExtension("jpg").shouldBeTrue()
      unModuloImagen.puedeSoportarExtension("gif").shouldBeTrue()
      unModuloImagen.puedeSoportarExtension("png").shouldBeTrue()
    }

    it("El modulo unModuloTexto agregado puede soportar extensiones docx, odt, txt, html"){
      unModuloTexto.puedeSoportarExtension("docx").shouldBeTrue()
      unModuloTexto.puedeSoportarExtension("odt").shouldBeTrue()
      unModuloTexto.puedeSoportarExtension("txt").shouldBeTrue()
      unModuloTexto.puedeSoportarExtension("html").shouldBeTrue()
    }
    it("El modulo unModuloVideo agregado puede soportar extensiones mpg avi mpeg"){
      unModuloVideo.puedeSoportarExtension("mpg").shouldBeTrue()
      unModuloVideo.puedeSoportarExtension("avi").shouldBeTrue()
      unModuloVideo.puedeSoportarExtension("mpeg").shouldBeTrue()
    }
    it("Verificar si ServidorWeb acepta imagenes y texto y rechaza video por falta de modulo"){
      ServidorWeb.serverPuedeResponderPedido(order1).shouldBeTrue()     // es html texto
      ServidorWeb.serverPuedeResponderPedido(order3).shouldBeTrue()     //es jpg imagen
      ServidorWeb.serverPuedeResponderPedido(order4).shouldBeTrue()     // es html texto
      ServidorWeb.serverPuedeResponderPedido(order5).shouldBeTrue()     // es avi extension correcta modulo video no agregado
      ServidorWeb.serverPuedeResponderPedido(order6).shouldBeFalse()    // es css
      ServidorWeb.serverPuedeResponderPedido(order7).shouldBeFalse()    // es json
    }

  }
  describe("Modulo Soporta pedido"){
    it("Probar el mensaje de acepta"){
      //(unModuloVideo.mensaje.body() == " ").shouldBeTrue()
      //(unModuloVideo.mensaje.codigo() == CodigoHttp.NOT_FOUND).shouldBeTrue()
      //(unModuloVideo.mensaje.tiempo() == 10 ).shouldBeTrue()
    }
    it("Modulo puede soportar pedido"){
      unModuloVideo.moduloPuedeProcesarPedido(order3).shouldBeFalse()
      unModuloVideo.moduloPuedeProcesarPedido(order5).shouldBeTrue()
    }
  }

})
