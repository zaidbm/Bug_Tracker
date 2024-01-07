package com.tracker.app.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.tracker.app.dao.AttachmentRepository;
import com.tracker.app.dao.BugRepository;
import com.tracker.app.dao.CategoryRepository;
import com.tracker.app.dao.CommentRepository;
import com.tracker.app.dao.CustomerSupportRepository;
import com.tracker.app.dao.DeveloperRepository;
import com.tracker.app.dao.EmployeRepository;
import com.tracker.app.dao.ManagerRepository;
import com.tracker.app.dao.PriorityRepository;
import com.tracker.app.dao.ProjectRepository;
import com.tracker.app.dao.SeverityRepository;
import com.tracker.app.dao.TagRepository;
import com.tracker.app.dao.TesterRepository;
import com.tracker.app.entities.Attachment;
import com.tracker.app.entities.Bug;
import com.tracker.app.entities.Category;
import com.tracker.app.entities.Comment;
import com.tracker.app.entities.Employe;
import com.tracker.app.entities.Manager;
import com.tracker.app.entities.Notification;
import com.tracker.app.entities.Priority;
import com.tracker.app.entities.Project;
import com.tracker.app.entities.Severity;
import com.tracker.app.entities.Tag;
import com.tracker.app.entities.Team;
import com.tracker.app.service.AttachmentService;
import com.tracker.app.service.BugService;
import com.tracker.app.service.CategoryService;
import com.tracker.app.service.CommentService;
import com.tracker.app.service.EmployeService;
import com.tracker.app.service.NotificationService;
import com.tracker.app.service.PriorityService;
import com.tracker.app.service.ProjectService;
import com.tracker.app.service.SeverityService;
import com.tracker.app.service.TagService;

import jakarta.validation.Valid;


@Controller
@RequestMapping("/Bugs")
public class BugController {
	private BugService bugService;
	private CategoryService categoryService;
	private PriorityService priorityService;
	private EmployeService employeService;
	private SeverityService severityService;
	private TagService tagService;
	private ProjectService projectService;
	private CommentService commentService;
	private AttachmentService attachmentService;
	private NotificationService notificationService;
	private SimpMessagingTemplate messagingTemplate;

	
	public BugController() {}
	@Autowired
	public BugController(BugRepository bugRepository,CategoryRepository categoryRepository,TagService tagService,
			PriorityRepository priorityRepository,ProjectRepository projectRepository,CommentRepository commentRepository,
			SeverityRepository severityRepository,EmployeRepository employeRepository,DeveloperRepository developerRepository,
			CustomerSupportRepository customerSupportRepository,TesterRepository testerRepository,
			ManagerRepository managerRepository,TagRepository tagRepository,AttachmentRepository attachmentRepository) {
		this.bugService=new BugService(bugRepository, priorityRepository, severityRepository, categoryRepository);
		this.categoryService=new CategoryService(categoryRepository);
		this.priorityService=new PriorityService(priorityRepository);
		this.employeService=new EmployeService(managerRepository, developerRepository, customerSupportRepository, testerRepository, employeRepository);
		this.severityService=new SeverityService(severityRepository);
		this.tagService=new TagService(tagRepository);
		this.projectService=new ProjectService(projectRepository);
		this.commentService=new CommentService(commentRepository);
		this.attachmentService=new AttachmentService(attachmentRepository);
		this.notificationService=new NotificationService(messagingTemplate);
	}
	
	@GetMapping("/Index")
	
	public String Index(@RequestParam(name="mc",defaultValue="") String mc
			,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model) {
		Page<Bug> bugs=bugService.getBugs(mc, page, size);
		model.addAttribute("mc",mc);
		model.addAttribute("bugs",bugs.getContent());
		model.addAttribute("pages",new int[bugs.getTotalPages()]);
		model.addAttribute("currentPage",page);
		return "Admin/Bug/Bugs";
	}
	
	@GetMapping("/Create")
	public String create(Model model) {
		List<Category> categories=categoryService.getCategories();
		List<Priority> priorities=priorityService.getPriorities();
		List<Severity> severities=severityService.getSeverities();
		List<Tag> tags=tagService.getTags();
		List<Project> projects=projectService.getProjects();
		List<Comment> comments=commentService.getComments();
		List<Attachment> attachments=attachmentService.getAttachments();
		model.addAttribute("categories", categories);
		model.addAttribute("priorities", priorities);
		model.addAttribute("severities", severities);
		model.addAttribute("tags", tags);
		model.addAttribute("projects", projects);
		model.addAttribute("comments", comments);
		model.addAttribute("attachements", attachments);
		model.addAttribute("bug",new Bug());
		return "Admin/Bug/Create";
	}
	
	@PostMapping("/Store")
	public String save(@Valid Bug bug,Errors errors) {
		if (errors.hasErrors()) {
			return "Admin/Bug/Create";
		}	
		bugService.createOrUpdate(bug);
		//get the bug creator
		Employe bugCreator=bug.getBugCreator();
		//create a notification and update the content
		Notification notification=new Notification();
		notification.setContent(bugCreator.getName()+"added a new Bug");
		notification.setCreatedBy(bugCreator);
		bug.getNotifications().add(notification);
		//Retrieve the team members currently involved in the project where the bug is present.
		Team team =bugService.getTeamFromBug(bug);
		//send notification to all team members
		for (Employe employe : team.getAllMembers()) {
			notificationService.sendToUser(employe,notification);
		}
		return "redirect:Index";
	}
	

	public void notifyTeamMembers(Bug bug,Employe currentEmploye) {
		Project project = bug.getProject();
		Set<Employe> team = bug.getProject().getTeam().getAllMembers();
		for (Employe teamMember : team) {
			Notification notification=new Notification();
			notification.setContent(currentEmploye.getName()+"added a new Bug");
			notification.setCreatedBy(currentEmploye);
			notificationService.sendToUser(teamMember,notification);
		}
	}

	@DeleteMapping("/Delete")
	public String Delete(Long id) {
		bugService.delete(id);
		return "redirect:Index";
	}
	
	@GetMapping("/Edit")
	public String Edit(Long id,Model model,@Valid Bug bug) {
		List<Category> categories=categoryService.getCategories();
		List<Priority> priorities=priorityService.getPriorities();
		List<Severity> severities=severityService.getSeverities();
		List<Tag> tags=tagService.getTags();
		List<Project> projects=projectService.getProjects();
		List<Comment> comments=commentService.getComments();
		List<Attachment> attachments=attachmentService.getAttachments();
		model.addAttribute("categories", categories);
		model.addAttribute("priorities", priorities);
		model.addAttribute("severities", severities);
		model.addAttribute("tags", tags);
		model.addAttribute("projects", projects);
		model.addAttribute("comments", comments);
		model.addAttribute("attachements", attachments);
		model.addAttribute("bug",new Bug());
		return "Admin/Bug/Edit";
	}
	
	@PutMapping("/Update")
	public String update(@Valid Project project
			,Errors errors
			,@RequestParam(name = "manager")Long[] managersId) {
		if(errors.hasErrors()) {
			return "Admin/Bug/Edit";
		}
		Set<Manager> managers=new HashSet<Manager>();
		managers=employeService.getManagersById(managersId);
		project.setProjectManagers(managers);
		projectService.saveOrUpdate(project);
		return "redirect:Index";
	}
	
	
	@GetMapping("/ByCategory")
    public String getBugsByCategory(@RequestParam("categoryId") Long categoryId
    		,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
    		, Model model) {
        Category category = categoryService.getCategoryById(categoryId);
        Page<Bug> bugs = bugService.getBugsByCategory(category,page,size);
        model.addAttribute("bugs", bugs.getContent());
        return "Admin/Bug/BugsBy";
	}
	
	@GetMapping("/ByPriority")
    public String getBugsByPriority(@RequestParam("priorityId") Long priorityId
    		,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
    		, Model model) {
        Priority priority = priorityService.getPriorityById(priorityId);
        Page<Bug> bugs = bugService.getBugsByPriority(priority,page,size);
        model.addAttribute("bugs", bugs.getContent());
        return "Admin/Bug/BugsBy";
	}
	
	@GetMapping("/BySeverity")
    public String getBugsBySeverity(@RequestParam("severityId") Long severityId
    		,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
    		, Model model) {
        Severity severity = severityService.getSeverityById(severityId);
        Page<Bug> bugs = bugService.getBugsBySeverity(severity,page,size);
        model.addAttribute("bugs", bugs.getContent());
        return "Admin/Bug/BugsBy";
	}
	
	@GetMapping("/ByPlatform")
    public String getBugsByPlatform(@RequestParam("platform") String platform
    		,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
    		, Model model) {
        Page<Bug> bugs = bugService.getBugsByPlatform(platform,page,size);
        model.addAttribute("bugs", bugs.getContent());
        return "Admin/Bug/BugsBy";
	}
	
	@GetMapping("/ByStatus")
    public String getBugsByStatus(@RequestParam("status") String status
    		,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
    		, Model model) {
        Page<Bug> bugs = bugService.getBugsByStatus(status,page,size);
        model.addAttribute("bugs", bugs.getContent());
        return "Admin/Bug/BugsBy";
	}
	
	@GetMapping("/ByEmploye")
    public String getBugsByEmploye(@RequestParam("employeId") Long employeId
    		,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
    		, Model model) {
		Employe employe = employeService.getEmployeById(employeId);
        Page<Bug> bugs = bugService.getBugsByEmploye(employe,page,size);
        model.addAttribute("bugs", bugs.getContent());
        return "Admin/Bug/BugsBy";
	}
	
	@GetMapping("/BugCount")
    public String BugCount(Model model) {
		Long AllBugs=bugService.Count();
		Long ActiveBugs=bugService.CountByStatus("active");
		Long ClosedBugs=bugService.CountByStatus("closed");
        model.addAttribute("allbugs",AllBugs);
        model.addAttribute("activeBugs",ActiveBugs);
        model.addAttribute("closedBugs",ClosedBugs);
        
        return "Admin/Dashboard";
	}	
	
	/*private void notifyTeamMembers(Bug bug) {
		Project project = bug.getProject();
        Set<Employe> team = bug.getProject().getTeam().getAllMembers();
        /*for (Employe teamMember : team) {
        	messagingTemplate.convertAndSendToUser(currentUsername, "/topic/notifications", notification);
        	messagingTemplate.
		}
    }*/
	
	
	
	


}
