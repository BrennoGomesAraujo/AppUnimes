package br.unimes.appunimes

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.unimes.appunimes.databinding.ActivityCadastroBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar ActionBar
        supportActionBar?.apply {
            title = "Cadastrar Produto"
            setDisplayHomeAsUpEnabled(true) // Botão de voltar
            setDisplayShowHomeEnabled(true)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val db = Firebase.firestore

        // Botão Cadastrar
        binding.btnCadastrar.setOnClickListener {
            cadastrarProduto(db)
        }

        // FAB para ir para a lista de produtos (único botão para lista)
        binding.fabLista.setOnClickListener {
            irParaListaProdutos()
        }
    }

    // Habilitar o botão de voltar na ActionBar
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun cadastrarProduto(db: com.google.firebase.firestore.FirebaseFirestore) {
        val nome = binding.edtNome.text.toString()
        val precoStr = binding.edtPreco.text.toString()
        val quantidadeStr = binding.edtQuantidade.text.toString()
        val categoria = binding.edtCategoria.text.toString()

        // Validação básica dos campos
        if (nome.isEmpty() || precoStr.isEmpty() || quantidadeStr.isEmpty() || categoria.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            return
        }

        // Validação de números
        try {
            val preco = precoStr.toDouble()
            val quantidade = quantidadeStr.toInt()

            // Validação de valores positivos
            if (preco < 0 || quantidade < 0) {
                Toast.makeText(this, "Preço e quantidade devem ser positivos", Toast.LENGTH_SHORT).show()
                return
            }

            val produto = hashMapOf(
                "nome" to nome,
                "preco" to preco,
                "quantidade" to quantidade,
                "categoria" to categoria
            )

            db.collection("produtos")
                .add(produto)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Produto cadastrado com sucesso", Toast.LENGTH_SHORT).show()
                        // Limpar campos após cadastro
                        binding.edtNome.text?.clear()
                        binding.edtPreco.text?.clear()
                        binding.edtQuantidade.text?.clear()
                        binding.edtCategoria.text?.clear()
                    } else {
                        Toast.makeText(this, "Falha no cadastro", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Erro: ${exception.message}", Toast.LENGTH_SHORT).show()
                }

        } catch (e: NumberFormatException) {
            Toast.makeText(this, "Preço e quantidade devem ser números válidos", Toast.LENGTH_SHORT).show()
        }
    }

    private fun irParaListaProdutos() {
        val intent = Intent(this, ListaProdutos::class.java)
        startActivity(intent)
    }
}