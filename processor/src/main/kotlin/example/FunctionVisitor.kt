package example

import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import java.io.OutputStream
import java.sql.Connection

@Suppress("UNREACHABLE_CODE")
class FunctionVisitor(
    private val logger: KSPLogger,
    private val connection: Connection,
    private val file: OutputStream,
) : KSVisitorVoid() {

    override fun visitFunctionDeclaration(function: KSFunctionDeclaration, data: Unit) {
        function.annotations.find {
            // check that annotation type is @example.Query
            TODO()
        }?.let { queryAnnotation ->

            var queryText: String = TODO()
            // function parameter index to parameter type
            val parametersTypes = linkedMapOf<Int, String>()

            for ((i, parameter) in function.parameters.withIndex()) {
                // 1. replace parameters in query
                //    :param -> ?
                // 2. save information about parameter type to `parameterTypes` map

                val parameterName: String = TODO()
                val parameterTypeName: String = TODO()

            }

            val returnTypeName: String = TODO()

            logger.warn("### Function return type: $returnTypeName")
            logger.warn("### Processing query: $queryText")
            logger.warn("### Parameters: $parametersTypes")

            connection.prepareStatement(queryText).use { stmt ->
                val pMetadata = stmt.parameterMetaData

                // check that query parameter & function parameters count is equal

                for ((i, parameterType) in parametersTypes) {
                    val sqlType = pMetadata.getParameterTypeName(i + 1)
                    // check kotlin parameter type against sql parameter type
                    val isOk: Boolean = TODO()
                    if (!isOk) throw Exception(
                        "for parameter $i expected type $parameterType" +
                                " but has $sqlType"
                    )
                }

                val functionParametersCode: String = TODO()

                val setStmtArgumentsCode: String = TODO()

                val rsMetadata = stmt.metaData
                val getResultCode: String = TODO()

                file += """
                |    fun ${function.simpleName.asString()}($functionParametersCode): List<$returnTypeName> {
                |        val result = mutableListOf<$returnTypeName>()
                |        ds.connection.use { conn ->
                |            val stmt = conn.prepareStatement("$queryText")
                |            $setStmtArgumentsCode
                | 
                |            val rs = stmt.executeQuery()
                |            while (rs.next()) {
                |                result.add(example.UserDao.User(
                |                    $getResultCode
                |                ))
                |            }
                |        }
                |        return result
                |    } 
                """.trimMargin()
            }
        }
    }
}