package com.mianasad.pico

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import android.provider.MediaStore
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants
import com.mianasad.pico.ResultActivity
import android.graphics.Bitmap
import android.net.Uri
import android.widget.Toast
import com.mianasad.pico.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    var IMAGE_REQUEST_CODE = 45
    var CAMERA_REQUEST_CODE = 14
    var RESULT_CODE = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        edit_Btn.setOnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent, IMAGE_REQUEST_CODE)

        }
        camera_Btn.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!=PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),32 )
            }
            else
            {
                val cameraintent=Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraintent,CAMERA_REQUEST_CODE)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==IMAGE_REQUEST_CODE && resultCode== RESULT_OK)
        {
            if(data?.data != null) {
//            Toast.makeText(this,data?.data.toString(),Toast.LENGTH_LONG).show()
                val filepath = data?.data
                val dsPhotoEditorIntent = Intent(this, DsPhotoEditorActivity::class.java)
                dsPhotoEditorIntent.setData(filepath)
                dsPhotoEditorIntent.putExtra(
                    DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
                    "SnapEditor"
                );
                val toolsToHide = intArrayOf(
                    DsPhotoEditorActivity.TOOL_ORIENTATION,
                    DsPhotoEditorActivity.TOOL_CROP
                )

                dsPhotoEditorIntent.putExtra(
                    DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
                    toolsToHide
                );
                startActivityForResult(dsPhotoEditorIntent, RESULT_CODE)
            }
        }
        if(requestCode==RESULT_CODE)
        {
            val intent =Intent(this,ResultActivity::class.java)
            intent.setData(data?.data)
            startActivity(intent)
            Toast.makeText(this,"Picture is saved automatically", Toast.LENGTH_LONG).show()
        }
        if(requestCode==CAMERA_REQUEST_CODE)
        {
            val photo = data!!.extras!!["data"] as Bitmap?
            val uri=getImgUri(photo)
            val dsPhotoEditorIntent = Intent(this, DsPhotoEditorActivity::class.java)
            dsPhotoEditorIntent.setData(uri)
            dsPhotoEditorIntent.putExtra(
                DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY,
                "SnapEditor"
            );
            val toolsToHide = intArrayOf(
                DsPhotoEditorActivity.TOOL_ORIENTATION,
                DsPhotoEditorActivity.TOOL_CROP
            )

            dsPhotoEditorIntent.putExtra(
                DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE,
                toolsToHide
            );
            startActivityForResult(dsPhotoEditorIntent, RESULT_CODE)
        }
    }
    fun getImgUri(bitmap: Bitmap?):Uri{
        val arrayOutputStream=ByteArrayOutputStream()
        bitmap!!.compress(Bitmap.CompressFormat.JPEG,10,arrayOutputStream)
        val path=MediaStore.Images.Media.insertImage(contentResolver,bitmap,"Title",null)
        return Uri.parse(path)
    }
}

