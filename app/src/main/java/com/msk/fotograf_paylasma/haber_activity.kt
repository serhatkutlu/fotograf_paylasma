package com.msk.fotograf_paylasma

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase

class haber_activity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseFirestore
    var postlistesi= arrayListOf<Post>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_haber)
        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()
        verileri_al()
    }

    private fun verileri_al() {
        database.collection("Post").orderBy("tarih",Query.Direction.DESCENDING).addSnapshotListener { value, error ->
            if (error!=null){

                Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
            }
            else if (!value!!.isEmpty){
                val documents=value.documents
                postlistesi.clear()
                for (document in documents){
                    var mail=document.get("mail").toString()
                    var uri=document.get("uri").toString()
                    var yorum=document.get("yorum").toString()
                    var liste=Post(mail,uri,yorum)
                    postlistesi.add(liste)
                    }

                }
            }
        }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.options,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.resim_paylas){
            val intent=Intent(this,fotograf_paylasma_activity::class.java)
            startActivity(intent)
        }
        if (item.itemId==R.id.cikis) {
            auth.signOut()
             val intent=Intent(this,kullanici_activity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}