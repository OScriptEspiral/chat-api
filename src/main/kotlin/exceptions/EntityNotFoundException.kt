package exceptions

class EntityNotFoundException(
    override val message: String = "Não foi possível encontrar nenhum registro!") : Exception(message)