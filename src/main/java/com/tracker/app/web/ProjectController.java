package com.tracker.app.web;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import com.tracker.app.dao.CustomerSupportRepository;
import com.tracker.app.dao.DeveloperRepository;
import com.tracker.app.dao.EmployeRepository;
import com.tracker.app.dao.ManagerRepository;
import com.tracker.app.dao.ProjectRepository;
import com.tracker.app.dao.TeamRepository;
import com.tracker.app.dao.TesterRepository;
import com.tracker.app.entities.Manager;
import com.tracker.app.entities.Project;
import com.tracker.app.entities.Team;
import com.tracker.app.service.EmployeService;
import com.tracker.app.service.ProjectService;
import com.tracker.app.service.TeamService;

@Controller
@RequestMapping("/Projects")
public class ProjectController {
	private ProjectService projectService;
	private TeamService teamService;
	private EmployeService employeService;
	public ProjectController() {}
	@Autowired
	public ProjectController(ProjectRepository projectRepository,TeamRepository teamRepository,DeveloperRepository developerRepository,
			TesterRepository testerRepository,ManagerRepository managerRepository,
			CustomerSupportRepository customerSupportRepository,EmployeRepository employeRepository) {
		this.projectService=new ProjectService(projectRepository);
		this.teamService=new TeamService(teamRepository);
		this.employeService=new EmployeService(managerRepository, developerRepository, customerSupportRepository, testerRepository, employeRepository);
	}
	
	@GetMapping("/Index")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String Index(@RequestParam(name="mc",defaultValue="") String mc
			,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model) {
		Page<Project> projects=projectService.getProjects(mc, page, size);
		model.addAttribute("mc",mc);
		model.addAttribute("projects",projects.getContent());
		model.addAttribute("pages",new int[projects.getTotalPages()]);
		model.addAttribute("currentPage",page);
		return "Admin/Project/Projects";
	}
	
	@GetMapping("/Create")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String create(Model model) {
		//List<Team> teams=teamService.getTeams();
		List<Manager> managers=employeService.getManagers();
		//model.addAttribute("teams", teams);
		model.addAttribute("managers", managers);
		model.addAttribute("project",new Project());
		return "Admin/Project/Create";
	}
	
	@PostMapping("/Store")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String Store(@Valid Project project,
			Errors errors,
			@RequestParam(name = "manager")Long idManager) {
		if (errors.hasErrors()) {
			return "Admin/Project/Create";
		}
		Manager manager=employeService.oneManager(idManager);
		//project.setManager(manager);
		projectService.saveOrUpdate(project);
		return "redirect:Index";
	}
	
	@DeleteMapping("/Delete")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String Delete(Long id) {
		projectService.delete(id);
		return "redirect:Index";
	}
	
	@GetMapping("/Edit")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String Edit(Long id,Model model) {
		Project project=projectService.oneProject(id);
		List<Manager> managers=employeService.getManagers();
		model.addAttribute("managers", managers);
		model.addAttribute("project",project);
		return "Admin/Project/Edit";
	}
	
	@PutMapping("/Update")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String update(@Valid Project project,
			Errors errors,
			@RequestParam(name = "manager")Long idManager) {
		if(errors.hasErrors()) {
			return "Admin/Project/Edit";
		}
		Manager manager=employeService.oneManager(idManager);	
		//project.setManager(manager);
		projectService.saveOrUpdate(project);
		return "redirect:Index";
	}
	
	
	
}
