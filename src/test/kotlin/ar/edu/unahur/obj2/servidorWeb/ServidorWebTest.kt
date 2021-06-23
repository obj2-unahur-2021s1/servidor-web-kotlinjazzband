package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({
  val servidorWeb = ServidorWeb()
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
    describe("Verificación de protocolo de ingreso de un pedido"){
      it("reply1 respuesta del servidor"){
        servidorWeb.esProtocoloHabilitado(order1).shouldBe(200)
      }
      it("reply2 respuesta del servidor"){
        servidorWeb.esProtocoloHabilitado(order2).shouldBe(501)
      }
    }
  }
  val fecha2 = LocalDateTime.of(2021, 6, 15, 14, 17, 22)
  val order3 = Pedido("207.46.13.8","http://pepito.com.ar/imagen/doc1.jpg", fecha2)
  val order4 = Pedido("207.46.130.9","http://pepito.com.ar/documentos/doc1.html", fecha2) // era https
  val order5 = Pedido("207.46.13.5","http://pepito.com.ar/videos/doc1.avi", fecha1)
  val order6 = Pedido("207.46.130.9","http://pepito.com.ar/documentos/doc1.css", fecha2)
  val order7 = Pedido("207.46.13.5","http://pepito.com.ar/documentos/doc1.json", fecha1)
  //val order8 = Pedido("207.46.13.5","http://pepito.com.ar/documentos/doc1.doc", fecha1)
  val order9 = Pedido("207.46.130.9","http://pepito.com.ar/imagen/doc1.jpg", fecha2)    // extension
  val order10 = Pedido("207.46.130.9","http://pepito.com.ar/imagen/doc1.png", fecha1)
  val order11 = Pedido("207.46.130.9","http://pepito.com.ar/video/doc1.mpeg", fecha1)
  val order12 = Pedido("207.46.130.9","http://pepito.com.ar/imagen/doc1.jpg", fecha1)   // extension
  val order13 = Pedido("207.46.130.9","http://pepito.com.ar/documentos/doc1.docx", fecha1)


  describe("Test Modulo") {
    ModuloImagen.agregarExtension("jpg")
    ModuloImagen.agregarExtension("png")
    ModuloImagen.agregarExtension("gif")

    ModuloTexto.agregarExtension("docx")
    ModuloTexto.agregarExtension("odt")
    ModuloTexto.agregarExtension("txt")
    ModuloTexto.agregarExtension("html")

    ModuloVideo.agregarExtension("mpg")
    ModuloVideo.agregarExtension("avi")
    ModuloVideo.agregarExtension("mpeg")

    //Agregamos a modulo habilitados en ServidorWeb
    servidorWeb.modulosHabilitados.add(ModuloImagen)
    servidorWeb.modulosHabilitados.add(ModuloTexto)
    servidorWeb.modulosHabilitados.add(ModuloVideo)

    describe("Pedido y Respuesta al servidor con Módulos") {
      it("El modulo unModuloImagen agregado puede soportar extensiones jpg gif y png y otras que ya vienen precargadas") {
        ModuloImagen.puedeSoportarExtension("jpg").shouldBeTrue()
        ModuloImagen.puedeSoportarExtension("gif").shouldBeTrue()
        ModuloImagen.puedeSoportarExtension("png").shouldBeTrue()
        ModuloImagen.puedeSoportarExtension("tiff").shouldBeTrue()
      }
      it("El modulo unModuloTexto agregado puede soportar extensiones docx, odt, txt, html y otras que ya vienen precargadas") {
        ModuloTexto.puedeSoportarExtension("docx").shouldBeTrue()
        ModuloTexto.puedeSoportarExtension("odt").shouldBeTrue()
        ModuloTexto.puedeSoportarExtension("txt").shouldBeTrue()
        ModuloTexto.puedeSoportarExtension("html").shouldBeTrue()
        ModuloTexto.puedeSoportarExtension("pdf").shouldBeTrue()

      }
      it("El modulo unModuloVideo agregado puede soportar extensiones mpg avi mpeg y otras que ya vienen precargadas") {
        ModuloVideo.puedeSoportarExtension("mpg").shouldBeTrue()
        ModuloVideo.puedeSoportarExtension("avi").shouldBeTrue()
        ModuloVideo.puedeSoportarExtension("mpeg").shouldBeTrue()
        ModuloVideo.puedeSoportarExtension("mov").shouldBeTrue()
      }
      it("Verificar si ServidorWeb acepta imagenes y texto y rechaza css y json por falta de modulo") {
        servidorWeb.serverPuedeResponderPedido(order1).shouldBeTrue()     // es html texto
        servidorWeb.serverPuedeResponderPedido(order3).shouldBeTrue()     //es jpg imagen
        servidorWeb.serverPuedeResponderPedido(order4).shouldBeTrue()     // es html texto
        servidorWeb.serverPuedeResponderPedido(order5).shouldBeTrue()     // es avi video
        servidorWeb.serverPuedeResponderPedido(order6).shouldBeFalse()    // es css, no hay modulo para esa extension
        servidorWeb.serverPuedeResponderPedido(order7).shouldBeFalse()    // es json, no hay modulo para esa extension
      }

    }
    describe("Modulo Soporta pedido") {
      it("Probar el mensaje de acepta") {

        (ModuloVideo.body).shouldBe("Este es un video")
        (ModuloVideo.tiempo == 15).shouldBeTrue()

        //Respuesta de Servidor para orden 5
        servidorWeb.atenderPedido(order5).codigo.shouldBe(CodigoHttp.OK)
        servidorWeb.atenderPedido(order5).body.shouldBe("Este es un video")
        servidorWeb.atenderPedido(order5).tiempo.shouldBe(15)
        servidorWeb.atenderPedido(order5).pedido.shouldBe(order5)
      }
      it("Probar el mensaje de denegar por falta de modulo ") {
        //Respuesta de Servidor para orden 6 sin modulo para css
        servidorWeb.atenderPedido(order6).codigo.shouldBe(CodigoHttp.NOT_FOUND)
        servidorWeb.atenderPedido(order6).body.shouldBe("")
        servidorWeb.atenderPedido(order6).tiempo.shouldBe(10)
        servidorWeb.atenderPedido(order6).pedido.shouldBe(order6)
      }
      it("Modulo puede soportar pedido") {
        ModuloVideo.moduloPuedeProcesarPedido(order3).shouldBeFalse()
        ModuloVideo.moduloPuedeProcesarPedido(order5).shouldBeTrue()
      }
    }
  }

  describe("Test Analizadores"){
    servidorWeb.analizadoresAsignados.add(AnalizadorDeDemora)
    servidorWeb.analizadoresAsignados.add(AnalizadorDeEstadisticas)
    servidorWeb.analizadoresAsignados.add(AnalizadorDeIPSospechosa)

    servidorWeb.atenderPedido(order1)
    servidorWeb.atenderPedido(order2)
    servidorWeb.atenderPedido(order3)
    servidorWeb.atenderPedido(order4)

    /*PEDIDOS DE IP SOSPECHOSAS*/
    servidorWeb.agregarPedidoIPSosp(order3)
    servidorWeb.agregarPedidoIPSosp(order4)
    servidorWeb.agregarPedidoIPSosp(order6)
    servidorWeb.agregarPedidoIPSosp(order9)
    servidorWeb.agregarPedidoIPSosp(order10)
    servidorWeb.agregarPedidoIPSosp(order11)
    servidorWeb.agregarPedidoIPSosp(order12)
    servidorWeb.agregarPedidoIPSosp(order13)

    describe("Analizadores"){
      it("Cantidad de respuestas en la lista"){
        //servidorWeb.respuestasModulos.size.shouldBe(4)
      }
    }
    describe("Analizador de Demora"){
      it("Respuestas demoradas"){
        AnalizadorDeDemora.cantidadDeRespuestasDemoradas().shouldBe(8)
      }
    }
    describe("Analizador De Estadísticas"){
      it("Promedio de tiempo de demora"){
        AnalizadorDeEstadisticas.tiempoRespuestaPromedio().shouldBe(10)
      }
      it("Porcentaje de respuestas exitosas"){

        //AnalizadorDeEstadisticas.porcentajeDeRespuestaExitosa(ServidorWeb).shouldBe(50)
      }
    }
    describe("Analizar IP Sospechosas"){
      it("cantidad de ip sospechosas"){
        AnalizadorDeIPSospechosa.cantidadPedidosIPSospechosas(servidorWeb, "207.46.130.9").shouldBe(7)
        AnalizadorDeIPSospechosa.cantidadPedidosIPSospechosas(servidorWeb, "207.46.13.8").shouldBe(1)
      }
      it("Pedidos de Ip sospechosas que solicitaron ruta especifica"){
        AnalizadorDeIPSospechosa.pedidosQueBuscaronRuta(servidorWeb,"/imagen/doc1.jpg").shouldContainExactly(order3, order9, order12)
        AnalizadorDeIPSospechosa.ipsRequirieronRuta(servidorWeb,"/imagen/doc1.jpg").shouldContainExactly("207.46.13.8", "207.46.130.9")
      }
      it("modulo de mas consulta de Ip Sospechosas"){

      }
    }
    describe("Analizar cantidad De Pedidos Entre Fechas"){
      it("peticiones en una fecha"){
        val fechaA = LocalDateTime.of(2021, 6, 14, 14, 17, 22)
        val fechaB = LocalDateTime.of(2021, 6, 16, 14, 17, 22)
        AnalizadorDeEstadisticas.cantidadDePedidosEntreFechas(fechaA,fechaB).shouldBe(16)
      }
    }
    describe("Analizar cantidad De respuestas con mismo body"){
      it("Prueba 1"){
        val body = "Esta es una imagen"
        AnalizadorDeEstadisticas.cantidadDeRespuestasConDeterminadoBody(body).shouldBe(0)
      }

    }



  }

})
