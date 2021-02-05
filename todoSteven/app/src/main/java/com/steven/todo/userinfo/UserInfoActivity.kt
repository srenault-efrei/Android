package com.steven.todo.userinfo

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import coil.load
import com.steven.todo.BuildConfig
import com.steven.todo.R
import com.steven.todo.tasklist.UserInfo
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserInfoActivity : AppCompatActivity() {

    private val viewModel: UserInfoViewModel by viewModels()
    private var uriAvatar: String = ""

    // create a temp file and get a uri for it
    private val photoUri by lazy {
        FileProvider.getUriForFile(
            this,
            BuildConfig.APPLICATION_ID + ".fileprovider",
            File.createTempFile("avatar", ".jpeg", externalCacheDir)

        )
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) openCamera()
            else showExplanationDialog()
        }

    private fun requestCameraPermission() =
        requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)

    private fun askCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED -> openCamera()
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> showExplanationDialog()
            else -> requestCameraPermission()
        }
    }

    private fun showExplanationDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("On a besoin de la caméra sivouplé ! 🥺")
            setPositiveButton("Bon, ok") { _, _ ->
                requestCameraPermission()
            }
            setCancelable(true)
            show()
        }
    }

    // register
    private val takePicture =
        registerForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) handleImage(photoUri)
            else Toast.makeText(this, "Erreur ! 😢", Toast.LENGTH_LONG).show()
        }

    // use
    private fun openCamera() = takePicture.launch(photoUri)

    // convert
    private fun convert(uri: Uri) =
        MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "temp.jpeg",
            body = contentResolver.openInputStream(uri)!!.readBytes().toRequestBody()
        )

    private fun handleImage(uri: Uri) {
        lifecycleScope.launch {
            viewModel.updateAvatar(convert(uri))
        }
    }

    // register
    private val pickInGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { handleImage(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userinfo)

        val takePictureButton = findViewById<Button>(R.id.take_picture_button)
        takePictureButton.setOnClickListener {
            System.out.println("click")
            askCameraPermissionAndOpenCamera()
        }

        val pickInGalleryButton = findViewById<Button>(R.id.upload_image_button)
        pickInGalleryButton.setOnClickListener {
            pickInGallery.launch("image/*")
        }

        viewModel.getInfos()
        val avatar = findViewById<ImageView>(R.id.image_view)
        val textName = findViewById<EditText>(R.id.user_name)
        val textFirstname = findViewById<EditText>(R.id.user_firstname)
        val textEmail = findViewById<EditText>(R.id.user_email)
        viewModel.infos.observe(this) { user ->
            uriAvatar = user.avatar
            avatar.load(user.avatar)
            textName.setText(user.lastName)
            textFirstname.setText(user.firstName)
            textEmail.setText(user.email)
        }

        val editUserButton = findViewById<Button>(R.id.valid_edit_user)
        editUserButton.setOnClickListener {
            val newUser = UserInfo(
                email = textEmail.text.toString(),
                firstName = textFirstname.text.toString(),
                lastName = textName.text.toString(),
                avatar = uriAvatar.toString(

                )
            )
            viewModel.update(newUser)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

}
