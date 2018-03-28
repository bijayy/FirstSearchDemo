package com.example.bijay.firstsearchdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.MenuItemCompat
import android.widget.ExpandableListView
import android.app.SearchManager
import android.content.Context
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView


class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    private var searchManager: SearchManager? = null
    private var searchView: android.widget.SearchView? = null
    private var listAdapter: MyExpandableListAdapter? = null
    private var myList: ExpandableListView? = null
    private var parentList = ArrayList<ParentRow>()
    private var showTheseParentList = ArrayList<ParentRow>()
    private var searchItem: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        parentList = ArrayList()
        showTheseParentList = ArrayList()

        // The app will crash if display list is not called here.
        displayList()

        // This expands the list.
        expandAll()


    }

    private fun loadData() {
        var childRows = ArrayList<ChildRow>()
        var parentRow: ParentRow? = null

        childRows.add(ChildRow(R.mipmap.generic_icon, "Lorem ipsum dolor sit amet, consectetur adipiscing elit."))
        childRows.add(ChildRow(R.mipmap.generic_icon, "Sit Fido, sit."))
        parentRow = ParentRow("First Group", childRows)
        parentList.add(parentRow)

        childRows = ArrayList()
        childRows.add(ChildRow(R.mipmap.generic_icon, "Fido is the name of my dog."))
        childRows.add(ChildRow(R.mipmap.generic_icon, "Add two plus two."))
        parentRow = ParentRow("Second Group", childRows)
        parentList.add(parentRow)

    }

    private fun expandAll() {
        val count = listAdapter!!.groupCount
        for (i in 0 until count) {
            myList!!.expandGroup(i)
        } //end for (int i = 0; i < count; i++)
    }

    private fun displayList() {
        loadData()

        myList = findViewById<View>(R.id.expandableListView_search) as ExpandableListView?
        listAdapter = MyExpandableListAdapter(this@MainActivity, parentList)

        myList!!.setAdapter(listAdapter)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        searchItem = menu.findItem(R.id.action_search)
        searchView = MenuItemCompat.getActionView(searchItem!!) as SearchView
        searchView!!.setSearchableInfo(searchManager!!.getSearchableInfo(componentName))
        searchView!!.setIconifiedByDefault(false)
        searchView!!.setOnQueryTextListener(this)
        searchView!!.setOnCloseListener(this)
        searchView!!.requestFocus()

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.getItemId()


        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }

    override fun onClose(): Boolean {
        listAdapter!!.filterData("")
        expandAll()
        return false
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        listAdapter!!.filterData(query)
        expandAll()
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        listAdapter!!.filterData(newText)
        expandAll()
        return false
    }
}
