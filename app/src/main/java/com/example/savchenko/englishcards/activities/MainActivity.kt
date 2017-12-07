package com.example.savchenko.englishcards.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.example.savchenko.englishcards.R
import com.example.savchenko.englishcards.adapters.VerbsAdapter
import com.example.savchenko.englishcards.storage.Const.PRESENT_CONTINUOUS_STR
import com.example.savchenko.englishcards.storage.Const.PRESENT_SIMPLE_STR
import com.example.savchenko.englishcards.entities.Verb
import com.example.savchenko.englishcards.storage.Const
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.FrameLayout
import java.util.*


class MainActivity : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName
    private val listStrings = mutableListOf(
            Verb(1, "work", "Where do you usually work", "Where are you working now"),
            Verb(2, "teach", "How long do you teach", "What are you teaching now"),
            Verb(3, "learn", "How do you learn per day", "What are you learning now"),
            Verb(4, "hear", "What do you usually hear at street", "Are you hearing"),
            Verb(5, "draw", "How do you draw this", "Are you drawing since yesterday"),
            Verb(6, "cook", "What do you usually cook", "Are you cooking"),
            Verb(7, "have", "What dinner you always have", "Are you having free time"),
            Verb(8, "do", "How do you do it", "What are you doing"),
            Verb(9, "make", "Do you make chairs", "Are you making people good"))
    private val verbsAdapter = VerbsAdapter(this, listStrings)
    private var position = 0
    private var isCurrent = true
    private lateinit var verb:Verb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvWhoTurn.text = "X"

        gvVerbs.adapter = verbsAdapter
        btnReady.setOnClickListener({ onReadyClick(position)})

        gvVerbs.setOnItemClickListener { _: AdapterView<*>, _: View, i: Int, _: Long ->
            if (listStrings[i].verb != "X" && listStrings[i].verb != "O") {
                position = i
                glTextViews.removeAllViews()
                this.verb = listStrings[i]
                fillTextViews()
                tvTimeName.text = getRandomTime()
                isCurrent = true
            }
        }
    }

    private fun getRandomTime():String{
        val randomNumber = (Math.random() * 2).toInt()
        Log.i(TAG, randomNumber.toString())
        return if(randomNumber== Const.PRESENT_CONTINUOUS)
            PRESENT_CONTINUOUS_STR
        else PRESENT_SIMPLE_STR
    }

    private fun onReadyClick(position:Int){
        val currentVerb = listStrings[position]
        if(isCurrent && checkPhrase()) {

            listStrings[position] = Verb(currentVerb.id, tvWhoTurn.text.toString(), currentVerb.presentSimple, currentVerb.presentContinuous)
            verbsAdapter.notifyDataSetChanged()
            gvVerbs.adapter = verbsAdapter
            allCombines()
        }else {
            toast("Неверно!")
        }
        tvPhrase.text = ""
        glTextViews.removeAllViews()
        setWhoTurnText()
    }

    private fun fillTextViews(){
        val allWords = verb.presentContinuous + " " + verb.presentSimple
        val words = allWords.toLowerCase().split(" ")
        val llp = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        llp.setMargins(4, 4, 4, 4)
        Collections.shuffle(words)
        for (i in 0 until words.size){
            val tvItem = TextView(this)
            tvItem.text = words[i].plus(" ")
            tvItem.setBackgroundResource(R.color.colorPrimary)
            tvItem.setTextColor(Color.parseColor("#ffffff"))
            tvItem.layoutParams = llp
            tvItem.setPadding(8, 8, 8, 8)
            tvItem.setOnClickListener {
                val containsStr = tvPhrase.text.toString() + tvItem.text.toString().toLowerCase().trim()
                if(tvTimeName.text == PRESENT_CONTINUOUS_STR) {
                    checkTimeContainsWords(verb.presentContinuous, tvItem, containsStr)
                }else if(tvTimeName.text == PRESENT_SIMPLE_STR){
                    checkTimeContainsWords(verb.presentSimple, tvItem, containsStr)
                }
            }
            glTextViews.addView(tvItem)
        }
    }

    private fun checkPhrase() :Boolean{
        return if(tvTimeName.text == PRESENT_CONTINUOUS_STR) {
            tvPhrase.text.toString().toLowerCase().trim() == verb.presentContinuous.toLowerCase().trim()
        }else {
            tvPhrase.text.toString().toLowerCase().trim() == verb.presentSimple.toLowerCase().trim()
        }
    }

    private fun checkTimeContainsWords(verbTime:String, tvItem:TextView, containsStr:String){
        if (verbTime.toLowerCase().trim().contains(containsStr)) {
            tvItem.visibility = View.INVISIBLE
            tvPhrase.text = tvPhrase.text.toString().plus(tvItem.text.toString())
        } else {
            tvItem.setBackgroundResource(R.color.colorAccent)
            isCurrent = false
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
