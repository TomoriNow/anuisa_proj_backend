package Anuisa_website.prototype.model;
import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;

@Entity
@Table(name = "Profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Profile {
    @Id
    private String uniId;
    
    @Column(nullable = false)
    private Instant timestamp;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String fullName;
    
    private String nickname;
    private String degree;
    private String college;
    private String school;
    private String program;
    private String intake;
    private String otherDegrees;
    private String researchProject;
    private String linkedIn;
    private String instagram;
    private String portfolio;
    private String expertise;
    private String careerInterests;
    private String profilePicture;
    private String bio;
}