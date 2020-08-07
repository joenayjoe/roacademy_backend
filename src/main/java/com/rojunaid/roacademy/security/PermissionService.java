package com.rojunaid.roacademy.security;

import com.rojunaid.roacademy.models.*;
import com.rojunaid.roacademy.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

  @Autowired private AuthenticationFacade authenticationFacade;
  @Autowired private UserRepository userRepository;
  @Autowired private ChapterRepository chapterRepository;
  @Autowired private LectureRepository lectureRepository;
  @Autowired private CourseCommentRepository courseCommentRepository;
  @Autowired private LectureCommentRepository lectureCommentRepository;

  public boolean canManageCourse(Long courseId) {

    User user = userRepository.findById(getCurrentUser().getId()).orElse(null);
    if (user == null) {
      return false;
    }

    return isTeacher(user)
        && user.getTeachingCourses().stream().anyMatch(e -> e.getId() == courseId);
  }

  public boolean canManageChapter(Long chapterId) {

    Chapter chapter = chapterRepository.findById(chapterId).orElse(null);
    if (chapter == null) {
      return false;
    }

    return canManageCourse(chapter.getCourse().getId());
  }

  public boolean canManageLecture(Long lectureId) {
    Lecture lecture = lectureRepository.findById(lectureId).orElse(null);
    if (lecture == null) {
      return false;
    }
    return canManageChapter(lecture.getChapter().getId());
  }

  public boolean canManageCourseComment(Long commentId) {
    CourseComment courseComment = courseCommentRepository.findById(commentId).orElse(null);
    if (courseComment == null) {
      return false;
    }

    User user = getCurrentUser();
    if (courseComment.getCommentedBy().getId() == user.getId()) {
      return true;
    }
    return false;
  }

  public boolean canManageLectureComment(Long commentId) {
    LectureComment lectureComment = lectureCommentRepository.findById(commentId).orElse(null);
    if (lectureComment == null) {
      return false;
    }
    User user = getCurrentUser();
    if (lectureComment.getCommentedBy().getId() == user.getId()) {
      return true;
    }
    return false;
  }

  private boolean isTeacher(User user) {
    return user.getRoles().stream().anyMatch(e -> e.getName().equals(RoleEnum.ROLE_TEACHER));
  }

  private User getCurrentUser() {
    CustomUserPrincipal userPrincipal =
        (CustomUserPrincipal) authenticationFacade.getAuthentication().getPrincipal();
    return userPrincipal.getUser();
  }
}
