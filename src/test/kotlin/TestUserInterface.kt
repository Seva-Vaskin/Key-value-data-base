import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.test.BeforeTest
import kotlin.test.AfterTest
import kotlin.test.assertFails

internal class TestUserInterface {
    private val standardOut = System.out
    private val standardIn = System.`in`
    private val myOut = ByteArrayOutputStream()

    @BeforeTest
    fun setUp() {
        System.setOut(PrintStream(myOut))
    }

    @AfterTest
    fun tearDown() {
        System.setOut(standardOut)
        System.setIn(standardIn)
    }

    @Test
    fun testGetCommand_EmptyInput() {
        val myIn = ByteArrayInputStream("".toByteArray())
        System.setIn(myIn)
        assertFails {
            getCommand()
        }
    }

    private fun isEqual(command1: Command, command2: Command): Boolean {
        return command1.commandName == command2.commandName && command1.arguments == command2.arguments
    }


    @Test
    fun testGetCommand_NoArgumentsCommand() {
        val myIn = ByteArrayInputStream("quit".toByteArray())
        System.setIn(myIn)
        val actual = getCommand()
        val expected = Command(CommandNames.Quit, listOf())
        assert(isEqual(expected, actual))
    }

    @Test
    fun testGetCommand_CommandWithArguments() {
        val myIn = ByteArrayInputStream("store a b".toByteArray())
        System.setIn(myIn)
        val actual = getCommand()
        val expected = Command(CommandNames.Store, listOf("a", "b"))
        assert(isEqual(expected, actual))
    }

    @Test
    fun testGetCommand_MissedArguments() {
        val myIn = ByteArrayInputStream("store a".toByteArray())
        System.setIn(myIn)
        assertFails {
            getCommand()
        }
    }

    @Test
    fun testGetCommand_ExtraArguments() {
        val myIn = ByteArrayInputStream("fetch a b".toByteArray())
        System.setIn(myIn)
        assertFails {
            getCommand()
        }
    }

    @Test
    fun testGetCommand_UpperCaseInput() {
        val myIn = ByteArrayInputStream("OPEN Q".toByteArray())
        System.setIn(myIn)
        val actual = getCommand()
        val expected = Command(CommandNames.Open, listOf("Q"))
        assert(isEqual(expected, actual))
    }

    @Test
    fun testGetCommand_IncorrectCommandName() {
        val myIn = ByteArrayInputStream("incorrectName".toByteArray())
        System.setIn(myIn)
        assertFails {
            getCommand()
        }
    }

}