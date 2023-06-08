    package br.edu.mouralacerda.dm2y2023projeto7

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

    class MainActivity : AppCompatActivity() {

        val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btnGravar).setOnClickListener {
            val intent = Intent(this, NovoCadastro::class.java)
            startActivity(intent)
        }

        findViewById<ListView>(R.id.lstDados).setOnItemLongClickListener { adapterView, view, position, id ->
            val dados = adapterView.adapter.getItem(position) as String
            val nome = dados.substringAfter("Nome: ").substringBefore(" | Curso:")
            val curso = dados.substringAfter("Curso: ")

            val builder = AlertDialog.Builder(this)
            builder
                .setTitle("Excluir este cadastro?")
                .setMessage("Tem certeza de que deseja excluir este cadastro?")
                .setPositiveButton("Tenho certeza") { dialog, wich ->
                    db.collection("pessoa")
                        .whereEqualTo("nome", nome)
                        .whereEqualTo("curso", curso)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            val documents = querySnapshot.documents

                            if(documents.isNotEmpty()) {
                                val documentId = documents[0].id

                                db.collection("pessoa")
                                    .document(documentId)
                                    .delete()
                                    .addOnSuccessListener {
                                        updateList()
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.d(ContentValues.TAG, "Erro ao excluir: ${exception}")
                                    }
                            }
                            Toast.makeText(this, "Cadastro removido!", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            Log.d(ContentValues.TAG, "Erro: ${exception}")
                        }
                }
                .setNegativeButton("Não") { dialog, wich ->
                    Toast.makeText(this, "cadastro não foi removido",
                        Toast.LENGTH_SHORT).show()
                }
                .show()
            true
        }

    }

        override fun onResume() {
            super.onResume()

            updateList()
        }

        fun updateList() {
            db.collection("pessoa")
                .get()
                .addOnSuccessListener { result ->
                    if(result == null) {
                        Log.d(ContentValues.TAG, "Documento não encontrado!")
                    } else {
                        val dados = result.map { document ->

                            val nome = document.getString("nome")
                            val curso = document.getString("curso")

                            "Nome: $nome | Curso: $curso"
                        }

                        findViewById<ListView>(R.id.lstDados).adapter = ArrayAdapter(
                            this@MainActivity,
                            android.R.layout.simple_list_item_1,
                            dados
                        )
                    }

                }
                .addOnFailureListener { expection ->
                    Log.d(ContentValues.TAG, "deu o seguinte erro: ${expection}")
                }
        }
}