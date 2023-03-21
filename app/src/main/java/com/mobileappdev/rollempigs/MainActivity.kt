package com.mobileappdev.rollempigs

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar
import com.mobileappdev.rollempigs.models.Dice
import com.mobileappdev.rollempigs.utils.DICE

class MainActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "MainActivity"
    }

    private lateinit var clRoot: ConstraintLayout
    private lateinit var player1Text: TextView
    private lateinit var player2Text: TextView
    private lateinit var roundScoreText: TextView
    private lateinit var dice1: ImageView
    private lateinit var dice2: ImageView

    private lateinit var player1: Dice
    private lateinit var player2: Dice

    private var vsComputer = false
    private var player1Turn = true
    private var gameFinished = false
    private var stopBtnEnable = false
    private var text1 = ""
    private var text2 = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        clRoot = findViewById(R.id.clRoot)
        player1Text = findViewById(R.id.player1Text)
        player2Text = findViewById(R.id.player2Text)
        roundScoreText = findViewById(R.id.roundScoreText)
        dice1 = findViewById(R.id.dice1)
        dice2 = findViewById(R.id.dice2)

        setupBoard(vsComputer)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.miRefresh -> {
                // if the game has already started, show alert message
                if(!gameFinished && (player1.getTotalScore() != 0 || player2.getTotalScore() != 0)) {
                    showAlertDialog("Quit your current game?", null, View.OnClickListener {
                        setupBoard(vsComputer)
                    })
                }
                else setupBoard(vsComputer)
            }
            R.id.miChangeMode -> {
                var mode = if(vsComputer) "Player vs Player" else "Player vs Computer"
                showAlertDialog("Change to $mode?", null, View.OnClickListener {
                    vsComputer = !vsComputer
                    setupBoard(vsComputer)
                })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun rollBtnClicked(view: View?){
        val player = if(player1Turn) player1 else player2
        if (player.haveWon()){
            Snackbar.make(clRoot, "You already won!", Snackbar.LENGTH_LONG).show()
            return
        }
        if (!vsComputer || player1Turn) stopBtnEnable = true
        if (player.rollDice() && !player.haveWon()) stopBtnClicked(view)
        if (player.haveWon()) {
            gameFinished = true
            stopBtnClicked(view)
        }
        dice1.setImageResource(DICE[player.getDice1() - 1])
        dice2.setImageResource(DICE[player.getDice2() - 1])
        roundScoreText.text = "Round Score: ${player.getRoundScore()}"
    }

    fun stopBtnClicked(view: View?){
        if(!stopBtnEnable) {
            var text = if(gameFinished) "You have already won"
            else if(vsComputer && !player1Turn) "You cannot stop when computer's turn"
            else "You have to roll a dice at least once"
            Snackbar.make(clRoot, text, Snackbar.LENGTH_LONG).show()
            return
        }

        roundScoreText.text = "Round Score: 0"
        stopBtnEnable = false

        if(player1Turn){
            player1Text.text = "$text1: ${player1.getTotalScore()}"
            player1.resetRoundScore()
            if (gameFinished) {
                player1Text.setBackgroundResource(R.color.won)
                return
            }
            player2Text.setBackgroundResource(R.color.highlight)
            player1Text.setBackgroundResource(R.color.white)
        }
        else{
            player2Text.text = "$text2: ${player2.getTotalScore()}"
            player2.resetRoundScore()
            if (gameFinished) {
                player2Text.setBackgroundResource(R.color.won)
                return
            }
            player1Text.setBackgroundResource(R.color.highlight)
            player2Text.setBackgroundResource(R.color.white)
        }
        player1Turn = !player1Turn
    }

    private fun showAlertDialog(title: String, view: View?, positiveClickListener: OnClickListener){
        AlertDialog.Builder(this)
        AlertDialog.Builder(this)
            .setTitle(title)
            .setView(view)
            .setNegativeButton("Cancel", null)
            .setPositiveButton("OK"){ _, _ ->
                positiveClickListener.onClick(null)
            }.show()
    }

    private fun setupBoard(vsComputer: Boolean){
        player1 = Dice(false)
        player2 = if (vsComputer) Dice(true) else Dice(false)
        text1 = if(vsComputer) "Player" else "Player1"
        text2 = if (vsComputer) "Computer" else "Player2"

        player1Text.text = "$text1: 0"
        player2Text.text = "$text2: 0"
        player1Text.setBackgroundResource(R.color.highlight)
        player2Text.setBackgroundResource(R.color.white)
        gameFinished = false
        stopBtnEnable = false
    }
}