package com.tracker.app.web;

import java.util.ArrayList;
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
import com.tracker.app.dao.BugRepository;
import com.tracker.app.dao.CategoryRepository;
import com.tracker.app.dao.CommentRepository;
import com.tracker.app.dao.CustomerSupportRepository;
import com.tracker.app.dao.DeveloperRepository;
import com.tracker.app.dao.EmployeRepository;
import com.tracker.app.dao.ManagerRepository;
import com.tracker.app.dao.PriorityRepository;
import com.tracker.app.dao.SeverityRepository;
import com.tracker.app.dao.TesterRepository;
import com.tracker.app.entities.Bug;
import com.tracker.app.entities.Comment;
import com.tracker.app.entities.CustomerSupport;
import com.tracker.app.entities.Developer;
import com.tracker.app.entities.Employe;
import com.tracker.app.entities.Manager;
import com.tracker.app.entities.Tester;
import com.tracker.app.service.BugService;
import com.tracker.app.service.CommentService;
import com.tracker.app.service.EmployeService;

@Controller
@RequestMapping("/Comments")
public class CommentController {
	private EmployeService employeService;
	private CommentService commentService;
	private BugService bugService;
	public CommentController() {}
	@Autowired
	public CommentController(ManagerRepository managerRepository,DeveloperRepository developerRepository,
			CustomerSupportRepository customerSupportRepository,TesterRepository testerRepository,
			CommentRepository commentRepository,BugRepository bugRepository,EmployeRepository employeRepository
			,PriorityRepository priorityRepository, SeverityRepository severityRepository,CategoryRepository categoryRepository) {
		this.employeService=new EmployeService(managerRepository, developerRepository, customerSupportRepository, testerRepository, employeRepository);
		this.commentService=new CommentService(commentRepository);
		this.bugService=new BugService(bugRepository, priorityRepository, severityRepository, categoryRepository);
	}
	@GetMapping("/Index")
	@Secured("ROLE_ADMIN")
	public String Index(@RequestParam(name="mc",defaultValue="") String mc
			,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model) {
		Page<Comment> comments=commentService.getComments(mc, page, size);
		model.addAttribute("mc",mc);
		model.addAttribute("comments",comments.getContent());
		model.addAttribute("pages",new int[comments.getTotalPages()]);
		model.addAttribute("currentPage",page);
		return "Admin/Comment/comments";
	}
	
	@GetMapping("/Create")
	@Secured("ROLE_ADMIN")
	public String create(Model model) {
		List<Employe> employees=new ArrayList<Employe>();
		List<Manager> managers=employeService.getManagers();
		List<Developer> developers=employeService.getDevelopers();
		List<Tester> testers=employeService.getTesters();
		List<CustomerSupport> customerSupports=employeService.getCustomerSupports();
		employees.addAll(developers);
		employees.addAll(customerSupports);
		employees.addAll(managers);
		employees.addAll(testers);
		List<Bug> bugs=bugService.getBugs();
		model.addAttribute("comment",new Comment());
		model.addAttribute("employees",employees);
		model.addAttribute("bugs",bugs);
		return "Admin/Comment/Create";
	}
	
	@PostMapping(value = "/Store")
	@Secured("ROLE_ADMIN")
	public String store(@Valid Comment comment,
			Errors errors,
			@RequestParam(name="bug") Long bugId,
			@RequestParam(name="employe") Long employeId){
		if (errors.hasErrors()) {
			return "Admin/Comment/Create";
		}
		Bug bug=bugService.oneBug(bugId);
		comment.setBug(bug);
		Employe employe=employeService.getEmployeById(employeId);
		comment.setEmploye(employe);
		commentService.saveOrUpdate(comment);
		return "redirect:Index";
	}
	
	@DeleteMapping("/Delete")
	@Secured("ROLE_ADMIN")
	public String Delete(Long id) {
		commentService.delete(id);
		return "redirect:Index";
	}
	
	@GetMapping("/Edit")
	@Secured("ROLE_ADMIN")
	public String Edit(Long id,Model model) {
		List<Employe> employees=new ArrayList<Employe>();
		List<Manager> managers=employeService.getManagers();
		List<Developer> developers=employeService.getDevelopers();
		List<Tester> testers=employeService.getTesters();
		List<CustomerSupport> customerSupports=employeService.getCustomerSupports();
		employees.addAll(developers);
		employees.addAll(customerSupports);
		employees.addAll(managers);
		employees.addAll(testers);
		List<Bug> bugs=bugService.getBugs();
		Comment comment=commentService.oneComment(id);
		model.addAttribute("employees",employees);
		model.addAttribute("bugs",bugs);
		model.addAttribute("comment",comment);
		return "Admin/Comment/Edit";
	}
	
	@PutMapping(value="/Update")
	@Secured("ROLE_ADMIN")
	public String update(@Valid Comment comment,Errors errors,
			@RequestParam(name="bug") Long bugId,
			@RequestParam(name="employe") Long employeId) {
		if(errors.hasErrors()) {
			return "Admin/Comment/Edit";
		}
		else {
			Bug bug=bugService.oneBug(bugId);
			comment.setBug(bug);
			Employe employe=employeService.getEmployeById(employeId);
			comment.setEmploye(employe);
			commentService.saveOrUpdate(comment);
		return "redirect:Index";
		}
}
}
