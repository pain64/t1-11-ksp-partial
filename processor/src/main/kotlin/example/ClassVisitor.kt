package example

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.ClassKind
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import java.sql.Connection

class ClassVisitor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
    private val connection: Connection,
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        if (classDeclaration.classKind != ClassKind.INTERFACE) {
            logger.error("@Dao annotation allowed only for interfaces")
            return
        }

        val file = codeGenerator.createNewFile(
            // Make sure to associate the generated file with sources to keep/maintain it across incremental builds.
            // Learn more about incremental processing in KSP from the official docs:
            // https://kotlinlang.org/docs/ksp-incremental.html
            dependencies = Dependencies(false, *(
                    classDeclaration.containingFile?.let { arrayOf(it) } ?: arrayOf()
                    )),
            packageName = classDeclaration.packageName.getQualifier(),
            fileName = classDeclaration.simpleName.getShortName() + "Impl"
        )

        file += "package ${classDeclaration.packageName.asString()}\n\n"
        file += "class ${classDeclaration.simpleName.asString()}" +
                "Impl(private val ds: javax.sql.DataSource) {\n"

        classDeclaration.getAllFunctions().forEach {
            FunctionVisitor(logger, connection, file).visitFunctionDeclaration(it, Unit)
        }

        file += "\n}"
    }
}