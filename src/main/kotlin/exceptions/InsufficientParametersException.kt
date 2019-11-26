package exceptions

class InsufficientParametersException(
    override val message: String = "Informe ao menos um parametro!") : Exception(message)