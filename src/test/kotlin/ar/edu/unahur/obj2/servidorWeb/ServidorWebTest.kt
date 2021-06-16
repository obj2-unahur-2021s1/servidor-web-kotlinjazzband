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
        ServidorWeb.verificacionDelProtocolo(order1).shouldBe(200)
      }
      it("reply2 respuesta del servidor"){
        ServidorWeb.verificacionDelProtocolo(order2).shouldBe(501)
      }
    }

  }

  val fecha2 = LocalDateTime.of(2021, 6, 15, 14, 17, 22)
  val order3 = Pedido("207.46.13.8","http://pepito.com.ar/documentos/doc1.jpg", fecha2)
  val order4 = Pedido("207.46.130.9","http://pepito.com.ar/documentos/doc1.html", fecha2) // era https
  val order5 = Pedido("207.46.13.5","http://pepito.com.ar/documentos/doc1.avi", fecha1)

  val reply3 = Respuesta(CodigoHttp.OK,"El servicio esta implementado",25,order3)
  val reply4 = Respuesta(CodigoHttp.OK,"El servicio esta implementado",25,order4)

  val unModuloImagen= Modulo(mutableListOf("jpg","png","gif"),"Es de imagen",10)
  val unModuloTexto= Modulo(mutableListOf("docx","odt","txt","html"),"Es de texto",10)
  val unModuloVideo= Modulo(mutableListOf("mpg","avi","mpeg"),"Es de video",10)

  //Agregamos modulo a lista de modulos habilitados en servidor
  ServidorWeb.modulosHabilitados.add(unModuloImagen)
  ServidorWeb.modulosHabilitados.add(unModuloTexto)
  ServidorWeb.modulosHabilitados.add(unModuloVideo)

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
    //order3 = jpg   order4 = html     order5 = avi
    it("Verificar si unModuloImagen solo acepta imagenes caso jpg al pedido"){
      unModuloImagen.moduloAceptaPedido(order4).shouldBeTrue()  // es html debería dar falso
      unModuloImagen.moduloAceptaPedido(order3).shouldBeTrue()
      unModuloImagen.moduloAceptaPedido(order5).shouldBeTrue() // es avi debería dar falso
    }
    it("Verificar si unModuloVideo solo acepta Video caso avi al pedido"){
      unModuloVideo.moduloAceptaPedido(order4).shouldBeTrue()
      unModuloVideo.moduloAceptaPedido(order3).shouldBeTrue()   // es jpg debería dar falso
      unModuloVideo.moduloAceptaPedido(order5).shouldBeTrue()   // es avi debería dar falso
    }
    it("Verificar si unModuloTexto solo acepta documentos caso html al pedido"){
      unModuloTexto.moduloAceptaPedido(order4).shouldBeTrue()
      unModuloTexto.moduloAceptaPedido(order3).shouldBeTrue()    // es jpg debería dar falso
      unModuloTexto.moduloAceptaPedido(order5).shouldBeTrue()    // es avi debería dar falso
    }
  }

})
