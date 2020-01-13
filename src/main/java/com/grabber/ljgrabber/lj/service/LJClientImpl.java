package com.grabber.ljgrabber.lj.service;

import com.grabber.ljgrabber.lj.entity.Author;
import com.grabber.ljgrabber.lj.entity.LJPost;
import com.grabber.ljgrabber.utils.Conversion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LJClientImpl implements LJClient {
	
	private static final String URL = "http://www.livejournal.com/interface/xmlrpc";
    private static final RestTemplate restTemplate = new RestTemplate();

	private VelocityEngine ve;

	@PostConstruct
	void init() {
		ve = new VelocityEngine();
		ve.init();
	}

	private List<LJPost> loadByReq(String reqXml, Author autor){

		List<LJPost> listPost = new ArrayList();
				
		try  {
		
			//String response = http.sendPOST(URL , reqXml.toString(), Proxy.NO_PROXY);
			ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL, reqXml, String.class);
			String response = responseEntity.getBody();
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();  
			DocumentBuilder builder;  
	    
	        builder = factory.newDocumentBuilder();  
	        Document document = builder.parse( new InputSource( new StringReader( response ) ) ); 
	        XPathFactory xPathfactory = XPathFactory.newInstance();
	        XPath xpath = xPathfactory.newXPath();
	        XPathExpression expr = xpath.compile("/methodResponse/params/param/value/struct/member/value/array/data/value");
	        NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);
	         
	        for (int i=0; i<nl.getLength(); i++){
	        	Node node = nl.item(i);	       
	        	NodeList attrlist = ((Element)node).getElementsByTagName("member");
	        	Map<String,String> map=new HashMap<>();
	        	for (int j=0;j<attrlist.getLength();j++){	        		
	        		Node attr = attrlist.item(j);
	        		NodeList vl = attr.getChildNodes();
	        		String key = null; String value = null;
	        		for (int k=0;k<vl.getLength();k++){
	        			Node val = vl.item(k);	        			
	        			if (val.getNodeName().equals("name"))
	        				key = val.getTextContent();
	        			if (val.getNodeName().equals("value")){
	        				value = val.getTextContent();
	        				if (Conversion.isBase64(value)){
				        		value = Conversion.base64ToString(value, "UTF-8");
				        		
				        	}
			        		if (key.equals("event")){
			        			value = value.replaceAll("\r\n","<br/>");
			        		}
	        			}	        			
	        		}
	        		if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value))	map.put(key, value);
	        	}	        	
	        	map.put("autor", String.valueOf(autor.getId()));
	    		if (!map.isEmpty() && map.containsKey("itemid")){							
	    			LJPost p = generatePost(map);
	    			if (p!=null) listPost.add(p);
	    		}
	        }
	    } catch (Exception e) {  
	    	e.printStackTrace();  
	    } 
	    
	    return listPost;
	}

	private LJPost generatePost(Map<String,String> data) {
//		LJPost.PostBuilder postBuilder = LJPost.builder();
//		Optional.ofNullable(data.get("itemid"))
//				.ifPresent(item -> postBuilder.itemid(Long.parseLong(item)));
//		Optional.ofNullable(data.get("anum"))
//				.ifPresent(item -> postBuilder.anum(item));
//		Optional.ofNullable(data.get("eventtime"))
//				.ifPresent(item -> postBuilder.eventtime(item));
//		Optional.ofNullable(data.get("subject"))
//				.ifPresent(item -> postBuilder.subject(item));
//		Optional.ofNullable(data.get("url"))
//				.ifPresent(item -> postBuilder.url(item));
//		Optional.ofNullable(data.get("can_comment"))
//				.ifPresent(item -> postBuilder.can_comment(item));
//		Optional.ofNullable(data.get("logtime"))
//				.ifPresent(item -> postBuilder.logtime(item));
//		Optional.ofNullable(data.get("event_timestamp"))
//				.ifPresent(item -> postBuilder.event_timestamp(item));
//		Optional.ofNullable(data.get("lastsync"))
//				.ifPresent(item -> postBuilder.lastsync(item));
//		Optional.ofNullable(data.get("ditemid"))
//				.ifPresent(item -> postBuilder.ditemid(item));
//		Optional.ofNullable(data.get("event"))
//				.ifPresent(item -> postBuilder.event(item));
//		Optional.ofNullable(data.get("reply_count"))
//				.ifPresent(item -> postBuilder.reply_count(item));
//		Optional.ofNullable(data.get("autor"))
//				.ifPresent(item -> {
//					postBuilder.autorId(Long.parseLong(data.get("autor")));
//				});
//		return postBuilder.build();
		return new LJPost();
	}

	@Override
	public List<LJPost> loadFromLJ(Author autor, String lastSync) {
		Template t = ve.getTemplate( "getevents2.vm" ,"UTF-8");
	    VelocityContext context = new VelocityContext();
	    context.put("journal", autor.getName());
	    context.put("lastsync", lastSync);	    
	    StringWriter reqXml = new StringWriter();
		t.merge( context, reqXml );	
			
		return loadByReq(reqXml.toString(), autor);
	}
	
	@Override
	public List<LJPost> loadFromLJ(Author author, int year, int month, int day) {
		
		String fileName = "getevents.vm";		
	    Template t = ve.getTemplate( fileName, "UTF-8");
	    VelocityContext context = new VelocityContext();
	    context.put("journal", author.getName());
	    context.put("year", year);
	    context.put("month", month);
	    context.put("day", day);
	    StringWriter reqXml = new StringWriter();
		t.merge( context, reqXml );		
		
		return loadByReq(reqXml.toString(), author);
	}

	@Override
    public List<LJPost> downloadPosts(Author author, int year) {
		List<LJPost> postList = new ArrayList<>();
		LocalDate checkedDate = LocalDate.of(year, 1, 1);
		LocalDate endYearDate = LocalDate.of(year, 12, 31);
		while (checkedDate.isBefore(endYearDate)) {
			log.info("Проверяемая дата {}", checkedDate.toString());
			List<LJPost> newPosts = this.loadFromLJ(author,
					checkedDate.getYear(), checkedDate.getMonthValue(), checkedDate.getDayOfMonth());
			postList.addAll(newPosts);
			checkedDate = checkedDate.plusDays(1);
			if (checkedDate.getMonthValue() == 1) {
				checkedDate = checkedDate.plusMonths(1);
				if (checkedDate.getMonthValue() == 1) {
					checkedDate = checkedDate.plusYears(1);
				}
			}
		}
		return postList;
	}
}
