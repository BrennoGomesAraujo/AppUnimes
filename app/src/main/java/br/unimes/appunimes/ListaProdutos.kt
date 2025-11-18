package br.unimes.appunimes

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.unimes.appunimes.databinding.ActivityListaProdutosBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.text.NumberFormat
import java.util.Locale

class ListaProdutos : AppCompatActivity() {
    private lateinit var binding: ActivityListaProdutosBinding
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListaProdutosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar ActionBar
        supportActionBar?.apply {
            title = "Lista de Produtos"
            setDisplayHomeAsUpEnabled(true) // Botão de voltar
            setDisplayShowHomeEnabled(true)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.container_produtos)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        carregarProdutos()
    }

    // Habilitar o botão de voltar na ActionBar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun carregarProdutos() {
        // Mostrar mensagem de carregamento
        binding.containerProdutos.removeAllViews()
        val tvCarregando = TextView(this).apply {
            text = "Carregando produtos..."
            textSize = 16f
            setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
        }
        binding.containerProdutos.addView(tvCarregando)

        db.collection("produtos")
            .get()
            .addOnSuccessListener { documents ->
                binding.containerProdutos.removeAllViews()

                if (documents.isEmpty) {
                    mostrarMensagemVazia()
                } else {
                    // Adiciona um título
                    adicionarTitulo("Meus Produtos Cadastrados")

                    // Para cada documento, cria um CardView
                    for (document in documents) {
                        val produto = document.data
                        criarCardProduto(produto)
                    }
                }
            }
            .addOnFailureListener { exception ->
                binding.containerProdutos.removeAllViews()
                mostrarErro("Erro ao carregar produtos: ${exception.message}")
            }
    }

    private fun criarCardProduto(produto: Map<String, Any>) {
        // Cria o CardView
        val cardView = CardView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 16.dpToPx())
            }
            cardElevation = 8f
            radius = 12f
            setCardBackgroundColor(ContextCompat.getColor(context, android.R.color.white))
        }

        // Cria o LinearLayout interno
        val linearLayout = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = LinearLayout.VERTICAL
            setPadding(16.dpToPx(), 16.dpToPx(), 16.dpToPx(), 16.dpToPx())
        }

        // Adiciona os campos do produto com tratamento seguro
        val nome = produto["nome"]?.toString() ?: "Não informado"
        val preco = (produto["preco"] as? Double) ?: (produto["preco"] as? Long)?.toDouble() ?: 0.0
        val quantidade = (produto["quantidade"] as? Long) ?: (produto["quantidade"] as? Int)?.toLong() ?: 0
        val categoria = produto["categoria"]?.toString() ?: "Não informada"

        // Formata o preço para Real brasileiro
        val formatador = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
        val precoFormatado = formatador.format(preco)

        // Cria os TextViews para cada campo
        val tvNome = criarTextView("Nome: $nome", 18, true)
        val tvPreco = criarTextView("Preço: $precoFormatado", 16, false)
        val tvQuantidade = criarTextView("Quantidade: $quantidade", 16, false)
        val tvCategoria = criarTextView("Categoria: $categoria", 16, false)

        // Adiciona linha separadora
        val separador = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                1
            ).apply {
                setMargins(0, 8.dpToPx(), 0, 8.dpToPx())
            }
            setBackgroundColor(ContextCompat.getColor(this@ListaProdutos, android.R.color.darker_gray))
        }

        // Adiciona todas as views ao LinearLayout
        linearLayout.addView(tvNome)
        linearLayout.addView(tvPreco)
        linearLayout.addView(tvQuantidade)
        linearLayout.addView(tvCategoria)
        linearLayout.addView(separador)

        // Adiciona o LinearLayout ao CardView
        cardView.addView(linearLayout)

        // Adiciona o CardView ao container principal
        binding.containerProdutos.addView(cardView)
    }

    private fun criarTextView(texto: String, tamanhoTexto: Int, negrito: Boolean): TextView {
        return TextView(this).apply {
            text = texto
            textSize = tamanhoTexto.toFloat()
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 4.dpToPx(), 0, 4.dpToPx())
            }

            if (negrito) {
                setTypeface(typeface, android.graphics.Typeface.BOLD)
            }
        }
    }

    private fun adicionarTitulo(titulo: String) {
        val tvTitulo = TextView(this).apply {
            text = titulo
            textSize = 24f
            setTextColor(ContextCompat.getColor(context, android.R.color.black))
            setTypeface(typeface, android.graphics.Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 0, 0, 24.dpToPx())
            }
        }
        binding.containerProdutos.addView(tvTitulo)
    }

    private fun mostrarMensagemVazia() {
        val tvMensagem = TextView(this).apply {
            text = "Nenhum produto cadastrado ainda.\nVolte à tela de cadastro para adicionar produtos."
            textSize = 16f
            setTextColor(ContextCompat.getColor(context, android.R.color.darker_gray))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = android.view.Gravity.CENTER
            }
        }
        binding.containerProdutos.addView(tvMensagem)
    }

    private fun mostrarErro(mensagem: String) {
        val tvErro = TextView(this).apply {
            text = mensagem
            textSize = 16f
            setTextColor(ContextCompat.getColor(context, android.R.color.holo_red_dark))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = android.view.Gravity.CENTER
            }
        }
        binding.containerProdutos.addView(tvErro)
    }

    // Extension function para converter dp para pixels
    private fun Int.dpToPx(): Int {
        val density = resources.displayMetrics.density
        return (this * density).toInt()
    }
}