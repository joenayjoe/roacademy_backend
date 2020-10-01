package com.rojunaid.roacademy.auth.oauth2.youtube;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.googleapis.media.MediaHttpUploaderProgressListener;
import com.google.api.client.http.InputStreamContent;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Video;
import com.google.api.services.youtube.model.VideoSnippet;
import com.google.api.services.youtube.model.VideoStatus;
import com.rojunaid.roacademy.exception.MediaUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.stream.Collectors;

@Component
public class YoutubeApiManager {

  public static final String BASE_URL = "https://www.youtube.com/watch?v=";
  private static final String VIDEO_FILE_FORMAT = "video/*";
  private static YouTube youtube;
  @Autowired private YoutubeApiConnectionProvider connectionProvider;

  public Video upload(YoutubeMetaData metaData, MultipartFile file) {

    try {
      // This object is used to make YouTube Data API requests.
      youtube = connectionProvider.getYoutubeApiProvider();

      // Add extra information to the video before uploading.
      Video videoObjectDefiningMetadata = new Video();

      // Set the video to be publicly visible. This is the default
      // setting. Other supporting settings are "unlisted" and "private."
      VideoStatus status = new VideoStatus();
      status.setPrivacyStatus(metaData.getStatus());
      videoObjectDefiningMetadata.setStatus(status);

      // Most of the video's metadata is set on the VideoSnippet object.
      VideoSnippet snippet = new VideoSnippet();
      snippet.setTitle(metaData.getTitle());
      snippet.setCategoryId("27"); // 27 = Education
      snippet.setTags(metaData.getTags());
      snippet.setDescription(metaData.getDescription());

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
              .insert("snippet,status", videoObjectDefiningMetadata, mediaContent);

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

      return returnedVideo;

    } catch (GoogleJsonResponseException e) {
      throw new MediaUploadException(e.getDetails().getMessage());
    } catch (IOException e) {
      throw new MediaUploadException(e.getMessage());
    } catch (Throwable t) {
      throw new MediaUploadException(t.getMessage());
    }
  }

  public void delete(String resourceId) {
    try{
      youtube = connectionProvider.getYoutubeApiProvider();
      YouTube.Videos.Delete request = youtube.videos().delete(resourceId);
      request.execute();

    }catch (IOException ex) {
      throw new MediaUploadException("Delete failed with error: ["+ex.getLocalizedMessage()+"]");
    }
  }
}
