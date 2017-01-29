package de.martinmois.amazon.rekognition;

import com.google.common.base.Joiner;
import de.martinmois.amazon.rekognition.actions.*;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

    private enum Action {
        DrawBoundingBoxes,
        IndexImageFolder,
        CreateCollection,
        DeleteCollection,
        SearchFacesByImage
    }

    public static void main(String[] args) throws Exception {
        Action action = null;
        if (args.length > 0) {
            String actionArg = args[0];
            for (Action currAction : Action.values()) {
                if (currAction.name().equalsIgnoreCase(actionArg)) {
                    action = currAction;
                }
            }
        }
        if (action == null) {
            LOGGER.log(Level.SEVERE, "Please provide one of the following actions as first argument: " + Joiner.on(' ').join(Action.values()));
            return;
        }
        switch (action) {
            case DrawBoundingBoxes:
                DrawBoundingBoxes drawBoundingBoxes = new DrawBoundingBoxes();
                drawBoundingBoxes.run(args);
                break;
            case IndexImageFolder:
                IndexImageFolder indexImageFolger = new IndexImageFolder();
                indexImageFolger.run(args);
                break;
            case CreateCollection:
                CreateCollection createCollection = new CreateCollection();
                createCollection.run(args);
                break;
            case SearchFacesByImage:
                SearchFacesByImage searchFacesByImage = new SearchFacesByImage();
                searchFacesByImage.run(args);
                break;
            case DeleteCollection:
                DeleteCollection deleteCollection = new DeleteCollection();
                deleteCollection.run(args);
                break;
            default:
                throw new IllegalStateException("Unsupported action: " + action);
        }
    }
}
