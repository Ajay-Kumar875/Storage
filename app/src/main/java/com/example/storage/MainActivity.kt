package com.example.storage

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID
import com.google.firebase.storage.ktx.component1
import com.google.firebase.storage.ktx.component2

class MainActivity : ComponentActivity() {

    lateinit var btnChoose:Button
    lateinit var btnUpload:Button
    lateinit var imageview:ImageView

    var fileUri: Uri?=null
    lateinit var getImage: ActivityResultLauncher<String>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnChoose=findViewById(R.id.btnChoose)
        btnUpload=findViewById(R.id.btnUpload)
        imageview=findViewById(R.id.imageview)

        getImage=registerForActivityResult(ActivityResultContracts.GetContent(),
            ActivityResultCallback{
                if(it!=null){
                    fileUri=it
                }
                imageview.setImageURI(fileUri)
            })
        //choose an image
        btnChoose.setOnClickListener {
            //choosing an image
            getImage.launch("image/*")
        }

        //on upload image
        btnUpload.setOnClickListener {
            //uploading the image into cloud storage
            uploadImage()
        }
    }

    fun uploadImage(){

        if(fileUri!=null){
            val progressDialog=ProgressDialog(this)
            progressDialog.setTitle("Uploading... ")
            progressDialog.setMessage("Uploading your image... ")
            progressDialog.show()

            //creating a storage reference
            var storageRef:StorageReference= FirebaseStorage.getInstance()
                .getReference()
                .child(UUID.randomUUID().toString())

            storageRef.putFile(fileUri!!).addOnSuccessListener {

                progressDialog.dismiss()
                Toast.makeText(this,"Image uploaded",Toast.LENGTH_SHORT).show()
            }
                .addOnFailureListener{

                    progressDialog.dismiss()
                    Toast.makeText(this,"Failed to upload the image",Toast.LENGTH_SHORT).show()
                }



        }
    }

    fun getList(view:View){

        var storageRef:StorageReference= FirebaseStorage.getInstance()
            .getReference()


        storageRef.listAll().addOnSuccessListener {(items, prefixes) ->

            for (item in items) {
                // All the items under listRef.
                Toast.makeText(this,item.name,Toast.LENGTH_SHORT).show()
            }
        }
            .addOnFailureListener {
                // Uh-oh, an error occurred!
                Toast.makeText(this,"error",Toast.LENGTH_SHORT).show()
            }
    }
}