package com.example.camerascanner_docscannerpdfmaker.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.camerascanner_docscannerpdfmaker.R
import com.example.camerascanner_docscannerpdfmaker.adapter.AllDocAdapter
import kotlinx.android.synthetic.main.fragment_all_doc.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class AllDocFragment : Fragment() {

    var filesArrayList : MutableList<File> = ArrayList<File>()
    lateinit var allDocAdapter: AllDocAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_all_doc, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPdfs()
    }

    fun getPdfs(){
        filesArrayList.clear()
        val pdfFiles = File("${activity!!.filesDir}/PDF")
        for (files in pdfFiles.listFiles()){
            filesArrayList.add(files)
        }
        getImages()
    }
    fun getImages(){
        val pdfFiles = File("${activity!!.filesDir}/Images")
        for (files in pdfFiles.listFiles()){
            filesArrayList.add(files)
        }

        Collections.sort<File>(
            filesArrayList,
            object : Comparator<File> {
                override fun compare(o1: File, o2: File): Int {
                    val s11 = o1.lastModified()
                    val s12 = o2.lastModified()
                    return java.lang.Long.valueOf(s12).compareTo(s11)
                }
            })

        allDocAdapter = AllDocAdapter(filesArrayList,object : AllDocAdapter.RefreshList{
            override fun refreshList() {
                getPdfs()
            }
        })
        allDocRecyclerView.layoutManager = LinearLayoutManager(activity)
        allDocRecyclerView.adapter = allDocAdapter

    }

}