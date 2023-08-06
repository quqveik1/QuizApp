import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kurlic.quizapp.R

class HomeButtonAdapter(private val data: List<String>) : RecyclerView.Adapter<HomeButtonItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeButtonItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.home_button_item, parent, false)
        return HomeButtonItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeButtonItemViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = data.size
}
