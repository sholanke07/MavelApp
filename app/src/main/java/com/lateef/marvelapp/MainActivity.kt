package com.lateef.marvelapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lateef.marvelapp.databinding.ActivityMainBinding
import com.lateef.marvelapp.domain.model.Character
import com.lateef.marvelapp.ui.characterList.CharactersViewModel
import com.lateef.marvelapp.ui.characterList.SearchCharactersViewModel
import com.lateef.marvelapp.util.CharacterListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener{

    val viewModel: CharactersViewModel by viewModels()
    private val viewModelSearch: SearchCharactersViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var searchTerm: String
    private val tempList = arrayListOf<Character>()
    private var flag = 3
    var paginatedValue = 0
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CharacterListAdapter
    lateinit var layoutManager: GridLayoutManager
    private val TAG = "activity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //setContentView(R.layout.activity_main)
        recyclerView = binding.charactersRecyclerView
        layoutManager = GridLayoutManager(this, 2)

        binding.btSort.setOnClickListener {
            tempList.sortWith{o1, o2 ->
                o1.name.compareTo(o2.name)
            }
            Toast.makeText(this, "sort", Toast.LENGTH_LONG).show()
            adapter.setData(tempList)
        }

        recyclerViewCharacters()
        //paginatedValue += 20
        //viewModel.getAllCharactersData(paginatedValue)
        //callApi()

        recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (layoutManager.findLastVisibleItemPosition() == layoutManager.itemCount - 1){
                    paginatedValue += 20
                    viewModel.getAllCharactersData(paginatedValue)
                    callApi()
                }
            }
        })
    }

    private fun callApi(){
        CoroutineScope(Dispatchers.Main).launch {
            repeat(flag){
                viewModel._marvelValue.collect {
                    when{
                        it.isLoading ->{
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        it.error.isNotBlank() ->{
                            binding.progressBar.visibility = View.GONE
                            flag = 0
                            Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_LONG).show()
                        }
                        it.characterList.isNotEmpty() ->{
                            binding.progressBar.visibility = View.GONE
                            flag = 0
                            tempList.addAll(it.characterList)
                            Log.d(TAG, "Result ${it.characterList}")
                            adapter.setData(it.characterList as ArrayList<Character>)
                        }
                    }
                    delay(1000)
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val search = menu?.findItem(R.id.menuSearch)
        val searchView = search?.actionView as androidx.appcompat.widget.SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null){
            searchTerm = query
        }
        if (searchTerm.isNotEmpty()){
            search()
        }
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null){
            searchTerm = newText
        }
        if (searchTerm.isNotEmpty()){
            search()
        }
        return true
    }

    private fun search(){
        viewModelSearch.getSearchCharacters(searchTerm)
            CoroutineScope(Dispatchers.Main).launch {
                viewModelSearch._marvelValueSearch.collect {
                    when {
                        it.isLoading -> {
                            binding.progressBar.visibility = View.VISIBLE
                        }
                        it.error.isNotBlank() -> {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@MainActivity, it.error, Toast.LENGTH_LONG).show()
                        }
                        it.characterList.isNotEmpty() -> {
                            binding.progressBar.visibility = View.GONE
                            adapter.setData(it.characterList as ArrayList<Character>)
                        }
                    }
                }
            }
    }

   /* override fun onStart() {
        super.onStart()
        viewModel.getAllCharactersData(paginatedValue)
        callApi()
    }*/

    private fun recyclerViewCharacters(){
        adapter = CharacterListAdapter(this, ArrayList())
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

    }
}