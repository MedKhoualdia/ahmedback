package com.dance.mo.Entities;


import com.dance.mo.Config.GrantedAuthorityDeserializer;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NonNull
    private String firstName;
    @NonNull
    private String lastName;

    private Date birthday;
    @NonNull
    @Email
    private String email;
    @NonNull
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;


    private boolean enabled;
    private long resetToken;
    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private  byte[] profileImage;
    @Transient
    private static byte[] defaultProfileImage;
    private Integer phoneNumber;

    Integer ban=0;
    LocalDateTime banTime;
    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_friends",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<User> friends;
    ///////REL
    @JsonIgnore
    @OneToMany(mappedBy = "postCreator")
    private List<ForumPost> forumPosts;


    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<SousComment> sousComments;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "user")
    List<React>reacts;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "creator")
    private List<ChatRoom> chatRooms;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "dsUsers")
    private  List<DanceSchool> danceSchools;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "buyer")
    private Ticket ticket;

    @OneToMany (mappedBy = "competitor",cascade = CascadeType.ALL)
    private List<Competition> competitions;

    @ManyToMany(cascade = CascadeType.ALL , mappedBy = "resUsers")
    private List<Result> results;
    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Reclamation> reclamations;

////////
///return list of roles

    ///get all the authoroties from roles


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
    static {
        try {
            defaultProfileImage = StreamUtils.copyToByteArray(
                    Objects.requireNonNull(User.class.getClassLoader().getResourceAsStream("mohamedimage/profileImage.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @PrePersist
    public void prePersist() {
        if (profileImage == null || profileImage.length == 0) {
            profileImage = defaultProfileImage;
        }
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return userId != null && userId.equals(user.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
