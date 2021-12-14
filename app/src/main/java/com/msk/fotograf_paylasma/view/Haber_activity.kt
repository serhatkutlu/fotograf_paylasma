package com.msk.fotograf_paylasma.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.msk.fotograf_paylasma.model.Post
import com.msk.fotograf_paylasma.R
import com.msk.fotograf_paylasma.adapter.recycler_adapter
import kotlinx.android.synthetic.main.activity_haber.*

class haber_activity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth
    private lateinit var database:FirebaseFirestore
    private lateinit var recyclerAdapter: recycler_adapter
    var postlistesi= arrayListOf<Post>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_haber)
        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()
        verileri_al()
        var layoutManager=LinearLayoutManager(this)
        recyclerview.layoutManager=layoutManager
        recyclerAdapter= recycler_adapter(postlistesi)
        recyclerview.adapter=recyclerAdapter

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
                    val mail=document.get("mail").toString()
                    val uri=document.get("uri").toString()
                    val yorum=document.get("yorum").toString()
                    val liste= Post(mail,uri,yorum)
                    postlistesi.add(liste)
                    }

                }
            recyclerAdapter.notifyDataSetChanged()
            }
        }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater=menuInflater
        inflater.inflate(R.menu.options,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId== R.id.resim_paylas){
            val intent=Intent(this, fotograf_paylasma_activity::class.java)
            startActivity(intent)
        }
        if (item.itemId== R.id.cikis) {
            auth.signOut()
             val intent=Intent(this, kullanici_activity::class.java)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}