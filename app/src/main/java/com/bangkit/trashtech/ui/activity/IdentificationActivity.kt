package com.bangkit.trashtech.ui.activity

import android.content.Intent
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
import androidx.lifecycle.ViewModelProvider
import com.bangkit.trashtech.R
import com.bangkit.trashtech.data.database.History
import com.bangkit.trashtech.databinding.ActivityIdentificationBinding
import com.bangkit.trashtech.getImageUri
import com.bangkit.trashtech.ml.ModelBaru
import com.bangkit.trashtech.ui.viewmodel.HistoryViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

class IdentificationActivity : AppCompatActivity() {
    private var currentImageUri: Uri? = null
    private lateinit var binding: ActivityIdentificationBinding
    private lateinit var historyViewModel: HistoryViewModel
    private lateinit var storageRef: StorageReference

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

        historyViewModel = ViewModelProvider(this)[HistoryViewModel::class.java]
        storageRef = Firebase.storage.reference

        binding.apply {
            btnGaleri.setOnClickListener { startGallery() }
            btnKamera.setOnClickListener { startCamera() }
            btnKlasifikasi.setOnClickListener {
                if (currentImageUri == null) {
                    Toast.makeText(
                        this@IdentificationActivity,
                        getString(R.string.alert_image_selected),
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                } else {
                    machineLearning()
                }
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
            contentResolver.openInputStream(it)
        })

        Log.d("uri", currentImageUri.toString())

        // Set the bitmap to the TensorImage
        tensorImage.load(bitmap)
        val imageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(150, 150, ResizeOp.ResizeMethod.BILINEAR))
            .build()

        imageProcessor.process(tensorImage)

        val model = ModelBaru.newInstance(this)

        // Creates inputs for reference.
        val inputFeature0 =
            TensorBuffer.createFixedSize(intArrayOf(1, 150, 150, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(tensorImage.buffer)

        // Runs model inference and gets result.
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer.floatArray
        // Process the outputFeature0 array and get the predicted label
        val prediction = getPredictedLabel(outputFeature0)
        val predictedLabel = prediction[0]
        val recommendation = prediction[1]

        // Display the result in tvResult
        binding.apply {
            tvResult.text = getString(R.string.predict_label_resource, predictedLabel)
            tvRecommendation.text = recommendation
            tvBankSampah.text = getString(R.string.text_trash_map)
            tvDisini.text = getString(R.string.here_text)
            tvDisini.setOnClickListener {
                startActivity(Intent(this@IdentificationActivity, MapsActivity::class.java))
            }
        }
        logAccuracy(outputFeature0)

        // Releases model resources if no longer used.
        model.close()

        currentImageUri?.let { storageRef.child("prediction images").child(dateNow(false)).putFile(it) }
            ?.addOnSuccessListener {taskSnapshot ->
                val task = taskSnapshot.storage.downloadUrl
                while (!task.isComplete){ }
                val urlImage = task.result.toString()

                // insert into room database
                val history = History(0, urlImage, predictedLabel, dateNow(true), recommendation)
                historyViewModel.insertHistory(history)

            }
            ?.addOnFailureListener{
                Log.e("error upload firebase", it.toString())
            }
    }

    private fun getPredictedLabel(outputFeature0: FloatArray): Array<String> {
        val maxIndex = outputFeature0.indices.maxBy { outputFeature0[it] }
        classLabels()
        // Get the probability score for the predicted class
        val confidenceScore = outputFeature0[maxIndex ?: 0]
        val wasteRecommendations = getWasteRecommendations(maxIndex ?: -1)

        val result = arrayOf(
            "${(confidenceScore * 100).toInt()}% ${classLabels()[maxIndex ?: 0]}",
            "Sampah kamu bisa dijadikan: $wasteRecommendations\""
        )
//        return "${(confidenceScore * 100).toInt()}% ${classLabels()[maxIndex ?: 0]} \n\nSampah kamu bisa dijadikan: $wasteRecommendations"

        return result
    }

    private fun getWasteRecommendations(predictedLabel: Int): String {
        val wasteRecommendations = mapOf(
            0 to getString(R.string.rec_0),
            1 to getString(R.string.rec_1),
            2 to getString(R.string.rec_2),
            3 to getString(R.string.rec_3),
            4 to getString(R.string.rec_4)
        )
        return wasteRecommendations[predictedLabel] ?: getString(R.string.rec_other)
    }

    private fun logAccuracy(outputFeature0: FloatArray) {
        classLabels()
        // Print accuracy for each class
        for (i in classLabels().indices) {
            val confidenceScore = outputFeature0[i]
            val accuracy = (confidenceScore * 100).toInt()
            Log.d("Class Accuracy", "${classLabels()[i]}: $accuracy%")
        }
    }

    private fun dateNow(state: Boolean): String {
        if (state){
            val calendar = Calendar.getInstance()
            val currentDate = calendar.time
            val dateFormat = SimpleDateFormat("EEEE, d MMM yyyy", Locale("id", "ID"))
            return dateFormat.format(currentDate)
        } else{
            val currentDateTime = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd_MM_yyyy_HH:mm")
            val formattedTime = currentDateTime.format(formatter)
            return formattedTime
        }

    }

    private fun classLabels(): Array<String> {
        return arrayOf(
            "Karton Kemasan",
            "Kaca",
            "Kertas",
            "Botol Plastik",
            "Botol Metal"
        )
    }
}
