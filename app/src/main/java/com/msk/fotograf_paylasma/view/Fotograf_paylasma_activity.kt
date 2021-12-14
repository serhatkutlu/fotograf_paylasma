package com.msk.fotograf_paylasma.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.msk.fotograf_paylasma.R
import kotlinx.android.synthetic.main.activity_fotograf_paylasma.*
import java.util.*

class fotograf_paylasma_activity : AppCompatActivity() {

    var secilengorsel:Uri?=null
    var Bitmap:Bitmap?=null
    lateinit var storage:FirebaseStorage
    lateinit var auth:FirebaseAuth
    lateinit var database:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotograf_paylasma)
        storage= FirebaseStorage.getInstance()
        auth= FirebaseAuth.getInstance()
        database= FirebaseFirestore.getInstance()
    }
    fun paylas(view:View){

        val uuid=UUID.randomUUID()
        val gorselismi=uuid.toString()+".jpg"

        var referance=storage.reference
        var image_referance=referance.child("images").child(gorselismi)

        if (secilengorsel!=null){
            image_referance.putFile(secilengorsel!!).addOnSuccessListener {
                val yuklenengorselreferans=storage.reference.child("images").child(gorselismi).downloadUrl.addOnSuccessListener {
                    val downloaduri=it.toString()
                    val email=auth.currentUser!!.email.toString()
                    val kullanici_yorumu=yorum.text.toString()
                    val tarih=com.google.firebase.Timestamp.now()
                    //veritabanı
                    val postHashMap= hashMapOf<String,Any>()
                    postHashMap.put("uri",downloaduri)
                    postHashMap.put("mail",email)
                    postHashMap.put("yorum",kullanici_yorumu)
                    postHashMap.put("tarih",tarih)

                    database.collection("Post").add(postHashMap).addOnCompleteListener {
                        if (it.isSuccessful){
                            finish()
                        }
                    }.addOnFailureListener {
                        Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                    }


                }.addOnFailureListener {
                    Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show()
                }

            }
        }
    }
    fun gorsel_sec(view: View){
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)

        }else{
            val galeri_intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galeri_intent,2)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode==1&&grantResults.size>0){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                val galeri_intent=Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galeri_intent,2)
            }

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode==2&&resultCode==Activity.RESULT_OK&&data!=null){

            secilengorsel=data.data
            if(secilengorsel!=null){
                if (Build.VERSION.SDK_INT>=28){
                    val source=ImageDecoder.createSource(this.contentResolver,secilengorsel!!)
                    Bitmap=ImageDecoder.decodeBitmap(source)
                    resim.setImageBitmap(Bitmap)
                }else{
                    Bitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,secilengorsel)
                    resim.setImageBitmap(Bitmap)
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}