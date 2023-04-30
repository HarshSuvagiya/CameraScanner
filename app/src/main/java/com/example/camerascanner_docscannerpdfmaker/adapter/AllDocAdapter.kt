package com.example.camerascanner_docscannerpdfmaker.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.camerascanner_docscannerpdfmaker.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class AllDocAdapter(list: MutableList<File>, param: RefreshList) : RecyclerView.Adapter<AllDocAdapter.MyViewHolder>() {

    lateinit var mContext: Context
    var myList = list
    var refreshList = param

    interface RefreshList{
        fun refreshList()
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title = itemView.findViewById<TextView>(R.id.title)
        var icon = itemView.findViewById<ImageView>(R.id.icon)
        var description = itemView.findViewById<TextView>(R.id.description)
        var mainLayout = itemView.findViewById<ConstraintLayout>(R.id.mainLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        mContext = parent.context
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.all_doc_adapter_layout, parent, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return myList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = myList.get(position).name
        holder.description.text =
            SimpleDateFormat("dd-MM-yyyy hh-mm-ss a").format(Date(myList[position].lastModified()))
        if (myList[position].name.contains("pdf")) {
            Glide.with(mContext).load(R.drawable.ic_pdf_icon).into(holder.icon)
            holder.mainLayout.setOnClickListener {
                openDialog(myList[position])
            }
        } else {
            Glide.with(mContext).load(myList[position].absolutePath).into(holder.icon)
            holder.mainLayout.setOnClickListener {
                openDialog(myList[position])
            }
        }
    }

    fun openDialog(file: File) {
        val dialog = Dialog(mContext)
        dialog.setContentView(R.layout.view_dialog)
        dialog.show()
        var title: TextView = dialog.findViewById(R.id.title)
        var view: TextView = dialog.findViewById(R.id.view)
        var delete: TextView = dialog.findViewById(R.id.delete)
        var rename: TextView = dialog.findViewById(R.id.rename)
        var share: TextView = dialog.findViewById(R.id.share)

        title.text = file.name
        view.setOnClickListener {
            val extension: String = file.absolutePath.substring(file.absolutePath.lastIndexOf("."))
            if (extension.equals(".pdf"))
                openPdf(file)
            else
                openImage(file)
            dialog.dismiss()
        }
        delete.setOnClickListener {
            deleteDialog(file)
            dialog.dismiss()
        }
        rename.setOnClickListener {
            renameDialog(file)
            dialog.dismiss()
        }
        share.setOnClickListener {
            share(file)
        }
    }

    fun deleteDialog(file: File) {
        val dialog = Dialog(mContext)
        dialog.setContentView(R.layout.delete_dialog)
        dialog.show()

        var cancel: Button = dialog.findViewById(R.id.cancel)
        var ok: Button = dialog.findViewById(R.id.ok)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        ok.setOnClickListener {
            if (file.exists())
                file.delete()
            notifyDataSetChanged()
            Toast.makeText(mContext, "Deleted", Toast.LENGTH_LONG).show()
            refreshList.refreshList()
            dialog.dismiss()
        }

    }

    fun renameDialog(file: File) {
        val dialog = Dialog(mContext)
        dialog.setContentView(R.layout.rename_dialog)
        dialog.show()

        var cancel: Button = dialog.findViewById(R.id.cancel)
        var ok: Button = dialog.findViewById(R.id.ok)
        var fileNameEditText: EditText = dialog.findViewById(R.id.fileNameEditText)

        cancel.setOnClickListener {
            dialog.dismiss()
        }
        ok.setOnClickListener {
            val extension: String = file.absolutePath.substring(file.absolutePath.lastIndexOf("."))

            if (fileNameEditText.text.trim().length != 0) {
                val oldFile = File(file.parentFile.absolutePath, file.name)
                val newFile =
                    File(file.parentFile.absolutePath, "${fileNameEditText.text}${extension}")
                oldFile.renameTo(newFile)
            } else
                Toast.makeText(mContext, "Please enter name", Toast.LENGTH_LONG).show()
            dialog.dismiss()
            notifyDataSetChanged()
            refreshList.refreshList()
        }
    }

    fun openPdf(file: File) {
        val photoURI: Uri = FileProvider.getUriForFile(
            mContext,
            mContext.applicationContext.packageName.toString() + ".provider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(photoURI, "application/pdf")
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.startActivity(intent)
    }

    fun openImage(file: File) {
        val photoURI: Uri = FileProvider.getUriForFile(mContext, mContext.applicationContext.packageName.toString() + ".provider", file)
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(photoURI, "image/*")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.startActivity(intent)
    }

    fun share(file: File) {
        val share = Intent("android.intent.action.SEND")
        share.type = "*/*"
        val photoURI: Uri = FileProvider.getUriForFile(
            mContext,
            mContext.applicationContext.packageName.toString() + ".provider",
            file
        )
        share.putExtra("android.intent.extra.STREAM", photoURI)
        mContext.startActivity(Intent.createChooser(share, "Share"))
    }

}