package com.zybooks.bird

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog



class MainActivity : AppCompatActivity(), View.OnClickListener {


    var playerOneActive = false
    private var playerOneScore: TextView? = null
    private var playerTwoScore: TextView? = null
    private var playerStatus: TextView? = null
    private val buttons = arrayOfNulls<Button>(9)
    private var reset: Button? = null
    private var playagain: Button? = null
    var gameState = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)
    var winningPositions = arrayOf(
        intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7),
        intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8),
        intArrayOf(2, 4, 6)
    )
    var rounds = 0
    private var playerOneScoreCount = 0
    private var playerTwoScoreCount = 0
    @SuppressLint("CutPasteId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playerOneScore = findViewById(R.id.score_Player1)
        playerTwoScore = findViewById(R.id.score_Player2)
        playerStatus = findViewById(R.id.textStatus)
        reset = findViewById(R.id.btn_reset)
        playagain = findViewById(R.id.btn_play_again)
        buttons[0] = findViewById(R.id.btn0)
        buttons[1] = findViewById(R.id.btn1)
        buttons[2] = findViewById(R.id.btn2)
        buttons[3] = findViewById(R.id.btn3)
        buttons[4] = findViewById(R.id.btn4)
        buttons[5] = findViewById(R.id.btn5)
        buttons[6] = findViewById(R.id.btn6)
        buttons[7] = findViewById(R.id.btn7)
        buttons[8] = findViewById(R.id.btn8)
        for (i in buttons.indices) {
            buttons[i]?.setOnClickListener(this)
            buttons[i]?.setOnLongClickListener { v ->
                openContextMenu(v)
                true
            }
        }
        playerOneScoreCount = 0
        playerTwoScoreCount = 0
        playerOneActive = true
        rounds = 0


        registerForContextMenu(findViewById<View>(R.id.btn1))


    }


    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.reset_cell -> {
                // Handle reset cell action here
                val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
                val position = info.position
                buttons[position]?.text = "" // Reset the text of the button
                true
            }
            R.id.clear_board -> {
                // Handle clear board action here
                playAgain() // Reset the entire board
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }
    override fun onCreateContextMenu(menu: ContextMenu?,
                                     v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu, menu)
    }

    override fun onClick(view: View) {
        if ((view as Button).text.toString() != "") {
            return
        } else if (checkWinner()) {
            return
        }
        val buttonID = view.getResources().getResourceEntryName(view.getId())
        val gameStatePointer = buttonID.substring(buttonID.length - 1, buttonID.length).toInt()
        if (playerOneActive) {
            view.text = "X"
            view.setTextColor(Color.parseColor("#ffc34a"))
            gameState[gameStatePointer] = 0
        } else {
            view.text = "O"
            view.setTextColor(Color.parseColor("#70fc3a"))
            gameState[gameStatePointer] = 1
        }
        rounds++
        if (checkWinner()) {
            if (playerOneActive) {
                playerOneScoreCount++
                updatePlayerScore()
                playerStatus!!.text = "Player-1 has won"
            } else {
                playerTwoScoreCount++
                updatePlayerScore()
                playerStatus!!.text = "Player-2 has won"
            }
        } else if (rounds == 9) {
            playerStatus!!.text = "No Winner"
        } else {
            playerOneActive = !playerOneActive
        }
        reset!!.setOnClickListener {
            playAgain()
            playerOneScoreCount = 0
            playerTwoScoreCount = 0
            updatePlayerScore()
        }
        playagain!!.setOnClickListener { playAgain() }
    }

    private fun checkWinner(): Boolean {
        var winnerResults = false
        for (winningPositions in winningPositions) {
            if (gameState[winningPositions[0]] == gameState[winningPositions[1]] && gameState[winningPositions[1]] == gameState[winningPositions[2]] && gameState[winningPositions[0]] != 2) {
                winnerResults = true
            }
        }
        return winnerResults
    }

    private fun playAgain() {
        rounds = 0
        playerOneActive = true
        for (i in buttons.indices) {
            gameState[i] = 2
            buttons[i]!!.text = ""
        }
        playerStatus!!.text = "Status"

        playagain!!.setOnClickListener {
            // Create an AlertDialog Builder
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Play Again") // Set the title of the dialog
            builder.setMessage("Do you want to play again?") // Set the message to show in the dialog

            // Adding the "Yes" button
            builder.setPositiveButton("Yes") { dialog, which ->
                playAgain()
            }

            // Adding the "No" button
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss() // Close the dialog
            }

            // Create and show the AlertDialog
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

    }

    private fun updatePlayerScore() {
        playerOneScore!!.text = Integer.toString(playerOneScoreCount)
        playerTwoScore!!.text = Integer.toString(playerTwoScoreCount)
    }
}


