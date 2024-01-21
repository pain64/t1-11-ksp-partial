package example

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider

class DaoProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): DaoProcessor {
        return DaoProcessor(
            options = environment.options,
            logger = environment.logger,
            codeGenerator = environment.codeGenerator
        )
    }
}