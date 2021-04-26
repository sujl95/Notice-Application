package me.noticeapplication.persistence.file.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import me.noticeapplication.persistence.file.entity.FileEntity;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
	List<FileEntity> findByNoticeId(Long id);
}
