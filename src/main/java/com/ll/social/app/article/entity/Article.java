package com.ll.social.app.article.entity;

import com.ll.social.app.base.entity.BaseEntity;
import com.ll.social.app.hashtag.entity.HashTag;
import com.ll.social.app.member.entity.Member;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class Article extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private String subject;
    private String content;

    public String getTags() {
        Map<String, Object> extra = getExtra();

        if (!extra.containsKey("hashTags")) {
            return "";
        }

        List<HashTag> hashTags = (List<HashTag>) extra.get("hashTags");
        if (hashTags.isEmpty()) {
            return "";
        }

        return hashTags
                .stream()
                .map(hashTag -> "#" + hashTag.getKeyword().getContent())
                .sorted()
                .collect(Collectors.joining(" "));
    }

    public String getExtra_hashTagLinks() {
        Map<String, Object> extra = getExtra();

        if (!extra.containsKey("hashTags")) {
            return "";
        }

        List<HashTag> hashTags = (List<HashTag>) extra.get("hashTags");

        if (hashTags.isEmpty()) {
            return "";
        }

        return hashTags
                .stream()
                .map(hashTag -> {
                    String text = "#" + hashTag.getKeyword().getContent();

                    return """
                            <a href="%s" target="_blank">%s</a>
                            """
                            .stripIndent()
                            .formatted(hashTag.getKeyword().getListUrl(), text);
                })
                .sorted()
                .collect(Collectors.joining(" "));
    }
}
