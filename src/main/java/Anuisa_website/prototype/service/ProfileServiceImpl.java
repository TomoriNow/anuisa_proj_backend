package Anuisa_website.prototype.service;

import Anuisa_website.prototype.model.Profile;
import Anuisa_website.prototype.repository.ProfileRepository;
import Anuisa_website.prototype.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    private ProfileRepository profileRepository;
    
    @Value("${upload.path:src/main/resources/static/uploads/}")
    private String uploadPath;

    @Override
    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    @Override
    public List<Profile> getAllActiveProfiles() {
        return profileRepository.findByActiveTrue();
    }

    @Override
    public Optional<Profile> getProfileById(String uniId) {
        return profileRepository.findById(uniId);
    }

    @Override
    public Profile getProfileByEmail(String email) {
        return profileRepository.findByEmail(email);
    }

    @Override
    public Profile saveProfile(Profile profile) {
        // Set creation timestamp if it's null
        if (profile.getTimestamp() == null) {
            profile.setTimestamp(Instant.now());
        }
        
        // Generate university ID if it's null or empty
        if (profile.getUniId() == null || profile.getUniId().isEmpty()) {
            profile.setUniId(UUID.randomUUID().toString());
        }
        
        return profileRepository.save(profile);
    }

    @Override
    public Profile updateProfile(Profile profile) {
        // Find existing profile
        Optional<Profile> existingProfile = profileRepository.findById(profile.getUniId());
        
        if (existingProfile.isPresent()) {
            // Preserve creation timestamp
            profile.setTimestamp(existingProfile.get().getTimestamp());
            
            // If profile picture is null in the update, preserve existing one
            if (profile.getProfilePicture() == null || profile.getProfilePicture().isEmpty()) {
                profile.setProfilePicture(existingProfile.get().getProfilePicture());
            }
        }
        
        return profileRepository.save(profile);
    }

    @Override
    public void deleteProfile(String uniId) {
        profileRepository.deleteById(uniId);
    }

    @Override
    public String uploadProfileImage(MultipartFile file, String uniId) {
        try {
            // Create upload directory if it doesn't exist
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }
            
            // Generate unique filename
            String filename = uniId + "_" + file.getOriginalFilename();
            Path filePath = uploadDir.resolve(filename);
            
            // Save the file
            Files.copy(file.getInputStream(), filePath);
            
            // Return the relative path for storing in the database
            return "/uploads/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage());
        }
    }

    @Override
    public List<Profile> searchProfiles(String keyword) {
        return profileRepository.searchProfiles(keyword);
    }

    @Override
    public List<Profile> getProfilesByCollege(String college) {
        return profileRepository.findByCollegeContainingIgnoreCase(college);
    }

    @Override
    public List<Profile> getProfilesBySchool(String school) {
        return profileRepository.findBySchoolContainingIgnoreCase(school);
    }

    @Override
    public List<Profile> getProfilesByProgram(String program) {
        return profileRepository.findByProgramContainingIgnoreCase(program);
    }

    @Override
    public List<Profile> getProfilesByIntake(String intake) {
        return profileRepository.findByIntakeContainingIgnoreCase(intake);
    }

    @Override
    public List<Profile> getProfilesByExpertise(String expertise) {
        return profileRepository.findByExpertiseContainingIgnoreCase(expertise);
    }

    @Override
    public List<Profile> getProfilesByCareerInterests(String careerInterests) {
        return profileRepository.findByCareerInterestsContainingIgnoreCase(careerInterests);
    }
}