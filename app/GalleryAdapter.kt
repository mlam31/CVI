import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.carnetdevoyageintelligent.cvi.R
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
class GalleryAdapter(private val imageUrls: List<String>) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView_gallery)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gallery, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUrl = imageUrls[position]
        // Charger l'image à partir de Firebase Storage et l'afficher dans ImageView avec Glide
        val storageReference = FirebaseStorage.getInstance().reference.child(imageUrl)
        Glide.with(holder.itemView)
            .load(storageReference)
            .into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return imageUrls.size
    }
}
