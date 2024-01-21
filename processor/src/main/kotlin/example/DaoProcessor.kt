package example

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import java.io.OutputStream
import java.sql.DriverManager

operator fun OutputStream.plusAssign(str: String) {
    this.write(str.toByteArray())
}

class DaoProcessor(
    private val options: Map<String, String>,
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
) : SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver
            .getSymbolsWithAnnotation("example.Dao")
            .filterIsInstance<KSClassDeclaration>()

        // Exit from the processor in case nothing is annotated with @Dao
        if (!symbols.iterator().hasNext()) return emptyList()

        logger.warn("#####$options")

        try {
            Class.forName("org.postgresql.Driver");
        } catch (e: ClassNotFoundException) {
            e.printStackTrace();
        }

        DriverManager.getConnection(
            options["dao.url"], options["dao.dbUsername"], options["dao.dbPassword"]
        ).use { connection ->
            logger.warn("#####$connection")

            symbols.forEach {
                ClassVisitor(logger, codeGenerator, connection)
                    .visitClassDeclaration(it, Unit)
            }
        }

        return listOf()
    }
}