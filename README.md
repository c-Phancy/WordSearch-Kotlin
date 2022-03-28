Assignment Instructions:

Write a program that will read a list of words from data file called “words.txt”.  This file should contain one word per line.  words.txt Download words.txt

Create a class for this structure called "Puzzle" and utilize methods and variables appropriately in this class.  NO PRINTING DIRECTLY FROM THE CLASS – the class methods should only return strings which the main program will utilize/print, etc.

This list of words will then need to be put into a grid (double/multi-dimensional array) whose size is 45 x 45 where each word is randomly inserted into the array in one of the following directions:

         Horizontal, vertical, diagonal-right, diagonal-left

and their corresponding backwards equivalents.  There would be a total of 8 possible directions for a word.

Once all the words are inserted into the array, then your program should fill in all the blank cells with random letters.  Use ONLY letters that are contained in the Words from the word list.

Once your array is filled, save the resulting puzzle along with the answer key, which would be the array without the random letters.  The puzzle output must be created in 2 separate files: puzzle.txt and puzzleKey.txt.  First file is the puzzle with the words to find under the puzzle.  The second is the KEY.  See example files:

puzzle.txt Download puzzle.txt

puzzleKey.txt Download puzzleKey.txt



Algorithm

Read the list of words and then sort them by the length of each word from largest to smallest.

This makes it easier to fit in the larger words first.  You can use something like this to sort an array called “words”:

         words.sortedArrayDescending()
Select a random row/col to start your placement of the word.

Choose a random “direction” to begin placement.

Start test-placing letters into the cells in the chosen direction.  You can only test-place a letter if the current cell is either empty or contains the correct letter in the correct position.  Make sure to test for boundaries on all sides of the puzzle array.

If you cannot continue for any reason start over with a new random direction for that word.  Don’t reuse a direction for the same word.

Once you can successfully place a word, place the word permanently and move on to the next word.

Some code might look like this:

         for( word in words ){
         loop until word is placed ...
         ... get a random starting row/col and direction
         if( testPlace( word, row, col, direction ) ){
         placeWord( word, row, col, direction )
         }
         ...
         }
Example:

Given a list of words in a file like:

Apple

Banana

Orange

Cantaloupe

Kiwi

Your output would be something like this.  (Example is much smaller scale)

Puzzle.txt

         A T R P P L A S O I U
         Z M N O P T W E E R F
         C I L O U P S I W I T
         Y W A D B Y E J K L H
         H I A F A G G H J K G
         D K S H N F G H J K D
         A Q S A A W E R Y H E
         D W R J N E R T Y U E
         H O O J A P P L E I A
         B E O P A E R T Y P S
         V D I O D W E G A E Q
         E P U O L A T N A C D

         List of Words to Find:
         Apple                  Cantaloupe
         Banana                 Kiwi
         Orange
------------------------------------------------

Answer Key

PuzzleKey.txt

         . . . . . . . . . . .
         . . . . . . . . . . .
         . I . . . . . . . . .
         . W . . B . E . . . .
         . I . . A G . . . . .
         . K . . N . . . . . .
         . . . A A . . . . . .
         . . R . N . . . . . .
         . O . . A P P L E . .
         . . . . . . . . . . .
         . . . . . . . . . . .
         E P U O L A T N A C .

         List of Words to Find:
         Apple                  Cantaloupe
         Banana                 Kiwi
         Orange
words.txt Download words.txt

Upload 4 files:  wordSearch.kt, words.txt, puzzle.txt, puzzleKey.txt

Upload YOUR generated files, not my example files.

Extra Credit Assignment Instructions:

To qualify for the extra credit, you must first submit a fully operational final project to the Final Assignment.  This is a separate submission.

Once that is complete, your puzzle class must have  variable row and column parameters so that smaller puzzles can be created. Similar to this:

         var puz = Puzzle(30, 30, "src/words.txt")
In order to receive all of the points for the extra credit, your code must be close to as fast as my code.  My results are:

         Total Time to create 10 puzzles: 22 milli-seconds
         Average Time to create 1 puzzle: 2 milli-seconds
Yours must be at least 10 milli-seconds (or less) average for your code execution.  Your "createPuzzle()" must do all of the tasks necessary to create a full puzzle and key like reading in the words list, placing all the words, etc.

Your main function for the extra credit can look something like this to measure your execution time.  NOTE: the puzzle row/col are both set to 30 instead of 45

         fun main(){
         val start = System.currentTimeMillis()
         for( i in 1..10) {
         var puz = Puzzle(30, 30, "src/words.txt")
         puz.createPuzzle()
         }
         var totalTime = System.currentTimeMillis() - start
         println( "Total Time to create 10 puzzles: $totalTime milli-seconds" )
         println( "Average Time to create 1 puzzle: ${totalTime / 10} milli-seconds" )
         }