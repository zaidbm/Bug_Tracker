package com.tracker.app.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.tracker.app.dao.BugRepository;
import com.tracker.app.dao.CategoryRepository;
import com.tracker.app.dao.CustomerSupportRepository;
import com.tracker.app.dao.DeveloperRepository;
import com.tracker.app.dao.EmployeRepository;
import com.tracker.app.dao.ManagerRepository;
import com.tracker.app.dao.PriorityRepository;
import com.tracker.app.dao.ProjectRepository;
import com.tracker.app.dao.SeverityRepository;
import com.tracker.app.dao.TeamRepository;
import com.tracker.app.dao.TesterRepository;
import com.tracker.app.entities.Bug;
import com.tracker.app.entities.Category;
import com.tracker.app.entities.CustomerSupport;
import com.tracker.app.entities.Developer;
import com.tracker.app.entities.Employe;
import com.tracker.app.entities.Manager;
import com.tracker.app.entities.Priority;
import com.tracker.app.entities.Project;
import com.tracker.app.entities.Severity;
import com.tracker.app.entities.Team;
import com.tracker.app.entities.Tester;
import com.tracker.app.service.BugService;
import com.tracker.app.service.CategoryService;
import com.tracker.app.service.EmployeService;
import com.tracker.app.service.PriorityService;
import com.tracker.app.service.ProjectService;
import com.tracker.app.service.SeverityService;
import com.tracker.app.service.TeamService;

@Controller
@RequestMapping("/Employees")
public class EmployeController {
	private EmployeService employeService;
	private TeamService teamService;
	private ProjectService projectService;
	private CategoryService categoryService;
	private SeverityService severityService;
	private PriorityService priorityService;
	private BugService bugService;
	@Value("${dir.images}")
	private String imagesDir;
	public EmployeController() {}
	@Autowired
	public EmployeController(ManagerRepository managerRepository,DeveloperRepository developerRepository,
			CustomerSupportRepository customerSupportRepository,TesterRepository testerRepository,
			TeamRepository teamRepository,EmployeRepository employeRepository,ProjectRepository projectRepository,
			CategoryRepository categoryRepository,PriorityRepository priorityRepository,SeverityRepository severityRepository
			,BugRepository bugRepository) {
		this.employeService=new EmployeService(managerRepository, developerRepository, customerSupportRepository, testerRepository, employeRepository);
		this.teamService=new TeamService(teamRepository);
		this.projectService=new ProjectService(projectRepository);
		this.categoryService=new CategoryService(categoryRepository);
		this.priorityService=new PriorityService(priorityRepository);
		this.severityService=new SeverityService(severityRepository);
		this.bugService=new BugService(bugRepository, priorityRepository, severityRepository, categoryRepository);
	}
	@GetMapping("/Managers")
	@Secured("ROLE_ADMIN")
	public String IndexManager(@RequestParam(name="mc",defaultValue="") String mc
			,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model) {
		Page<Manager> managers=employeService.getManagers(mc,page,size);
		model.addAttribute("mc",mc);
		model.addAttribute("managers",managers.getContent());
		model.addAttribute("pages",new int[managers.getTotalPages()]);
		model.addAttribute("currentPage",page);
		return "Admin/Manager/Managers";
	}
	
	@GetMapping("/CreateManager")
	@Secured("ROLE_ADMIN")
	public String createManager(Model model) {
		model.addAttribute("manager",new Manager());
		List<Team> teams=teamService.getTeams();
		model.addAttribute("teams",teams);
		return "Admin/Manager/Create";
	}
	
	@PostMapping("/StoreManager")
	@Secured("ROLE_ADMIN")
	public String storeManager(@Valid Manager manager,
			Errors errors,@RequestParam(name="teams") Long[] teamsID,@RequestParam(name="projects") Long[] ProjectID	
			,@RequestParam(name="photo") MultipartFile file) throws IllegalStateException,IOException {
		if (errors.hasErrors()) {
			return "Admin/Manager/Create";
		}
		//teams managed by Project Manager
		Set<Team> teams=new HashSet<Team>();
		teams=teamService.getTeamsById(teamsID);
		manager.setTeamsManaged(teams);
		Set<Project> projects=new HashSet<Project>();
		projects=projectService.getProjectsById(ProjectID);
		manager.setManagerProjects(projects);
		employeService.saveOrUpdate(manager);
		if(!file.isEmpty()) {
            file.transferTo(new File(imagesDir+manager.getId()));
		}
		return "redirect:Managers";
	}
	
	@DeleteMapping("/DeleteManager")
	@Secured("ROLE_ADMIN")
	public String DeleteManager(Long id) {
		File photo = new File(imagesDir+id);
		if (photo.exists()) {
			photo.delete();
		}
		employeService.deleteManager(id);
		return "redirect:Managers";
	}
	
	@GetMapping("/EditManager")
	@Secured("ROLE_ADMIN")
	public String EditManager(Long id,Model model) {
		Manager manager=employeService.oneManager(id);
		List<Team> teams=teamService.getTeams();
		List<Project> projects=projectService.getProjects();
		model.addAttribute("teams",teams);
		model.addAttribute("projects", projects);
		model.addAttribute("manager", manager);
		return "Admin/Manager/Edit";
	}
	//@RequestParam(name="team") Long[] teamsID,
	@PutMapping("/UpdateManager")
	@Secured("ROLE_ADMIN")
	public String updateManager(@Valid Manager manager,Errors errors) {
		if(errors.hasErrors()) {
			return "Admin/Manager/Edit";
		}
		else {
			//teams managed by Project Manager
			/*Set<Team> teams=new HashSet<Team>();
			teams=teamService.getTeamsById(teamsID);
			manager.setTeamsManaged(teams);*/
			employeService.saveOrUpdate(manager);
			return "redirect:Managers";
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////
										//Developers
	@GetMapping("/Developers")
	@Secured("ROLE_ADMIN")
	public String IndexDeveloper(@RequestParam(name="mc",defaultValue="") String mc
			,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model) {
		Page<Developer> developers=employeService.getDevelopers(mc,page,size);
		model.addAttribute("mc",mc);
		model.addAttribute("developers",developers.getContent());
		model.addAttribute("pages",new int[developers.getTotalPages()]);
		model.addAttribute("currentPage",page);
		return "Admin/Developer/Developers";
	}
	
	@GetMapping("/CreateDeveloper")
	@Secured("ROLE_ADMIN")
	public String createDeveloper(Model model) {
		model.addAttribute("developer",new Developer());
		return "Admin/Developer/Create";
	}
	
	@PostMapping("/StoreDeveloper")
	@Secured("ROLE_ADMIN")
	public String storeDeveloper(@Valid Developer developer,
			Errors errors,
			@RequestParam(name="photo") MultipartFile file) throws IllegalStateException,IOException {
		if (errors.hasErrors()) {
			return "Admin/Developer/Create";
		}
		employeService.saveOrUpdate(developer);
		if(!file.isEmpty()) {
            file.transferTo(new File(imagesDir+developer.getId()));
		}
		return "redirect:Developers";
	}
	
	@GetMapping("/DeleteDeveloper")
	@Secured("ROLE_ADMIN")
	public String DeleteDeveloper(Long id) {
		File photo = new File(imagesDir+id);
		if (photo.exists()) {
			photo.delete();
		}
		employeService.deleteDeveloper(id);
		return "redirect:Developers";
	}
	
	@GetMapping("/EditDeveloper")
	@Secured("ROLE_ADMIN")
	public String EditDeveloper(Long id,Model model) {
		Developer developer=employeService.oneDeveloper(id);
		model.addAttribute("developer", developer);
		return "Admin/Developer/Edit";
	}
	
	@PostMapping("/UpdateDeveloper")
	@Secured("ROLE_ADMIN")
	public String updateDeveloper(@Valid Developer developer,Errors errors) {
		if(errors.hasErrors()) {
			return "Admin/Developer/Edit";
		}
		else {
		employeService.saveOrUpdate(developer);
		return "redirect:Developers";
		}
	}
	/********************************************************************************************************************
											CustomerSupport
	*********************************************************************************************************************/
	@GetMapping("/CustomerSupport")
	@Secured("ROLE_ADMIN")
	public String IndexCustomerSupport(@RequestParam(name="mc",defaultValue="") String mc
			,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model) {
		Page<CustomerSupport> customerSupports=employeService.getCustomerSupports(mc, page, size);
		model.addAttribute("mc",mc);
		model.addAttribute("customerSupports",customerSupports.getContent());
		model.addAttribute("pages",new int[customerSupports.getTotalPages()]);
		model.addAttribute("currentPage",page);
		return "Admin/CustomerSupport/CustomerSupports";
	}
	
	@GetMapping("/CreateCustomerSupport")
	@Secured("ROLE_ADMIN")
	public String createCustomerSupport(Model model) {
		model.addAttribute("customerSupport",new CustomerSupport());
		return "Admin/CustomerSupport/Create";
	}
	
	@PostMapping("/StoreCustomerSupport")
	@Secured("ROLE_ADMIN")
	public String storeCustomerSupport(@Valid CustomerSupport customerSupport,
			Errors errors,
			@RequestParam(name="photo") MultipartFile file) throws IllegalStateException,IOException {
		if (errors.hasErrors()) {
			return "Admin/CustomerSupport/Create";
		}
		employeService.saveOrUpdate(customerSupport);
		if(!file.isEmpty()) {
            file.transferTo(new File(imagesDir+customerSupport.getId()));
		}
		return "redirect:CustomerSupports";
	}
	
	@DeleteMapping("/DeleteCustomerSupport")
	@Secured("ROLE_ADMIN")
	public String DeleteCustomerSupport(Long id) {
		File photo = new File(imagesDir+id);
		if (photo.exists()) {
			photo.delete();
		}
		employeService.deleteCustomerSupport(id);
		return "redirect:CustomerSupports";
	}
	
	@GetMapping("/EditCustomerSupport")
	@Secured("ROLE_ADMIN")
	public String EditCustomerSupport(Long id,Model model) {
		CustomerSupport customerSupport=employeService.oneCustomerSupport(id);
		model.addAttribute("customerSupport", customerSupport);
		return "Admin/CustomerSupport/Edit";
	}
	
	@PutMapping("/UpdateCustomerSupport")
	@Secured("ROLE_ADMIN")
	String updateCustomerSupport(@Valid CustomerSupport customerSupport,Errors errors) {
		if(errors.hasErrors()) {
			return "Admin/CustomerSupport/Edit";
		}
		else {
		employeService.saveOrUpdate(customerSupport);
		return "redirect:IndexCustomerSupports";
		}
	}
	
	@RequestMapping(value="/getPhoto", produces=MediaType.IMAGE_JPEG_VALUE)
	@ResponseBody
	public byte[] getPhoto(Long id) throws IOException {
		return Files.readAllBytes(Paths.get(imagesDir+id));
	}
	/********************************************************************************************************************
												Manager profile
	*********************************************************************************************************************/
	@GetMapping("/ManagerProfile")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String Indexstudent(Model model,HttpSession session) {
		Long id=(Long) session.getAttribute("id");
		Manager manager=employeService.oneManager(id);
		model.addAttribute("manager",manager);
		return "Admin/Manager/Profile";
	}
	@GetMapping("/ManagerProjects")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String ManagerProjects(@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model, Long id,HttpSession session) {
		Manager manager = null;
		if(!(id==null)) {
			manager=employeService.oneManager(id);
			session.setAttribute("idManager", id);
		}
		else manager=employeService.oneManager((Long) session.getAttribute("idManager"));
		model.addAttribute("mesprojets",manager.getManagerProjects());
		model.addAttribute("manager", manager);
		return "Admin/Manager/MesProjets";
	}
	
	@GetMapping("/ManagerTeams")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String ManagerTeams(@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model, Long id,HttpSession session) {
		Manager manager = null;
		if(!(id==null)) {
			manager=employeService.oneManager(id);
			session.setAttribute("idManager", id);
		}
		else manager=employeService.oneManager((Long) session.getAttribute("idManager"));
		model.addAttribute("teams",manager.getTeamsManaged());
		model.addAttribute("manager", manager);
		return "Admin/Manager/MyTeams";
	}
	
	@GetMapping("/addProject")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String addProject(Model model, Long id,HttpSession session) {
		Manager manager = null;
		if(!(id==null)) {
			manager=employeService.oneManager(id);
			session.setAttribute("idManager", id);
		}
		//Créer une objet projet pour remplir
		model.addAttribute("project", new Project());
		//diriger vers le fichier Addproject.html
		return "Admin/Manager/AddProject";
	}
	
	@GetMapping("/storeProject")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String storeProject(@Valid Project project,Errors errors
			,Model model, Long id,HttpSession session) {
		Manager manager = null;
		if(!(id==null)) {
			manager=employeService.oneManager(id);
			session.setAttribute("idManager", id);
		}
		//stocker le projet dans la base de données
		projectService.saveOrUpdate(project);
		//stocker le objet projet dans le session
		session.setAttribute("project", project);
		//creer une instance de objet Team
		model.addAttribute("team", new Team());
		//retourne une liste de developers
		model.addAttribute("developers", employeService.getDevelopers());
		//rediriger vers la page AddTeam.html
		return "redirect:AddTeam";
	}
	
	
	@GetMapping("/AddTeam")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String addTeam(Model model, Long id,HttpSession session) {
		Manager manager = null;
		if(!(id==null)) {
			manager=employeService.oneManager(id);
			session.setAttribute("idManager", id);
		}
		Project project=(Project) session.getAttribute("project");
		System.out.println(project.getId());
		model.addAttribute("team", new Team());
		model.addAttribute("project", project);
		//retourne une liste de developers
		model.addAttribute("developers", employeService.getDevelopers());
		//retourne une liste de testers
		model.addAttribute("testers", employeService.getTesters());
		//diriger vers la page AddTeam.html
		return "Admin/Manager/AddTeam";
	}
	
	
	@PostMapping("/storeTeam")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String storeTeam(@Valid Team team,Errors errors
			,@RequestParam(name="developer") Long[] DevelopersId
			,@RequestParam(name="project") Project project
			,Model model, Long id,HttpSession session) {
		Manager manager = null;
		if(!(id==null)) {
			manager=employeService.oneManager(id);
			session.setAttribute("idManager", id);
		}
		//affecter le projet a l'equipe créé
		project.setTeam(team);
		team.setProject(project);
		//ajouter le chef de projet a la liste de chefs de cet équipe
		team.getTeamsManagers().add(employeService.oneManager((Long) session.getAttribute("idManager")));
		//project.setManager(employeService.oneManager((Long) session.getAttribute("idManager")));
		//ajouter les developers a l'equipe crée
		Set<Developer> developers=new HashSet<Developer>();
		developers=employeService.getDevelopersById(DevelopersId);
		team.setTeamsMembers(developers);
		//stocker l'équipe dans la base de donnée
		teamService.saveOrUpdate(team);
		return "redirect:ManagerProjects";
	}
	
	
	/************************************************************************************************************************
													Developer profile
	************************************************************************************************************************/
	
	@GetMapping("/DeveloperProfile")
	@Secured({"ROLE_ADMIN", "ROLE_DEVELOPER"})
	public String DeveloperProfile(Model model,HttpSession session) {
		Long id=(Long) session.getAttribute("id");
		Developer developer=employeService.oneDeveloper(id);
		model.addAttribute("developer",developer);
		return "Admin/Developer/Profile";
	}
	/*@GetMapping("/DeveloperProjects")
	public String DeveloperProjects(@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model, Long id,HttpSession session) {
		Developer developer = null;
		if(!(id==null)) {
			developer=employeService.oneDeveloper(id);
			session.setAttribute("idDeveloper", id);
		}
		else developer=employeService.oneDeveloper((Long) session.getAttribute("idDeveloper"));
		//model .mes projets
		model.addAttribute("developer", developer);
		return "Admin/Developer/MesProjets";
	}*/
	
	@GetMapping("/DeveloperTeams")
	@Secured({"ROLE_ADMIN", "ROLE_DEVELOPER"})
	public String DeveloperTeams(@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model, Long id,HttpSession session) {
		Developer developer = null;
		if(!(id==null)) {
			developer=employeService.oneDeveloper(id);
			session.setAttribute("idDeveloper", id);
		}
		else developer=employeService.oneDeveloper((Long) session.getAttribute("idDeveloper"));
		model.addAttribute("teams",developer.getDeveloperTeams());
		model.addAttribute("developer", developer);
		return "Admin/Developer/MyTeams";
	}
	
	/************************************************************************************************************************
												Tester profile
	************************************************************************************************************************/
	
	@GetMapping("/TesterProfile")
	@Secured({"ROLE_ADMIN", "ROLE_TESTER"})
	public String TesterProfile(Model model,HttpSession session) {
		Long id=(Long) session.getAttribute("id");
		Tester tester=employeService.oneTester(id);
		model.addAttribute("tester",tester);
		return "Admin/Tester/Profile";
	}
	/*@GetMapping("/TesterProjects")
	 @Secured({"ROLE_ADMIN", "ROLE_TESTER"})
	public String TesterProjects(@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model, Long id,HttpSession session) {
		Developer developer = null;
		if(!(id==null)) {
			developer=employeService.oneDeveloper(id);
			session.setAttribute("idDeveloper", id);
		}
		else developer=employeService.oneDeveloper((Long) session.getAttribute("idDeveloper"));
		//model .mes projets
		model.addAttribute("developer", developer);
		return "Admin/Developer/MesProjets";
	}*/
	
	@GetMapping("/TesterTeams")
	@Secured({"ROLE_ADMIN", "ROLE_TESTER"})
	public String TesterTeams(@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model, Long id,HttpSession session) {
		Tester tester = null;
		if(!(id==null)) {
			tester=employeService.oneTester(id);
			session.setAttribute("idDeveloper", id);
		}
		else tester=employeService.oneTester((Long) session.getAttribute("idDeveloper"));
		model.addAttribute("teams",tester.getTesterTeams());
		model.addAttribute("tester", tester);
		return "Admin/Tester/MyTeams";
	}
	
	/************************************************************************************************************************
												CustomerSupport profile
	************************************************************************************************************************/
	
	@GetMapping("/CustomerSupportProfile")
	@Secured({"ROLE_ADMIN", "ROLE_SUPPORT"})
	public String CustomerSupportProfile(Model model,HttpSession session) {
		Long id=(Long) session.getAttribute("id");
		CustomerSupport customerSupport=employeService.oneCustomerSupport(id);
		model.addAttribute("customerSupport",customerSupport);
		return "Admin/CustomerSupport/Profile";
	}
	
	
	
	/*@PostMapping("/VerifyLogin")
	public String VerifyLogin(@RequestParam(name="name") String username,
			@RequestParam(name="password") String password,HttpSession session) {
		Employe employe=employeService.findByName(username);
		session.setAttribute("id", employe.getId());
		if(employe.getRole().equals("admin")) return "redirect:Managers";
		if(employe.getRole().equals("manager")) return "redirect:ManagerProfile";
		if(employe.getRole().equals("developer")) return "redirect:DeveloperProfile";
		if(employe.getRole().equals("tester")) return "redirect:TesterProfile";
		if(employe.getRole().equals("support")) return "redirect:CustomerSupportProfile";
		
		
		return password;
		
		
		}*/
	
	
	/*@RequestMapping(value="/Login")
	public String Login() {
		return "login";
	}
	@RequestMapping(value="/logout")
	public String Logout() {
		return "redirect:Login";
	*/
	
	@GetMapping("/AddBug")
	@Secured({"ROLE_ADMIN","ROLE_DEVELOPER","ROLE_TESTER","ROLE_SUPPORT"})
	public String create(Model model) {
		List<Category> categories=categoryService.getCategories();
		List<Priority> priorities=priorityService.getPriorities();
		List<Severity> severities=severityService.getSeverities();
		List<Project> projects=projectService.getProjects();
		model.addAttribute("categories", categories);
		model.addAttribute("priorities", priorities);
		model.addAttribute("severities", severities);
		model.addAttribute("projects", projects);
		model.addAttribute("bug",new Bug());
		return "Admin/Developer/AddBug";
	}
	@PostMapping("/StoreBug")
	@Secured({"ROLE_ADMIN","ROLE_DEVELOPER","ROLE_TESTER","ROLE_SUPPORT"})
	public String Store(@Valid Bug bug,
			HttpSession session,
			@RequestParam(name="category") Long idcatgeory,
			@RequestParam(name="priority") Long idPriority,
			@RequestParam(name="severity") Long idSeverity,
			@RequestParam(name="project") Long idProject,
			Errors errors) {
		if (errors.hasErrors()) {
			return "Admin/Bug/Create";
		}
		Category category=categoryService.getCategoryById(idcatgeory);
		bug.setCategory(category);
		Priority priority=priorityService.getPriorityById(idPriority);
		bug.setPriority(priority);
		Severity severity=severityService.getSeverityById(idSeverity);
		bug.setSeverity(severity);
		Project project=projectService.oneProject(idProject);
		bug.setProject(project);
		Long id=(Long) session.getAttribute("id");
		Employe employe=employeService.getEmployeById(id);
		bug.setBugCreator(employe);
		bugService.createOrUpdate(bug);
		return "redirect:DeveloperBugs";
	}
	
	
	
	@GetMapping("/DeveloperBugs")
	@Secured({"ROLE_ADMIN","ROLE_DEVELOPER"})
	public String DeveloperBugs(@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model, Long id,HttpSession session) {
		Developer developer = null;
		if(!(id==null)) {
			developer=employeService.oneDeveloper(id);
			session.setAttribute("idDeveloper", id);
		}
		else developer=employeService.oneDeveloper((Long) session.getAttribute("id"));
		model.addAttribute("bugs",developer.getBugs());
		model.addAttribute("developer", developer);
		return "Admin/Developer/MyBugs";
	}
	@GetMapping("/TesterBugs")
	@Secured({"ROLE_ADMIN","ROLE_TESTER"})
	public String TesterBugs(@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model, Long id,HttpSession session) {
		Tester tester = null;
		if(!(id==null)) {
			tester=employeService.oneTester(id);
			session.setAttribute("idDeveloper", id);
		}
		else tester=employeService.oneTester((Long) session.getAttribute("id"));
		model.addAttribute("bugs",tester.getBugs());
		model.addAttribute("developer", tester);
		return "Admin/Tester/MyBugs";
	}
	
	@PostMapping(value = "/StoreBugt")
	@Secured({"ROLE_ADMIN","ROLE_TESTER"})
	public String Storet(@Valid Bug bug,
			HttpSession session,
			@RequestParam(name="category") Long idcatgeory,
			@RequestParam(name="priority") Long idPriority,
			@RequestParam(name="severity") Long idSeverity,
			@RequestParam(name="project") Long idProject,
			Errors errors) {
		if (errors.hasErrors()) {
			return "Admin/Bug/Create";
		}
		Category category=categoryService.getCategoryById(idcatgeory);
		bug.setCategory(category);
		Priority priority=priorityService.getPriorityById(idPriority);
		bug.setPriority(priority);
		Severity severity=severityService.getSeverityById(idSeverity);
		bug.setSeverity(severity);
		Project project=projectService.oneProject(idProject);
		bug.setProject(project);
		Long id=(Long) session.getAttribute("id");
		Employe employe=employeService.getEmployeById(id);
		bug.setBugCreator(employe);
		bugService.createOrUpdate(bug);
		return "redirect:TesterBugs";
	}
	@GetMapping("/AddBugt")
	@Secured({"ROLE_ADMIN","ROLE_TESTER"})
	public String createt(Model model) {
		List<Category> categories=categoryService.getCategories();
		List<Priority> priorities=priorityService.getPriorities();
		List<Severity> severities=severityService.getSeverities();
		List<Project> projects=projectService.getProjects();
		model.addAttribute("categories", categories);
		model.addAttribute("priorities", priorities);
		model.addAttribute("severities", severities);
		model.addAttribute("projects", projects);
		model.addAttribute("bug",new Bug());
		return "Admin/Tester/AddBug";
}
	
	@GetMapping("/SupportBugs")
	public String SupportBugs(@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model, Long id,HttpSession session) {
		CustomerSupport customerSupport = null;
		if(!(id==null)) {
			customerSupport=employeService.oneCustomerSupport(id);
			session.setAttribute("idDeveloper", id);
		}
		else customerSupport=employeService.oneCustomerSupport((Long) session.getAttribute("id"));
		model.addAttribute("bugs",customerSupport.getBugs());
		model.addAttribute("developer", customerSupport);
		return "Admin/CustomerSupport/MyBugs";
	}
	
	@PostMapping("/StoreBugc")
	public String Storec(@Valid Bug bug,
			HttpSession session,
			@RequestParam(name="category") Long idcatgeory,
			@RequestParam(name="priority") Long idPriority,
			@RequestParam(name="severity") Long idSeverity,
			@RequestParam(name="project") Long idProject,
			Errors errors) {
		if (errors.hasErrors()) {
			return "Admin/Bug/Create";
		}
		Category category=categoryService.getCategoryById(idcatgeory);
		bug.setCategory(category);
		Priority priority=priorityService.getPriorityById(idPriority);
		bug.setPriority(priority);
		Severity severity=severityService.getSeverityById(idSeverity);
		bug.setSeverity(severity);
		Project project=projectService.oneProject(idProject);
		bug.setProject(project);
		Long id=(Long) session.getAttribute("id");
		Employe employe=employeService.getEmployeById(id);
		bug.setBugCreator(employe);
		bugService.createOrUpdate(bug);
		return "redirect:SupportBugs";
	}
	@GetMapping("/AddBugc")
	public String createtc(Model model) {
		List<Category> categories=categoryService.getCategories();
		List<Priority> priorities=priorityService.getPriorities();
		List<Severity> severities=severityService.getSeverities();
		List<Project> projects=projectService.getProjects();
		model.addAttribute("categories", categories);
		model.addAttribute("priorities", priorities);
		model.addAttribute("severities", severities);
		model.addAttribute("projects", projects);
		model.addAttribute("bug",new Bug());
		return "Admin/CustomerSupport/AddBug";
}
	
}
