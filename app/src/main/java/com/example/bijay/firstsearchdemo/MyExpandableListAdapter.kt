package com.example.bijay.firstsearchdemo

import android.content.Context
import android.widget.Toast
import android.widget.TextView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.ImageView


class MyExpandableListAdapter(private val context: Context, originalList: ArrayList<ParentRow>) : BaseExpandableListAdapter() {
    private val parentRowList: ArrayList<ParentRow>
    private val originalList: ArrayList<ParentRow>

    init {
        this.parentRowList = ArrayList()
        this.parentRowList.addAll(originalList)
        this.originalList = ArrayList()
        this.originalList.addAll(originalList)
    }

    override fun getGroupCount(): Int {
        return parentRowList.size
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return parentRowList[groupPosition].childList?.size!!
    }

    override fun getGroup(groupPosition: Int): Any {
        return parentRowList[groupPosition]
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return parentRowList[groupPosition].childList?.get(childPosition)!!
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val parentRow = getGroup(groupPosition) as ParentRow

        if (convertView == null) {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.parent_row, null)
        }

        val heading = convertView!!.findViewById(R.id.parent_text) as TextView

        heading.text = parentRow.name!!.trim { it <= ' ' }
        return convertView
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        val childRow = getChild(groupPosition, childPosition) as ChildRow
        if (convertView == null) {
            val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            convertView = layoutInflater.inflate(R.layout.child_row, null)
        }

        val childIcon = convertView!!.findViewById(R.id.child_icon) as ImageView
        childIcon.setImageResource(childRow.icon)

        val childText = convertView!!.findViewById(R.id.child_text) as TextView
        childText.text = childRow.text!!.trim { it <= ' ' }

        val finalConvertView = convertView
        childText.setOnClickListener(View.OnClickListener {
            Toast.makeText(finalConvertView!!.getContext(), childText.text, Toast.LENGTH_SHORT).show()
        })

        return convertView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    fun filterData(query: String) {
        var query = query
        query = query.toLowerCase()
        parentRowList.clear()

        if (query.isEmpty()) {
            parentRowList.addAll(originalList)
        } else {
            for (parentRow in originalList) {
                val childList = parentRow.childList
                val newList = ArrayList<ChildRow>()

                for (childRow in childList!!) {
                    if (childRow.text!!.toLowerCase().contains(query)) {
                        newList.add(childRow)
                    }
                } // end for (com.example.user.searchviewexpandablelistview.ChildRow childRow: childList)
                if (newList.size > 0) {
                    val nParentRow = ParentRow(parentRow.name, newList)
                    parentRowList.add(nParentRow)
                }
            } // end or (com.example.user.searchviewexpandablelistview.ParentRow parentRow : originalList)
        } // end else

        notifyDataSetChanged()
    }
}
