package com.google.firebase.samples.apps.mlkit.textrecognition;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;
import com.google.firebase.samples.apps.mlkit.FrameMetadata;
import com.google.firebase.samples.apps.mlkit.GraphicOverlay;
import com.google.firebase.samples.apps.mlkit.VisionProcessorBase;

import java.io.IOException;
import java.util.List;

/** Processor for the text recognition demo. */
public class TextRecognitionProcessor extends VisionProcessorBase<FirebaseVisionText> {

  private static final String TAG = "TextRecognitionProcessor";

  private final FirebaseVisionTextDetector detector;

  public TextRecognitionProcessor() {
    detector = FirebaseVision.getInstance().getVisionTextDetector();
  }

  @Override
  public void stop() {
    try {
      detector.close();
    } catch (IOException e) {
      Log.e(TAG, "Exception thrown while trying to close Text Detector: " + e);
    }
  }

  @Override
  protected Task<FirebaseVisionText> detectInImage(FirebaseVisionImage image) {
    return detector.detectInImage(image);
  }

  @Override
  protected void onSuccess(
      @NonNull FirebaseVisionText results,
      @NonNull FrameMetadata frameMetadata,
      @NonNull GraphicOverlay graphicOverlay) {
    graphicOverlay.clear();
    List<FirebaseVisionText.Block> blocks = results.getBlocks();
    for (int i = 0; i < blocks.size(); i++) {
      List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
      for (int j = 0; j < lines.size(); j++) {
        List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
        for (int k = 0; k < elements.size(); k++) {
          GraphicOverlay.Graphic textGraphic = new TextGraphic(graphicOverlay, elements.get(k));
          graphicOverlay.add(textGraphic);

        }
      }
    }
  }

  @Override
  protected void onFailure(@NonNull Exception e) {
    Log.w(TAG, "Text detection failed." + e);
  }
}
