package ar.edu.unahur.obj2.servidorWeb

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime

class ServidorWebTest : DescribeSpec({

  val fecha = LocalDateTime.of(2021, 6, 10, 14, 17, 22)
  val order1 = Pedido("207.46.13.5","http://pepito.com.ar/documentos/doc1.html", fecha)

  describe("Un pedido") {
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


  }

})
