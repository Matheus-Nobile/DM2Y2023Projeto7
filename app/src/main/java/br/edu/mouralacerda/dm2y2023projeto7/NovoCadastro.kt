package br.edu.mouralacerda.dm2y2023projeto7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import br.edu.mouralacerda.dm2y2023projeto7.databinding.ActivityListaDadosBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class NovoCadastro : AppCompatActivity() {

    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_dados)

        findViewById<Button>(R.id.btnSalvar).setOnClickListener {

            val nome = findViewById<EditText>(R.id.edtNome).text.toString()
            val curso = findViewById<EditText>(R.id.edtCurso).text.toString()

            val dados = hashMapOf(
                "nome" to nome,
                "curso" to curso,
            )

            db.collection("pessoa").add(dados)
                .addOnSuccessListener {
                    Toast.makeText(this, "Sucesso!", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                }
                .addOnFailureListener {
                    Toast.makeText(this, "Erro ao gravar os dados!", Toast.LENGTH_SHORT).show()
                }
        }
    }
}