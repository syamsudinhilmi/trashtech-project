package com.bangkit.trashtech.ui.activity

import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bangkit.trashtech.R
import com.bangkit.trashtech.databinding.ActivityIdentificationBinding
import com.bangkit.trashtech.getImageUri
import com.bangkit.trashtech.ml.Model
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

class IdentificationActivity : AppCompatActivity() {
    private var currentImageUri: Uri? = null
    private lateinit var binding: ActivityIdentificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIdentificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(
                ContextCompat.getColor(
                    this,
                    R.color.primary
                )
            )
        )



        binding.btnKamera.setOnClickListener {
            startCamera()
        }
        binding.btnGaleri.setOnClickListener {
            startGallery()
        }
        binding.btnKlasifikasi.setOnClickListener {
            if (currentImageUri == null) {
                Toast.makeText(this, "Pilih gambar dulu ya!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                machineLearning()
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.imgPrev.setImageURI(it)
        }
    }

    private fun machineLearning() {
        val tensorImage = TensorImage(DataType.FLOAT32)

        // Load image from URI and convert it to a Bitmap
        val bitmap = BitmapFactory.decodeStream(currentImageUri?.let {
            contentResolver.openInputStream(
                it
            )
        })

        // Set the bitmap to the TensorImage
        tensorImage.load(bitmap)

        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(150, 150, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        imageProcessor.process(tensorImage)

        val model = Model.newInstance(this)

        // Creates inputs for reference.
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
        // Process the outputFeature0 array and get the predicted label
        val predictedLabel = getPredictedLabel(outputFeature0)

        // Display the result in tvResult
        binding.tvResult.text = "Yey hasilnya $predictedLabel"
        logAccuracy(outputFeature0)

        // Releases model resources if no longer used.
        model.close()
    }

    private fun getPredictedLabel(outputFeature0: FloatArray): String {
        // Determine the index with the highest probability
        val maxIndex = outputFeature0.indices.maxBy { outputFeature0[it] }

        // Define class labels
        val classLabels = arrayOf(
            "Carton Packaging",
            "Glass",
            "Paper",
            "Plastic Bottles",
            "Tin Bottles"
        )

        // Get the probability score for the predicted class
        val confidenceScore = outputFeature0[maxIndex]

        // Format the result string with class label and confidence percentage
        val resultString = "${(confidenceScore * 100).toInt()}% ${classLabels[maxIndex]} "
        Log.d("TAG", "machineLearning: $confidenceScore")

        return resultString
    }

    private fun logAccuracy(outputFeature0: FloatArray) {
        // Define class labels
        val classLabels = arrayOf(
            "Carton Packaging",
            "Glass",
            "Paper",
            "Plastic Bottles",
            "Tin Bottles"
        )

        // Print accuracy for each class
        for (i in classLabels.indices) {
            val confidenceScore = outputFeature0[i]
            val accuracy = (confidenceScore * 100).toInt()
            Log.d("Class Accuracy", "${classLabels[i]}: $accuracy%")
        }
    }
}
