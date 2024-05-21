import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fintrack.CategoriaUi
import com.example.fintrack.DespesaUi
import com.example.fintrack.databinding.ItemDespesaBinding

class DespesaAdapter(
    private val context: Context,
    private val listaDespesa: List<DespesaUi>,
    private val listaCategoria: List<CategoriaUi>,
    private val onCategoriaSelected: (DespesaUi, CategoriaUi) -> Unit
) : RecyclerView.Adapter<DespesaAdapter.DespesaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DespesaViewHolder {
        val binding = ItemDespesaBinding.inflate(LayoutInflater.from(context), parent, false)
        return DespesaViewHolder(binding)
    }

    override fun getItemCount() = listaDespesa.size

    override fun onBindViewHolder(holder: DespesaViewHolder, position: Int) {
        val despesa = listaDespesa[position]
        val categoria = listaCategoria.find { it.iconeCategoria == despesa.categoria }
        holder.bind(despesa, categoria)
    }

    inner class DespesaViewHolder(private val binding: ItemDespesaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(despesa: DespesaUi, categoria: CategoriaUi?) {
            binding.despesa = despesa
            binding.categoria = categoria // Passando a instância da categoria para o layout
            binding.executePendingBindings()
        }
    }
}
