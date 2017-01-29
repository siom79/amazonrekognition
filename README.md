# amazonrekognition

Sample client for Amazon's rekognition service.

## Build

Requires:

* JDK >= 1.8
* maven >= 3.x

Build with:

	mvn clean install
	
## Execution
	
Run with:

	java -jar target/amazon-rekognition-0.0.1-SNAPSHOT-jar-with-dependencies.jar <action> <parameter for action...>
	
Action must be one of the following:
	DrawBoundingBoxes
	IndexImageFolder
	CreateCollection
	DeleteCollection
	SearchFacesByImage
	
The actions are explained in the following in more detail. After the action name you can provide the parameters
for each action.

### DrawBoundingBoxes

Parameters: <path-to-image-folder>
	
Sends all images within the folder `<path-to-image-folder>` to Amazon's rekognition service to detect faces and creates 
for each image a new image with file ending `_withBox.jpg` with the returned bounding box drawn as red rectangle.

### CreateCollection

Parameters: <collection-name>

Creates a collection with the provided name.

### IndexImageFolder

Parameters: <path-to-image-folder> <collection-name>

Inserts all images in the provided folder structure into the given collection.

### DeleteCollection

Parameters: <collection-name>

Deletes the given collection.

## SearchFacesByImage

Parameters: <collection-name> <image-for-search>

Searches the given collection for the provided face on the image. If the image contains more than one image, the
largest face is used for the search.

## Credentials
 
The credentials for AWS can be provided using the file ` ~/.aws/credentials` with a default section:
 
	[default]
	aws_access_key_id=<your key>
	aws_secret_access_key=<your secrect key>

