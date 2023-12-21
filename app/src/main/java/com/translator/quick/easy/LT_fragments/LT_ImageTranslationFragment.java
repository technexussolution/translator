package com.translator.quick.easy.LT_fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.translator.quick.easy.R;
import com.translator.quick.easy.LT_utils.LT_FragmentChangeListener;

import java.io.IOException;


public class LT_ImageTranslationFragment extends Fragment {
    private CameraSource cameraSource;
    FloatingActionButton fabCamera;
    boolean isCamera;
    String result;
    String stringText;
    SurfaceView surfaceView;
    private TextRecognizer textRecognizer;
    TextView textView;
    View view;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_image_translation, viewGroup, false);
        this.view = inflate;
        this.surfaceView = (SurfaceView) inflate.findViewById(R.id.surfaceV);
        FloatingActionButton floatingActionButton = (FloatingActionButton) this.view.findViewById(R.id.fabCamera);
        this.fabCamera = floatingActionButton;
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (LT_ImageTranslationFragment.this.stringText == null) {
                    Toast.makeText(LT_ImageTranslationFragment.this.getActivity(), "Could not catch text!", 0).show();
                    return;
                }
                LT_MainFragment LTMainFragment = new LT_MainFragment();
                Bundle bundle2 = new Bundle();
                bundle2.putString("key", LT_ImageTranslationFragment.this.stringText);
                LTMainFragment.setArguments(bundle2);
                ((LT_FragmentChangeListener) LT_ImageTranslationFragment.this.getContext()).replaceFragment(LTMainFragment);
            }
        });
        ActivityCompat.requestPermissions(getActivity(), new String[]{"android.permission.CAMERA"}, 0);
        this.textView = (TextView) this.view.findViewById(R.id.textV);
        TextRecognizer(layoutInflater, viewGroup);
        return this.view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraSource != null) {
            cameraSource.release();
        }
    }

    private void TextRecognizer(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        this.textRecognizer = new TextRecognizer.Builder(getContext()).build();
        this.cameraSource = new CameraSource.Builder(getContext(), this.textRecognizer).setRequestedPreviewSize(300, 300).setAutoFocusEnabled(true).build();
        this.surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override // android.view.SurfaceHolder.Callback
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(LT_ImageTranslationFragment.this.getActivity(), "android.permission.CAMERA") != 0) {
                        return;
                    }
                    LT_ImageTranslationFragment.this.cameraSource.start(LT_ImageTranslationFragment.this.surfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                LT_ImageTranslationFragment.this.cameraSource.stop();
            }
        });
        this.textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                SparseArray<TextBlock> detectedItems = detections.getDetectedItems();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < detectedItems.size(); i++) {
                    TextBlock valueAt = detectedItems.valueAt(i);
                    if (valueAt != null && valueAt.getValue() != null) {
                        sb.append(valueAt.getValue() + " ");
                    }
                }
                LT_ImageTranslationFragment.this.stringText = sb.toString();
                new Handler(Looper.getMainLooper()).post(new Runnable() { // from class: com.translator.quick.easy.translator_fragments.ImageTranslationFragment.3.1
                    @Override // java.lang.Runnable
                    public void run() {
                        LT_ImageTranslationFragment.this.result = LT_ImageTranslationFragment.this.stringText;
                        LT_ImageTranslationFragment.this.textView.setText(LT_ImageTranslationFragment.this.stringText);
                    }
                });
            }
        });
    }
}
