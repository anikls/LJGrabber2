package com.grabber.ljgrabber.controller.admin;

import com.grabber.ljgrabber.config.ApplicationProperties;
import com.grabber.ljgrabber.db.service.PostService;
import com.grabber.ljgrabber.entity.dto.PostDto;
import com.grabber.ljgrabber.lj.entity.Author;
import com.grabber.ljgrabber.lj.entity.LJPost;
import com.grabber.ljgrabber.lj.service.LJClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Контроллер для администрирования приложения.
 * @author aniko
 */
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final PostService postService;
    private final LJClient ljClient;
    private final ApplicationProperties applicationProperties;

    /**
     * Маппер для преобразования сущностей.
     */
    private final ModelMapper modelMapper;

    @GetMapping("/posts")
    public List<PostDto> retrieveAllPosts() {
        return postService.findAll();
    }

    @GetMapping("/posts/{id}")
    public PostDto retrievePost(@PathVariable long id) {
        return postService.getById(id);
    }

    /**
     * Загрузка публикаций из LJ за указанный год в БД.
     *
     * @param year год загрузки публикаций
     * @return
     */
    @GetMapping("/posts/download/lj/{year}")
    public ResponseEntity<String> downloadPosts(@PathVariable("year") Integer year) {
        Author author = Author.builder()
                .id(1L)
                .name(applicationProperties.getAuthor())
                .build();

        List<LJPost> ljPosts = ljClient.downloadPosts(author, year);
        ljPosts.forEach(ljpost -> postService.save( modelMapper.map(ljpost, PostDto.class)));

        return ResponseEntity.ok("Успешно загружено " + ljPosts.size() + " публикаций из LJ за " + year + " год");
    }
/*
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
            String lastEventTime = postRepository.getLastPost()
                    .map(post -> post.getEventtime())
                    .orElse(applicationProperties.getStartDate());
            lastDate = LocalDate.parse(lastEventTime, FORMATTER);
        }

        while (lastDate.isBefore(LocalDate.now())) {
            log.info("Проверяемая дата {}", lastDate.toString());
            List<LJPost> newPosts = ljClient.loadFromLJ(author,
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

        String firstEventTime = postRepository.getFirstPost()
                .map(post -> post.getEventtime())
                .orElse(applicationProperties.getStartDate());
        Integer startYear = LocalDate.parse(firstEventTime, FORMATTER).getYear();
        String lastEventTime = postRepository.getLastPost()
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

            List<LJPost> allPosts = postRepository.findAllByYear(sCurrentYear);
            HtmlBuilder.generateOneHtml("index.vm",
                    applicationProperties.getOutPath()+"html\\"+currentYear+".html",
                    predYear,
                    nextYear,
                    allPosts);
            for (LJPost post: allPosts)
                HtmlBuilder.generateHtml(applicationProperties.getOutPath()+"html\\post\\"+post.getItemid()+".html", post);

        }


        String currentYear = String.valueOf(endYear);
        List<LJPost> allPosts = postRepository.findAllByYear(currentYear);
        LinkPost predYear = endYear == startYear
                ? null :
                new LinkPost( (endYear - 1) +".html", String.valueOf(endYear - 1));
        HtmlBuilder.generateOneHtml("index.vm",
                applicationProperties.getOutPath()+"html\\index.html",
                predYear,
                null,
                allPosts);
        for (LJPost post: allPosts)
            HtmlBuilder.generateHtml(applicationProperties.getOutPath()+"html\\post\\"+post.getItemid()+".html", post);

        return ResponseEntity.ok().build();
    }
*/
}
