package com.meldcx.captureweb

import android.os.Bundle
import android.widget.ListView
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

/**
 * Created by Lakshmi Srividya on 11/26/2019.
 */

class HistoryActivity : AppCompatActivity() {

    var dataList = ArrayList<HashMap<String, String>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        var databaseHelper = DatabaseHelper(this)
        val listview = findViewById(R.id.listView) as ListView
        val searchView = findViewById(R.id.searchView) as SearchView
        val cursor = databaseHelper.getAllImage()
        cursor!!.moveToFirst()

        while (!cursor.isAfterLast) {
            val map = HashMap<String, String>()
            map["url"] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_URL))
            map["date"] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_DATE))
            map["bitmap"] =
                cursor.getString(cursor.getColumnIndex(DatabaseHelper.KEY_BITMAP)).toString()
            dataList.add(map)
            cursor.moveToNext()
        }

        val customAdapter = CustomAdapter(this, dataList)
        //Set Adapter to ArrayList
        listview.adapter = customAdapter

        //On Click for ListView Item
        listview.setOnItemClickListener { adapterView, view, position, l ->

            //Provide the data on Click position in our listview
            val hashMap: HashMap<String, String> =
                customAdapter.getItem(position) as HashMap<String, String>

            Toast.makeText(
                this,
                "Name : " + hashMap.get("name") + "\nVersion : " + hashMap.get("version"),
                Toast.LENGTH_LONG
            ).show()
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val text = newText
                /*Call filter Method Created in Custom Adapter
                    This Method Filter ListView According to Search Keyword
                 */
                customAdapter.filter(text)
                return false
            }
        })


    }

}
