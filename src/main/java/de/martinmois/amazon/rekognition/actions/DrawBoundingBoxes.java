package de.martinmois.amazon.rekognition.actions;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.*;
import com.amazonaws.services.rekognition.model.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static de.martinmois.amazon.rekognition.util.Scaling.scaleImage;

public class DrawBoundingBoxes {
    private static final Logger LOGGER = Logger.getLogger(DrawBoundingBoxes.class.getName());

    public void run(String[] args) throws Exception {

        if (args.length < 2) {
            System.out.println("Please provide the following arguments: <path-to-image-folder>");
            return;
        }
        String dirArg = args[1];
        Path dirPath = Paths.get(dirArg);
        if (!Files.exists(dirPath)) {
            System.err.println("The provided directory does not exist.");
            return;
        }
        if (!Files.isDirectory(dirPath)) {
            System.err.println("The provided file is not a directory.");
            return;
        }

        AWSCredentials credentials;
        try {
            credentials = new ProfileCredentialsProvider("default").getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException("Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (/Users/<userid>/.aws/credentials), and is in a valid format.", e);
        }
        AmazonRekognitionClient rekognitionClient = new AmazonRekognitionClient(credentials)
                .withEndpoint("rekognition.eu-west-1.amazonaws.com");
        rekognitionClient.setSignerRegionOverride("eu-west-1");

        Files.walkFileTree(dirPath, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                detectFaces(rekognitionClient, file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });
    }

    private static void detectFaces(AmazonRekognitionClient rekognitionClient, Path imagePath) throws IOException {
        try {
            if (imagePath.toString().endsWith("_withBox.jpg")) {
                return;
            }
            LOGGER.info("Scaling image " + imagePath);
            byte[] scaledImg = scaleImage(imagePath);
            LOGGER.info("Detecting faces on image " + imagePath);
            DetectFacesRequest request = new DetectFacesRequest()
                    .withImage(new Image().withBytes(ByteBuffer.wrap(scaledImg)));
            DetectFacesResult result = rekognitionClient.detectFaces(request);
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(scaledImg));
            Graphics2D graphics = bufferedImage.createGraphics();
            graphics.setColor(Color.red);
            graphics.setStroke(new BasicStroke(20.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 10.0f));
            List<FaceDetail> faceDetails = result.getFaceDetails();
            if (!faceDetails.isEmpty()) {
                for (FaceDetail faceDetail : faceDetails) {
                    BoundingBox boundingBox = faceDetail.getBoundingBox();
                    int left = (int) (boundingBox.getLeft() * bufferedImage.getWidth());
                    int top = (int) (boundingBox.getTop() * bufferedImage.getHeight());
                    int width = (int) (boundingBox.getWidth() * bufferedImage.getWidth());
                    int height = (int) (boundingBox.getHeight() * bufferedImage.getHeight());
                    LOGGER.info("BoundingBox: " + left + "/" + top + " " + width + "/" + height);
                    graphics.drawRect(left, top, width, height);
                    Gender gender = faceDetail.getGender();
                    if (gender != null) {
                        LOGGER.info("Gender: " + gender.getValue() + " " + gender.getConfidence());
                    }
                }
            } else {
                LOGGER.info("No face details returned for " + imagePath);
            }
            graphics.dispose();
            Path newFilePath = Paths.get(imagePath.toString() + "_withBox.jpg");
            ImageIO.write(bufferedImage, "jpg", newFilePath.toFile());
        } catch (AmazonRekognitionException e) {
            LOGGER.log(Level.SEVERE, "Failed to detect faces on image '" + imagePath + "': " + e.getLocalizedMessage());
        }
    }
}
