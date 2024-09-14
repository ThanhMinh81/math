package com.example.matheasyapp.view.home.pdf.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matheasyapp.R
import com.example.matheasyapp.adapter.PDFAdapter
import com.example.matheasyapp.databinding.FragmentStartPDFBinding
import com.example.matheasyapp.db.HistoryDatabase
import com.example.matheasyapp.model.PdfFile


class StartPDFFragment : Fragment() {

    lateinit var binding : FragmentStartPDFBinding
    private lateinit var database: HistoryDatabase
    private lateinit var adapter : PDFAdapter
    private lateinit var pdfFiles : List<PdfFile>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentStartPDFBinding.inflate(layoutInflater)
        database = HistoryDatabase.getDatabase(requireActivity())
        binding.rcvPDF.layoutManager = LinearLayoutManager(requireContext())
//        adapter = PDFAdapter(requireContext(), pdfFiles, this, listEmpty)
        binding.rcvPDF.adapter = adapter

        return binding.root

    }

}