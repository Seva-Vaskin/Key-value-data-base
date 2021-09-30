import java.io.File


fun main(args: Array<String>) {
    if ("--help" in args) {
        println(File("src/files/HelpMessage.txt").readText())
        return
    }

}
