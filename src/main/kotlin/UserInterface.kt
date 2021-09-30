enum class Commands(val str: String) {
    Quit("quit"),
    Fetch("fetch"),
    DeleteKey("delete"),
    Store("store"),
    CreateNewDataBase("newdb"),
    DeleteDataBase("deldb"),
    OpenDataBase("open"),
    CloseDataBase("close"),
}

fun processCommand(command: Commands) {
    TODO()
}