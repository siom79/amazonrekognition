package de.martinmois.amazon.rekognition.actions;

import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.DeleteCollectionRequest;
import com.amazonaws.services.rekognition.model.DeleteCollectionResult;
import de.martinmois.amazon.rekognition.util.AmazonClientFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DeleteCollection {
    private static final Logger LOGGER = Logger.getLogger(DeleteCollection.class.getName());

    public void run(String[] args) {
        if (args.length < 2) {
            LOGGER.log(Level.SEVERE, "Please provide the following arguments: <collection-name>");
            return;
        }
        try {
            AmazonRekognitionClient client = AmazonClientFactory.createClient();
            DeleteCollectionRequest deleteCollectionRequest = new DeleteCollectionRequest();
            deleteCollectionRequest.setCollectionId(args[1]);
            DeleteCollectionResult result = client.deleteCollection(deleteCollectionRequest);
            LOGGER.log(Level.INFO, "Status Code: " + result.getStatusCode());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to delete collection: " + e.getLocalizedMessage());
        }
    }
}
