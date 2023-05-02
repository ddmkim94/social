package com.ll.social.app.hashtag.repository;

import com.ll.social.app.hashtag.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {

    Optional<HashTag> findByArticleIdAndKeywordId(Long articleId, Long keywordId);
}
