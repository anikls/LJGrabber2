package com.grabber.ljgrabber.component;

import com.grabber.ljgrabber.entity.dto.PostDto;
import com.grabber.ljgrabber.entity.html.LinkPost;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.tools.generic.DateTool;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.List;

/**
 * Компонент для генерации html-страниц по шаблону.
 */
@Slf4j
@Component
public class HtmlBuilder {

	private static final String INDEX_TEMPLATE = "template/html/index.vm";
	private static final String POST_TEMPLATE = "template/html/post.vm";
	
	private static VelocityEngine ve;
	
	private static VelocityEngine getVE() {
		if (ve == null) {
			ve = new VelocityEngine();
		    ve.init();
		} 
		return ve;
	}

	@SneakyThrows
	public String generatePostHtml(File outFileName, PostDto post) {

	    Template t = getVE().getTemplate(POST_TEMPLATE ,"UTF-8");
	    VelocityContext context = new VelocityContext();
	    context.put("title",
				StringUtils.isBlank(post.getSubject()) ? post.getEventTime() : post.getSubject());
	    context.put("header",
				StringUtils.isBlank(post.getSubject()) ? post.getEventTime() : post.getSubject());
	    context.put("post", post);
		context.put("date", new DateTool());

		try (FileWriter outHtml = new FileWriter(outFileName)) {
			t.merge(context, outHtml);
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}

		return new String((Files.readAllBytes(outFileName.toPath())));
	}
	
	public void generateIndexHtml(File outFileName,
								  String title,
								  LinkPost predYear,
								  Integer currYear,
								  LinkPost nextYear,
								  List<PostDto> listPost) {
	    Template t = getVE().getTemplate(INDEX_TEMPLATE,"UTF-8");
	    VelocityContext context = new VelocityContext();
	    context.put("title", title);
	    context.put("header", title);
	    context.put("postList", listPost);
		context.put("predYear", predYear);
        context.put("nextYear", nextYear);
		context.put("currYear", currYear);
		try (FileWriter outHtml = new FileWriter(outFileName)) {
			t.merge( context, outHtml );			
		} catch(Exception e) {
			log.error(e.getMessage(), e);
		}
	}
}
