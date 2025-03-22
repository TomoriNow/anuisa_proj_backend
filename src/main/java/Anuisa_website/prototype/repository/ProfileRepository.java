package Anuisa_website.prototype.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import Anuisa_website.prototype.model.Profile;
import java.util.List;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {
    // Find by email (already in your code)
    Profile findByEmail(String email);
    
    // Find all active profiles
    List<Profile> findByActiveTrue();
    
    // Find by university ID
    Profile findByUniId(String uniId);
    
    // Find by college
    List<Profile> findByCollegeContainingIgnoreCase(String college);
    
    // Find by school
    List<Profile> findBySchoolContainingIgnoreCase(String school);
    
    // Find by program
    List<Profile> findByProgramContainingIgnoreCase(String program);
    
    // Find by intake
    List<Profile> findByIntakeContainingIgnoreCase(String intake);
    
    // Find by expertise
    List<Profile> findByExpertiseContainingIgnoreCase(String expertise);
    
    // Find by career interests
    List<Profile> findByCareerInterestsContainingIgnoreCase(String careerInterests);
    
    // Custom query to search across multiple fields
    @Query("SELECT p FROM Profile p WHERE " +
           "p.active = true AND " +
           "(LOWER(p.fullName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.nickname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.degree) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.program) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.expertise) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.college) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(p.school) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Profile> searchProfiles(String keyword);
}
