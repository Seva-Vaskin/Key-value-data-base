import java.io.File

/**
 * Describes bucket of database
 */
data class Bucket(val map: MutableMap<String, String>, val hash: Int)

/**
 * Describes database object
 */
class DataBase(private var path: String = "") {
    private fun sizeFile() = File("$path/size")
    private val nameExtension = ".db"
    val splitChar = '\t'
    var size = -1
        private set
    private var isOpened = false

    init {
        fixName()
    }

    /**
     * Loads database size from file
     */
    private fun loadSize() {
        size = sizeFile().readText().toIntOrNull() ?: throw Exception("Database's size file is broken")
    }

    /**
     * Uploads database size to file
     */
    private fun uploadSize() {
        if (!sizeFile().exists())
            sizeFile().createNewFile()
        sizeFile().writeText(size.toString())
    }

    /**
     * Check that database path suits for database naming
     */
    private fun isGoodName(): Boolean {
        return path.endsWith(nameExtension)
    }

    /**
     * Makes database path suitable for database naming
     */
    private fun fixName() {
        if (!isGoodName())
            path += nameExtension
    }

    /**
     * Check that given path suits for database format
     */
    private fun isDataBase(): Boolean {
        return File(path).isDirectory && isGoodName() && sizeFile().isFile
    }

    /**
     * Throw an exception if database isn't opened
     */
    private fun checkOpened() {
        if (!isOpened)
            throw Exception("Database wasn't opened")
    }

    /**
     * Loads bucket of given key.
     * if file is broken throws an exception
     */
    private fun loadBucket(key: String): Bucket {
        val hash = getHash(key)
        val file = File("$path/$hash")
        val map = mutableMapOf<String, String>()
        if (!file.exists())
            return Bucket(map, hash)
        file.readLines().forEach { line ->
            val lineValues = line.split(splitChar)
            if (lineValues.size != 2) {
                throw Exception("bucket $hash is broken")
            }
            map[lineValues.component1()] = lineValues.component2()
        }
        return Bucket(map, hash)
    }

    /**
     * Uploads given bucket into file
     */
    private fun uploadBucket(bucket: Bucket) {
        val file = File("$path/${bucket.hash}")
        if (!file.exists())
            file.createNewFile()
        if (bucket.map.isEmpty())
            file.delete()
        bucket.map.forEach { (key, value) ->
            file.writeText("$key$splitChar$value\n")
        }
    }

    /**
     * Fetches value of given key.
     * Returns null if key wasn't found
     */
    fun fetch(key: String): String? {
        checkOpened()
        val bucket = loadBucket(key)
        return bucket.map[key]
    }

    /**
     * Stores (key, value) pair into database
     */
    fun store(key: String, value: String) {
        checkOpened()
        val bucket = loadBucket(key)
        if (!bucket.map.containsKey(key))
            size++
        bucket.map[key] = value
        uploadSize()
        uploadBucket(bucket)
    }

    /**
     * Deletes given key from database.
     * Throws an exception if key wasn't in database
     */
    fun deleteKey(key: String) {
        checkOpened()
        val bucket = loadBucket(key)
        if (!bucket.map.containsKey(key)) {
            throw Exception("database doesn't contain key \"$key\"")
        }
        bucket.map.remove(key)
        size--
        uploadSize()
        uploadBucket(bucket)
    }

    /**
     * Opens database
     */
    fun open() {
        if (!isDataBase()) {
            throw Exception("Given path \"$path\" is not database")
        }
        loadSize()
        isOpened = true
    }

    /**
     * Creates an empty database.
     * If it is not possible create database throws an exception
     */
    fun create() {
        if (isDataBase()) {
            throw Exception("Database $path already exists")
        }
        if (File(path).exists()) {
            throw  Exception("File with name \"$path\" already exists")
        }
        if (!File(path).mkdir()) {
            throw Exception("Can't create \"$path\" database")
        }
        size = 0
        uploadSize()
        isOpened = true
    }

    /**
     * Deletes database.
     * If it is not possible to delete database throws an exception
     */
    fun deleteDB() {
        if (!isOpened) {
            throw Exception("Can't delete unopened database")
        }
        if (!isDataBase()) {
            throw Exception("\"$path\" is not a database")
        }
        if (!File(path).deleteRecursively()) {
            throw Exception("Can't delete \"$path\" database")
        }
        size = -1
        isOpened = false
    }
}