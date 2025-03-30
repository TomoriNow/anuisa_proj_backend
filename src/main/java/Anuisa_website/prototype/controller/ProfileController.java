package Anuisa_website.prototype.controller;

import Anuisa_website.prototype.model.Profile;
import Anuisa_website.prototype.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;
    
    // Display all active profiles
    @GetMapping
    public ResponseEntity<List<Profile>> getAllProfiles(Model model) {
        List<Profile> profiles = profileService.getAllProfiles();
        return ResponseEntity.ok(profiles);
    }

    // Search profiles
    @GetMapping("/search")
    public String searchProfiles(@RequestParam String keyword, Model model) {
        List<Profile> profiles = profileService.searchProfiles(keyword);
        model.addAttribute("profiles", profiles);
        model.addAttribute("keyword", keyword);
        return "profiles/index";
    }
    
    // Show profile details
    @GetMapping("/{uniId}")
    public String getProfile(@PathVariable String uniId, Model model) {
        Optional<Profile> profileOpt = profileService.getProfileById(uniId);
        
        if (profileOpt.isPresent()) {
            model.addAttribute("profile", profileOpt.get());
            return "profiles/details";
        } else {
            return "redirect:/profiles";
        }
    }
    
    // Show profile creation form
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("profile", new Profile());
        return "profiles/create";
    }
    
    // Process profile creation
    @PostMapping("/new")
    public String createProfile(@ModelAttribute Profile profile,
                               @RequestParam("imageFile") MultipartFile file,
                               RedirectAttributes redirectAttributes) {
        
        // Set timestamp to now
        profile.setTimestamp(Instant.now());
        
        // Handle file upload if provided
        if (!file.isEmpty()) {
            try {
                String imagePath = profileService.uploadProfileImage(file, profile.getUniId());
                profile.setProfilePicture(imagePath);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
                return "redirect:/profiles/new";
            }
        }
        
        // Save the profile
        profileService.saveProfile(profile);
        redirectAttributes.addFlashAttribute("success", "Profile created successfully!");
        return "redirect:/profiles";
    }
    
    // Show profile edit form
    @GetMapping("/{uniId}/edit")
    public String showEditForm(@PathVariable String uniId, Model model) {
        Optional<Profile> profileOpt = profileService.getProfileById(uniId);
        
        if (profileOpt.isPresent()) {
            model.addAttribute("profile", profileOpt.get());
            return "profiles/edit";
        } else {
            return "redirect:/profiles";
        }
    }
    
    // Process profile update
    @PostMapping("/{uniId}/edit")
    public String updateProfile(@PathVariable String uniId, 
                               @ModelAttribute Profile profile,
                               @RequestParam("imageFile") MultipartFile file,
                               RedirectAttributes redirectAttributes) {
        
        // Ensure ID matches path variable
        profile.setUniId(uniId);
        
        // Handle file upload if provided
        if (!file.isEmpty()) {
            try {
                String imagePath = profileService.uploadProfileImage(file, profile.getUniId());
                profile.setProfilePicture(imagePath);
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
                return "redirect:/profiles/" + uniId + "/edit";
            }
        }
        
        // Save the updated profile
        profileService.updateProfile(profile);
        redirectAttributes.addFlashAttribute("success", "Profile updated successfully!");
        return "redirect:/profiles/" + uniId;
    }
    
    // Delete profile
    @GetMapping("/{uniId}/delete")
    public String deleteProfile(@PathVariable String uniId, RedirectAttributes redirectAttributes) {
        profileService.deleteProfile(uniId);
        redirectAttributes.addFlashAttribute("success", "Profile deleted successfully!");
        return "redirect:/profiles";
    }
    
    // Filter by college
    @GetMapping("/filter/college")
    public String filterByCollege(@RequestParam String college, Model model) {
        List<Profile> profiles = profileService.getProfilesByCollege(college);
        model.addAttribute("profiles", profiles);
        model.addAttribute("filter", "College: " + college);
        return "profiles/index";
    }
    
    // Filter by school
    @GetMapping("/filter/school")
    public String filterBySchool(@RequestParam String school, Model model) {
        List<Profile> profiles = profileService.getProfilesBySchool(school);
        model.addAttribute("profiles", profiles);
        model.addAttribute("filter", "School: " + school);
        return "profiles/index";
    }
    
    // Filter by program
    @GetMapping("/filter/program")
    public String filterByProgram(@RequestParam String program, Model model) {
        List<Profile> profiles = profileService.getProfilesByProgram(program);
        model.addAttribute("profiles", profiles);
        model.addAttribute("filter", "Program: " + program);
        return "profiles/index";
    }
    
    // Filter by expertise
    @GetMapping("/filter/expertise")
    public String filterByExpertise(@RequestParam String expertise, Model model) {
        List<Profile> profiles = profileService.getProfilesByExpertise(expertise);
        model.addAttribute("profiles", profiles);
        model.addAttribute("filter", "Expertise: " + expertise);
        return "profiles/index";
    }
}