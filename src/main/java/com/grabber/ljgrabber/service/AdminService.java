package com.grabber.ljgrabber.service;

import com.grabber.ljgrabber.config.ApplicationProperties;
import com.grabber.ljgrabber.entity.Author;
import com.grabber.ljgrabber.entity.LinkPost;
import com.grabber.ljgrabber.entity.Post;
import com.grabber.ljgrabber.repository.PostRepository;
import com.grabber.ljgrabber.service.lj.LJClient;
import com.grabber.ljgrabber.utils.HtmlBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminService {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final PostRepository postRepository;
    private final LJClient ljClient;
    private final ApplicationProperties applicationProperties;

    @GetMapping("/posts")
    public List<Post> retrieveAllPosts() {
        return postRepository.readAll();
    }

    @GetMapping("/posts/{id}")
    public Post retrievePost(@PathVariable long id) {
        return postRepository.read(id)
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/posts/new")
    public ResponseEntity loadNewPosts(
            @Nullable
            @RequestParam("lastDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDate) {
        Author author = Author.builder()
                .id(1L)
                .name(applicationProperties.getAuthor())
                .build();

        if (lastDate == null) {
            String lastEventTime = postRepository.lastPost()
                    .map(post -> post.getEventtime())
                    .orElse(applicationProperties.getStartDate());
            lastDate = LocalDate.parse(lastEventTime, FORMATTER);
        }

        while (lastDate.isBefore(LocalDate.now())) {
            log.info("Проверяемая дата {}", lastDate.toString());
            List<Post> newPosts = ljClient.loadFromLJ(author,
                    lastDate.getYear(), lastDate.getMonthValue(), lastDate.getDayOfMonth());
            if (!newPosts.isEmpty()) {
                postRepository.saveNew(newPosts);
            }

            lastDate = lastDate.plusDays(1);
            if (lastDate.getMonthValue() == 1) {
                lastDate = lastDate.plusMonths(1);
                if (lastDate.getMonthValue() == 1) {
                    lastDate = lastDate.plusYears(1);
                }
            }
        }


        File outDir = new File(applicationProperties.getOutPath()+"\\html\\post");
        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        String firstEventTime = postRepository.firstPost()
                .map(post -> post.getEventtime())
                .orElse(applicationProperties.getStartDate());
        Integer startYear = LocalDate.parse(firstEventTime, FORMATTER).getYear();
        String lastEventTime = postRepository.lastPost()
                .map(post -> post.getEventtime())
                .orElse(applicationProperties.getStartDate());
        Integer endYear = LocalDate.parse(lastEventTime, FORMATTER).getYear();


        for (Integer currentYear = startYear; currentYear < endYear; currentYear++) {
            String sCurrentYear = String.valueOf(currentYear);

            LinkPost predYear = null;
            if (currentYear > startYear) {
                predYear = new LinkPost((currentYear - 1)+".html", String.valueOf(currentYear - 1));
            };
            LinkPost nextYear = null;
            if (currentYear < endYear) {
                if (currentYear + 1 == endYear) {
                    nextYear = new LinkPost("index.html", String.valueOf(currentYear + 1));
                } else {
                    nextYear = new LinkPost((currentYear + 1) + ".html", String.valueOf(currentYear + 1));
                }
            }

            List<Post> allPosts = postRepository.readAllByYear(sCurrentYear);
            HtmlBuilder.generateOneHtml("index.vm",
                    applicationProperties.getOutPath()+"html\\"+currentYear+".html",
                    predYear,
                    nextYear,
                    allPosts);
            for (Post post: allPosts)
                HtmlBuilder.generateHtml(applicationProperties.getOutPath()+"html\\post\\"+post.getItemid()+".html", post);

        }


        String currentYear = String.valueOf(endYear);
        List<Post> allPosts = postRepository.readAllByYear(currentYear);
        LinkPost predYear = endYear == startYear
                ? null :
                new LinkPost( (endYear - 1) +".html", String.valueOf(endYear - 1));
        HtmlBuilder.generateOneHtml("index.vm",
                applicationProperties.getOutPath()+"html\\index.html",
                predYear,
                null,
                allPosts);
        for (Post post: allPosts)
            HtmlBuilder.generateHtml(applicationProperties.getOutPath()+"html\\post\\"+post.getItemid()+".html", post);

        return ResponseEntity.ok().build();
    }

}
