package rustyice.game

class MissingParentReference(parent: String):
        IllegalStateException("Can't use method without access $parent.")