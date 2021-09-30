const val primeNumber = 257L
const val ModuloNumber = 1009L

fun getHash(key: String): Int {
    var hash = 0
    key.forEach {
        hash = ((hash.toLong() * primeNumber + it.code) % ModuloNumber).toInt()
    }
    return hash
}