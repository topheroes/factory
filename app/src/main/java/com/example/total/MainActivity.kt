package com.example.total

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.color_item.*
import java.util.Random


class ColorAdapter(val context: Context, val items: MutableList<Color> ):RecyclerView.Adapter<ColorAdapter.ColorAdapterHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorAdapterHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.color_item, null)
        val vh = ColorAdapterHolder(view)
        return vh
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ColorAdapterHolder, position: Int) {
        holder.text.text = items[position].color
        holder.view.setBackgroundColor(items[position].number)
        holder.view.setOnClickListener{_->
            items.removeAt(position)
            notifyDataSetChanged()
        }
    }


    inner class ColorAdapterHolder(view: View):RecyclerView.ViewHolder(view){
        val text = view.findViewById<TextView>(R.id.colorText)
        val view = view.findViewById<View>(R.id.view)

    }

}

@Parcelize
data class Color(val number:Int, val color:String = number.toString()):Parcelable


class ColorViewModelFactory(val savedInstanceState: Bundle?):ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return (ColorViewModel(savedInstanceState) as T)
    }

}

class ColorViewModel(savedInstanceState: Bundle?):ViewModel() {
    var ourItems = savedInstanceState?.getParcelableArrayList<Color>("colors")?:arrayListOf<Color>()
    init{
        if( ourItems.count() == 0 ){
            ourItems.addAll(MutableList(25) { _ -> Color(Random().nextInt()) })
        }

    }


    fun onSaveInstanceState(savedInstanceState: Bundle?){
        savedInstanceState?.putParcelableArrayList("colors", ourItems)


    }

}


class MainActivity : AppCompatActivity() {

    lateinit var vm:ColorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vm = ViewModelProviders.of(this, ColorViewModelFactory(savedInstanceState)).get(ColorViewModel::class.java)

        val adapter = ColorAdapter(this, vm.ourItems)
        mainList.adapter = adapter
        mainList.layoutManager = LinearLayoutManager(this)



    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        vm.onSaveInstanceState(outState)

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }
}
