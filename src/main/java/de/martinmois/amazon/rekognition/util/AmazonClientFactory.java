package de.martinmois.amazon.rekognition.util;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.rekognition.AmazonRekognitionClient;

public class AmazonClientFactory {

    public static AmazonRekognitionClient createClient() {
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
        return rekognitionClient;
    }
}
