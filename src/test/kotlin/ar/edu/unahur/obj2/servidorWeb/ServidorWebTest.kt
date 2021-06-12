package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
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

})
