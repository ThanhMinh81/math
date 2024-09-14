package com.example.matheasyapp.view.home.pdf.fragment


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.R
import com.example.matheasyapp.adapter.PDFAdapter
import com.example.matheasyapp.databinding.FragmentSearchPDFBinding
import com.example.matheasyapp.db.HistoryDatabase
import com.example.matheasyapp.model.PdfFile
import com.example.matheasyapp.model.SearchPDF
import com.example.matheasyapp.view.home.ViewPDFActivity
import com.example.matheasyapp.view.home.pdf.OnItemClickListener
import com.example.matheasyapp.view.home.pdf.SearchAdapter
import com.example.matheasyapp.view.home.pdf.ViewModelPDF
import com.example.matheasyapp.view.home.pdf.onClickItemPDF
import kotlinx.coroutines.launch

class SearchPDFFragment : Fragment(), onClickItemPDF, OnItemClickListener {

    lateinit var binding: FragmentSearchPDFBinding

    lateinit var viewModel: ViewModelPDF

    private var adapter: PDFAdapter? = null
    private lateinit var listDisplayPDF: ArrayList<PdfFile>

    private lateinit var listDisplayData: ArrayList<PdfFile>
    private lateinit var listDisplayStartData: ArrayList<PdfFile>

    private lateinit var database: HistoryDatabase

    private lateinit var listSearch: ArrayList<SearchPDF>

    private var keyWords: String = ""

    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        database = HistoryDatabase.getDatabase(requireActivity())

        binding = FragmentSearchPDFBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(requireActivity()).get(ViewModelPDF::class.java)

        listDisplayPDF = ArrayList()

        listDisplayData = ArrayList()

        listDisplayStartData = ArrayList()

        if (viewModel.getListPdfShaed().value != null && viewModel.getListStart().value != null) {
            listDisplayData.addAll(viewModel.getListPdfShaed().value!!)
            listDisplayStartData.addAll(viewModel.getListStart().value!!)
        }

        binding.rcvSearch.layoutManager = LinearLayoutManager(requireContext())
        adapter = PDFAdapter(requireContext(), listDisplayPDF, this, listDisplayStartData)
        binding.rcvSearch.adapter = adapter
        binding.tvAmountResult

        listSearch = ArrayList()
        searchAdapter = SearchAdapter(requireContext(), listSearch, this)
        getListKeyWord()

        binding.listViewHistory.adapter = searchAdapter

        adapter!!.notifyDataSetChanged()

        viewModel.getSearchKeyWord().observe(requireActivity(), Observer { value ->
            keyWords = value
            if (value.length > 0) {
                listDisplayData.forEach { it ->
                    if (it.name.toUpperCase().contains(value.toUpperCase())) {
                        if (!(listDisplayPDF.contains(it))) {
                            listDisplayPDF.add(it)
                            adapter!!.notifyDataSetChanged()
                            binding.tvAmountResult.text =
                                listDisplayPDF.size.toString() + " result found"
                        }
                    }
                }
            } else {
                listDisplayPDF.clear()
                binding.tvAmountResult.text = " 0 result found"
                adapter!!.notifyDataSetChanged()
                getListKeyWord()
            }

            if (listDisplayPDF.size > 0) {
                binding.layoutResult.visibility = View.VISIBLE
                binding.layoutHistory.visibility = View.GONE

            } else {
                binding.layoutResult.visibility = View.GONE
                binding.layoutHistory.visibility = View.VISIBLE
                getListKeyWord()
            }

        })

        return binding.root
    }


    override fun onClickItem(item: PdfFile) {
        val intent = Intent(requireActivity(), ViewPDFActivity::class.java)
        intent.putExtra("url_pdf", item.path)
        intent.putExtra("title_pdf", item.name)
        startActivity(intent)
        var pdf = SearchPDF(0, keyWords)
        val value = database.searchPDF().insert(pdf)
        Log.d("453252", value.toString())

    }


    override fun onClickItemStart(item: PdfFile, start: Boolean) {
        if (start) {
            lifecycleScope.launch {
                database.pdfStart().insert(item)
//                getAllListStart()
            }
        } else {
            lifecycleScope.launch {
                database.pdfStart().delete(item)
//                getAllListStart()
            }
        }
    }

    fun getListKeyWord() {
        listSearch.clear()
        listSearch.addAll(database.searchPDF().getListKeyWord)
        searchAdapter.notifyDataSetChanged()
    }

    override fun onClickItemEditPDF(item: PdfFile) {}


    override fun onItemClick(item: SearchPDF) {

    }

    override fun onClearClick(item: SearchPDF) {

        database.searchPDF().delete(item)
        getListKeyWord()

    }

}
