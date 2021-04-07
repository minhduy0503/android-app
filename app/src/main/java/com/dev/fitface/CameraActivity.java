package com.dev.fitface;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.mlkit.vision.face.FaceDetection;

import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;


import java.util.List;

import io.fotoapparat.Fotoapparat;
import io.fotoapparat.error.CameraErrorListener;
import io.fotoapparat.exception.camera.CameraException;
import io.fotoapparat.log.Logger;
import io.fotoapparat.parameter.ScaleType;
import io.fotoapparat.preview.Frame;
import io.fotoapparat.view.CameraView;

import static io.fotoapparat.log.LoggersKt.fileLogger;
import static io.fotoapparat.log.LoggersKt.logcat;
import static io.fotoapparat.log.LoggersKt.loggers;
import static io.fotoapparat.selector.AspectRatioSelectorsKt.standardRatio;
import static io.fotoapparat.selector.LensPositionSelectorsKt.front;
import static io.fotoapparat.selector.ResolutionSelectorsKt.highestResolution;

public class CameraActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private static final String TAG = "CAMERA ACTIVITY";

    TextView tvAction;
    CameraView cameraView;
    Fotoapparat fotoapparat;
    Boolean isRotatedRight, isRotatedLeft, isFaceProcessRunning;
    FirebaseVisionFaceDetector detector;
    long currentTime, prevTime;
    float minSmileProb, maxSmileProb;

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
                    if (currentTime - 500 > prevTime){
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
                            if (faces.size() > 1){
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Có nhiều hơn 1 khuôn mặt");
                                });
                                return;
                            } else if (faces.size() < 1){
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Không tìm thấy khuôn mặt");
                                });
                                return;
                            }
                            FirebaseVisionFace face = faces.get(0);
                            Log.i(TAG ,face.getBoundingBox().toString());
                            // Check front face
                            if (face.getHeadEulerAngleY() > -12 && face.getHeadEulerAngleY() < 12){
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Vui lòng nhìn thẳng");
                                });
                            } else {
                                this.runOnUiThread(() -> {
                                    tvAction.setText("Đã thẳng");
                                });
                            }
                            // Check face size
                            int left = cameraView.getLeft();
                            int top = cameraView.getTop();
                            int right = cameraView.getRight();
                            int bottom = cameraView.getBottom();
                            Log.i(TAG,String.format()
                        }
                )
                .addOnFailureListener( e -> {

                });
    }

    private FirebaseVisionImage getVisionImageFromFrame(Frame frame){
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

    private void setupUI(){
        cameraView = findViewById(R.id.camera_front);
        tvAction = findViewById(R.id.tvAction);
    }
}