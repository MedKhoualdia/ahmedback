package com.dance.mo.auth.Service;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class FaceRecognitionService {
    private final CascadeClassifier faceDetector;

    public FaceRecognitionService() throws IOException {
        // Load the pre-trained face detection model
        faceDetector = new CascadeClassifier();
        if (!faceDetector.load("haarcascade_frontalface_default.xml")) {
            throw new IOException("Failed to load face detection model.");
        }
    }

    public boolean compareImages(byte[] storedImageBytes, byte[] capturedImageBytes) {
        // Convert byte arrays to OpenCV Mat objects
        Mat storedImage = Imgcodecs.imdecode(new MatOfByte(storedImageBytes), Imgcodecs.IMREAD_UNCHANGED);
        Mat capturedImage = Imgcodecs.imdecode(new MatOfByte(capturedImageBytes), Imgcodecs.IMREAD_UNCHANGED);

        // Convert both images to grayscale
        Mat storedGray = new Mat();
        Mat capturedGray = new Mat();
        Imgproc.cvtColor(storedImage, storedGray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.cvtColor(capturedImage, capturedGray, Imgproc.COLOR_BGR2GRAY);

        // Detect faces in the captured image
        MatOfRect detectedFaces = new MatOfRect();
        faceDetector.detectMultiScale(capturedGray, detectedFaces);

        // Check if any faces are detected
        if (detectedFaces.toArray().length == 0) {
            return false; // No faces detected
        }

        // Get the first detected face
        Rect face = detectedFaces.toArray()[0];

        // Extract the region of interest (ROI) from the captured image
        Mat capturedFace = new Mat(capturedGray, face);

        // Resize the stored image to match the size of the captured face
        Mat resizedStoredImage = new Mat();
        Imgproc.resize(storedGray, resizedStoredImage, new Size(face.width, face.height));

        // Calculate the absolute difference between the captured face and the stored face
        Mat diff = new Mat();
        Core.absdiff(capturedFace, resizedStoredImage, diff);

        // Convert the difference image to grayscale
        Mat diffGray = new Mat();
        Imgproc.cvtColor(diff, diffGray, Imgproc.COLOR_BGR2GRAY);

        // Calculate the mean pixel intensity of the difference image
        Scalar meanDiff = Core.mean(diffGray);

        // Define a threshold for determining if the faces match
        double threshold = 30.0; // Adjust as needed

        // Compare the mean pixel intensity with the threshold
        return meanDiff.val[0] <= threshold;
    }
}
