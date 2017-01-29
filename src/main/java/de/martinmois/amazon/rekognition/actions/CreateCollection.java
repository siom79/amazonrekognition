package de.martinmois.amazon.rekognition.actions;

import com.amazonaws.services.rekognition.AmazonRekognitionClient;
import com.amazonaws.services.rekognition.model.CreateCollectionRequest;
import com.amazonaws.services.rekognition.model.CreateCollectionResult;
import de.martinmois.amazon.rekognition.util.AmazonClientFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CreateCollection {
    private static final Logger LOGGER = Logger.getLogger(CreateCollection.class.getName());

    public void run(String[] args) {
        if (args.length < 2) {
            LOGGER.log(Level.SEVERE, "Please provide the following argument: <collection-name>");
            return;
        }
        try {
            AmazonRekognitionClient client = AmazonClientFactory.createClient();
            CreateCollectionRequest createCollectionRequest = new CreateCollectionRequest();
            createCollectionRequest.setCollectionId(args[1]);
            CreateCollectionResult result = client.createCollection(createCollectionRequest);
            Integer statusCode = result.getStatusCode();
            LOGGER.info("Status code: " + String.valueOf(statusCode));
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to create collection: " + e.getLocalizedMessage());
        }
    }
}
