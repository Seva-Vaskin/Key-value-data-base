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
    NewDB(1),
    DelDB(1),
    Open(1),
    Rename(2);

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

/**
 * Processes given command.
 * Returns true if the program must be closed
 */
fun processCommand(command: Command): Boolean {
    try {
        when (command.commandName) {
            CommandNames.Quit -> return true
            CommandNames.Help -> printHelp()
            CommandNames.Fetch -> TODO()
            CommandNames.Delete -> TODO()
            CommandNames.Store -> TODO()
            CommandNames.NewDB -> TODO()
            CommandNames.DelDB -> TODO()
            CommandNames.Open -> TODO()
            CommandNames.Rename -> TODO()
        }
    } catch (e: Exception) {
        println(e.message)
    }
    return false
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
        throw Exception("Command \"$commandName\" doesn't exist. Use \"help\" to see possible commands\"")
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
    while (true) {
        try {
            val command = getCommand()
            if (processCommand(command))
                return
        } catch (e: Exception) {
            println(e.message)
        }
    }
}