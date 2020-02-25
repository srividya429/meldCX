package com.meldcx.captureweb

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.method.LinkMovementMethod
import android.util.Base64
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import java.util.*
import kotlin.collections.ArrayList


/**
 * Created by Lakshmi Srividya on 11/26/2019.
 */

class CustomAdapter(private val context: Context,
                    private val dataList: ArrayList<HashMap<String, String>>) : BaseAdapter() {

    var tempImagesList = ArrayList(dataList)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var view = convertView
        val holder: ViewHolder
        if (convertView == null) {

            val mInflater = (context as Activity).layoutInflater
            //Inflating the list_row.
            view = mInflater!!.inflate(R.layout.list_row, parent, false)
            holder = ViewHolder()
            holder.mImage = view!!.findViewById<ImageView>(R.id.imageview) as ImageView
            holder.mUrl = view!!.findViewById<TextView>(R.id.header) as TextView
            holder.mDate = view!!.findViewById<TextView>(R.id.subHeader) as TextView
            holder.mDelete= view!!.findViewById<ImageView>(R.id.delete) as ImageView
            view.setTag(holder)
        } else {
            holder = view!!.getTag() as ViewHolder
        }
        val map = dataList.get(position)
        var bitmap: Bitmap

        //Convert bitmap to drawable
        /*val imageBytes = Base64.decode(map.get("bitmap"), 0)
        Log.d("CustomAdapter", "From Database imageBytes" + imageBytes)
        val image = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        Log.d("CustomAdapter", "From Database image" + image)*/
        /* val decodedString: ByteArray =
             Base64.decode(map.get("bitmap"), Base64.DEFAULT)
         val decodedByte =
             BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
         holder.mImageView!!.setImageBitmap(decodedByte)*/

        //Setting name to TextView it's Key from HashMap At Position

        /*val decodedString: ByteArray = android.util.Base64.decode(map.get("bitmap"), Base64.URL_SAFE)
        val decodedByte =
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        holder.mImage!!.setImageBitmap(map.get("bitmap"))*/
        holder.mUrl!!.setText(map.get("url"))
        holder.mUrl!!.setOnClickListener {
            val intent: Intent = Intent(context.getApplicationContext(),WebActivity::class.java)
            intent.putExtra("URL", holder.mUrl!!.text)
            context.getApplicationContext().startActivity(intent)
        }
        holder.mDate!!.setText(map.get("date"))
        holder.mDelete!!.setOnClickListener {
            dataList.remove(getItem(position))
            notifyDataSetChanged()
        }
        return view
    }

    override fun getItem(position: Int): Any {
        return dataList.get(position)
    }

    override fun getItemId(id: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return dataList.size
    }

    //Class To hold views in list_row.xml
    class ViewHolder {
        var mImage: ImageView? = null
        var mUrl: TextView? = null
        var mDate: TextView? = null
        var mDelete: ImageView?=null
    }

    //Filter the list according to the search keyword entered
    fun filter(text: String?) {
        val text = text!!.toLowerCase(Locale.getDefault())
        dataList.clear()
        if (text.length == 0) {
            dataList.addAll(tempImagesList)
        } else {
            for (i in 0..tempImagesList.size - 1) {
                if (tempImagesList.get(i).get("url")!!.toLowerCase(Locale.getDefault()).contains(
                        text
                    )
                ) {
                    dataList.add(tempImagesList.get(i))
                }
            }
        }
        notifyDataSetChanged()
    }
}