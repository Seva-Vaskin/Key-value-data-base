import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertFalse

internal class TestBigDataBase {
    private val bigDataBasePath = "src/test/files/big.db"
    private val maxDataBaseSize = 1e5.toInt()

    @BeforeTest
    fun setUp() {
        File(bigDataBasePath).deleteRecursively()
    }

    @Test
    fun bigTest() {
        val dataBase = DataBase(bigDataBasePath)
        dataBase.create()
        for (i in 1..maxDataBaseSize) {
            dataBase.store(i.toString(), i.toString())
            assertEquals(i, dataBase.size)
        }
        for (i in 1..maxDataBaseSize) {
            val fetch = dataBase.fetch(i.toString())
            assertEquals(i.toString(), fetch)
        }
        for (i in maxDataBaseSize downTo 1) {
            assertEquals(i, dataBase.size)
            dataBase.deleteKey(i.toString())
        }
        dataBase.deleteDB()
        assertFalse(File(bigDataBasePath).exists())
    }
}