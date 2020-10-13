package com.grabber.ljgrabber.service.impl;

import com.grabber.ljgrabber.config.ApplicationProperties;
import com.grabber.ljgrabber.entity.db.Post;
import com.grabber.ljgrabber.entity.dto.PostDto;
import com.grabber.ljgrabber.entity.html.LinkPost;
import com.grabber.ljgrabber.service.HtmlService;
import com.grabber.ljgrabber.service.PostService;
import com.grabber.ljgrabber.component.HtmlBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Сервис для работы с генерацией html
 */
@Service
@RequiredArgsConstructor
public class HtmlServiceImpl implements HtmlService {

    private static final String HTML = ".html";

    private final PostService postService;
    private final ApplicationProperties applicationProperties;
    private final HtmlBuilder htmlBuilder;

    /**
     * Сгенерировать html-представление публикация для всех постов
     */
    public String generateAll(String author) {

        final File outDir = new File(applicationProperties.getOutPath(),"html\\post");
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        final LocalDateTime firstEventTime = postService.getFirstPost(author)
                .map(PostDto::getEventTime)
                .orElse(applicationProperties.getStartDate().atTime(0,0));
        final Integer startYear = firstEventTime.getYear();
        final LocalDateTime lastEventTime = postService.getLastPost(author)
                .map(PostDto::getEventTime)
                .orElse(applicationProperties.getStartDate().atTime(0,0));
        final Integer endYear = lastEventTime.getYear();

        for (Integer currentYear = startYear; currentYear < endYear; currentYear++) {
            final Integer sCurrentYear = currentYear;

            File outDirPost = new File(outDir.getPath(), String.valueOf(sCurrentYear));
            if (!outDirPost.exists()) {
                outDirPost.mkdirs();
            }

            LinkPost predYear = null;
            if (currentYear > startYear) {
                predYear = new LinkPost((currentYear - 1) + HTML, String.valueOf(currentYear - 1));
            }
            LinkPost nextYear = null;
            if (currentYear < endYear) {
                if (currentYear + 1 == endYear) {
                    nextYear = new LinkPost("index.html", String.valueOf(currentYear + 1));
                } else {
                    nextYear = new LinkPost((currentYear + 1) + HTML, String.valueOf(currentYear + 1));
                }
            }

            List<PostDto> allPosts = postService.findAllByYear(author, sCurrentYear);
            htmlBuilder.generateOneHtml("template/html/index.vm",
                    applicationProperties.getOutPath() + "html\\" + currentYear + HTML,
                    predYear,
                    sCurrentYear,
                    nextYear,
                    allPosts);
            for (PostDto post: allPosts)
                htmlBuilder.generateHtml(new File(outDirPost, post.getItemId() + HTML), post);

        }

        Integer currentYear = endYear;

        File outDirPost = new File(outDir.getPath(), String.valueOf(currentYear));
        if (!outDirPost.exists()) {
            outDirPost.mkdirs();
        }

        List<PostDto> allPosts = postService.findAllByYear(author, currentYear);
        LinkPost predYear = endYear.equals(startYear)
                ? null :
                new LinkPost( (endYear - 1) + HTML, String.valueOf(endYear - 1));
        htmlBuilder.generateOneHtml("template/html/index.vm",
                applicationProperties.getOutPath() + "html\\index.html",
                predYear,
                currentYear,
                null,
                allPosts);
        for (PostDto post: allPosts){
            htmlBuilder.generateHtml(new File(outDirPost, post.getItemId() + HTML), post);
        }

        return "OK";
    }

    /**
     * Выгрузить конкретную публикации автора в html
     */
    public String generateHtmlPost(String author, long itemId) {

        final File outDir = new File(applicationProperties.getOutPath(),"html\\post");
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        PostDto post = postService.getByItemId(author, itemId);
        Integer currentYear = post.getEventTime().getYear();

        File outDirPost = new File(outDir.getPath(), String.valueOf(currentYear));
        if (!outDirPost.exists()) {
            outDirPost.mkdirs();
        }

        return htmlBuilder.generateHtml(new File(outDirPost, post.getItemId() + HTML), post);
    }
}
