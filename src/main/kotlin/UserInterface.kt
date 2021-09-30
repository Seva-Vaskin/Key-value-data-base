enum class Commands(val str: String) {
    Quit("quit"),
    FetchKey("fetch"),
    DeleteKey("delete"),
    Store("store"),
    CreateNewDataBase("newdb"),
    DeleteDataBase("deldb"),
    OpenDataBase("open"),
}

fun processCommand(command: Commands) {
    TODO()
}