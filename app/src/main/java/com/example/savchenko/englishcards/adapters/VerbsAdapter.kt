package com.example.savchenko.englishcards.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.savchenko.englishcards.R
import com.example.savchenko.englishcards.entities.Verb

/**
 * Created by savchenko on 06.12.17.
 */
class VerbsAdapter(var context: Context?, var itemList: List<Verb>) : BaseAdapter() {
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        var gridView:View
        if(p1==null){
            gridView = View(context)

            gridView = inflater.inflate(R.layout.item_verb, null)
            val tvVerb = gridView.findViewById<TextView>(R.id.tvVerb)
            tvVerb.setText(itemList.get(p0).verb)
        }else{
            gridView = p1
        }
        return gridView
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return itemList.size
    }

    override fun getItem(p0: Int): Any? {
        return null
    }
}