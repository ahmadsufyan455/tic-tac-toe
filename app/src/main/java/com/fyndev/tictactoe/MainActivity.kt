package com.fyndev.tictactoe

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fyndev.tictactoe.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding

    private var buttons = arrayOfNulls<Button>(9)
    private val gameState = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)
    private val winningPos = arrayOf(
        intArrayOf(0, 1, 2), intArrayOf(3, 4, 5), intArrayOf(6, 7, 8), // rows
        intArrayOf(0, 3, 6), intArrayOf(1, 4, 7), intArrayOf(2, 5, 8), // columns
        intArrayOf(0, 4, 8), intArrayOf(2, 4, 6) // cross
    )

    private var activePlayer: Boolean = true
    private var p1ScoreCount = 0
    private var p2ScoreCount = 0
    private var rCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        for (i in buttons.indices) {
            val buttonId = "btn_$i"
            val resourceId = resources.getIdentifier(buttonId, "id", packageName)
            buttons[i] = findViewById(resourceId)
            buttons[i]?.setOnClickListener(this)
        }

        binding?.resetGame?.setOnClickListener {
            playAgain()
            p1ScoreCount = 0
            p2ScoreCount = 0
            binding!!.playerStatus.text = ""
            updatePlayerScore()
            Toast.makeText(this, "Let's begin", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(v: View?) {
        // do action
        if ((v as Button).text.toString() != "") {
            return
        }
        val buttonId = v.resources.getResourceEntryName(v.id)
        val gameStatePointer = buttonId.substring(buttonId.length - 1, buttonId.length)

        if (activePlayer) {
            v.text = "X"
            v.setTextColor(Color.parseColor("#b61827"))
            gameState[gameStatePointer.toInt()] = 0
        } else {
            v.text = "O"
            v.setTextColor(Color.parseColor("#cabf45"))
            gameState[gameStatePointer.toInt()] = 1
        }
        rCount++

        if (checkWinner()) {
            if (activePlayer) {
                p1ScoreCount++
                updatePlayerScore()
                Toast.makeText(this, getString(R.string.p1_win), Toast.LENGTH_SHORT).show()
                playAgain()
            } else {
                p2ScoreCount++
                updatePlayerScore()
                Toast.makeText(this, getString(R.string.p2_win), Toast.LENGTH_SHORT).show()
                playAgain()
            }
        } else if (rCount == 9) {
            playAgain()
            Toast.makeText(this, getString(R.string.no_winner), Toast.LENGTH_SHORT).show()
        } else {
            activePlayer = !activePlayer
        }

        when {
            p1ScoreCount > p2ScoreCount -> {
                binding?.playerStatus?.text = getString(R.string.p1_win)
            }
            p2ScoreCount > p1ScoreCount -> {
                binding?.playerStatus?.text = getString(R.string.p2_win)
            }
            else -> {
                binding?.playerStatus?.text = ""
            }
        }
    }

    private fun checkWinner(): Boolean {
        var winnerResult = false

        for (winPos in winningPos) {
            if (gameState[winPos[0]] == gameState[winPos[1]] && gameState[winPos[1]] == gameState[winPos[2]] && gameState[winPos[0]] != 2) {
                winnerResult = true
            }
        }
        return winnerResult
    }

    private fun updatePlayerScore() {
        binding?.playerOneScore?.text = p1ScoreCount.toString()
        binding?.playerTwoScore?.text = p2ScoreCount.toString()
    }

    private fun playAgain() {
        rCount = 0
        activePlayer = true

        for (i in buttons.indices) {
            gameState[i] = 2
            buttons[i]?.text = ""
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}