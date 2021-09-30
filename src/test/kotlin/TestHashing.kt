import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class TestHashing {

    @Test
    fun testConstants() {
        fun isPrime(n: Long): Boolean {
            var d = 2L
            while (d * d <= n && n % d != 0L)
                d++
            return n >= 2L && d * d > n
        }
        assert(MultiplyNumber > 256) // prime number have to be greater than max character value
        assert(isPrime(MultiplyNumber)) // multiply number have to be prime
        assert(isPrime(ModuloNumber)) // modulo number have to be prime
    }

    @Test
    fun testGetHash_EmptyString() {
        val actual = getHash("")
        val expected = 0
        assertEquals(expected, actual)
    }
    @Test
    fun testGetHash_SingleCharacter() {
        val actual = getHash("d")
        val expected = 100
        assertEquals(expected, actual)
    }

    @Test
    fun testGetHash_TwoCharacters() {
        val actual = getHash("!~")
        val expected = ((33 * MultiplyNumber + 126) % ModuloNumber).toInt()
        assertEquals(expected, actual)
    }

    @Test
    fun testGetHash() {
        val actual = getHash("!3@G^k~")
        var expected = 0 // empty string
        expected = ((expected * MultiplyNumber + 33) % ModuloNumber).toInt() // add '!' to hash
        expected = ((expected * MultiplyNumber + 51) % ModuloNumber).toInt() // add '3' to hash
        expected = ((expected * MultiplyNumber + 64) % ModuloNumber).toInt() // add '@' to hash
        expected = ((expected * MultiplyNumber + 71) % ModuloNumber).toInt() // add 'G' to hash
        expected = ((expected * MultiplyNumber + 94) % ModuloNumber).toInt() // add '^' to hash
        expected = ((expected * MultiplyNumber + 107) % ModuloNumber).toInt() // add 'k' to hash
        expected = ((expected * MultiplyNumber + 126) % ModuloNumber).toInt() // add '~' to hash
        assertEquals(expected, actual)
    }

}