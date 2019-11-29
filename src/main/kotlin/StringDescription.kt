class StringDescription(override val key: String) : PropertyDescription {
    override val type: Any
        get() {
            return String
        }
}