package com.app.lms.course_management.repository;

import com.app.lms.course_management.model.MediaFile;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MediaFileRepository extends JpaRepository<MediaFile, Long> {
}
