import java.io.File
import kotlin.random.Random.Default.nextInt

/************************************************************
 *  Name:         Christina Phan
 *  Date:         2022/03/21
 *  Assignment:   Word Search Extra Credit
 *  Class Number: CIS 283
 *  Description:  Create a program that generates a word search using words from a file in under 10ms
 ************************************************************/

class Puzzle(private var rows: Int, private var cols: Int, fileName: String) {
    private val startingChar = '.'
    private val words = mutableListOf<String>()
    private val discard = mutableListOf<String>()
    private var keyGrid = Array(rows) { Array(cols) { startingChar } } // key to puzzle
    private val file = fileName
    private lateinit var puzzleGrid: Array<Array<Char>> // filled puzzle

    init {
        File(file).forEachLine { words += it.replace(" ", "").uppercase() }
        createPuzzle()
    }

    private fun createPuzzle() {
        words.forEachIndexed { index, word ->
            if (word.length > rows || word.length > cols) {
                discard += words.removeAt(index)
                return@forEachIndexed
            }
            var placed = false
            do {
                val coordinates = Pair(nextInt(rows), nextInt(cols))
                val validDirections = mutableListOf(Pair(-1, 0), // N
                    Pair(-1, 1), // NE
                    Pair(0, 1), // E
                    Pair(1, 1), // SE
                    Pair(1, 0), // S
                    Pair(1, -1), // SW
                    Pair(0, -1), // W
                    Pair(-1, -1)) // NW
                var direction = validDirections.removeAt(validDirections.indices.random())

                fun checkAndPlace(place: Boolean): Boolean { // returns isValid
                    word.forEachIndexed { index, it ->
                        val currentRow = coordinates.first + (direction.first * index)
                        val currentCol = coordinates.second + (direction.second * index)
                        val exists = keyGrid.getOrNull(currentRow)?.getOrNull(currentCol)
                        if (exists != startingChar && exists != it) return false
                        if (place) keyGrid[currentRow][currentCol] = it
                    }
                    return true
                }

                do {
                    if (!checkAndPlace(false)) direction = validDirections.removeAt(validDirections.indices.random())
                    else placed = checkAndPlace(true)
                } while (!placed && validDirections.isNotEmpty())
            } while (!placed)
        }
        val letters = mutableListOf<Char>()
        words.forEach { word -> word.forEach { character -> if (!letters.contains(character)) letters += character } }

        // copy() is SLOW
            // Can be taken out if the keyGrid is replaced using a function that inserts random characters on display()
                // This means keyGrid would not be saved AKA would change every time it is displayed
        fun Array<Array<Char>>.copy() = Array(size) { get(it).clone() }
        puzzleGrid = keyGrid.copy()
        puzzleGrid.forEach { row ->
            row.forEachIndexed { index, character ->
                if (character == startingChar) row[index] = letters.random()
            }
        }
    }

    fun display(puzzleGrid: Array<Array<Char>>): String {
        var retStr = ""
        var counter = 1
        puzzleGrid.forEach { row ->
            row.forEach {
                retStr += " $it "
                if (counter % cols == 0) retStr += "\n"
                counter++
            }
        }
        return "$retStr\n${displayWordList()}"
    }

    // access grids for display() from main()
    fun getGrid(puzzleOrKey: String): Array<Array<Char>> {
        return if (puzzleOrKey.lowercase() == "key") keyGrid else puzzleGrid
    }

    private fun displayWordList(): String {
        var retStr = "Find the following" + if (words.size == 1) {
            " word"
        } else {
            " ${words.size} words"
        } + ":\n\n"
        words.chunked(3).forEach { it ->
            retStr += " ".repeat(15)
            it.forEach {
                retStr += String.format("%-20s", it)
            }
            retStr += "\n"
        }
        return retStr
    }

    // class should not savePuzzle (not in main for testing purposes)
    fun savePuzzle() {
        File("src/puzzleKey.txt").writeText(display(keyGrid))
        File("src/puzzle.txt").writeText(display(puzzleGrid))
    }
}


fun main() {
    val start = System.currentTimeMillis()
    for (i in 1..10) {
        Puzzle(30, 30, "src/words.txt")
    }
    val totalTime = System.currentTimeMillis() - start
    println("Total Time to create 10 puzzles: $totalTime milli-seconds")
    println("Average Time to create 1 puzzle: ${totalTime / 10} milli-seconds")

//    Puzzle(30, 30, "src/words.txt").savePuzzle()
}