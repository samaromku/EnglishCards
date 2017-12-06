package com.example.savchenko.englishcards.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.savchenko.englishcards.R
import com.example.savchenko.englishcards.adapters.VerbsAdapter
import com.example.savchenko.englishcards.storage.Const.PRESENT_CONTINUOUS_STR
import com.example.savchenko.englishcards.storage.Const.PRESENT_SIMPLE_STR
import com.example.savchenko.englishcards.entities.Verb
import com.example.savchenko.englishcards.storage.Const
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    val TAG = this.javaClass.simpleName
    private val listStrings = mutableListOf(
            Verb(1, "work", "Where do you usually work", "Where are you working now"),
            Verb(2, "teach", "How long do you teach", "What are you teaching now"),
            Verb(3, "learn", "", ""),
            Verb(4, "hear", "", ""),
            Verb(5, "know", "", ""),
            Verb(6, "mean", "", ""),
            Verb(7, "have", "", ""),
            Verb(8, "do", "", ""),
            Verb(9, "make", "", ""))
    private val adapter = VerbsAdapter(this, listStrings)
    private var position = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvWhoTurn.text = "X"

        gvVerbs.adapter = adapter
        btnReady.setOnClickListener({onBtnClick(position)})

        gvVerbs.setOnItemClickListener { _: AdapterView<*>, _: View, i: Int, _: Long ->
            if (listStrings[i].verb != "X" && listStrings[i].verb != "O") {
                position = i
                llTextViews.removeAllViews()
                fillTextViews(listStrings.get(i))
                tvTime.text = getRandomTime()
            }
        }
    }

    private fun getRandomTime():String{
        val randomNumber = (Math.random() * 2).toInt()
        Log.i(TAG, randomNumber.toString())
        if(randomNumber== Const.PRESENT_CONTINUOUS)
            return PRESENT_CONTINUOUS_STR
        else return PRESENT_SIMPLE_STR
    }

    private fun onBtnClick(position:Int){
        tvPhrase.text = ""
        val currentVerb = listStrings[position]
        listStrings[position] = Verb(currentVerb.id, tvWhoTurn.text.toString(), currentVerb.presentSimple, currentVerb.presentContinuous)
        adapter.notifyDataSetChanged()
        gvVerbs.adapter = adapter
        setWhoTurnText()
        allCombines()
    }

    private fun fillTextViews(verb:Verb){
        val allWords = verb.presentContinuous + " " + verb.presentSimple
        val words = allWords.toLowerCase().split(" ")
        val llp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        llp.setMargins(8, 0, 8, 0)

        for (i in 1 until words.size){
            val tvItem = TextView(this)
            tvItem.text = words.get(i) + " "
            tvItem.setBackgroundResource(R.color.colorPrimary)
            tvItem.setTextColor(Color.parseColor("#ffffff"))
            tvItem.layoutParams = llp
            tvItem.setPadding(8, 8, 8, 8)
            tvItem.setOnClickListener {
                tvItem.visibility = View.INVISIBLE
                tvPhrase.text = tvPhrase.text.toString() + tvItem.text.toString()
            }
            llTextViews.addView(tvItem)
        }
    }

    private fun allCombines(){
        checkVictory(0,1,2)
        checkVictory(3,4,5)
        checkVictory(6,7,8)
        checkVictory(0,3,6)
        checkVictory(1,4,7)
        checkVictory(2,5,8)
        checkVictory(0,4,8)
        checkVictory(2,4,6)
    }

    private fun checkVictory(first: Int, second: Int, third: Int) {
        if((listStrings[first] == listStrings[second]) &&
                ((listStrings[first] == listStrings[third]) &&
                        ((listStrings[second] == listStrings[third]))) &&
                !(listStrings[first].verb.isEmpty() &&
                        listStrings[second].verb.isEmpty() &&
                        listStrings[third].verb.isEmpty())){
            toast("victory")
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun toast(text:String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    private fun setWhoTurnText() {
        if (tvWhoTurn.text.toString() == "X") {
            tvWhoTurn.text = "O"
        } else {
            tvWhoTurn.text = "X"
        }
    }
}
