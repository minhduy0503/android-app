package com.dev.fitface;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;

import android.os.Bundle;

import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;

import com.google.mlkit.vision.face.FaceDetectorOptions;


import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;

import java.util.concurrent.ExecutionException;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.error.CameraErrorListener;
import io.fotoapparat.exception.camera.CameraException;
import io.fotoapparat.log.Logger;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.preview.Frame;
import io.fotoapparat.result.BitmapPhoto;
import io.fotoapparat.result.Photo;
import io.fotoapparat.result.PhotoResult;
import io.fotoapparat.result.WhenDoneListener;
import io.fotoapparat.view.CameraView;
import kotlin.Unit;

import static io.fotoapparat.log.LoggersKt.fileLogger;
import static io.fotoapparat.log.LoggersKt.logcat;
import static io.fotoapparat.log.LoggersKt.loggers;
import static io.fotoapparat.selector.AspectRatioSelectorsKt.standardRatio;
import static io.fotoapparat.selector.LensPositionSelectorsKt.front;
import static io.fotoapparat.selector.ResolutionSelectorsKt.highestResolution;

public class CameraActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final String TAG = "CAMERA ACTIVITY";

    RoundedFrameLayout roundedFrameLayout;
    TextView tvAction;
    CameraView cameraView;
    Fotoapparat fotoapparat;
    Boolean isRotatedRight, isRotatedLeft, isFaceProcessRunning;
    FirebaseVisionFaceDetector detector;
    long currentTime, prevTime;
    float minSmileProb, maxSmileProb;
    int leftF, topF, widthF, heightF;
    Bitmap bitmapF;
    String base64Str;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setupUI();
        initServices();
        prevTime = System.currentTimeMillis();
        fotoapparat = startFotoapparat();
        checkPermission();

    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            fotoapparat.start();
        }
    }

    private Fotoapparat startFotoapparat() {
        return Fotoapparat
                .with(this)
                .into(cameraView)
                .frameProcessor(frame -> {
                    currentTime = System.currentTimeMillis();
                    if (currentTime - 500 > prevTime) {
                        faceOptions(frame);
                        prevTime = currentTime;
                    }
                })
                .previewScaleType(ScaleType.CenterCrop)
                .lensPosition(front())
                .logger(loggers(
                        logcat(),
                        fileLogger(this)
                ))
                .cameraErrorCallback(e -> {
                    Toast.makeText(CameraActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                })
                .photoResolution(standardRatio(
                        highestResolution()
                ))
                .build();

    }

    @Override
    protected void onPause() {
        super.onPause();
        fotoapparat.stop();
    }

    private void faceOptions(Frame frame) {
        detector.detectInImage(getVisionImageFromFrame(frame))
                .addOnSuccessListener(
                        faces -> {
 /*                           // Ki???m tra c?? t???n t???i duy nh???t 1 khu??n m???t kh??ng
                            if (faces.size() > 1){
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Nh???n di???n l???i");
                                });
                                tvAction.setText("Nh???n di???n l???i");
                                return;
                            } else if(faces.size() < 1) {
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Nh???n d???ng th??nh c??ng");
                                });
*//*                                tvAction.setText("Kh??ng t???n t???i ng?????i n??o ");
                                isFaceProcessRunning = true;*//*
                            }
                            FirebaseVisionFace face = faces.get(0);
                            Log.i(TAG, face.getBoundingBox().toString());
                            // Check front face
                            if (face.getHeadEulerAngleY() < -12 || face.getHeadEulerAngleY() > 12) {
                                isFaceProcessRunning = true;
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Vui l??ng nh??n th???ng");
                                });
                            } else {
                                leftF = face.getBoundingBox().left;
                                topF = face.getBoundingBox().top;
                                widthF = face.getBoundingBox().width();
                                heightF = face.getBoundingBox().height();
//                                startCountdown(3);
                            }*/


                            if (faces.size() > 1) {
                                this.runOnUiThread(() -> {
                                    tvAction.setText("C?? nhi???u khu??n m???t");
                                });
                                isFaceProcessRunning = true;
                                return;
                            } else if (faces.size() < 1) {
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Kh??ng t??m th???y khu??n m???t");
                                });
                                isFaceProcessRunning = true;
                                return;
                            }
                            else if(faces.size() == 1){
                                FirebaseVisionFace face = faces.get(0);
                                Log.i(TAG, face.getBoundingBox().toString());
                                // Check front face
                                if (face.getHeadEulerAngleY() < -12 || face.getHeadEulerAngleY() > 12) {
                                    this.runOnUiThread(() -> {
                                        tvAction.setText("Vui l??ng nh??n th???ng");
                                    });
                                    isFaceProcessRunning = true;
                                } else {
                                    this.runOnUiThread(() -> {
                                        tvAction.setText("Nh???n di???n th??nh c??ng");
                                    });
                                    leftF = face.getBoundingBox().left;
                                    topF = face.getBoundingBox().top;
                                    widthF = face.getBoundingBox().width();
                                    heightF = face.getBoundingBox().height();
                                    isFaceProcessRunning = false;
                                    startCountdown(2);
                                }
                            }


                        /*   // Check face sizeint
                          int left = roundedFrameLayout.getLeft();
                            int top = roundedFrameLayout.getTop();
                            int right = roundedFrameLayout.getRight();
                            int bottom = roundedFrameLayout.getBottom();

                            if (((leftF - left) > 0) && ((topF - top) > 0) && ((right - rightF) > 0) && (bottom - bottomF) > 0){
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Ph??t hi???n khu??n m???t");
                                });
                            } else {
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Kh??ng t??m th???y khu??n m???t");
                                });
                            }
                            return;*/
                        }
                )
                .addOnFailureListener(e -> {
                });
    }


    private void startCountdown(int second) {
        /*new CountDownTimer(second * 1000, 1000) {
            int time = second;
            public void onTick(long millisUntilFinished) {
                if (!isFaceProcessRunning) {
                    tvAction.setText("B???t ?????u ch???p trong " + time + " gi??y n???a!");
                    time--;
                    if(time == 0){
                        takePhoto();
                    }
                }
            }

            public void onFinish() {
            }
        }.start();*/
        new CountDownTimer(second * 1000, 1000){
            int time = second;

            @Override
            public void onTick(long millisUntilFinished) {
                tvAction.setText("B???t ?????u ch???p trong " + time + " gi??y n???a!");
                time--;
            }

            @Override
            public void onFinish() {
                tvAction.setText("B???t ?????u ch???p trong " + time + " gi??y n???a!");
                takePhoto();
            }
        }.start();
    }

    private void takePhoto() {
        final PhotoResult picture = fotoapparat.takePicture();
        fotoapparat.stop();
        fotoapparat.start();
        /*picture.toBitmap().whenDone(bitmapPhoto -> {
            fotoapparat.stop();
            fotoapparat.start();
        });*/
/*        fotoapparat.stop();
        fotoapparat.start();*/
    }

    private String bitmapToBase64(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    private FirebaseVisionImage getVisionImageFromFrame(Frame frame) {
        byte[] bytes = frame.getImage();
        FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setRotation(FirebaseVisionImageMetadata.ROTATION_270)
                .setHeight(frame.getSize().height)
                .setWidth(frame.getSize().width)
                .build();

        FirebaseVisionImage image = FirebaseVisionImage.fromByteArray(bytes, metadata);
        return image;
    }

    private void initServices() {
        FirebaseVisionFaceDetectorOptions realtimeOpts =
                new FirebaseVisionFaceDetectorOptions.Builder()
                        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
                        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
                        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                        .build();

        detector = FirebaseVision.getInstance().getVisionFaceDetector(realtimeOpts);
        minSmileProb = 1;
        maxSmileProb = 0;
    }

    private void setupUI() {
        cameraView = findViewById(R.id.camera_front);
        tvAction = findViewById(R.id.tvAction);
        roundedFrameLayout = findViewById(R.id.main_frame);
    }
}