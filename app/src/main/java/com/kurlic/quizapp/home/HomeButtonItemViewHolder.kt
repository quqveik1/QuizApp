import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.kurlic.quizapp.R

class HomeButtonItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val button: Button = view.findViewById(R.id.homeButtonItem)

    fun bind(text: String) {
        button.text = text
    }
}
