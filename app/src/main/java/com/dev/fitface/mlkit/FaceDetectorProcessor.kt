package com.dev.fitface.mlkit

import android.graphics.Rect
import android.util.Log
import com.dev.fitface.camerax.BaseImageAnalyzer
import com.dev.fitface.camerax.GraphicOverlay
import com.dev.fitface.interfaces.FaceResultCallback
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions

class FaceDetectorProcessor(private val view: GraphicOverlay, private val callback: FaceResultCallback?) :
        BaseImageAnalyzer<List<Face>>() {

    private val realtimeOpts = FaceDetectorOptions.Builder()
            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
            .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
            .build()

    private val detector = FaceDetection.getClient(realtimeOpts)

    override val graphicOverlay: GraphicOverlay
        get() = view

    override fun detectInImage(image: InputImage): Task<List<Face>> {
        return detector.process(image)
    }

    override fun stop() {
        try {
            detector.close()
        } catch (e: Exception) {
            Log.e(TAG, "Exception thrown while trying to close Face Detector: $e")
        }
    }



    override fun onSuccess(results: List<Face>, graphicOverlay: GraphicOverlay, rect: Rect) {
        graphicOverlay.clear()
        results.forEach { face ->
            val faceGraphic = FaceContourGraphic(graphicOverlay, face , rect)
            graphicOverlay.add(faceGraphic)
            callback?.onFaceLocated(face.boundingBox)
        }
        graphicOverlay.postInvalidate()
    }

    override fun onFailure(e: Exception) {

    }


    companion object {
        private const val TAG = "FaceDetectorProcessor"
    }




}

/*  val realLeft = 800 - rightV
            val realRight = 800 - leftF
            val realTop = topF
            val realBottom = bottomV

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
            width = (face.boundingBox.right) * 1F
            bottomF = (face.boundingBox.bottom) * 1F
*/

/* private fun takePicture() {
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
            graphicOverlay.processCanvas.drawBitmap(
                           bitmap,
                           0f,
                           bitmap.getBaseYByView(
                                   cameraManager.getCameraView(),
                                   cameraManager.isHorizontalMode()
                           ),
                           Paint().apply {
                               xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OVER)
                           })

               Createe bitmap and encode to Base64 string
                 var res = Bitmap.createBitmap(bitmap, leftF, topF, width, height)
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
//        val token = SharedPrefs.instance["token", String::class.java]
     val token = "1"
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

*/
