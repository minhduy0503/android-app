package com.dev.fitface.ui.activity;


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


import com.dev.fitface.R;

public class CameraActivity extends AppCompatActivity {

    /*private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
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
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
  /*      setupUI();
        initServices();
        prevTime = System.currentTimeMillis();
        fotoapparat = startFotoapparat();
        checkPermission();
*/
    }
/*
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
 *//*                           // Kiểm tra có tồn tại duy nhất 1 khuôn mặt không
                            if (faces.size() > 1){
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Nhận diện lỗi");
                                });
                                tvAction.setText("Nhận diện lỗi");
                                return;
                            } else if(faces.size() < 1) {
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Nhận dạng thành công");
                                });
*//**//*                                tvAction.setText("Không tồn tại người nào ");
                                isFaceProcessRunning = true;*//**//*
                            }
                            FirebaseVisionFace face = faces.get(0);
                            Log.i(TAG, face.getBoundingBox().toString());
                            // Check front face
                            if (face.getHeadEulerAngleY() < -12 || face.getHeadEulerAngleY() > 12) {
                                isFaceProcessRunning = true;
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Vui lòng nhìn thẳng");
                                });
                            } else {
                                leftF = face.getBoundingBox().left;
                                topF = face.getBoundingBox().top;
                                widthF = face.getBoundingBox().width();
                                heightF = face.getBoundingBox().height();
//                                startCountdown(3);
                            }*//*


                            if (faces.size() > 1) {
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Có nhiều khuôn mặt");
                                });
                                isFaceProcessRunning = true;
                                return;
                            } else if (faces.size() < 1) {
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Không tìm thấy khuôn mặt");
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
                                        tvAction.setText("Vui lòng nhìn thẳng");
                                    });
                                    isFaceProcessRunning = true;
                                } else {
                                    this.runOnUiThread(() -> {
                                        tvAction.setText("Nhận diện thành công");
                                    });
                                    leftF = face.getBoundingBox().left;
                                    topF = face.getBoundingBox().top;
                                    widthF = face.getBoundingBox().width();
                                    heightF = face.getBoundingBox().height();
                                    isFaceProcessRunning = false;
                                    startCountdown(2);
                                }
                            }


                        *//*   // Check face sizeint
                          int left = roundedFrameLayout.getLeft();
                            int top = roundedFrameLayout.getTop();
                            int right = roundedFrameLayout.getRight();
                            int bottom = roundedFrameLayout.getBottom();

                            if (((leftF - left) > 0) && ((topF - top) > 0) && ((right - rightF) > 0) && (bottom - bottomF) > 0){
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Phát hiện khuôn mặt");
                                });
                            } else {
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Không tìm thấy khuôn mặt");
                                });
                            }
                            return;*//*
                        }
                )
                .addOnFailureListener(e -> {
                });
    }


    private void startCountdown(int second) {
        *//*new CountDownTimer(second * 1000, 1000) {
            int time = second;
            public void onTick(long millisUntilFinished) {
                if (!isFaceProcessRunning) {
                    tvAction.setText("Bắt đầu chụp trong " + time + " giây nữa!");
                    time--;
                    if(time == 0){
                        takePhoto();
                    }
                }
            }

            public void onFinish() {
            }
        }.start();*//*
        new CountDownTimer(second * 1000, 1000){
            int time = second;

            @Override
            public void onTick(long millisUntilFinished) {
                tvAction.setText("Bắt đầu chụp trong " + time + " giây nữa!");
                time--;
            }

            @Override
            public void onFinish() {
                tvAction.setText("Bắt đầu chụp trong " + time + " giây nữa!");
                takePhoto();
            }
        }.start();
    }

    private void takePhoto() {
        final PhotoResult picture = fotoapparat.takePicture();
        fotoapparat.stop();
        fotoapparat.start();
        *//*picture.toBitmap().whenDone(bitmapPhoto -> {
            fotoapparat.stop();
            fotoapparat.start();
        });*//*
*//*        fotoapparat.stop();
        fotoapparat.start();*//*
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
    }*/
}