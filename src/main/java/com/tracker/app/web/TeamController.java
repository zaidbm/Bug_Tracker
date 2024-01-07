package com.tracker.app.web;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
import com.tracker.app.entities.Developer;
import com.tracker.app.entities.Manager;
import com.tracker.app.entities.Project;
import com.tracker.app.entities.Team;
import com.tracker.app.service.EmployeService;
import com.tracker.app.service.ProjectService;
import com.tracker.app.service.TeamService;

@RequestMapping("/Teams")
@Controller
public class TeamController {
	private EmployeService employeService;
	private TeamService teamService;
	private ProjectService projectService;
	public TeamController() {}
	@Autowired
	public TeamController(ManagerRepository managerRepository,TeamRepository teamRepository,
			ProjectRepository projectRepository,DeveloperRepository developerRepository,
			CustomerSupportRepository customerSupportRepository,TesterRepository testerRepository,
			EmployeRepository employeRepository) {
		this.employeService=new EmployeService(managerRepository, developerRepository, customerSupportRepository, testerRepository, employeRepository);
		this.teamService=new TeamService(teamRepository);
		this.projectService=new ProjectService(projectRepository);
	}
	@GetMapping("/Index")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String Index(@RequestParam(name="mc",defaultValue="") String mc
			,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model) {
		Page<Team> teams=teamService.getTeams(mc,page,size);
		model.addAttribute("mc",mc);
		model.addAttribute("teams",teams.getContent());
		model.addAttribute("pages",new int[teams.getTotalPages()]);
		model.addAttribute("currentPage",page);
		return "Admin/Team/teams";
	}
	
	@GetMapping("/Create")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String create(Model model) {
		List<Manager> managers=employeService.getManagers();
		List<Developer> developers=employeService.getDevelopers();
		List<Project> projects=projectService.getProjectsWhereTeamIsNull();
		model.addAttribute("team",new Team());
		model.addAttribute("managers",managers);
		model.addAttribute("developers",developers);
		model.addAttribute("projects",projects);
		return "Admin/Team/Create";
	}
	
	@PostMapping("/Store")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String store(@Valid Team team,
			Errors errors,
			@RequestParam(name="developer") Long[] DevelopersId,
			@RequestParam(name="manager") Long[] ManagersId,
			@RequestParam(name="project") Long idProject){
		if (errors.hasErrors()) {
			return "Admin/Team/Create";
		}
		Set<Developer> developers=new HashSet<Developer>();
		developers=employeService.getDevelopersById(DevelopersId);
		team.setTeamsMembers(developers);
		Set<Manager> managers=new HashSet<Manager>();
		managers=employeService.getManagersById(ManagersId);
		team.setTeamsManagers(managers);
		Project project=projectService.oneProject(idProject);
		team.setProject(project);
		project.setTeam(team);
		teamService.saveOrUpdate(team);
		return "redirect:Index";
	}
	
	@DeleteMapping("/Delete")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String Delete(Long id) {
		teamService.delete(id);
		return "redirect:Index";
	}
	
	@GetMapping("/Edit")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String Edit(Long id,Model model) {
		Team team=teamService.oneTeam(id);
		List<Manager> managers=employeService.getManagers();
		List<Developer> developers=employeService.getDevelopers();
		List<Project> projects=projectService.getProjectsWhereTeamIsNull();
		model.addAttribute("project", team.getProject());
		model.addAttribute("team",team);
		model.addAttribute("managers",managers);
		model.addAttribute("developers",developers);
		model.addAttribute("projects",projects);
		return "Admin/Team/Edit";
	}
	
	@PostMapping("/Update")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String update(@Valid Team team,Errors errors,
			@RequestParam(name="teamsMembers") Long[] DevelopersId,
			@RequestParam(name="teamsManagers") Long[] ManagersId,
			@RequestParam(name="project") Long idProject) {
		if(errors.hasErrors()) {
			return "Admin/Team/Edit";
		}
		else {
			Team originalTeam=teamService.oneTeam(team.getId());
			originalTeam.getProject().setTeam(null);
			originalTeam.setProject(null);
			
			Set<Developer> developers=new HashSet<Developer>();
			developers=employeService.getDevelopersById(DevelopersId);
			team.setTeamsMembers(developers);
			Set<Manager> managers=new HashSet<Manager>();
			managers=employeService.getManagersById(ManagersId);
			team.setTeamsManagers(managers);
			Project project=projectService.oneProject(idProject);
			team.setProject(project);
			project.setTeam(team);
			teamService.saveOrUpdate(team);
		return "redirect:Index";
		}
	}
		
}
