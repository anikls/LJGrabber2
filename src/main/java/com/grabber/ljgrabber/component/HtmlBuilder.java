package com.grabber.ljgrabber.component;

import com.grabber.ljgrabber.entity.dto.PostDto;
import com.grabber.ljgrabber.entity.html.LinkPost;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.util.List;

/**
 * Компонет для геерации html страниц по шаблону.
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
	
	public void generateHtml(String outFileName, PostDto post){
		
		String fileName = "template/html/post.vm";
	    Template t = getVE().getTemplate( fileName,"UTF-8");
	    VelocityContext context = new VelocityContext();
	    context.put("title",
				StringUtils.isBlank(post.getSubject()) ? post.getEventTime() : post.getSubject());
	    context.put("header",
				StringUtils.isBlank(post.getSubject()) ? post.getEventTime() : post.getSubject());

	    context.put("post", post);	          

		try (FileWriter outHtml = new FileWriter(outFileName)){
			t.merge( context, outHtml );			
		} catch(Exception e){
			log.error(e.getMessage(), e);
		}
	}
	
	public void generateOneHtml(String template,
                                       String outFileName,
                                       LinkPost predYear,
									   Integer currYear,
                                       LinkPost nextYear,
                                       List<PostDto> listPost){
						
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
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
	}
}
