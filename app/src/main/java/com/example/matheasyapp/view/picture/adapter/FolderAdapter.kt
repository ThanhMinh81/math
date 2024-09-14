package com.example.matheasyapp.view.picture.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.matheasyapp.databinding.ItemFolderBinding

class FolderAdapter(
    private val folderMap: Map<String, List<String>>,
    private val onFolderClick: (String) -> Unit,
    private val context : Context
) : RecyclerView.Adapter<FolderAdapter.FolderViewHolder>() {

    inner class FolderViewHolder(private val binding: ItemFolderBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(folderName: String, imagePath: String, imageCount: Int) {
            binding.tvFolderName.text = folderName
            binding.tvImageCount.text = "$imageCount images"

            Glide.with(context)
                .load(imagePath)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_close_clear_cancel)
                .into(binding.ivRepresentative)

            // Load the representative image using your preferred image loading library
            // Example: Glide, Picasso
            // Glide.with(binding.ivRepresentative.context).load(imagePath).into(binding.ivRepresentative)

            binding.root.setOnClickListener {
                onFolderClick(folderName)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FolderViewHolder {
        val binding = ItemFolderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FolderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FolderViewHolder, position: Int) {
        val folderName = folderMap.keys.elementAt(position)
        val images = folderMap[folderName]!!
        holder.bind(folderName, images[0], images.size)
    }

    override fun getItemCount(): Int {
        return folderMap.size
    }
}
