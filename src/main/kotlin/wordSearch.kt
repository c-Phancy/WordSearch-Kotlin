import java.io.File

/************************************************************
 *  Name:         Christina Phan
 *  Date:         2022/03/21
 *  Assignment:   Word Search
 *  Class Number: CIS 283
 *  Description:  Create a program that generates and saves a word search using words from a file
 ************************************************************/


class PuzzleOriginal(var rows: Int, var cols: Int, fileName: String) {
    private var words = mutableListOf<String>()
    private val discardWords = mutableListOf<String>()
    private val grid = Array(rows) { Array(cols) { " . " } }
    private val letters = mutableListOf<Char>()
    private lateinit var puzzle: Array<Array<String>>

    private fun Array<Array<String>>.copy() = Array(size) { get(it).clone() }

    init {
        // adds to word list
        File(fileName).forEachLine {
            if (it.length <= rows || it.length <= cols) {
                words += it.replace(" ", "").uppercase()
            } else {
                discardWords += it.replace(" ", "").uppercase()
            }
        }
        words = words.sortedWith(compareByDescending { it.length }).toMutableList()
        // adds to letter list
        words.forEach { word -> word.forEach { character -> if (!letters.contains(character)) letters += character } }
        createPuzzle()
        // sort words alphabetically
        words.sort()
    }

    fun createPuzzle() {
        // reset board / grid so you can reuse createPuzzle
        grid.forEachIndexed { row, it -> it.forEachIndexed {col, _ -> grid[row][col] = " . " } }
        words.forEach { word ->
            var placed = false
            val validCoordinates = mutableListOf<Pair<Int, Int>>()
            grid.indices.forEach { row -> grid[row].indices.forEach { col -> validCoordinates += Pair(row, col) } }
            var coordinates = validCoordinates.removeAt(validCoordinates.indices.random())
            var row = coordinates.first
            var col = coordinates.second

            fun resetCoordinates() {
                coordinates = validCoordinates.removeAt(validCoordinates.indices.random())
                row = coordinates.first
                col = coordinates.second
            }

            do {
                val validDirections = mutableListOf("N", "NE", "E", "SE", "S", "SW", "W", "NW")
                var direction = validDirections.removeAt(validDirections.indices.random())
                var rowIncrement = when (direction) {
                    "N", "NE", "NW" -> -1
                    "S", "SE", "SW" -> 1
                    else -> 0
                }
                var colIncrement = when (direction) {
                    "NE", "E", "SE" -> 1
                    "SW", "W", "NW" -> -1
                    else -> 0
                }

                fun resetDirection() {
                    direction = validDirections.removeAt(validDirections.indices.random())
                    rowIncrement = when (direction) {
                        "N", "NE", "NW" -> -1
                        "S", "SE", "SW" -> 1
                        else -> 0
                    }
                    colIncrement = when (direction) {
                        "NE", "E", "SE" -> 1
                        "SW", "W", "NW" -> -1
                        else -> 0
                    }
                }

                fun checkAndPlace(place: Boolean): Boolean { // returns isValid
                    var counter = 0
                    word.forEach {
                        val currentRow = row + (rowIncrement * counter)
                        val currentCol = col + (colIncrement * counter)
                        if (((grid.getOrNull(currentRow) == null) || (grid[currentRow].getOrNull(currentCol) == null)) || !arrayOf(
                                " . ",
                                " $it ").contains(grid[currentRow][currentCol])
                        ) return false
                        if (place) {
                            grid[currentRow][currentCol] = " $it "
                        }
                        counter++
                    }
                    if (place) placed = true
                    return true
                }

                do {
                    if (!checkAndPlace(false)) resetDirection()
                    else checkAndPlace(true)
                } while (!placed && validDirections.isNotEmpty())

                if (!placed) {
                    resetCoordinates()
                }
            } while (!placed && validCoordinates.isNotEmpty())
            if (!placed) discardWords += word
        }
        words.removeAll { discardWords.contains(it) }
        puzzle = grid.copy()
        puzzle.forEach { row ->
            row.forEachIndexed { index, character ->
                if (character == " . ") row[index] = " ${letters.random()} "
            }
        }
    }

    fun displayPuzzleKey(): String {
        var retStr = ""
        var counter = 1
        grid.forEach { row ->
            row.forEach {
                retStr += it
                if (counter % cols == 0 && counter != 0) retStr += "\n"
                counter++
            }
        }
        return retStr
    }

    fun displayWordList(): String {
        var retStr = "Find the following" + if (words.size == 1) {
            " word"
        } else {
            " ${words.size} words"
        } + ":\n\n"
        var chunks = words.chunked(3)
        fun chunk() {
            chunks.forEach { it ->
                retStr += " ".repeat(15)
                it.forEach {
                    retStr += String.format("%-20s", it)
                }
                retStr += "\n"
            }
        }
        chunk()
        if (discardWords.isNotEmpty()) {
            retStr += "\n\nThe following" + if (discardWords.size == 1) {
                " word is "
            } else {
                " ${discardWords.size} words are "
            } + "not included:\n\n"
            chunks = discardWords.chunked(3)
            chunk()
        }
        return retStr
    }

    fun displayPuzzle(): String {
        var retStr = ""
        var counter = 1
        puzzle.forEach { row ->
            row.forEach {
                retStr += it
                if (counter % cols == 0 && counter != 0) retStr += "\n"
                counter++
            }
        }
        return retStr
    }
}

fun main() {
    var puz = PuzzleOriginal(30, 30, "src/words.txt")
    puz.createPuzzle()
    println(puz.displayPuzzle())
    println(puz.displayWordList())
    println(puz.displayPuzzleKey())

//    File("src/puzzleKey.txt").writeText(puz.displayPuzzleKey())
//    File("src/puzzle.txt").writeText(puz.displayPuzzle())
}