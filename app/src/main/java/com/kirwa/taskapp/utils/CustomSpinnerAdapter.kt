package com.kirwa.taskapp.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.kirwa.taskapp.R
import com.kirwa.taskapp.databinding.DropdownSpinnerItemBinding

data class SpinnerItem(val name: String, val id: String, val body: String? = null)

class CustomSpinnerAdapter(context: Context, val list: java.util.ArrayList<SpinnerItem>) :
    ArrayAdapter<SpinnerItem>(context, R.layout.dropdown_spinner_item) {

    var filtered = ArrayList<SpinnerItem>()

    init {
        filtered = list
    }

    override fun getCount(): Int = filtered.size

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding: DropdownSpinnerItemBinding =
            if (convertView != null) DropdownSpinnerItemBinding.bind(convertView)
            else DropdownSpinnerItemBinding.inflate(LayoutInflater.from(context), parent, false)
        binding.spinnerText.text = filtered[position].name
        return binding.root
    }

    override fun getItem(position: Int): SpinnerItem? = filtered[position]

    override fun getFilter(): Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            val searchResults = list.filter {
                it.name.contains(constraint.toString(), true)
            }
            results.count = searchResults.size
            results.values = searchResults
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            filtered = results?.values as ArrayList<SpinnerItem>
            notifyDataSetInvalidated()
        }

        override fun convertResultToString(resultValue: Any?): CharSequence =
            (resultValue as SpinnerItem).name
    }
}