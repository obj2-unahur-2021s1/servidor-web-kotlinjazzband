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

  val reply1 = Respuesta(CodigoHttp.OK,"El servicio esta implementado",25,order1)
  val reply2 = Respuesta(CodigoHttp.NOT_IMPLEMENTED,"Servicio no implementado",10,order2)

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

    describe("respuesta del servidor al pedido"){
      it("reply1 respuesta del servidor"){
        reply1.verificacionDelProtocolo().shouldBe(200)
      }
      it("reply2 respuesta del servidor"){
        reply2.verificacionDelProtocolo().shouldBe(501)
      }

    }

  }

  val fecha2 = LocalDateTime.of(2021, 6, 15, 14, 17, 22)
  val order3 = Pedido("207.46.13.8","http://pepito.com.ar/documentos/doc1.jpg", fecha2)
  val order4 = Pedido("207.46.130.9","https://pepito.com.ar/documentos/doc1.html", fecha2)

  val reply3 = Respuesta(CodigoHttp.OK,"El servicio esta implementado",25,order3)
  val reply4 = Respuesta(CodigoHttp.OK,"El servicio esta implementado",25,order4)

  val jpg = Extension()
  val png = Extension()
  val gif = Extension()
  val docx = Extension()
  val odt = Extension()
  val txt = Extension()
  val html = Extension()
  val mpg = Extension()
  val avi = Extension()
  val mpeg = Extension()

  val unModuloImagen= Modulo(mutableListOf(jpg,png,gif),"Es de imagen",10)
  val unModuloTexto= Modulo(mutableListOf(docx,odt,txt,html),"Es de texto",10)
  val unModuloVideo= Modulo(mutableListOf(mpg,avi,mpeg),"Es de video",10)

//Agregamos modulo a lista de modulos habilitados en servidor
  ServidorWeb.modulosHabilitados.add(unModuloImagen)
  ServidorWeb.agregarModulo(unModuloImagen)

  describe("Pedido y Respuesta al servidor con Módulos") {
    it("El modulo un Modulo Imagen agregado puede soportar extensiones jpg gif y png"){
      unModuloImagen.puedeSoportarExtension(jpg).shouldBeTrue()
      unModuloImagen.puedeSoportarExtension(gif).shouldBeTrue()
      unModuloImagen.puedeSoportarExtension(png).shouldBeTrue()
    }

    it("El modulo un Modulo Texto agregado puede soportar extensiones docx, odt, txt, html"){
      unModuloTexto.puedeSoportarExtension(docx).shouldBeTrue()
      unModuloTexto.puedeSoportarExtension(odt).shouldBeTrue()
      unModuloTexto.puedeSoportarExtension(txt).shouldBeTrue()
      unModuloTexto.puedeSoportarExtension(html).shouldBeTrue()
    }
    it("El modulo un Modulo Video agregado puede soportar extensiones mpg avi mpeg"){
      unModuloVideo.puedeSoportarExtension(mpg).shouldBeTrue()
      unModuloVideo.puedeSoportarExtension(avi).shouldBeTrue()
      unModuloVideo.puedeSoportarExtension(mpeg).shouldBeTrue()
    }
    it("Buscar modulos que acepten el pedido(EN DESARROLLO)"){
      //Revisar, dando false moduloAceptaPedido()
      reply3.moduloAceptaPedido().shouldBeFalse()
      reply4.moduloAceptaPedido().shouldBeFalse()
    }
  }

})
