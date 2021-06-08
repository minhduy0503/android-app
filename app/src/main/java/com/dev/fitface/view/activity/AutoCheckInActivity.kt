package com.dev.fitface.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.*
import android.media.Image
import android.os.Bundle
import android.util.Base64
import android.view.OrientationEventListener
import android.view.View
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dev.fitface.R
import com.dev.fitface.api.models.face.FaceRequest
import com.dev.fitface.camerax.CameraManager
import com.dev.fitface.interfaces.CameraCallback
import com.dev.fitface.utils.*
import com.dev.fitface.view.BaseActivity
import com.dev.fitface.view.fragments.CheckInReportFragment
import com.dev.fitface.view.fragments.CheckInResultFragment
import com.dev.fitface.viewmodel.AutoCheckInActivityViewModel
import kotlinx.android.synthetic.main.activity_auto_check_in.*
import java.io.ByteArrayOutputStream


class AutoCheckInActivity : BaseActivity<AutoCheckInActivityViewModel>(),
    CheckInResultFragment.OnResultCheckInFragmentInteractionListener,
    CheckInReportFragment.OnCheckInReportFragmentInteractionListener,View.OnClickListener {

    private lateinit var cameraManager: CameraManager
    private var mCallback: CameraCallback? = null
    private var token: String? = null

    override fun setLoadingView(): View? {
        return null
    }

    override fun setActivityView() {
        setContentView(R.layout.activity_auto_check_in)
    }

    override fun setActivityName(): String {
        return Constants.ActivityName.autoCheckInActivity
    }

    override fun createViewModel(): AutoCheckInActivityViewModel {
        return ViewModelProvider(this).get(AutoCheckInActivityViewModel::class.java)
    }

    override fun fetchData() {
        //Fetch data
    }

    override fun handleError(statusCode: Int?, message: String?, bundle: Bundle?) {

    }

    override fun observeData() {
        observeFaceResponseCheckIn()
    }

    private fun observeFaceResponseCheckIn() {
       /* viewModel.faceResponse.observe(this, Observer {
            it?.resource?.data?.let {

            }
        })*/
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initValue()
        initView()
        initListener()
        initCameraManager()
        askPermission()
    }

    private fun initCameraManager() {
        cameraManager = CameraManager(
                this,
                cameraView,
                this,
                graphicOverlay,
                mCallback,
                Constants.CameraMode.automatic
        )
    }

    private fun initValue() {
        token = SharedPrefs.instance[Constants.Param.token, String::class.java]

        mCallback = object : CameraCallback {
            override fun onFaceCapture(rect: Rect) {
                tvAction.text = "Đang xử lí"
                captureFace(rect)
                viewModel.faceStr?.observe(this@AutoCheckInActivity, Observer {
                    // Send to fragment
                    it?.let {
                        val bundle = Bundle()
                        val fragment = CheckInReportFragment.newInstance(bundle)
                        fragment.isCancelable = false
                        fragment.show(supportFragmentManager, Constants.FragmentName.checkInReportFragment)
                    }
                })
            }

            override fun onFaceSizeNotify(size: FaceSize) {
                when (size) {
                    FaceSize.SMALL -> {
                        tvAction.text = " Vui lòng tiến gần thiết bị hơn"
                    }
                    FaceSize.BIG -> {
                        tvAction.text = "Vui lòng cách xa thiết bị hơn"
                    }
                }
            }

            override fun onFrontFaceNotify() {
                tvAction.text = "Vui lòng nhìn thẳng vào thiết bị"
            }

            override fun onFaceOutsideNotify() {
                tvAction.text = "Vui lòng đặt khuôn mặt vào vùng hiển thị"
            }

            override fun onFaceInvalidNumber() {
                tvAction.text = "Có nhiều hơn một người"
            }

            override fun onFaceNone() {
                tvAction.text = ""
            }

            override fun onEyeNotify(status: EyeStatus) {
                when (status) {
                    EyeStatus.LEFT_EYE_CLOSE -> {
                        tvAction.text = "Vui lòng mở mắt trái"
                    }
                    EyeStatus.RIGHT_EYE_CLOSE -> {
                        tvAction.text = "Vui lòng mở mắt phải"
                    }
                    EyeStatus.ALL_EYES_CLOSE -> {
                        tvAction.text = "Vui lòng mở hai mắt"
                    }
                }
            }

        }
    }

    private fun initView() {
        tvRoomID.text = intent.getStringExtra(Constants.Param.roomId)
    }

    private fun initListener() {
        btnBack.setOnClickListener(this)
        btnLock.setOnClickListener(this)
    }

    private fun captureFace(faceRect: Rect) {
        setOrientationEvent()
        cameraManager.imageCapture.takePicture(
                cameraManager.cameraExecutor,
                object : ImageCapture.OnImageCapturedCallback() {
                    @SuppressLint("UnsafeExperimentalUsageError")
                    override fun onCaptureSuccess(image: ImageProxy) {
                        image.image?.let {
                            convertImageTpBitmap(it, faceRect)
                        }
                        super.onCaptureSuccess(image)
                    }
                })
    }

    private fun convertImageTpBitmap(image: Image, faceRect: Rect) {
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

//                  Create bitmap and encode to Base64 string
                    val left = faceRect.left
                    val top = faceRect.top
                    val width = faceRect.width()
                    val height = faceRect.height()
                    var res = Bitmap.createBitmap(bitmap, left, top, width, height)
                    var byteArrayOutputStream = ByteArrayOutputStream();
                    res.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    var byteArray = byteArrayOutputStream.toByteArray()
                    val str64 = Base64.encodeToString(byteArray, Base64.DEFAULT)
                    viewModel.faceStr?.postValue(str64)
                }
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

    private fun askPermission() {
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

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
            requestCode: Int, permissions: Array<String>, grantResults:
            IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                cameraManager.startCamera()
            } else {
/*                ToastMessage.makeText(this, "Permissions not granted by the user.", CustomToast.ERROR, CustomToast.SHORT)
                        .show()*/
                finish()
            }
        }
    }

    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(
                Manifest.permission.CAMERA
        )
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btnBack -> {
                super.onBackPressed()
            }

            R.id.btnLock -> {

            }
        }
    }

    override fun onResultInteraction(bundle: Bundle?) {
/*        val req = bundle?.getString(Constants.FragmentName.autoCheckInResultFragment)
        when (req){
            Constants.Param.confirm -> {
                val data = bundle.getString(Constants.Obj.faceStr)!!
                val roomId:Int  = 1
                var param = listOf(data)
                val faceReq = FaceRequest()
                faceReq.collection = "CNTT"
                faceReq.images = param
                viewModel.postCheckIn(token!!,roomId,faceReq)
            }
        }*/
    }

    override fun onCheckInReportFragmentInteraction(bundle: Bundle) {

    }

    /* private lateinit var cameraManager: CameraManager
     private var mCallback: CameraCallback? = null
     private var base64List: ArrayList<String>? = null
     private var roomId: String? = null
     private var token: String? = null

     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         setContentView(R.layout.activity_camera)
         initValue()
         initService()
         createCameraManager()
         askPermission()
     }

     private fun initValue() {
         roomId = intent.getStringExtra("room_checkin")
         tvRoomID.text = roomId
 //        token = SharedPrefs.instance["Token", String::class.java]
         base64List = arrayListOf()
     }

     init {
         mCallback = object : CameraCallback {
             override fun onFaceCapture(rect: Rect) {
                 tvAction.text = "Đã điểm danh"*//*
                captureFace(rect)
                checkin(base64List!!, "CNTT3", roomId!!)
*//*
*//*                cameraManager.stopCamera()
                Handler(Looper.getMainLooper()).postDelayed({
                    //Do something after 5000ms
                    cameraManager.startCamera()
                }, 2000)*//*
                *//*  imgCheckIn.setImageBitmap(a.base64ToImage(a))
                  imgCheckIn.visibility = View.VISIBLE

                  Handler(Looper.getMainLooper()).postDelayed({
                      //Do something after 5000ms
                      imgCheckIn.visibility = View.INVISIBLE
                      cameraManager.startCamera()
                      a = ""
                  }, 5000)*//*
            }

            override fun onFaceSizeNotify(size: FaceSize) {
                when (size) {
                    FaceSize.SMALL -> {
                        tvAction.text = " Vui lòng tiến gần thiết bị hơn"
                    }
                    FaceSize.BIG -> {
                        tvAction.text = "Vui lòng cách xa thiết bị hơn"
                    }
                }
            }

            override fun onFrontFaceNotify() {
                tvAction.text = "Vui lòng nhìn thẳng vào thiết bị"
            }

            override fun onFaceOutsideNotify() {
                tvAction.text = "Vui lòng đặt khuôn mặt vào vùng hiển thị"
            }

            override fun onFaceInvalidNumber() {
                tvAction.text = "Có nhiều hơn một người"
            }

            override fun onFaceNone() {
                tvAction.text = ""
            }

            override fun onEyeNotify(status: EyeStatus) {
                when (status) {
                    EyeStatus.LEFT_EYE_CLOSE -> {
                        tvAction.text = "Vui lòng mở mắt trái"
                    }
                    EyeStatus.RIGHT_EYE_CLOSE -> {
                        tvAction.text = "Vui lòng mở mắt phải"
                    }
                    EyeStatus.ALL_EYES_CLOSE -> {
                        tvAction.text = "Vui lòng mở hai mắt"
                    }
                }
            }
        }
    }

    private fun askPermission() {
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

    private fun initService() {
//        service = ApiService.create()
    }LENGTH_SHORT
LENGTH_SHORT
    private fun createCameraManager() {
        cameraManager = CameraManager(
                this,
                cameraView,
                this,
                graphicOverlay,
                mCallback,
                Constants.CameraMode.manual
        )
    }

    private fun captureFace(faceRect: Rect) {
        setOrientationEvent()
        cameraManager.imageCapture.takePicture(
                cameraManager.cameraExecutor,
                object : ImageCapture.OnImageCapturedCallback() {
                    @SuppressLint("UnsafeExperimentalUsageError")
                    override fun onCaptureSuccess(image: ImageProxy) {
                        image.image?.let {
                            convertImageTpBitmap(it, faceRect)
                        }
                        super.onCaptureSuccess(image)
                    }
                })
    }

    private fun convertImageTpBitmap(image: Image, faceRect: Rect) {
        lateinit var resultStr: String
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

//                  Create bitmap and encode to Base64 string
                    val left = faceRect.left
                    val top = faceRect.top
                    val right = faceRect.right
                    val bottom = faceRect.bottom
                    val width = right - left
                    val height = bottom - top
                    var res = Bitmap.createBitmap(bitmap, left, top, width, height)
                    var byteArrayOutputStream = ByteArrayOutputStream();
                    res.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                    var byteArray = byteArrayOutputStream.toByteArray()
                    resultStr = Base64.encodeToString(byteArray, Base64.DEFAULT)
                    base64List?.clear()
                    base64List?.add(resultStr)

*//*
                    runOnUiThread {
                        base64List?.add(resultStr)
                        Log.i("Debug","${base64List?.size}")
                        checkin(base64List!!, "CNTT3", roomId!!)
                    }*//*
                }

    }

  *//*  private fun checkin(base64Str: ArrayList<String>, collection: String, roomId: String) {
        // Test
        Log.i("Debug", "${base64Str.size}  -call")
        val faceReq = FaceRequest(base64Str, collection)
        service.postCheckin(roomId, token!!, faceReq).enqueue(object : Callback<FaceResponse?> {
            override fun onResponse(call: Call<FaceResponse?>, response: Response<FaceResponse?>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        if (body.status == 200 && body.data.isNotEmpty()) {
                            body.data.forEach { res ->
                                val mssv = res.username
                                val firstname = res.firstname
                                val lastname = res.lastname

                                val welcomStr = "Xin chào $mssv - $firstname $lastname"
                                tvAction.text = welcomStr
                            }
                        }
                    } else {
                        tvAction.text = "Điểm danh không hợp lệ"
                    }
                }
                else {
                    tvAction.text = "Điểm danh lỗi"
                }
            }

            override fun onFailure(call: Call<FaceResponse?>, t: Throwable) {
                CustomToast.makeText(this@CameraActivity, "Error", 400, CustomToast.ERROR).show()
            }

        })
    }
*//*

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
    }*/

}