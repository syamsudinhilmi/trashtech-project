package com.bangkit.trashtech.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bangkit.trashtech.databinding.ActivityAboutBinding
import com.bumptech.glide.Glide

class AboutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAboutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.apply {
            Glide.with(this@AboutActivity)
                .load("https://raw.githubusercontent.com/syamsudinhilmi/trashtech-capstone-team/main/capstone%20foto/Hilmi_.jpg?token=GHSAT0AAAAAACLCZYH3PMV4QHOA6CL5BU6KZMBSAKQ")
                .into(profileImage1)
            Glide.with(this@AboutActivity)
                .load("https://raw.githubusercontent.com/syamsudinhilmi/trashtech-capstone-team/main/capstone%20foto/gatau%20siapa.jpg?token=GHSAT0AAAAAACLCZYH3ZBD73D6PI3QC3DXSZMBSCKA")
                .into(profileImage2)
            Glide.with(this@AboutActivity)
                .load("https://raw.githubusercontent.com/syamsudinhilmi/trashtech-capstone-team/main/capstone%20foto/Fakhir.jpg?token=GHSAT0AAAAAACLCZYH3QYCXAVN4ESAHINUCZMBSDOA")
                .into(profileImage3)
            Glide.with(this@AboutActivity)
                .load("https://raw.githubusercontent.com/syamsudinhilmi/trashtech-capstone-team/main/capstone%20foto/Nathan.jpg?token=GHSAT0AAAAAACLCZYH3UT5YPHIKO6KYCEAWZMBSD4A")
                .into(profileImage4)
            Glide.with(this@AboutActivity)
                .load("https://raw.githubusercontent.com/syamsudinhilmi/trashtech-capstone-team/main/capstone%20foto/Cesa.jpg?token=GHSAT0AAAAAACLCZYH2RAYTTBXNOPOHXIGAZMBSEMQ")
                .into(profileImage5)
            Glide.with(this@AboutActivity)
                .load("https://raw.githubusercontent.com/syamsudinhilmi/trashtech-capstone-team/main/capstone%20foto/nnnn.JPG?token=GHSAT0AAAAAACLCZYH32P5CBZVFUGFY3SPUZMBSEWQ")
                .into(profileImage6)
        }
    }
}