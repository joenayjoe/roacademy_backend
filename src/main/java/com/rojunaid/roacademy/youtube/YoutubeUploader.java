package com.rojunaid.roacademy.youtube;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.rojunaid.roacademy.exception.YoutubeUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

@Component
public class YoutubeUploader {

  private static final String VIDEO_FILE_FORMAT = "video/*";
  private static YouTube youtube;
  @Autowired private Auth authProvider;

  public Video upload(YoutubeMetaData metaData, MultipartFile file) {

    try {

      Credential credential = authProvider.authorize();

      // This object is used to make YouTube Data API requests.
      youtube =
          new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
              .setApplicationName("roacademy-youtube-video-upload")
              .build();

      // Add extra information to the video before uploading.
      Video videoObjectDefiningMetadata = new Video();

      // Set the video to be publicly visible. This is the default
      // setting. Other supporting settings are "unlisted" and "private."
      VideoStatus status = new VideoStatus();
      status.setPrivacyStatus(metaData.getStatus());
      videoObjectDefiningMetadata.setStatus(status);

      // Most of the video's metadata is set on the VideoSnippet object.
      VideoSnippet snippet = new VideoSnippet();

      // This code uses a Calendar instance to create a unique name and
      // description for test purposes so that you can easily upload
      // multiple files. You should remove this code from your project
      // and use your own standard names instead.
      snippet.setTitle(metaData.getTitle());
      snippet.setCategoryId("27"); // 27 = Education

      snippet.setTags(metaData.getTags());

      String hashTags =
          metaData.getTags().stream().map(tag -> "#" + tag).collect(Collectors.joining(" "));

      snippet.setDescription(metaData.getDescription() + "\n\n" + hashTags);

      // Add the completed snippet object to the video resource.
      videoObjectDefiningMetadata.setSnippet(snippet);

      InputStream inputStream = file.getInputStream();

      InputStreamContent mediaContent = new InputStreamContent(VIDEO_FILE_FORMAT, inputStream);

      // Insert the video. The command sends three arguments. The first
      // specifies which information the API request is setting and which
      // information the API response should return. The second argument
      // is the video resource that contains metadata about the new video.
      // The third argument is the actual video content.
      YouTube.Videos.Insert videoInsert =
          youtube
              .videos()
              .insert("snippet,statistics,status", videoObjectDefiningMetadata, mediaContent);

      // Set the upload type and add an event listener.
      MediaHttpUploader uploader = videoInsert.getMediaHttpUploader();

      // Indicate whether direct media upload is enabled. A value of
      // "True" indicates that direct media upload is enabled and that
      // the entire media content will be uploaded in a single request.
      // A value of "False," which is the default, indicates that the
      // request will use the resumable media upload protocol, which
      // supports the ability to resume an upload operation after a
      // network interruption or other transmission failure, saving
      // time and bandwidth in the event of network failures.
      uploader.setDirectUploadEnabled(false);

      MediaHttpUploaderProgressListener progressListener =
          new MediaHttpUploaderProgressListener() {
            public void progressChanged(MediaHttpUploader uploader) throws IOException {
              switch (uploader.getUploadState()) {
                case INITIATION_STARTED:
                  System.out.println("Initiation Started");
                  break;
                case INITIATION_COMPLETE:
                  System.out.println("Initiation Completed");
                  break;
                case MEDIA_IN_PROGRESS:
                  System.out.println("Upload in progress");
                  System.out.println("Upload percentage: " + uploader.getProgress());
                  break;
                case MEDIA_COMPLETE:
                  System.out.println("Upload Completed!");
                  break;
                case NOT_STARTED:
                  System.out.println("Upload Not Started!");
                  break;
              }
            }
          };
      uploader.setProgressListener(progressListener);

      // Call the API and upload the video.
      Video returnedVideo = videoInsert.execute();

      // Print data about the newly inserted video from the API response.
      System.out.println("\n================== Returned Video ==================\n");
      System.out.println("  - Id: " + returnedVideo.getId());
      System.out.println("  - Title: " + returnedVideo.getSnippet().getTitle());
      System.out.println("  - Tags: " + returnedVideo.getSnippet().getTags());
      System.out.println("  - Privacy Status: " + returnedVideo.getStatus().getPrivacyStatus());
      System.out.println("  - Video Count: " + returnedVideo.getStatistics().getViewCount());

      return returnedVideo;

    } catch (GoogleJsonResponseException e) {
      System.err.println(
          "GoogleJsonResponseException code: "
              + e.getDetails().getCode()
              + " : "
              + e.getDetails().getMessage());
      e.printStackTrace();
      throw new YoutubeUploadException(e.getDetails().getMessage());
    } catch (IOException e) {
      System.err.println("IOException: " + e.getMessage());
      e.printStackTrace();
      throw new YoutubeUploadException(e.getMessage());
    } catch (Throwable t) {
      System.err.println("Throwable: " + t.getMessage());
      t.printStackTrace();
      throw new YoutubeUploadException(t.getMessage());
    }
  }
}
