package nl.parlio.api.tweedekamer.audit.types

enum class ChangeEventModel(val shortName: String) {
    PERSON("p"),
    PERSON_GIFT("pg"),
    PERSON_TRIP("pt");

    companion object {
        private val BY_SHORT_NAME: Map<String, ChangeEventModel> =
            values().associateBy { it.shortName }

        fun valueOfShortName(shortName: String): ChangeEventModel {
            return BY_SHORT_NAME[shortName]
                ?: throw IllegalArgumentException("Argument '$shortName' is not a ChangeEventModel")
        }
    }
}
