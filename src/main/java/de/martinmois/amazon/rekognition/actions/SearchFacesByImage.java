package de.martinmois.amazon.rekognition.actions;

import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.*;
import de.martinmois.amazon.rekognition.util.AmazonClientFactory;
import de.martinmois.amazon.rekognition.util.Scaling;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchFacesByImage {
    private static final Logger LOGGER = Logger.getLogger(SearchFacesByImage.class.getName());

    public void run(String[] args) {
        if (args.length < 3) {
            LOGGER.log(Level.SEVERE, "Please provide the following arguments: <collection-name> <image-for-search>");
            return;
        }
        try {
            AmazonRekognitionClient client = AmazonClientFactory.createClient();
            SearchFacesByImageRequest searchFacesRequest = new SearchFacesByImageRequest();
            searchFacesRequest.setCollectionId(args[1]);
            searchFacesRequest.setImage(new Image().withBytes(ByteBuffer.wrap(Scaling.scaleImage(Paths.get(args[2])))));
            SearchFacesByImageResult result = client.searchFacesByImage(searchFacesRequest);
            List<FaceMatch> faceMatches = result.getFaceMatches();
            LOGGER.info("Got " + faceMatches.size() + " matches:");
            for (FaceMatch faceMatch : faceMatches) {
                Face face = faceMatch.getFace();
                String imageId = face.getImageId();
                String faceId = face.getFaceId();
                Float similarity = faceMatch.getSimilarity();
                LOGGER.info("FaceMatch: imageId=" + imageId + "; faceId=" + faceId + "; similarity=" + similarity);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to search for face: " + e.getLocalizedMessage());
        }
    }
}
