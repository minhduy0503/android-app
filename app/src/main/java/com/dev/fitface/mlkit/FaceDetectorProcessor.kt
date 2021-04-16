package com.dev.fitface.mlkit

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.media.Image
import android.os.Handler
import android.util.Base64
import android.util.Log
import android.view.OrientationEventListener
import android.widget.Button
import android.widget.TextView
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import com.dev.fitface.R
import com.dev.fitface.api.ApiService
import com.dev.fitface.camerax.BaseImageAnalyzer
import com.dev.fitface.camerax.CameraManager
import com.dev.fitface.models.requests.FaceRequest
import com.dev.fitface.models.response.FaceResponse
import com.dev.fitface.ui.CustomToast
import com.dev.fitface.ui.activity.CameraActivity
import com.dev.fitface.utils.*
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.activity_camera.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


@Suppress("DEPRECATION")
class FaceDetectorProcessor(private val cameraManager: CameraManager, val context: Context, private val cameraActivity: CameraActivity, private val apiService: ApiService) :
        BaseImageAnalyzer<List<Face>>() {

    private var leftF: Float = 0F
    private var topF: Float = 0F
    private var rightF: Float = 0F
    private var bottomF: Float = 0F
    private var isProcessing: Boolean = false

    private var leftV = 140
    private var topV = 167
    private var rightV = 660
    private var bottomV = 687


    private val realtimeOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()

    private val detector = FaceDetection.getClient(realtimeOpts)

    override fun stop() {
        try {
            detector.close()
        } catch (e: Exception) {
            Log.e(TAG, "Exception thrown while trying to close Face Detector: $e")
        }
    }

    override fun detectInImage(image: InputImage): Task<List<Face>> {
        return detector.process(image)
    }

    override fun onSuccess(image: Image?, results: List<Face>, rect: Rect) {
//        graphicOverlay.clear()
        results.forEach { face ->

            /**
             * Xử lí các trường hợp của face khi detect *
             * */
            // Convert imageProxy into Bitmap:
            val bitmap = image?.imageToBitmap()
            if (bitmap != null) {
//                Log.i("Debug", "YES")
            } else {
//                Log.i("Debug", "NO")
            }

            // Brightness process:
            val brightnessLevel = bitmap?.calculateBrightnessEstimate(bitmap)
            if (brightnessLevel != null) {
                if (brightnessLevel < lowBrightnessThreshold) {
                    isProcessing = true
                    cameraActivity.runOnUiThread {
                        val v = cameraActivity.findViewById<TextView>(R.id.tvAction)
                        v.text = "Low brightness"
                    }
                    return
                }
            }

            if (brightnessLevel != null) {
                if (brightnessLevel > highBrightnessThreshold) {
                    isProcessing = true
                    cameraActivity.runOnUiThread {
                        val v = cameraActivity.findViewById<TextView>(R.id.tvAction)
                        v.text = "High brightness"
                    }
                    return
                }
            }

            // Check: Is front face:
            if (face.headEulerAngleY < -12 || face.headEulerAngleY > 12) {
                isProcessing = true
                cameraActivity.runOnUiThread {
                    val v = cameraActivity.findViewById<TextView>(R.id.tvAction)
                    v.text = "Not front face"
                }
                return
            }

            // Check: Face is located in the frame
            leftF = (face.boundingBox.left) * 1F
            topF = (face.boundingBox.top) * 1F
            topF = (face.boundingBox.top) * 1F
            topF = (face.boundingBox.top) * 1F
            rightF = (face.boundingBox.right) * 1F
            bottomF = (face.boundingBox.bottom) * 1F

            cameraActivity.runOnUiThread {
/*                val v = cameraActivity.findViewById<TextView>(R.id.tvAction)
                v.text = "Your face is outside"*/
                val btn = cameraActivity.findViewById<Button>(R.id.btnCheckin)
                btn.setOnClickListener{
                    takePicture()
                }
            }

           /* if (isInside(leftF, topF, rightF, bottomF, leftV.toFloat(), topV.toFloat(), rightV.toFloat(), bottomV.toFloat())) {
                isProcessing = true
                cameraActivity.runOnUiThread {
                    val v = cameraActivity.findViewById<TextView>(R.id.tvAction)
                    v.text = "Your face is outside"
                    val btn = cameraActivity.findViewById<Button>(R.id.btnCheckin)
                    btn.setOnClickListener {
                        takePicture()
                    }
                }
                return
            } else {
                isProcessing = false
                val faceWidth = face.boundingBox.width()
                val faceHeight = face.boundingBox.height()
//                faceDetectorDelegate?.onCapture(isProcessing, leftF.toInt(), topF.toInt(), faceWidth, faceHeight)
                Log.i("Debug", "Captured")
                return
            }*/
        }
//        graphicOverlay.postInvalidate()

    }

    private fun isInside(leftF: Float, topF: Float, rightF: Float, bottomF: Float,
                         leftV: Float, topV: Float, rightV: Float, bottomV: Float): Boolean {
        if (topF < topV) return false
        if (bottomF > bottomV) return false
        if (leftF < leftV) return false
        return rightF <= rightV
    }


    companion object {
        private const val lowBrightnessThreshold = 80
        private const val highBrightnessThreshold = 220

        private const val LEFT_EYE_OPEN_PROBABILITY = 0.4F
        private const val RIGHT_EYE_OPEN_PROBABILITY = 0.4F

        private const val TAG = "FaceDetectorProcessor"
    }

    override fun onFailure(e: Exception) {

    }

    private fun takePicture() {
        // shutter effect
        CustomToast.makeText(context, "Captured", CustomToast.SUCCESS, CustomToast.SHORT).show()
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
                    /*  graphicOverlay.processCanvas.drawBitmap(
                              bitmap,
                              0f,
                              bitmap.getBaseYByView(
                                      cameraManager.getCameraView(),
                                      cameraManager.isHorizontalMode()
                              ),
                              Paint().apply {
                                  xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
                              })
  */
//                     Createe bitmap and encode to Base64 string
                    var res = Bitmap.createBitmap(bitmap, leftF.toInt(), topF.toInt(), (rightF - leftF).toInt(), (bottomF - topF).toInt())
                    var byteArrayOutputStream = ByteArrayOutputStream();
                    res.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    var byteArray = byteArrayOutputStream.toByteArray()
                    var base64Str = Base64.encodeToString(byteArray, Base64.DEFAULT)
                    var listFace = FaceRequest()
                    var list = mutableListOf<String>(base64Str)
                    listFace.images = list
                    // Call API
                    checkin(listFace)
                    cameraActivity.runOnUiThread {
                        val v = cameraActivity.findViewById<TextView>(R.id.tvAction)
                        v.text = ""
                    }
                }
    }


    private fun checkin(base64Str: FaceRequest) {
        // Test
        val token = SharedPrefs.instance["token", String::class.java]
        val id = "109"

        apiService.postCheckin(id, token, base64Str).enqueue(object : Callback<FaceResponse?> {
            override fun onResponse(call: Call<FaceResponse?>, response: Response<FaceResponse?>) {
                if (response.isSuccessful) {
                    if (response.body()?.status == 200 && !response.body()?.data.isNullOrEmpty()) {
                        val mssv = response.body()?.data?.get(0)?.username
                        val firstname = response.body()?.data?.get(0)?.firstname
                        val lastname = response.body()?.data?.get(0)?.lastname

                        val fullname = "Xin chào $mssv - $firstname $lastname"
                        cameraActivity.runOnUiThread {
                            val v = cameraActivity.findViewById<TextView>(R.id.tvAction)
                            v.text = fullname
                        }
                    }
                    else if (response.body()?.data.isNullOrEmpty()){
                        cameraActivity.runOnUiThread {
                            val v = cameraActivity.findViewById<TextView>(R.id.tvAction)
                            v.text = "Điểm danh không hợp lệ"
                        }
                    }
                }
                else {
                    cameraActivity.runOnUiThread {
                        val v = cameraActivity.findViewById<TextView>(R.id.tvAction)
                        v.text = "Điểm danh không hợp lệ"
                    }
                }
            }

            override fun onFailure(call: Call<FaceResponse?>, t: Throwable) {
            }

        })
    }


    private fun setOrientationEvent() {
        val orientationEventListener = object : OrientationEventListener(context) {
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
}