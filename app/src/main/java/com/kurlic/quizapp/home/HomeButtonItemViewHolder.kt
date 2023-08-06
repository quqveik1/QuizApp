import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.kurlic.quizapp.R
import com.kurlic.quizapp.game.GameFragment

class HomeButtonItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val button: Button = view.findViewById(R.id.homeButtonItem)

    fun bind(text: String) {
        button.text = text

        itemView.setOnClickListener{
            Toast.makeText(itemView.context, text, Toast.LENGTH_SHORT).show()

            val bData = Bundle()
            bData.putString(GameFragment.NAMEKEY, text)
            itemView.findNavController().navigate(R.id.action_HomeFragment_to_GameFragment, bData)
        }
    }
}
