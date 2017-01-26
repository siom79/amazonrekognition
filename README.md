# amazonrekognition

Sample client for Amazon's rekognition service.

Requires:

* JDK >= 1.8
* maven >= 3.x

Build with:

	mvn clean install
	
Run with:

	java -jar target/amazon-rekognition-0.0.1-SNAPSHOT-jar-with-dependencies.jar <path-to-image-folder>
	
Sends all images within the folder `<path-to-image-folder>` to Amazon's rekognition service to detect faces and creates 
for each image a new image with file ending `_withBox.jpg` with the returned bounding box drawn as red rectangle.
 
The credentials for AWS can be provided using the file ` ~/.aws/credentials` with a default section:
 
	[default]
	aws_access_key_id=<your key>
	aws_secret_access_key=<your secrect key>

