package com.app.lms.course_management.service;

import com.app.lms.course_management.model.Lesson;
import com.app.lms.course_management.repository.LessonRepository;
import org.springframework.stereotype.Service;

@Service
public class LessonService {
    private final LessonRepository lessonRepository;

    public LessonService(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    public Lesson saveLesson(Lesson lesson){
        return lessonRepository.save(lesson);
    }

    public Lesson getByID(Long id){
        return lessonRepository.findById(id).orElseThrow(() -> new RuntimeException("Lesson not found with ID: " + id));
    }

    public void delete(Lesson lesson){
        lessonRepository.delete(lesson);
    }

    public void save(Lesson lesson){
        lessonRepository.save(lesson);
    }
}
