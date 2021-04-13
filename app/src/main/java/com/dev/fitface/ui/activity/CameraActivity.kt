package com.dev.fitface.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.media.Image
import android.os.Bundle;
import android.view.OrientationEventListener
import android.widget.Toast;
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy

import com.dev.fitface.R;
import com.dev.fitface.api.ApiService
import com.dev.fitface.camerax.CameraManager
import com.dev.fitface.interfaces.FaceDetectorDelegate
import com.dev.fitface.interfaces.UIDelegate
import com.dev.fitface.mlkit.*
import com.dev.fitface.models.FaceRequest
import com.dev.fitface.models.Student
import com.dev.fitface.utils.DETECT_STATUS
import com.dev.fitface.utils.getBaseYByView
import com.dev.fitface.utils.imageToBitmap
import com.dev.fitface.utils.rotateFlipImage
import kotlinx.android.synthetic.main.activity_camera.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CameraActivity : AppCompatActivity(), FaceDetectorDelegate {

    private lateinit var cameraManager: CameraManager
    private lateinit var service: ApiService
    private var UIDelegate: UIDelegate? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        createCameraManager()
        getCoordinateOfView()
        initService()
        askPermission()



    }

    private fun askPermission(){
        if (allPermissionsGranted()) {
            cameraManager.startCamera()
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
            )
        }
    }

    private fun getCoordinateOfView(){
        val l = main_frame.mRect.left
        val t = main_frame.mRect.top
        val r = main_frame.mRect.right
        val b = main_frame.mRect.bottom

        UIDelegate?.onViewListener(l, t, r, b)
    }

    private fun initService() {
        service = ApiService.create()
    }



    private fun createCameraManager() {
        cameraManager = CameraManager(
                this,
                cameraView,
                this,
                graphicOverlay
        )
    }

    private fun takePicture() {
        // shutter effect
        Toast.makeText(this@CameraActivity, "take a picture!", Toast.LENGTH_SHORT).show()
        setOrientationEvent()

        cameraManager.imageCapture.takePicture(
                cameraManager.cameraExecutor,
                object : ImageCapture.OnImageCapturedCallback() {
                    @SuppressLint("UnsafeExperimentalUsageError", "RestrictedApi")
                    override fun onCaptureSuccess(image: ImageProxy) {
                        image.image?.let {
                            convertImageTpBitmap(it)
                        }
                        super.onCaptureSuccess(image)
                    }
                })
    }

    private fun convertImageTpBitmap(image: Image) {
        image.imageToBitmap()
                ?.rotateFlipImage(
                        cameraManager.rotation,
                        cameraManager.isFrontMode()
                )
                ?.let { bitmap ->
                    graphicOverlay.processCanvas.drawBitmap(
                            bitmap,
                            0f,
                            bitmap.getBaseYByView(
                                    cameraView,
                                    cameraManager.isHorizontalMode()
                            ),
                            Paint().apply {
                                xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
                            })

//                     Createe bitmap and encode to Base64 string
                 /*   var res = Bitmap.createBitmap(bitmap, left , top, width , height)
                    var byteArrayOutputStream = ByteArrayOutputStream();
                    res.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    var byteArray = byteArrayOutputStream .toByteArray()
                    base64Str = Base64.encodeToString(byteArray, Base64.DEFAULT)
                    var listFace = FaceRequest()
                    var list = mutableListOf<String>(base64Str)
                    listFace.images = list
                    // Call API
                    checkin(listFace)*/
                }
    }


    private fun checkin(base64Str: FaceRequest) {
        // Test
        val token = "ed898d0e7296158731c1e582f3055625"
        val id = "17"

        service.postCheckin(id,token, base64Str).enqueue(object : Callback<Student?>{
            override fun onResponse(call: Call<Student?>, response: Response<Student?>) {
                if (response.isSuccessful){
                    Toast.makeText(this@CameraActivity, "Check in successfully", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<Student?>, t: Throwable) {
                Toast.makeText(this@CameraActivity, "Check in failure", Toast.LENGTH_LONG).show()
            }

        })
    }


    private fun setOrientationEvent() {
        val orientationEventListener = object : OrientationEventListener(this as Context) {
            override fun onOrientationChanged(orientation: Int) {
                val rotation: Float = when (orientation) {
                    in 45..134 -> 270f
                    in 135..224 -> 180f
                    in 225..314 -> 90f
                    else -> 0f
                }
                cameraManager.rotation = rotation
            }
        }
        orientationEventListener.enable()
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults:
            IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraManager.startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                        .show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(
                Manifest.permission.CAMERA
        )
    }

    override fun onCapture(isProcessing: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        TODO("Not yet implemented")
    }

    override fun onProcess(status: DETECT_STATUS) {
        TODO("Not yet implemented")
    }
}