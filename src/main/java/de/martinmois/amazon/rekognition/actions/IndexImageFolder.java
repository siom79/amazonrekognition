package de.martinmois.amazon.rekognition.actions;

import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.*;
import de.martinmois.amazon.rekognition.util.AmazonClientFactory;
import de.martinmois.amazon.rekognition.util.Scaling;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IndexImageFolder {
    private static final Logger LOGGER = Logger.getLogger(IndexImageFolder.class.getName());

    public void run(String[] args) throws IOException {
        if (args.length < 3) {
            LOGGER.log(Level.SEVERE, "Please provide the following arguments: <path-to-image-folder> <collection-name>");
            return;
        }
        String dirArg = args[1];
        final String collectionArg = args[2];
        Path dirPath = Paths.get(dirArg);
        if (!Files.exists(dirPath)) {
            LOGGER.log(Level.SEVERE, "The provided directory does not exist.");
            return;
        }
        if (!Files.isDirectory(dirPath)) {
            LOGGER.log(Level.SEVERE, "The provided file is not a directory.");
            return;
        }
        AmazonRekognitionClient rekognitionClient = AmazonClientFactory.createClient();

        Files.walkFileTree(dirPath, new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                indexImages(rekognitionClient, file, collectionArg);
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

    private void indexImages(AmazonRekognitionClient rekognitionClient, Path file, String collectionArg) throws IOException {
        try {
            IndexFacesRequest indexFacesRequest = new IndexFacesRequest();
            indexFacesRequest.setImage(new Image().withBytes(ByteBuffer.wrap(Scaling.scaleImage(file))));
            indexFacesRequest.setCollectionId(collectionArg);
            IndexFacesResult result = rekognitionClient.indexFaces(indexFacesRequest);
            LOGGER.log(Level.INFO, "Indexed image '" + file.toString() + "'.");
            List<FaceRecord> faceRecords = result.getFaceRecords();
            LOGGER.log(Level.INFO, "Got " + faceRecords.size() + " records:");
            for (FaceRecord faceRecord : faceRecords) {
                Face face = faceRecord.getFace();
                String faceId = face.getFaceId();
                String imageId = face.getImageId();
                LOGGER.log(Level.INFO, "FaceRecord: faceId=" + faceId + "; imageId=" + imageId);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to index image '" + file.toString() + "': " + e.getLocalizedMessage());
        }
    }
}
