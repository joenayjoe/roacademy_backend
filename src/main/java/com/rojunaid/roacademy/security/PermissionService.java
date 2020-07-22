package com.rojunaid.roacademy.security;

import com.rojunaid.roacademy.models.Chapter;
import com.rojunaid.roacademy.models.Lecture;
import com.rojunaid.roacademy.models.RoleEnum;
import com.rojunaid.roacademy.models.User;
import com.rojunaid.roacademy.repositories.ChapterRepository;
import com.rojunaid.roacademy.repositories.LectureRepository;
import com.rojunaid.roacademy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {

  @Autowired private AuthenticationFacade authenticationFacade;
  @Autowired private UserRepository userRepository;
  @Autowired private ChapterRepository chapterRepository;
  @Autowired private LectureRepository lectureRepository;

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

  private boolean isTeacher(User user) {
    return user.getRoles().stream().anyMatch(e -> e.getName().equals(RoleEnum.ROLE_TEACHER));
  }

  private User getCurrentUser() {
    CustomUserPrincipal userPrincipal =
        (CustomUserPrincipal) authenticationFacade.getAuthentication().getPrincipal();
    return userPrincipal.getUser();
  }
}
