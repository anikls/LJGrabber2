package com.grabber.ljgrabber.service.impl;

import com.grabber.ljgrabber.config.ApplicationProperties;
import com.grabber.ljgrabber.entity.lj.LJPost;
import com.grabber.ljgrabber.service.LJClient;
import com.grabber.ljgrabber.service.PostService;
import com.grabber.ljgrabber.utils.Conversion;
import com.grabber.ljgrabber.utils.DateUtils;
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
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LJClientImpl implements LJClient {

    private static final String URL = "http://www.livejournal.com/interface/xmlrpc";
    private static final RestTemplate restTemplate = new RestTemplate();

    private final PostService postService;
    private final ApplicationProperties applicationProperties;


    private VelocityEngine ve;

    @PostConstruct
    void init() {
        ve = new VelocityEngine();
        ve.init();
    }

    private List<LJPost> loadByReq(String reqXml, String author) {

        List<LJPost> listPost = new ArrayList();

        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(URL, reqXml, String.class);
            String response = responseEntity.getBody();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;

            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(response)));
            XPathFactory xPathfactory = XPathFactory.newInstance();
            XPath xpath = xPathfactory.newXPath();
            XPathExpression expr = xpath.compile("/methodResponse/params/param/value/struct/member/value/array/data/value");
            NodeList nl = (NodeList) expr.evaluate(document, XPathConstants.NODESET);

            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                NodeList attrlist = ((Element) node).getElementsByTagName("member");
                Map<String, String> map = new HashMap<>();
                for (int j = 0; j < attrlist.getLength(); j++) {
                    Node attr = attrlist.item(j);
                    NodeList vl = attr.getChildNodes();
                    String key = null;
                    String value = null;
                    for (int k = 0; k < vl.getLength(); k++) {
                        Node val = vl.item(k);
                        if (val.getNodeName().equals("name"))
                            key = val.getTextContent();
                        if (val.getNodeName().equals("value")) {
                            value = val.getTextContent();
                            if (Conversion.isBase64(value)) {
                                value = Conversion.base64ToString(value, StandardCharsets.UTF_8.name());

                            }
                        }
                    }
                    if (StringUtils.isNotBlank(key) && StringUtils.isNotBlank(value)) map.put(key, value);
                }
                map.put("author", author);
                if (!map.isEmpty() && map.containsKey("itemid")) {
                    LJPost p = generatePost(map);
                    log.info("loaded post {}", p.getItemid());
                    if (p != null) listPost.add(p);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return listPost;
    }

    private LJPost generatePost(Map<String, String> data) {
		LJPost.LJPostBuilder postBuilder = LJPost.builder();
		Optional.ofNullable(data.get("itemid"))
				.ifPresent(item -> postBuilder.itemid(Long.parseLong(item)));
		Optional.ofNullable(data.get("anum"))
				.ifPresent(item -> postBuilder.anum(item));
		Optional.ofNullable(data.get("eventtime"))
				.ifPresent(item -> postBuilder.eventtime(item));
		Optional.ofNullable(data.get("subject"))
				.ifPresent(item -> postBuilder.subject(item));
		Optional.ofNullable(data.get("url"))
				.ifPresent(item -> postBuilder.url(item));
		Optional.ofNullable(data.get("can_comment"))
				.ifPresent(item -> postBuilder.canComment(item));
		Optional.ofNullable(data.get("logtime"))
				.ifPresent(item -> postBuilder.logtime(item));
		Optional.ofNullable(data.get("event_timestamp"))
				.ifPresent(item -> postBuilder.eventTimestamp(item));
		Optional.ofNullable(data.get("lastsync"))
				.ifPresent(item -> postBuilder.lastsync(item));
		Optional.ofNullable(data.get("ditemid"))
				.ifPresent(item -> postBuilder.ditemid(item));
		Optional.ofNullable(data.get("event"))
				.ifPresent(item -> postBuilder.event(item));
		Optional.ofNullable(data.get("replyCount"))
				.ifPresent(item -> postBuilder.replyCount(item));
		Optional.ofNullable(data.get("author"))
				.ifPresent(item -> postBuilder.author(item));
		return postBuilder.build();
    }

    @Override
    public List<LJPost> loadFromLJ(String author, LocalDate date) {

        String fileName = "template/lj/getevents.vm";
        Template t = ve.getTemplate(fileName, StandardCharsets.UTF_8.name());
        VelocityContext context = new VelocityContext();
        context.put("journal", author);
        context.put("year", date.getYear());
        context.put("month", date.getMonthValue());
        context.put("day", date.getDayOfMonth());
        StringWriter reqXml = new StringWriter();
        t.merge(context, reqXml);

        return loadByReq(reqXml.toString(), author);
    }

    @Override
    public List<LJPost> downloadPosts(String author, int year) {
        List<LJPost> postList = new ArrayList<>();

        // Первый день года
        LocalDate startDate = LocalDate.of(year, 1, 1);
        // Последний день года
        LocalDate endYearDate = LocalDate.of(year, 12, 31);

        DateUtils.getDatesBetween(startDate, endYearDate)
                .forEach(checkedDate -> {
                    log.info("Проверяемая дата {}", checkedDate.toString());
                    postList.addAll(this.loadFromLJ(author, checkedDate));
                });
        return postList;
    }

    @Override
    public List<LJPost> downloadNewPosts(String author, LocalDate startDate) {
        List<LJPost> postList = new ArrayList<>();

        // Первый день года
        if (startDate == null) {
            startDate = postService.getLastPost(author)
                    .map(post -> post.getEventTime().toLocalDate())
                    .orElse(applicationProperties.getStartDate());
        }

        // Текущая дата
        LocalDate endDate = LocalDate.now();

        DateUtils.getDatesBetween(startDate, endDate)
                .forEach(checkedDate -> {
                    log.info("Проверяемая дата {}", checkedDate.toString());
                    postList.addAll(this.loadFromLJ(author, checkedDate));
                });
        return postList;
    }
}
