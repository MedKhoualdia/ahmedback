package com.dance.mo.Services;

import com.dance.mo.Entities.Comment;
import com.dance.mo.Entities.ForumPost;
import com.dance.mo.Entities.User;
import com.dance.mo.Repositories.ForumRepository;
import com.dance.mo.Repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;



@Service
@Transactional
@Slf4j
public class ForumService {
    @Autowired
    private ForumRepository forumRepository;

@Autowired
private UserRepository userRepository;

    BadWordImpl b = new BadWordImpl();


    @Value("${facebook.page.access.token}")
    private String facebookPageAccessToken;

    @Value("${facebook.page.id}")
    private String facebookPageId;

    public List<ForumPost> getallForumPost() {
        return forumRepository.findAll();

    }


    public ResponseEntity<String> addForumPost(ForumPost forumPost, Long idUser) {
        Optional<User> optionalUser = userRepository.findById(idUser);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            LocalDateTime currentTime = LocalDateTime.now();

            if (b.filterText(forumPost.getPostContent()) || b.filterText(forumPost.getTitle())) {
                int currentBanLevel = user.getBan() == null ? 0 : user.getBan();

                if (currentBanLevel == 0) {
                    user.setBan(currentBanLevel + 1);
                    userRepository.save(user);
                    return ResponseEntity.ok("Warning of bad words");

                } else {
                    switch (currentBanLevel) {
                        case 1:
                            user.setBanTime(currentTime.plusHours(1));
                            return ResponseEntity.ok("You have been banned for 1 hour");

                        default:
                            user.setBan(currentBanLevel + 1);
                            userRepository.save(user);
                            break;
                    }
                }
            } else {
                forumPost.setPostCreator(user);
                forumPost.setPostDate(new Date());
                forumRepository.save(forumPost);

                return ResponseEntity.ok("Post is added successfully!");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }
    public ResponseEntity<String> addForumPostToFacebook(String message) {
        String apiUrl = "https://graph.facebook.com/" + facebookPageId + "/feed";
        String urlParameters = "message=" + message + "&access_token=" + facebookPageAccessToken;

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForEntity(apiUrl, null, String.class, urlParameters);

        return ResponseEntity.ok("Post added to Facebook successfully!");
    }
    public ForumPost updateForumPost( ForumPost updatePost) {

            return forumRepository.save(updatePost);

    }

    public ForumPost getForumPostById(Long postId) {
        return forumRepository.findById(postId).orElse(null);
    }

    public void deleteForumPostById(Long postId) {
        ForumPost f=forumRepository.findById(postId).get();
        f.setPostCreator(null);
    for (Comment c:f.getComments()){
        c.setForumPost(null);
    }


        forumRepository.deleteById(postId);
    }

    public List<ForumPost> getForumPostsByUser(Long userId) {

        return forumRepository.findByPostCreatorUserId(userId);
    }

    public Long countCommentsForPost(Long postId) {
        Optional<ForumPost> optionalForumPost = forumRepository.findById(postId);
        if (optionalForumPost.isPresent()) {
            ForumPost post = optionalForumPost.get();
            return (long) post.getComments().size();
        } else {
            return null;
        }
    }
    public ForumPost savePostFile(int userId, MultipartFile file, ForumPost postDetails) {
        ForumPost post = new ForumPost();
        post.setTitle(postDetails.getTitle());
        post.setPostContent(postDetails.getPostContent());
        post.setPostDate(new Timestamp(new Date().getTime()));
        // Set other properties...

        return forumRepository.save(post);
    }


    public List<ForumPost> searchForumPosts(String keyword) {
        List<ForumPost> postsByTitle = forumRepository.findByTitleContainingIgnoreCase(keyword);

        if (!postsByTitle.isEmpty()) {
            return postsByTitle;
        } else {
            // If no posts are found by title, search by content
            return forumRepository.findByPostContentIgnoreCaseContaining(keyword);
        }
    }

    public ForumPost getNewestForumPost() {
        List<ForumPost> forumPosts = forumRepository.findAllByOrderByPostDateDesc();
        if (!forumPosts.isEmpty()) {
            return forumPosts.get(0); // Get the first element (newest post) from the list
        } else {
            return null; // Return null if no posts are found
        }
    }

    public ForumPost getNewestForumPostById() {
        ForumPost newestPost = getNewestForumPost(); // Get the newest post
        // You can implement additional logic here if needed, such as fetching the post by its ID
        return newestPost; // Return the newest post
    }

    private static final String UPLOAD_DIR = "uploads/";

    public String uploadImage(MultipartFile file) throws IOException {

        String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        Path uploadPath = Paths.get("C:\\Users\\SYRINE\\OneDrive\\Bureau\\projet\\DanceScapeExplorer_Front-end_Angular-master\\DanceScapeExplorer_Front-end_Angular-master\\DanceScapeExplorer\\src\\assets\\images").toAbsolutePath().normalize();
        Files.createDirectories(uploadPath);

        // Save the file to the 'uploads' directory
        Path filePath = uploadPath.resolve(uniqueFileName);
        Files.copy(file.getInputStream(), filePath);

        // Return the relative path of the uploaded file
        return  uniqueFileName;
    }
    public String getImageUrl(String imageName) {
        return UPLOAD_DIR + imageName;
    }

    @Scheduled(cron = "0 0 8 * * *")
    public void activateBannedAccounts(){
        LocalDateTime d = LocalDateTime.now();
        List<User> list = new ArrayList<>();
        userRepository.findAll().forEach(list::add);
        for (User u : list) {
            if (u.getBan() == 1){
                if (d.isAfter(u.getBanTime())) {
                    u.setBanTime(null);
                    u.setBan(0);
                    userRepository.save(u);
                }
        }
        }
    }


}
