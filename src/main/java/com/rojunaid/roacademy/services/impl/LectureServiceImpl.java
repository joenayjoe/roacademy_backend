package com.rojunaid.roacademy.services.impl;

import com.rojunaid.roacademy.auth.oauth2.UploadedResourceInfo;
import com.rojunaid.roacademy.auth.oauth2.youtube.YoutubeMetaData;
import com.rojunaid.roacademy.dto.*;
import com.rojunaid.roacademy.exception.BadRequestException;
import com.rojunaid.roacademy.exception.ResourceNotFoundException;
import com.rojunaid.roacademy.models.*;
import com.rojunaid.roacademy.repositories.*;
import com.rojunaid.roacademy.security.CustomUserPrincipal;
import com.rojunaid.roacademy.services.FileUploadService;
import com.rojunaid.roacademy.services.LectureService;
import com.rojunaid.roacademy.services.TagService;
import com.rojunaid.roacademy.util.SortingUtils;
import com.rojunaid.roacademy.util.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class LectureServiceImpl implements LectureService {

  @Autowired private LectureRepository lectureRepository;
  @Autowired private LectureResourceRepository resourceRepository;
  @Autowired private ChapterRepository chapterRepository;
  @Autowired private TagService tagService;
  @Autowired private FileUploadService fileUploadService;
  @Autowired private LectureCommentRepository lectureCommentRepository;
  @Autowired private UserRepository userRepository;

  @Override
  public LectureResponse createLecture(LectureRequest lectureRequest) {
    Lecture lecture = this.lectureRequestToLecture(lectureRequest);
    lecture = lectureRepository.save(lecture);
    return lectureToLectureResponse(lecture);
  }

  @Override
  public LectureResponse updateLecture(Long lectureId, LectureUpdateRequest request) {
    Lecture lecture = getLecture(lectureId);
    lecture.setName(request.getName());
    lecture.setDescription(request.getDescription());
    lecture.setChapter(this.getChapter(request.getChapterId()));

    Set<Tag> tags = tagService.findOrCreateByNames(request.getTags());
    lecture.setTags(tags);
    return lectureToLectureResponse(lectureRepository.save(lecture));
  }

  @Override
  public void updatePositions(LecturePositionUpdateRequest[] positions) {

    boolean isFailed = false;
    for (LecturePositionUpdateRequest request : positions) {
      Lecture lecture = lectureRepository.findById(request.getLectureId()).orElse(null);
      Chapter chapter = chapterRepository.findById(request.getChapterId()).orElse(null);
      if (lecture != null && chapter != null) {
        lecture.setChapter(chapter);
        lecture.setPosition(request.getPosition());
        lectureRepository.save(lecture);
      } else {
        isFailed = true;
      }
    }

    if (isFailed) {
      throw new BadRequestException(Translator.toLocale("${BadRequest}"));
    }
  }

  @Transactional
  @Override
  public LectureResponse uploadResource(Long lectureId, MultipartFile file) {
    Lecture lecture = getLecture(lectureId);

    String contentType = file.getContentType();

    UploadedResourceInfo resourceInfo;
    LectureResource resource = new LectureResource();
    resource.setFileName(file.getOriginalFilename());
    resource.setFileSize(file.getSize());
    resource.setContentType(contentType);

    if (contentType.startsWith("video/")) {
      YoutubeMetaData youtubeMetaData = new YoutubeMetaData();

      youtubeMetaData.setTitle(lecture.getName());
      youtubeMetaData.setDescription(lecture.getDescription());
      List<String> tags =
          lecture.getTags().stream().map(t -> t.getName()).collect(Collectors.toList());
      youtubeMetaData.setTags(tags);
      youtubeMetaData.setStatus("public");
      resourceInfo = fileUploadService.uploadToYoutube(youtubeMetaData, file);
      resource.setFileUrl(resourceInfo.getResourceUrl());
      resource.setResourceId(resourceInfo.getResourceId());

    } else {
      resourceInfo =
          fileUploadService.uploadToBox(
              Lecture.class.getSimpleName().toLowerCase(), lecture.getId(), file);
      resource.setFileUrl(resourceInfo.getResourceUrl());
      resource.setResourceId(resourceInfo.getResourceId());
    }

    lecture.addLectureResource(resource);
    lecture = lectureRepository.save(lecture);

    return lectureToLectureResponse(lecture);
  }

  @Transactional
  @Override
  public void deleteLectureResource(Long lectureId, Long resourceId) {
    Lecture lecture = getLecture(lectureId);
    LectureResource resource =
        resourceRepository
            .findById(resourceId)
            .orElseThrow(
                () ->
                    new ResourceNotFoundException(
                        Translator.toLocale(
                            "${LectureResource.id.notfound}", new Object[] {resourceId})));
    if (resource.getContentType().startsWith("video")) {
      lecture.getLectureResources().remove(resource);
      lectureRepository.save(lecture);
      fileUploadService.deleteFromYoutube(resource.getResourceId());
    } else {
      lecture.getLectureResources().remove(resource);
      lectureRepository.save(lecture);
      fileUploadService.deleteFromBox(resource.getResourceId());
    }
  }

  @Override
  @Transactional
  public void deleteLecture(Long lectureId) {
    Lecture lecture = getLecture(lectureId);
    if (lecture.getLectureResources().size() > 0) {
      throw new BadRequestException(
          "Lecture with contents cannot be deleted. Delete the contents first.");
    }
    lectureRepository.delete(lecture);
  }

  @Override
  public CommentResponse addComment(Long lectureId, CommentRequest commentRequest) {
    Lecture lecture = this.getLecture(lectureId);
    LectureComment comment = commentRequestToLectureComment(commentRequest);
    lecture.addComment(comment);

    comment = lectureCommentRepository.save(comment);

    return commentToCommentResponse(comment);
  }

  @Override
  public CommentResponse addCommentReply(
      Long lectureId, Long commentId, CommentRequest commentRequest) {
    LectureComment parentComment = getComment(commentId);

    LectureComment reply = commentRequestToLectureComment(commentRequest);
    if (parentComment.getParent() != null) {
      reply.setParent(parentComment.getParent());
    } else {
      reply.setParent(parentComment);
    }
    reply = lectureCommentRepository.save(reply);
    return commentToCommentResponse(reply);
  }

  @Override
  public Page<CommentResponse> getComments(Long lectureId, int page, int size, String order) {
    PageRequest pageRequest = PageRequest.of(page, size, SortingUtils.SortBy(order));
    Page<LectureComment> comments =
        lectureCommentRepository.findAllByLectureId(lectureId, pageRequest);
    Page<CommentResponse> responses = comments.map(x -> commentToCommentResponse(x));
    return responses;
  }

  @Override
  public Page<CommentResponse> getCommentReplies(
      Long lectureId, Long commentId, int page, int size, String order) {
    PageRequest pageRequest = PageRequest.of(page, size, SortingUtils.SortBy(order));
    Page<LectureComment> replies =
        lectureCommentRepository.findCommentReplies(commentId, pageRequest);
    Page<CommentResponse> responses = replies.map(x -> commentToCommentResponse(x));
    return responses;
  }

  @Override
  public CommentResponse updateComment(
      Long lectureId, Long commentId, CommentUpdateRequest commentUpdateRequest) {
    LectureComment comment = this.getComment(commentId);

    if (canManageComment(comment)) {
      comment.setCommentBody(commentUpdateRequest.getCommentBody());
      comment = lectureCommentRepository.save(comment);
      return commentToCommentResponse(comment);
    }
    throw new AccessDeniedException(Translator.toLocale("AccessDenied"));
  }

  @Override
  public void deleteComment(Long lectureId, Long commentId) {
    LectureComment comment = this.getComment(commentId);
    if (canManageComment(comment)) {
      lectureCommentRepository.delete(comment);
    } else {
      throw new AccessDeniedException(Translator.toLocale("AccessDenied"));
    }
  }

  @Override
  public LectureResponse lectureToLectureResponse(Lecture lecture) {
    LectureResponse response = new LectureResponse();
    response.setId(lecture.getId());
    response.setName(lecture.getName());
    response.setDescription(lecture.getDescription());
    response.setPosition(lecture.getPosition());
    response.setLectureResources(lecture.getLectureResources());
    response.setTags(
        lecture.getTags().stream().map(tag -> tag.getName()).collect(Collectors.toList()));
    response.setCreatedAt(lecture.getCreatedAt());
    response.setUpdatedAt(lecture.getUpdatedAt());

    return response;
  }

  private Chapter getChapter(Long chapterId) {
    return chapterRepository
        .findById(chapterId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    Translator.toLocale("${Chapter.id.notfound}", new Object[] {chapterId})));
  }

  private Lecture getLecture(Long lectureId) {
    return lectureRepository
        .findById(lectureId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    Translator.toLocale("${Lecture.id.notfound}", new Object[] {lectureId})));
  }

  private User getUser(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    Translator.toLocale("User.id.notfound", new Object[] {userId})));
  }

  private LectureComment getComment(Long commentId) {
    return lectureCommentRepository
        .findById(commentId)
        .orElseThrow(
            () ->
                new ResourceNotFoundException(
                    Translator.toLocale("LectureComment.id.notfound", new Object[] {commentId})));
  }

  private Lecture lectureRequestToLecture(LectureRequest lectureRequest) {
    Lecture lecture = new Lecture();
    lecture.setName(lectureRequest.getName());
    lecture.setDescription(lectureRequest.getDescription());
    lecture.setPosition(lectureRequest.getPosition());
    lecture.setChapter(getChapter(lectureRequest.getChapterId()));

    Set<Tag> tags = tagService.findOrCreateByNames(lectureRequest.getTags());
    lecture.setTags(tags);

    return lecture;
  }

  private boolean canManageComment(LectureComment comment) {
    CustomUserPrincipal principal =
        (CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = principal.getUser();
    user = this.getUser(user.getId());
    if (comment.getCommentedBy().getId() == user.getId()) {
      return true;
    }
    return false;
  }

  private LectureComment commentRequestToLectureComment(CommentRequest commentRequest) {
    LectureComment comment = new LectureComment();
    comment.setCommentBody(commentRequest.getCommentBody());

    CustomUserPrincipal principal =
        (CustomUserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    User user = principal.getUser();
    user = this.getUser(user.getId());
    comment.addCommentedBy(user);
    return comment;
  }

  private CommentResponse commentToCommentResponse(LectureComment comment) {
    CommentResponse response = new CommentResponse();

    response.setId(comment.getId());
    response.setCommentBody(comment.getCommentBody());
    if (comment.getParent() != null) {
      response.setParentId(comment.getParent().getId());
    }
    response.setCreatedAt(comment.getCreatedAt());
    response.setUpdatedAt(comment.getUpdatedAt());

    PrimaryUser primaryUser = new PrimaryUser();
    primaryUser.setId(comment.getCommentedBy().getId());
    primaryUser.setFirstName(comment.getCommentedBy().getFirstName());
    primaryUser.setLastName(comment.getCommentedBy().getLastName());
    primaryUser.setEmail(comment.getCommentedBy().getEmail());
    primaryUser.setImageUrl(comment.getCommentedBy().getImageUrl());
    response.setCommentedBy(primaryUser);

    response.setNumberOfReplies(comment.getReplies().size());
    return response;
  }
}
