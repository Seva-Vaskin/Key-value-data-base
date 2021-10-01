import java.io.File

/**
 * Describes possible user's command names for the program.
 * argc means number of arguments needed for each command
 */
enum class CommandNames(val argc: Int) {
    Quit(0),
    Help(0),
    Fetch(1),
    Delete(1),
    Store(2),
    Create(1),
    DelDB(0),
    Open(1);

    override fun toString(): String {
        return name.lowercase()
    }
}

/**
 * Describes program commands
 */
class Command(val commandName: CommandNames, val arguments: List<String>) {}

/**
 * Prints help message
 */
fun printHelp() {
    println(File("src/files/HelpMessage.txt").readText())
}

fun runFetch(database: DataBase, arguments: List<String>) {
    val result = database.fetch(arguments[0]) ?: throw Exception("No item \"${arguments[0]}\" in database")
    println(result)
}

fun runDelete(database: DataBase, arguments: List<String>) {
    database.deleteKey(arguments[0])
}

fun runStore(database: DataBase, arguments: List<String>) {
    database.store(arguments[0], arguments[1])
}

fun runCreate(arguments: List<String>): DataBase {
    val database = DataBase(arguments[0])
    database.create()
    return database
}

fun runDelDB(database: DataBase): DataBase {
    database.deleteDB()
    return database
}

fun runOpen(arguments: List<String>): DataBase {
    val database = DataBase(arguments[0])
    database.open()
    return database
}

/**
 * Splits string of arguments to list of arguments
 * 1) It splits arguments by space symbol
 * 2) It parses strings in double quotes to one argument
 * Note: Use \" to write double quotes inside argument
 * Note: Use \\ to write backslash symbol
 */
fun Split(str: String): List<String> {
    val builder = StringBuilder()
    val list = mutableListOf<String>()
    var isWaitingQuotes = false
    var isAfterBackslash = false
    str.forEach {
        when {
            isAfterBackslash -> {
                builder.append(it)
                isAfterBackslash = false
            }
            isWaitingQuotes && it == '"' -> {
                isWaitingQuotes = false
                if (builder.isEmpty()) {
                    throw Exception("Can't use empty string as argument")
                }
                list.add(builder.toString())
                builder.clear()
            }
            it == '\\' -> isAfterBackslash = true
            it == '"' -> isWaitingQuotes = true
            it == ' ' && !isWaitingQuotes -> {
                if (builder.isNotEmpty())
                    list.add(builder.toString())
                builder.clear()
            }
            else -> builder.append(it)
        }
    }
    if (isWaitingQuotes) {
        throw Exception("Quotes aren't closed. Note: use \\\" to use \" as symbol in text")
    }
    if (isAfterBackslash) {
        throw Exception("Unexpected \\ symbol. Note: use \\\\ to write \\ symbol")
    }
    if (builder.isNotEmpty())
        list.add(builder.toString())
    return list
}

/**
 * Gets command from console.
 * Throw an exception if command wasn't defined
 */
fun getCommand(): Command {
    // get command from input
    val commandString: String = readLine() ?: throw Exception("Can't read command")
    // make list from input string
    val list = Split(commandString)
    if (list.isEmpty())
        throw Exception("Can't process an empty command")
    // Determine a command name
    val commandNameString = list[0].lowercase()
    var commandName: CommandNames? = null
    CommandNames.values().forEach {
        if (it.toString() == commandNameString)
            commandName = it
    }
    if (commandName == null)
        throw Exception("Command \"$commandNameString\" doesn't exist. Use \"help\" to see possible commands")
    // check arguments count
    if (commandName!!.argc != list.size - 1)
        throw Exception(
            "Command \"${commandName.toString()}\" needs ${commandName!!.argc} arguments, " +
                    "but ${list.size - 1} was given"
        )
    val arguments = if (list.size == 1) listOf() else list.subList(1, list.size)
    return Command(commandName!!, arguments)
}

/**
 * Runs shell.
 * Gets and processes commands from user
 */
fun runShell() {
    var database = DataBase()
    while (true) {
        print("${if (database.isOpened) database.path else "KVDB"}> ")
        try {
            val command = getCommand()
            when (command.commandName) {
                CommandNames.Quit -> break
                CommandNames.Help -> printHelp()
                CommandNames.Fetch -> runFetch(database, command.arguments)
                CommandNames.Delete -> runDelete(database, command.arguments)
                CommandNames.Store -> runStore(database, command.arguments)
                CommandNames.Create -> database = runCreate(command.arguments)
                CommandNames.DelDB -> database = runDelDB(database)
                CommandNames.Open -> database = runOpen(command.arguments)
            }
        } catch (e: Exception) {
            println(e.message)
        }
    }
}