fun main(args: Array<String>) {
    if ("--help" in args) {
        printHelp()
        return
    }
    try {
        runShell()
    } catch (e: Exception) {
        System.err.println("Something went wrong :(")
        System.err.println(e.message)
    }
}
