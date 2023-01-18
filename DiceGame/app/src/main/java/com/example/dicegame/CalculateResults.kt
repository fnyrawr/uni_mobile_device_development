package com.example.dicegame

class CalculateResults {
    // n of a kind (n=1..6)
    fun sumNofKind(n: Int, diceValues: Array<Int>): Int {
        var sum = 0
        for(i in 0..4) {
            if(diceValues[i] == n)
                sum += n
        }
        return sum
    }

    // three of a kind
    fun sumThreeOfAKind(diceValues: Array<Int>): Int {
        // check if condition is met
        var sum = 0
        var threeOfKind = false
        for(i in 1..6) {
            var count = 0
            for(j in 0..4) {
                if(diceValues[j] == i) count++

                if(count > 2) threeOfKind = true
            }
        }
        // sum values
        if(threeOfKind) {
            for(i in 0..4) {
                sum += diceValues[i]
            }
        }
        return sum
    }

    // four of a kind
    fun sumFourOfAKind(diceValues: Array<Int>): Int {
        // check if condition is met
        var sum = 0
        var fourOfKind = false
        for(i in 1..6) {
            var count = 0
            for(j in 0..4) {
                if(diceValues[j] == i) count++

                if(count > 3) fourOfKind = true
            }
        }
        // sum values
        if(fourOfKind) {
            for(i in 0..4) {
                sum += diceValues[i]
            }
        }
        return sum
    }

    // five of a kind
    fun sumFiveOfAKind(diceValues: Array<Int>): Int {
        // check if condition is met
        for(i in 1..6) {
            var count = 0
            for(j in 0..4) {
                if(diceValues[j] == i) count++

                if(count > 4) return 50
            }
        }
        return 0
    }

    // full house
    fun sumFullHouse(diceValues: Array<Int>): Int {
        // check if condition is met
        diceValues.sort()
        if(
            // first two and last two dices have same values
            (diceValues[0] == diceValues[1] && diceValues[3] == diceValues[4]) &&
            // third dice is either same value as first or last
            (diceValues[1] == diceValues[2] || diceValues[2] == diceValues[3])
        ) return 25
        return 0
    }

    // small straight
    fun sumSmallStraight(diceValues: Array<Int>): Int {
        diceValues.sort()
        println("[" + diceValues[0] + "][" + diceValues[1] + "][" + diceValues[2] + "][" + diceValues[3] + "][" + diceValues[4] + "]")
        for(i in 0..3) {
            var tmp = 0
            if(diceValues[i] == diceValues[i+1]) {
                tmp = diceValues[i]
                for(j in i..3) {
                    diceValues[j] = diceValues[j+1]
                }
                diceValues[4] = tmp
            }
        }
        if( ((diceValues[0] == 1) && (diceValues[1] == 2) && (diceValues[2] == 3) && (diceValues[3] == 4)) ||
            ((diceValues[0] == 2) && (diceValues[1] == 3) && (diceValues[2] == 4) && (diceValues[3] == 5)) ||
            ((diceValues[0] == 3) && (diceValues[1] == 4) && (diceValues[2] == 5) && (diceValues[3] == 6)) ||
            ((diceValues[1] == 1) && (diceValues[2] == 2) && (diceValues[3] == 3) && (diceValues[4] == 4)) ||
            ((diceValues[1] == 2) && (diceValues[2] == 3) && (diceValues[3] == 4) && (diceValues[4] == 5)) ||
            ((diceValues[1] == 3) && (diceValues[2] == 4) && (diceValues[3] == 5) && (diceValues[4] == 6)) ) {
            return 30
        }
        return 0
    }

    // large straight
    fun sumLargeStraight(diceValues: Array<Int>): Int {
        diceValues.sort()
        // condition can't be met if smallest number is greater than 2
        val firstFace = diceValues[0]
        if (firstFace > 2) return 0
        for(i in diceValues.indices) {
            if(diceValues[i] != i + firstFace) return 0
        }
        return 40
    }

    // sum total
    fun sumTotal(values: Array<Int>): Int {
        // sum values
        var sum = 0
        for(i in values.indices) {
            if (values[i] > 0) sum += values[i]
        }
        return sum
    }
}