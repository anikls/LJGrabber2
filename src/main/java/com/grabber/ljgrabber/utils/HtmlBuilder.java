package com.grabber.ljgrabber.utils;

import com.grabber.ljgrabber.lj.entity.LJPost;
import com.grabber.ljgrabber.lj.entity.LinkPost;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.FileWriter;
import java.util.List;

@Slf4j
public class HtmlBuilder {
	
	private static VelocityEngine ve;
	
	private static VelocityEngine getVE() {
		if (ve == null) {
			ve = new VelocityEngine();
		    ve.init();
		} 
		return ve;
	}
	
	public static void generateHtml(String outFileName, LJPost post){
		
		String fileName = "post.vm";		
	    Template t = getVE().getTemplate( fileName,"UTF-8");
	    VelocityContext context = new VelocityContext();
	    context.put("title", StringUtils.isBlank(post.getSubject())?post.getEventtime():post.getSubject());
	    context.put("header", StringUtils.isBlank(post.getSubject())?post.getEventtime():post.getSubject());

	    context.put("post", post);	          

		try (FileWriter outHtml = new FileWriter(outFileName)){
			t.merge( context, outHtml );			
		} catch(Exception e){
			log.error(e.getMessage(), e);
		}
	}
	
	public static void generateOneHtml(String template,
                                       String outFileName,
                                       LinkPost predYear,
                                       LinkPost nextYear,
                                       List<LJPost> listPost){
						
	    Template t = getVE().getTemplate( template,"UTF-8");
	    VelocityContext context = new VelocityContext();
	    context.put("title", "Все сны Немо");
	    context.put("header", "Все сны Немо");
	    context.put("postList", listPost);
		context.put("predYear", predYear);
        context.put("nextYear", nextYear);
		try (FileWriter outHtml = new FileWriter(outFileName)) {
			t.merge( context, outHtml );			
		}catch(Exception e){
			log.error(e.getMessage(), e);
		}
	}
}
