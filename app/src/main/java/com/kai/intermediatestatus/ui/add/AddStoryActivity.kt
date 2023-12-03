package com.kai.intermediatestatus.ui.add

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kai.intermediatestatus.R
import com.kai.intermediatestatus.data.api.Result
import com.kai.intermediatestatus.databinding.ActivityAddstoryBinding
import com.kai.intermediatestatus.ui.ViewModelFactory
import com.kai.intermediatestatus.ui.main.MainActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddstoryBinding
    private lateinit var myButton: Button
    private lateinit var myEditText: EditText
    private var myLocation: Location? = null
    private val viewModel by viewModels<AddStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private var imageUri: Uri? = null
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted: Boolean ->
        if (granted) {
            Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun permissionGranted() = ContextCompat.checkSelfPermission(
        this, REQUIRED_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED
//test add comment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddstoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!permissionGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.btnGallery.setOnClickListener { startGallery() }
        binding.btnCamera.setOnClickListener { startCamera() }
        binding.btnUpload.setOnClickListener { uploadImage() }

        myButton = findViewById(R.id.btn_upload)
        myEditText = findViewById(R.id.description)

        setCustomButton()
        setupView()
        setAdd()
    }

    @Suppress("DEPRECATION")
    private fun setupView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setCustomButton() {
        val result = myEditText.text
        myButton.isEnabled = result != null && result.toString().isNotEmpty()

        myEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setCustomButton()
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    private fun setAdd() {
        viewModel.addStory.observe(this) {
            when (it) {
                is Result.Loading -> {
                    showLoading(true)
                }

                is Result.Success -> {
                    showLoading(false)
                    AlertDialog.Builder(this).apply {
                        setTitle("Congratulation!!")
                        setMessage(getString(R.string.message))
                        setCancelable(false)
                        setPositiveButton(getString(R.string.next)) { _, _ ->
                            val toMain = Intent(context, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(toMain)
                            finish()
                        }
                        create()
                        show()
                    }
                }

                is Result.Error -> {
                    showLoading(false)
                }
            }
        }
    }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startGallery() {
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage() {
        imageUri?.let {
            Log.d("Image Uri", "showImage: $it")
            binding.ivPreviewImage.setImageURI(it)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            showImage()
        }
    }

    private fun startCamera() {
        imageUri = getImageUri(this)
        launcherIntentCamera.launch(imageUri)
    }

    private fun uploadImage() {
        var token: String
        imageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceSize()
            val description = binding.description.text.toString()
            Log.d("Image File", "showImage: ${imageFile.path}")

            showLoading(true)

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody =
                MultipartBody.Part.createFormData("photo", imageFile.name, requestImageFile)

            viewModel.getSession().observe(this) { user ->
                token = user.token
                viewModel.addStory(token, multipartBody, requestBody, myLocation)
            }

        } ?: showToast(getString(R.string.warning_no_image_selected))
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
    }
}