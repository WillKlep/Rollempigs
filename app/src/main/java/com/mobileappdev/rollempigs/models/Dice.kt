package com.mobileappdev.rollempigs.models

import android.util.Log
import kotlin.math.round
import kotlin.random.Random
import kotlin.random.nextInt

class Dice (isComputer: Boolean, private val WIN_SCORE: Int = 100) {

    // explain
    companion object{
        private const val COMPUTER_MAX_SCORE = 20
    }

    private var totalScore = 0
    private var roundScore = 0
    private var dice1 = 1
    private var dice2 = 1
    private val isComputer = isComputer

    // rolls two dice
    // if either of them is 1, return true since there turn is over
    // if it's for computer, then if the roundScore exceeds the maximum, return true as well
    // otherwise return false indicating that they can roll again
    fun rollDice(): Boolean{
        dice1 = Random.nextInt(1,7)
        dice2 = Random.nextInt(1,7)

        // if both of them are 1, the total is set back to 0
        if(dice1 == 1 && dice2 == 1){
            totalScore = 0
            roundScore = 0
            return true
        }
        // if one of them is 1, current turn is lost
        if(dice1 == 1 || dice2 == 1){
            roundScore = 0
            return true
        }
        roundScore += dice1 + dice2
        if(isComputer && roundScore >= COMPUTER_MAX_SCORE) return true
        return false
    }

    fun resetRoundScore(){ roundScore = 0 }

    fun getTotalScore(): Int {
        updateScore()
        return totalScore
    }

    fun getRoundScore(): Int { return roundScore }

    fun getDice1(): Int { return dice1 }

    fun getDice2(): Int { return dice2 }

    fun haveWon(): Boolean { return totalScore + roundScore >= WIN_SCORE }

    private fun updateScore(){
        totalScore += roundScore
    }
}