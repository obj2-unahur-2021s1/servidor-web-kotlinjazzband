package ar.edu.unahur.obj2.servidorWeb


// Para no tener los códigos "tirados por ahí", usamos un enum que le da el nombre que corresponde a cada código
// La idea de las clases enumeradas es usar directamente sus objetos: CodigoHTTP.OK, CodigoHTTP.NOT_IMPLEMENTED, etc

enum class CodigoHttp(val codigo: Int) {
    OK(200),
    NOT_IMPLEMENTED(501),
    NOT_FOUND(404),
}
