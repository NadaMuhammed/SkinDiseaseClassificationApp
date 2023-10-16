package com.example.gp.list

import android.app.ProgressDialog
import android.content.ClipData
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gp.R
import com.example.gp.login.LoginViewModel

class listFragment : Fragment(), androidx.appcompat.widget.SearchView.OnQueryTextListener,
    SearchView.OnQueryTextListener {
    val viewModel: ListViewModel by lazy {
        ViewModelProvider(this).get(ListViewModel::class.java)
    }
    private val adapter: Adapter by lazy {
        Adapter(Adapter.OnClickListener({
            viewModel.displayPropertyDetails(it)
        }))
    }
    private lateinit var navController: NavController
    private lateinit var searchView: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val list = arrayListOf<Disease>(
            Disease(
                1,
                R.drawable.disease1,
                getString(R.string.Disease1),
                getString(R.string.Disease1Description)
            ),
            Disease(
                2,
                R.drawable.disease2,
                getString(R.string.Disease2),
                getString(R.string.Disease2Description)
            ),
            Disease(
                3,
                R.drawable.disease3,
                getString(R.string.Disease3),
                getString(R.string.Disease3Description)
            ),
            Disease(
                4,
                R.drawable.disease4,
                getString(R.string.Disease4),
                getString(R.string.Disease4Description)
            ),
            Disease(
                5,
                R.drawable.disease5,
                getString(R.string.Disease5),
                getString(R.string.Disease5Description)
            ),
            Disease(
                6,
                R.drawable.disease6,
                getString(R.string.Disease6),
                getString(R.string.Disease6Description)
            ),
            Disease(
                7,
                R.drawable.disease7,
                getString(R.string.Disease7),
                getString(R.string.Disease7Description)
            ),
            Disease(
                8,
                R.drawable.disease8,
                getString(R.string.Disease8),
                getString(R.string.Disease8Description)
            ),
            Disease(
                9,
                R.drawable.disease9,
                getString(R.string.Disease9),
                getString(R.string.Disease9Description)
            ),
            Disease(
                10,
                R.drawable.disease10,
                getString(R.string.Disease10),
                getString(R.string.Disease10Description)
            ),
            Disease(
                11,
                R.drawable.disease11,
                getString(R.string.Disease11),
                getString(R.string.Disease11Description)
            ),
            Disease(
                12,
                R.drawable.disease12,
                getString(R.string.Disease12),
                getString(R.string.Disease12Description)
            ),
            Disease(
                13,
                R.drawable.melanoma,
                getString(R.string.Disease13),
                getString(R.string.Disease13Description)
            ),
            Disease(
                14,
                R.drawable.disease14,
                getString(R.string.Disease14),
                getString(R.string.Disease14Description)
            ),
            Disease(
                15,
                R.drawable.disease15,
                getString(R.string.Disease15),
                getString(R.string.Disease15Description)
            ),
            Disease(
                16,
                R.drawable.disease16,
                getString(R.string.Disease16),
                getString(R.string.Disease16Description)
            ),
            Disease(
                17,
                R.drawable.disease17,
                getString(R.string.Disease17),
                getString(R.string.Disease17Description)
            ),
            Disease(
                18,
                R.drawable.disease18,
                getString(R.string.Disease18),
                getString(R.string.Disease18Description)
            ),
            Disease(
                19,
                R.drawable.disease19,
                getString(R.string.Disease19),
                getString(R.string.Disease19Description)
            ),
            Disease(
                20,
                R.drawable.disease20,
                getString(R.string.Disease20),
                getString(R.string.Disease20Description)
            )
        )
        viewModel.addDiseases(list)
        viewModel.readAllData.observe(viewLifecycleOwner, Observer { user ->
            adapter.setData(user)
        })

        navController = findNavController()
        searchView = view.findViewById<SearchView>(R.id.searchView)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(this)


        viewModel.navigateToSelectedProperty.observe(viewLifecycleOwner, Observer {
            if (null != it) {
                this.findNavController()
                    .navigate(listFragmentDirections.actionListFragmentToDiseaseFragment(it))
                viewModel.displayPropertyDetailsComplete()
            }
        })

        setHasOptionsMenu(true)

        return view
    }

//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.search_menu, menu)
//        val search = menu.findItem(R.id.menu_search)
//        val searchView = search.actionView as SearchView
//        searchView.isSubmitButtonEnabled = true
//        searchView.setOnQueryTextListener(this)
//
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return super.onOptionsItemSelected(item)
//    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"
        viewModel.searchDisease(searchQuery).observe(viewLifecycleOwner) { list ->

            list?.let {
                adapter.setData(it)
            }

        }
    }


}