package com.example.aplicacion

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LifecycleOwner
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class camara : AppCompatActivity() {

        private lateinit var viewFinder: PreviewView
        private lateinit var captureButton: Button
        private lateinit var regresarButton: Button

        private var camera: Camera? = null
        private var imageCapture: ImageCapture? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_camara)

            // Inicializar vistas
            viewFinder = findViewById(R.id.viewFinder)
            captureButton = findViewById(R.id.camera_capture_button)
            regresarButton = findViewById(R.id.button_regresar)

            // Solicitar permisos de cámara
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                requestPermissions.launch(Manifest.permission.CAMERA)
            }

            // Acción del botón "Foto"
            captureButton.setOnClickListener { takePhoto() }

            // Acción del botón "Regresar"
            regresarButton.setOnClickListener { finish() }
        }

        // Configurar la cámara
        private fun startCamera() {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = androidx.camera.core.Preview.Builder()
                    .build()
                    .also { it.setSurfaceProvider(viewFinder.surfaceProvider) }

                imageCapture = ImageCapture.Builder().build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()

                    camera = cameraProvider.bindToLifecycle(
                        this as LifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )

                } catch (exc: Exception) {
                    Toast.makeText(this, "Error al iniciar la cámara: ${exc.message}", Toast.LENGTH_SHORT).show()
                }

            }, ContextCompat.getMainExecutor(this))
        }

        // Tomar una foto
        private fun takePhoto() {
            val imageCapture = imageCapture ?: return

            // Directorio para guardar fotos
            val photoFile = File(
                externalMediaDirs.firstOrNull(),
                SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                    .format(System.currentTimeMillis()) + ".jpg"
            )

            val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

            imageCapture.takePicture(
                outputOptions,
                ContextCompat.getMainExecutor(this),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        val msg = "Foto guardada: ${photoFile.absolutePath}"
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    }

                    override fun onError(exception: ImageCaptureException) {
                        val msg = "Error al guardar la foto: ${exception.message}"
                        Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }

        // Verificar permisos
        private fun allPermissionsGranted() =
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED

        // Manejo de permisos
        private val requestPermissions = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                startCamera()
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }
