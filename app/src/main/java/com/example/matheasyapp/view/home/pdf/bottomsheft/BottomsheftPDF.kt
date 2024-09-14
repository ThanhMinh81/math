package com.example.matheasyapp.view.home.pdf.bottomsheft

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import com.example.matheasyapp.R
import com.example.matheasyapp.databinding.BottomsheftPdfLayoutBinding
import com.example.matheasyapp.model.PdfFile
import com.example.matheasyapp.view.home.pdf.ViewModelEditPDF
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomsheftPDF : BottomSheetDialogFragment() {


    private lateinit var binding: BottomsheftPdfLayoutBinding

    // Định nghĩa Interface để giao tiếp với Fragment
    interface OnBottomSheetItemClickListener {
        fun onItemClick(item: String)
    }

    //
//    private var nameFile: String? = null
    private var start: Boolean? = null
    private var pdfFile: PdfFile? = null

    private lateinit var viewModel: ViewModelEditPDF


    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate layout for BottomSheet
        binding = BottomsheftPdfLayoutBinding.inflate(layoutInflater)

        viewModel = ViewModelProvider(requireActivity()).get(ViewModelEditPDF::class.java)

        arguments?.let {
//            pdfFile = it.getParcelable("pdf_edit_data", PdfFile::class.java)
            pdfFile = it.getParcelable("pdf_edit_data")
        }

        pdfFile?.let {
            binding.tvNamefile.setText(pdfFile!!.name.toString())

            if (pdfFile!!.piority) {
                binding.imgStart.setImageResource(R.drawable.ic_start_check)
            } else {
                binding.imgStart.setImageResource(R.drawable.ic_start_uncheck)
            }

            start = pdfFile!!.piority
        }


        binding.imgStart.setOnClickListener {
            start = !start!!
            if (start!!) {
                binding.imgStart.setImageResource(R.drawable.ic_start_check)
            } else {
                binding.imgStart.setImageResource(R.drawable.ic_start_uncheck)
            }
            pdfFile!!.piority = start!!
            viewModel.setStartPDF(pdfFile)
        }

        binding.layoutEdit.setOnClickListener {
            viewModel.setReadName(pdfFile!!)
            dismiss()
        }

        binding.layoutShare.setOnClickListener {

        }

        binding.layoutDelete.setOnClickListener {
            viewModel.setDeleteFile(pdfFile)
            dismiss()
        }

        return binding.root
    }


    companion object {
        fun newInstance(item: PdfFile): BottomsheftPDF {
            val fragment = BottomsheftPDF()
            val args = Bundle().apply {
                putParcelable("pdf_edit_data", item)
            }
            fragment.arguments = args
            return fragment
        }
    }


}