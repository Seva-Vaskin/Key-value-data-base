const val MultiplyNumber = 257L
const val ModuloNumber = 1009L

fun getHash(key: String): Int {
    var hash = 0
    key.forEach {
        hash = ((hash.toLong() * MultiplyNumber + it.code) % ModuloNumber).toInt()
    }
    return hash
}