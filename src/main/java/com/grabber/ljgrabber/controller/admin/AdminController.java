package com.grabber.ljgrabber.controller.admin;

import com.grabber.ljgrabber.config.ApplicationProperties;
import com.grabber.ljgrabber.db.service.PostService;
import com.grabber.ljgrabber.entity.dto.PostDto;
import com.grabber.ljgrabber.lj.entity.LJPost;
import com.grabber.ljgrabber.lj.service.LJClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.time.LocalDate;
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
    public ResponseEntity<String> downloadPosts(@PathParam("year") Integer year,
                                                @PathParam("author") String author) {

        List<LJPost> ljPosts = ljClient.downloadPosts(author, year);
        ljPosts.forEach(ljpost -> postService.save( modelMapper.map(ljpost, PostDto.class)));

        return ResponseEntity.ok("Успешно загружено " + ljPosts.size() + " публикаций из LJ за " + year + " год");
    }

    /**
     * Загрузка последних публикаций из LJ,
     * отсутствующих в локальном хранилище
     *
     * @return
     */
    @GetMapping("/posts/download/lj/new")
    public ResponseEntity loadNewPosts(
            @PathParam("author") String author,
            @Nullable
            @RequestParam("lastDate")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDate) {

        final StringBuffer console = new StringBuffer();

        if (lastDate == null) {
            lastDate = postService.getLastPost()
                    .map(post -> post.getEventTime().toLocalDate())
                    .orElse(applicationProperties.getStartDate());
        }

        while (lastDate.isBefore(LocalDate.now())) {
            log.info("Проверяемая дата {}", lastDate.toString());
            List<LJPost> newPosts = ljClient.loadFromLJ(author,
                    lastDate.getYear(), lastDate.getMonthValue(), lastDate.getDayOfMonth());
            if (!newPosts.isEmpty()) {
                newPosts.forEach(ljpost -> {
                    postService.save( modelMapper.map(ljpost, PostDto.class));
                    console.append("Сохранен пост от ").append(ljpost.getEventtime()).append("<br/>");
                });
            }

            lastDate = lastDate.plusDays(1);
            if (lastDate.getMonthValue() == 1) {
                lastDate = lastDate.plusMonths(1);
                if (lastDate.getMonthValue() == 1) {
                    lastDate = lastDate.plusYears(1);
                }
            }
        }

        return ResponseEntity.ok(console);
    }

}
