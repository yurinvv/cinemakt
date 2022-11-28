package cinema

const val smallScreenRoom = 60
const val ticketPrice1 = 10
const val ticketPrice2 = 8
const val seat = 'S'
const val boughtSeat = 'B'

//Menu items
const val SHOW_SEATS = 1
const val BUY_SEAT = 2
const val STATE = 3
const val EXIT = 0
const val NONE = -1

var purchasedTickets = 0
var currentIncome = 0
var totalIncome = 0
var totalSeats = 0


fun main() {
    println("Enter the number of rows:")
    val rows = readLine()!!.toInt()
    println("Enter the number of seats in each row:")
    val columns = readLine()!!.toInt()

    totalIncome = totalIncome(rows, columns)
    totalSeats = rows * columns

    val screenRoom: MutableList<MutableList<Char>> = createScreenRoom(rows, columns)

    var item = NONE

    while (item != EXIT) {
        menuHandler(item, screenRoom)

        println("""
            
            1. Show the seats
            2. Buy a ticket
            3. Statistics
            0. Exit
            
        """.trimIndent())

        item = readLine()!!.toInt()
    }
}

fun createScreenRoom(rows: Int, columns: Int): MutableList<MutableList<Char>> {
    return MutableList(rows) { MutableList(columns) { seat } }
}

fun printScreenRoom(screenRoom: MutableList<MutableList<Char>> ) {
    print("Cinema:\n ")

    for (i in 1..screenRoom[0].size) print(" $i")
    println()

    for (i in 0 until screenRoom.size) {
        println("${i + 1} ${screenRoom[i].joinToString(" ")}")
    }
    println()
}

fun buySeat(screenRoom: MutableList<MutableList<Char>>) {
    var done = false

    while (!done) {
        try {
            println("Enter a row number:")
            var row = readLine()!!.toInt()
            println("Enter a seat number in that row:")
            var column = readLine()!!.toInt()

            if (row !in 1..screenRoom.size || column !in 1..screenRoom[0].size) {
                throw Exception()
            }

            if (screenRoom[row - 1][column - 1] == boughtSeat) {
                println("That ticket has already been purchased!")
            } else {
                screenRoom[row - 1][column - 1] = boughtSeat
                purchasedTickets++
                val price = getSeatPrice(screenRoom, row, column)
                currentIncome += price

                println("Ticket price: \$${price}")
                done = true
            }
        } catch (e: Exception) {
            println("Wrong input!")
        }
    }

}

fun getSeatPrice(screenRoom: MutableList<MutableList<Char>>, row: Int, column: Int): Int {
    val totalNumber = screenRoom.size * screenRoom[0].size
    return if (totalNumber <= smallScreenRoom || screenRoom.size / 2 >= row) {
        ticketPrice1
    } else {
        ticketPrice2
    }
}

fun totalIncome(rows: Int, columns: Int): Int {
    val totalNumber = rows * columns

    return if (totalNumber <= smallScreenRoom) {
        totalNumber * ticketPrice1
    } else {
        val firstHalf = rows / 2
        val secondHalf = rows - firstHalf
        firstHalf * columns * ticketPrice1 + secondHalf * columns * ticketPrice2
    }
}

fun printStatistics() {

    val percentage: Float = try {
        ((purchasedTickets.toFloat() * 100) / totalSeats.toFloat())
    } catch (e: ArithmeticException) {
        0.00F
    }

    println("""
        Number of purchased tickets: $purchasedTickets
        Percentage: ${"%.2f".format(percentage).replace(',','.')}% 
        Current income: $$currentIncome
        Total income: $$totalIncome
        
    """.trimIndent())
}

fun menuHandler(state: Int, screenRoom: MutableList<MutableList<Char>>) {
    when (state) {
        SHOW_SEATS -> printScreenRoom(screenRoom)
        BUY_SEAT -> buySeat(screenRoom)
        STATE -> printStatistics()
    }
}
