import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.*

internal class TestDataBase {

    private val nonexistenceDataBasePath = "src/test/files/nonexistenceDB.db"
    private val notDataBasePath = "src/test/files/notDataBase.db"
    private val emptyDataBasePath = "src/test/files/empty.db"

    @BeforeTest
    fun setUp() {
        File(nonexistenceDataBasePath).deleteRecursively()
    }

    @AfterTest
    fun tearDown() {
        File(nonexistenceDataBasePath).deleteRecursively()
    }

    @Test
    fun testCreate() {
        val db = DataBase(nonexistenceDataBasePath)
        db.create()
        val dbFile = File(nonexistenceDataBasePath)
        assert(dbFile.isDirectory)
        assert(File("$nonexistenceDataBasePath/size").isFile)
        val list = dbFile.list()
        assert(list != null && list.size == 1)
    }

    @Test
    fun testCreate_DataBaseAlreadyExists() {
        val db = DataBase(emptyDataBasePath)
        assertFails {
            db.create()
        }
    }

    @Test
    fun testCreate_PathIsNotDataBase() {
        val db = DataBase(notDataBasePath)
        assertFails {
            db.create()
        }
    }

    @Test
    fun testOpen_NotDataBase() {
        val db = DataBase(notDataBasePath)
        assertFails {
            db.open()
        }
    }

    @Test
    fun testOpen() {
        val db = DataBase(emptyDataBasePath)
        db.open()
        assertEquals(0, db.size)
    }

    @Test
    fun testDeleteDB_Unopened() {
        val db = DataBase(emptyDataBasePath)
        assertFails {
            db.deleteDB()
        }
    }

    @Test
    fun testDeleteDB() {
        val db = DataBase(nonexistenceDataBasePath)
        db.create()
        db.deleteDB()
        assert(!File(nonexistenceDataBasePath).exists())
    }

    @Test
    fun testStore() {
        val db = DataBase(nonexistenceDataBasePath)
        db.create()
        db.store("key", "value")
        val list = File(nonexistenceDataBasePath).list()!!
        assertEquals(list.size, 2)
        list.forEach {
            if (it != "size") {
                val actual = File("$nonexistenceDataBasePath/$it").readText().trim()
                val expected = "key${db.splitChar}value"
                assertEquals(expected, actual)
            }
        }
    }

    @Test
    fun testFetch_nonexistenceKey() {
        val db = DataBase(emptyDataBasePath)
        db.open()
        assertNull(db.fetch("key"))
    }

    @Test
    fun testFetch() {
        val db = DataBase(nonexistenceDataBasePath)
        db.create()
        db.store("key1", "value1")
        db.store("key2", "value2")
        assertEquals(db.fetch("key2"), "value2")
    }

    @Test
    fun testDeleteKey_nonexistenceKey() {
        val db = DataBase(emptyDataBasePath)
        db.open()
        assertFails {
            db.deleteKey("key")
        }
    }

    @Test
    fun testDeleteKey() {
        val db = DataBase(nonexistenceDataBasePath)
        db.create()
        db.store("key", "value")
        db.deleteKey("key")
        assertEquals(1, File(nonexistenceDataBasePath).list()!!.size)
    }
}