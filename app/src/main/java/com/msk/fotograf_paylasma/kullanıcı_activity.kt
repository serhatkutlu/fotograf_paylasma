package com.msk.fotograf_paylasma

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Text

class kullanıcı_activity : AppCompatActivity() {
    private lateinit var aut:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        aut= FirebaseAuth.getInstance()

    }
    fun giris(view: View){
        aut.signInWithEmailAndPassword(email.text.toString(),sifre.text.toString()).addOnCompleteListener { Task->
            if (Task.isSuccessful){
                val guncelkullanıcı=aut.currentUser!!.email
                Toast.makeText(this,"Hoşgeldin ${guncelkullanıcı}",Toast.LENGTH_LONG).show()
                val intent=Intent(this,haber_activity::class.java)
                startActivity(intent)
                finish()

            }
        }.addOnFailureListener { e->
            Toast.makeText(this,e.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }
    fun kayit(view: View){

        var mail=email.text.toString()
        var sifre=sifre.text.toString()
        aut.createUserWithEmailAndPassword(mail,sifre).addOnCompleteListener { Task->
            if (Task.isSuccessful){
                var intent =Intent(this,haber_activity::class.java)
                startActivity(intent)
                finish()
            }
        }.addOnFailureListener { e->
            Toast.makeText(this,e.localizedMessage,Toast.LENGTH_LONG).show()
        }


    }

}