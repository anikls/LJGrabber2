package com.grabber.ljgrabber.component;

import com.grabber.ljgrabber.entity.dto.PostDto;
import com.grabber.ljgrabber.entity.html.LinkPost;
import com.grabber.ljgrabber.utils.Conversion;
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
	
	private static VelocityEngine ve;
	
	private static VelocityEngine getVE() {
		if (ve == null) {
			ve = new VelocityEngine();
		    ve.init();
		} 
		return ve;
	}

	@SneakyThrows
	public String generateHtml(File outFileName, PostDto post) {
		
		String fileName = "template/html/post.vm";
	    Template t = getVE().getTemplate( fileName,"UTF-8");
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
	
	public void generateOneHtml(String template,
                                String outFileName,
                                LinkPost predYear,
								Integer currYear,
                                LinkPost nextYear,
                                List<PostDto> listPost) {
						
	    Template t = getVE().getTemplate( template,"UTF-8");
	    VelocityContext context = new VelocityContext();
	    context.put("title", "Все предсказания Немо");
	    context.put("header", "Все предсказания Немо");
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
