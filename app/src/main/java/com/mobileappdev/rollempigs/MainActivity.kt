package com.mobileappdev.rollempigs

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.mobileappdev.rollempigs.models.Dice
import com.mobileappdev.rollempigs.utils.DICE
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    companion object{
        private const val TAG = "MainActivity"
    }

    private lateinit var player1Text: TextView
    private lateinit var player2Text: TextView
    private lateinit var roundScoreText: TextView
    private lateinit var rollBtn: Button
    private lateinit var stopBtn: Button
    private lateinit var dice1: ImageView
    private lateinit var dice2: ImageView

    private lateinit var player1: Dice
    private lateinit var player2: Dice

    private var vsComputer: Boolean = false
    private var player1Turn = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        player1Text = findViewById(R.id.player1Text)
        player2Text = findViewById(R.id.player2Text)
        roundScoreText = findViewById(R.id.roundScoreText)
        rollBtn = findViewById(R.id.rollBtn)
        stopBtn = findViewById(R.id.stopBtn)
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
                if(player1.getTotalScore() != 0 || player2.getTotalScore() != 0) {
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
        if (player.rollDice()){
            stopBtnClicked(view)
        }
        dice1.setImageResource(DICE[player.getDice1() - 1])
        dice2.setImageResource(DICE[player.getDice2() - 1])
        roundScoreText.text = "Round Score: ${player.getRoundScore()}"
    }

    fun stopBtnClicked(view: View?){
        if(player1Turn){
            player1Text.text = "Player1: ${player1.getTotalScore()}"
            player1.resetRoundScore()
            if(vsComputer) stopBtn.isEnabled = false
//            player1Text.setBackgroundColor(R.color.white as Int)
//            player2Text.setBackgroundColor(R.color.highlight as Int)
        }
        else{
            player2Text.text = "Player2: ${player2.getTotalScore()}"
            player2.resetRoundScore()
            if (vsComputer) stopBtn.isEnabled = true
//            player2Text.setBackgroundColor(R.color.white as Int)
//            player1Text.setBackgroundColor(R.color.highlight as Int)
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

    private fun showChangeModeDialog(){
        var mode = if(vsComputer) "Player vs Player" else "Player vs Computer"
        //showAlertDialog("Change to $mode", null)
    }

    private fun setupBoard(vsComputer: Boolean){
        player1 = Dice(false)
        player2 = if (vsComputer) Dice(true) else Dice(false)

        player1Text.text = "Player1: 0"
        player2Text.text = "Player2: 0"
//        player1Text.setBackgroundColor(R.color.highlight as Int)
//        player2Text.setBackgroundColor(R.color.white as Int)
    }
}