package com.ll.social.app.article.repository;

import com.ll.social.app.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepositoryCustom  {

    List<Article> getQslArticles();

    List<Article> searchQsl(String kwType, String kw);
}
