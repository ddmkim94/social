package com.ll.social.app.fileupload.repository;

import com.ll.social.app.fileupload.entity.GenFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenFileRepository extends JpaRepository<GenFile, Long> {
}
