package edu.example.springbootblog.service;

import edu.example.springbootblog.domain.Article;
import edu.example.springbootblog.dto.apidto.AddArticleRequest;
import edu.example.springbootblog.dto.apidto.UpdateArticleRequest;
import edu.example.springbootblog.repository.BlogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor // add constructor what having final, @Notnull
@Service // make the bean
public class BlogService {
    private final BlogRepository blogRepository;

    public Article save(AddArticleRequest request){
        return blogRepository.save(request.toEntity());
    }

    // Read ALL
    public List<Article> findAll(){
        return blogRepository.findAll();
        //jpa 지원 메서드 파인드올 호출하여, 아티클 테이블에 저장되어 있는 모든 데이터를 조회
    }

    // Read 1
    public Article findById(Long id){ // Long & long 둘 다 가능
        return blogRepository.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("Not Found : " + id));
    }

    public void delete(Long id){
        blogRepository.deleteById(id);
        // 오늘 진행한 스프링과 다르게 모든 메소드가 JPA에 구현되어 있고 (우리는 그 인터페이스를 상속받은 상태)
    }

    @Transactional // 트랜잭션용 메서드
    public Article update(long id, UpdateArticleRequest request){ // 설마 이거 하나?
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Not Found : " + id));

        article.update(request.getTitle(), request.getContent());
        return article;
    }
}
