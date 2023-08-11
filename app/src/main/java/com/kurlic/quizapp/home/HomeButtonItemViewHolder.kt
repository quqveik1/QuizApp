import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kurlic.quizapp.R
import com.kurlic.quizapp.game.GameFragment
import com.kurlic.quizapp.common.getImageResource
import com.kurlic.quizapp.common.loadImage

class HomeButtonItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val buttonImage: ImageView = view.findViewById(R.id.homeButtonImage)
    private val buttonText: TextView = view.findViewById(R.id.homeButtonText)

    fun bind(text: String) {

        buttonText.text = text

        loadImage(text, itemView.context, buttonImage, true)

        itemView.setOnClickListener {
            val bData = Bundle()
            bData.putString(GameFragment.NAMEKEY, text)
            itemView.findNavController().navigate(R.id.action_HomeFragment_to_GameFragment, bData)
        }
    }

}
