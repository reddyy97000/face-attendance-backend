package com.attendance.service;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Base64;

/**
 * OpenCV face module: detects the largest face with a Haar cascade,
 * crops + normalizes it, and builds a feature vector (64x64 equalized
 * grayscale pixels). Matching uses normalized cross-correlation.
 */
@Service
public class FaceRecognitionService {
    public static final int SIZE = 64;
    private final CascadeClassifier detector;

    public FaceRecognitionService() throws Exception {
        File tmp = File.createTempFile("haar", ".xml");
        try (InputStream in = new ClassPathResource("haarcascade_frontalface_default.xml").getInputStream()) {
            Files.copy(in, tmp.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
        detector = new CascadeClassifier(tmp.getAbsolutePath());
        if (detector.empty()) throw new IllegalStateException("Could not load Haar cascade");
    }

    /** @return feature vector or null if no face found */
    public float[] extractFeatures(String base64Image) {
        if (base64Image == null || base64Image.isBlank()) return null;
        Mat img = decode(base64Image);
        if (img == null || img.empty()) return null;
        Mat gray = new Mat();
        Imgproc.cvtColor(img, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(gray, gray);

        MatOfRect faces = new MatOfRect();
        detector.detectMultiScale(gray, faces, 1.1, 5, 0, new Size(80, 80), new Size());
        Rect best = null;
        for (Rect r : faces.toArray()) if (best == null || r.area() > best.area()) best = r;
        if (best == null) return null;

        Mat face = new Mat(gray, best);
        Mat resized = new Mat();
        Imgproc.resize(face, resized, new Size(SIZE, SIZE));
        Imgproc.equalizeHist(resized, resized);

        float[] v = new float[SIZE * SIZE];
        byte[] px = new byte[SIZE * SIZE];
        resized.get(0, 0, px);
        double mean = 0;
        for (int i = 0; i < px.length; i++) { v[i] = px[i] & 0xFF; mean += v[i]; }
        mean /= v.length;
        double norm = 0;
        for (int i = 0; i < v.length; i++) { v[i] -= mean; norm += v[i] * v[i]; }
        norm = Math.sqrt(norm) + 1e-9;
        for (int i = 0; i < v.length; i++) v[i] /= norm;
        return v;
    }

    /** Similarity in [0,1] (1 = identical). */
    public double similarity(float[] a, float[] b) {
        double dot = 0;
        for (int i = 0; i < a.length; i++) dot += a[i] * b[i];
        return (dot + 1) / 2;
    }

    public String serialize(float[] v) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < v.length; i++) { if (i > 0) sb.append(','); sb.append(v[i]); }
        return sb.toString();
    }

    public float[] deserialize(String s) {
        String[] parts = s.split(",");
        float[] v = new float[parts.length];
        for (int i = 0; i < parts.length; i++) v[i] = Float.parseFloat(parts[i]);
        return v;
    }

    private Mat decode(String base64) {
        try {
            if (base64 == null || base64.isBlank()) return null;
            String data = base64.contains(",") ? base64.substring(base64.indexOf(',') + 1) : base64;
            byte[] bytes = Base64.getDecoder().decode(data.trim());
            return Imgcodecs.imdecode(new MatOfByte(bytes), Imgcodecs.IMREAD_COLOR);
        } catch (Exception e) {
            return null;
        }
    }
}
