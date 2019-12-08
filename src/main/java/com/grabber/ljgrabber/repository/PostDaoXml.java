package com.grabber.ljgrabber.repository;

import com.grabber.ljgrabber.config.ApplicationProperties;
import com.grabber.ljgrabber.entity.ListPost;
import com.grabber.ljgrabber.entity.Post;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Slf4j
public class PostDaoXml implements PostRepository {
	private static final String FILE_NAME = "posts.xml";
	private String fileName;
	private List<Post> cache = new ArrayList<>();
	private JAXBContext jaxbContext;
	private Unmarshaller unmarshaller;
	private Marshaller marshaller;
	
	public PostDaoXml(ApplicationProperties properties) {

		log.info(properties.toString());
		this.fileName = properties.getOutPath()+FILE_NAME;
		
		try {
			jaxbContext = JAXBContext.newInstance(ListPost.class);
			unmarshaller = jaxbContext.createUnmarshaller();			
			marshaller = jaxbContext.createMarshaller();		
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);			
			
			File file = new File(fileName);
			if (file.exists()) {
				ListPost listPost;
				Reader reader = new FileReader(fileName);			
				listPost = (ListPost) unmarshaller.unmarshal(reader);			
				for (Post post:listPost.getPosts()){
					cache.add(post);
				}
			} else {
				file.createNewFile();
			}	
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		
	}

	private Optional<Post> getPostById(Long id) {
		return cache.stream()
				.filter(post -> post.getItemid() == id)
				.findAny();
	}

	private boolean existIn(Long id) {
		return cache.stream()
				.anyMatch(post -> post.getItemid() == id);
	}

//	public Post save(Post item) {
//		if (cache.containsKey(item.getItemid()))
//			throw new RuntimeException("Запись с id="+item.getItemid()+" уже существует в файле");
//		flash();
//		return cache.get(item.getItemid());
//	}
	
	public void saveNew(List<Post> items) {

		for (Post post :  items) {
			if (existIn(post.getItemid())) {
				log.warn("Запись с id="+post.getItemid()+" уже существует в базе");
			} else {
				cache.add(post);
				log.info("Запись с id="+post.getItemid()+" добавлена");
			}
			flash();
		}
	}

	public Optional<Post> lastPost() {
		return cache.size() == 0
				? Optional.empty()
				: Optional.ofNullable(cache)
					.map(cache -> cache.get(cache.size()-1));
	}

	public Optional<Post> firstPost() {
		return cache.size() == 0
				? Optional.empty()
				: Optional.ofNullable(cache)
				.map(cache -> cache.get(0));
	}

	public Optional<Post> read(long id) {
		return getPostById(id);
	}
	
	public List<Post> readAll() {
		return cache;
	}

	public List<Post> readAllByYear(String year) {
		return cache.stream()
				.filter(post -> post.getEventtime().startsWith(year))
				.collect(Collectors.toList());
	}

	public void update(Post item) {
		int indx = cache.indexOf(item);
		if (indx > 0) {
			cache.add(indx, item);
			flash();
		}
	}

	public void erase(long id) {
		getPostById(id).ifPresent(
			post -> {
				cache.remove(post);
				flash();
			}
		);
	}

	private void flash() {
		if (!cache.isEmpty()) {
			ListPost postsXml = new ListPost();
			postsXml.setPosts(cache);
			try (FileWriter writer = new FileWriter(fileName)){
				marshaller.marshal(postsXml, writer);
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}		
	}
}
