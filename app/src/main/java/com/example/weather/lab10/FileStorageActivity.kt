package com.example.weather.lab10

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.weather.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FileStorageActivity : AppCompatActivity() {

    companion object {
        private const val INTERNAL_FILE = "weather_notes_internal.txt"
        private const val EXTERNAL_FILE = "weather_notes_export.txt"
    }

    private lateinit var etCity: EditText
    private lateinit var etNote: EditText
    private lateinit var tvOutput: TextView

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) saveExternal()
        else Toast.makeText(this, R.string.lab10_no_permission, Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab10_activity_file_storage)

        etCity = findViewById(R.id.etCity)
        etNote = findViewById(R.id.etNote)
        tvOutput = findViewById(R.id.tvOutput)

        findViewById<Button>(R.id.btnSaveInternal).setOnClickListener { saveInternal() }
        findViewById<Button>(R.id.btnReadInternal).setOnClickListener { readInternal() }
        findViewById<Button>(R.id.btnSaveExternal).setOnClickListener { checkAndSaveExternal() }
        findViewById<Button>(R.id.btnReadExternal).setOnClickListener { readExternal() }
    }

    private fun buildNoteText(): String? {
        val city = etCity.text.toString().trim()
        val note = etNote.text.toString().trim()
        if (city.isEmpty() || note.isEmpty()) {
            Toast.makeText(this, R.string.lab10_error_empty, Toast.LENGTH_SHORT).show()
            return null
        }
        val time = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date())
        return getString(R.string.lab10_note_template, city, time, note)
    }

    private fun saveInternal() {
        val text = buildNoteText() ?: return
        try {
            openFileOutput(INTERNAL_FILE, MODE_APPEND).use { it.write(text.toByteArray()) }
            Toast.makeText(this, R.string.lab10_saved_internal, Toast.LENGTH_SHORT).show()
            etNote.text.clear()
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.lab10_error, e.message), Toast.LENGTH_SHORT).show()
        }
    }

    private fun readInternal() {
        try {
            val text = openFileInput(INTERNAL_FILE).bufferedReader().use { it.readText() }
            tvOutput.visibility = View.VISIBLE
            tvOutput.text = getString(R.string.lab10_internal_header, text)
        } catch (e: Exception) {
            tvOutput.text = getString(R.string.lab10_no_notes)
        }
    }

    private fun checkAndSaveExternal() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            saveExternal()
            return
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            saveExternal()
        } else {
            requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    private fun getExportFile(): File? {
        val dir = getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS) ?: return null
        if (!dir.exists()) dir.mkdirs()
        return File(dir, EXTERNAL_FILE)
    }

    private fun saveExternal() {
        val text = buildNoteText() ?: return
        try {
            val file = getExportFile() ?: run {
                Toast.makeText(this, R.string.lab10_external_unavailable, Toast.LENGTH_SHORT).show()
                return
            }
            file.appendText(text)
            Toast.makeText(this, R.string.lab10_saved_external, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            Toast.makeText(this, getString(R.string.lab10_error, e.message), Toast.LENGTH_SHORT).show()
        }
    }

    private fun readExternal() {
        try {
            val file = getExportFile()
            if (file == null || !file.exists()) {
                tvOutput.text = getString(R.string.lab10_no_export)
                return
            }
            tvOutput.text = getString(R.string.lab10_external_header, file.readText())
        } catch (e: Exception) {
            tvOutput.text = getString(R.string.lab10_read_error)
        }
    }
}