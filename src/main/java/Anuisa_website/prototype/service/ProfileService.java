package Anuisa_website.prototype.service;
import Anuisa_website.prototype.model.Profile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ProfileService {
    // Basic CRUD operations
    List<Profile> getAllProfiles();
    List<Profile> getAllActiveProfiles();
    Optional<Profile> getProfileById(String uniId);
    Profile getProfileByEmail(String email);
    Profile saveProfile(Profile profile);
    Profile updateProfile(Profile profile);
    void deleteProfile(String uniId);
    
    // File upload handling
    String uploadProfileImage(MultipartFile file, String uniId);
    
    // Searching and filtering
    List<Profile> searchProfiles(String keyword);
    List<Profile> getProfilesByCollege(String college);
    List<Profile> getProfilesBySchool(String school);
    List<Profile> getProfilesByProgram(String program);
    List<Profile> getProfilesByIntake(String intake);
    List<Profile> getProfilesByExpertise(String expertise);
    List<Profile> getProfilesByCareerInterests(String careerInterests);
}