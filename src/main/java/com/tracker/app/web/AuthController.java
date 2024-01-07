package com.tracker.app.web;


import java.util.List;
import java.util.Map;
import com.tracker.app.entities.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import com.tracker.app.dao.BugRepository;
import com.tracker.app.dao.CategoryRepository;
import com.tracker.app.dao.PriorityRepository;
import com.tracker.app.dao.ProjectRepository;
import com.tracker.app.dao.RoleRepository;
import com.tracker.app.dao.SeverityRepository;
import com.tracker.app.dao.UserRepository;
import com.tracker.app.entities.User;
import com.tracker.app.service.BugService;
import com.tracker.app.service.CategoryService;
import com.tracker.app.service.NotificationService;
import com.tracker.app.service.PriorityService;
import com.tracker.app.service.ProjectService;
import com.tracker.app.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class AuthController {
   private UserService userService;
   private BugService bugService;
   private CategoryService categoryService;
   private PriorityService priorityService;
   private NotificationService notificationService;
   private ProjectService projectService;
	 
   public  AuthController() {}
	@Autowired
	public AuthController(UserRepository userRepository,RoleRepository roleRepository,BugRepository bugRepository
			,CategoryRepository categoryRepository,PriorityRepository priorityRepository,SeverityRepository severityRepository
			,ProjectRepository projectRepository) {
		this.userService=new UserService(userRepository,roleRepository);
		this.bugService=new BugService(bugRepository, priorityRepository, severityRepository, categoryRepository);
		this.categoryService=new CategoryService(categoryRepository);
		this.priorityService=new PriorityService(priorityRepository);
		this.projectService=new ProjectService(projectRepository);
		
	}
	
	
	@RequestMapping(value = "/login")
	public String login() {
		return "login";
	}
	@GetMapping("/logout")
	public RedirectView logout(RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("message", "You are now Logged out.");
		//session.invalidate();
		return new RedirectView("login");
	}
	
	@GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }
	@PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult result) {
        /*if (result.hasErrors()) {
            return "registration";
        }*/
		user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        userService.save(user);
        return "redirect:/login?registrationSuccess";
    }

	@RequestMapping("/Admin/Dashboard")
	public String AdminDashboard(Model model) {
		//total Bug Count
		Long BugCount=bugService.Count();
		model.addAttribute("allbugs",BugCount);
		//Bug Count By Status(active,InProgress,closed)
		Long Active=bugService.CountByStatus("active");
		Long InProgress=bugService.CountByStatus("InProgress");
		Long Resolved=bugService.CountByStatus("InProgress");
		Long Closed=bugService.CountByStatus("closed");   
        model.addAttribute("Active",Active);
        model.addAttribute("InProgress",InProgress);
        model.addAttribute("Resolved",Resolved);
        model.addAttribute("Closed",Closed);
        //Bug Count By Priority        
        Map<String, Long> BugCountPerPriority=bugService.BugCountByPriority();
        model.addAttribute("BugCountPerPriority",BugCountPerPriority);
        //Bug Count By Severity
        Map<String, Long> BugCountPerSeverity=bugService.BugCountBySeverity();
        model.addAttribute("BugCountPerSeverity",BugCountPerSeverity);
        //Bug Count By Category
        Map<String, Long> BugCountPerCategory=bugService.BugCountByCategory();
        model.addAttribute("BugCountPerCategory",BugCountPerCategory);
        System.out.println("priority"+BugCountPerPriority);
        System.out.println("category"+BugCountPerCategory);
        System.out.println("severity"+BugCountPerSeverity);
        //Total Projects
        Long ProjectCount=projectService.Count();
        model.addAttribute("ProjectCount",ProjectCount);
        //number of Active Projects
        Long ActiveProjectCount=projectService.CountByStatus("active");
        model.addAttribute("ActiveProjects",ActiveProjectCount);
        //projects bug count in descending order(most to least)
        List<Project> sortedprojects=projectService.ProjectWithMostBugs();
        model.addAttribute("sortedProjects", sortedprojects);
        
        
        
		return "Tester/index";
		
	}
	
	@RequestMapping("/Manager/Dashboard")
	public String ManagerDashboard() {
	 //notificationService.sendtouser();
		return "Manager/Dashboard";
	}
	
	
	@RequestMapping("/Developer/Dashboard")
	public String DeveloperDashboard() {
		return "Developer/Dashboard";
	}
	
	
	@RequestMapping("/Tester/Dashboard")
	public String TesterDashboard() {
		return "Tester/Dashboard";
	}
	
	
	@RequestMapping("/Support/Dashboard")
	public String SupportDashboard() {
		return "CustomerSupport/Dashboard";
	}
	
	@RequestMapping("Developer/username")
	public String getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();
		System.out.println(currentUsername);
		return currentUsername;
	}
	 
}
