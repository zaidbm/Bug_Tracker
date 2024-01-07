package com.tracker.app.web;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.tracker.app.dao.PriorityRepository;
import com.tracker.app.entities.Priority;
import com.tracker.app.service.PriorityService;

@Controller
@RequestMapping("/Priorities")

public class PriorityController {
	public PriorityService priorityService;
	public PriorityController() {}
	@Autowired
	public PriorityController(PriorityRepository priorityRepository) {
		this.priorityService=new PriorityService(priorityRepository);
	}
	@GetMapping("/Index")
	@Secured("ROLE_ADMIN")
	public String Index(@RequestParam(name="mc",defaultValue="") String mc
			,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model) {
		Page<Priority> priorities=priorityService.getPriorities(mc, page, size);
		model.addAttribute("mc",mc);
		model.addAttribute("priorities",priorities.getContent());
		model.addAttribute("pages",new int[priorities.getTotalPages()]);
		model.addAttribute("currentPage",page);
		return "Admin/Priority/Priorities";
	}
	
	@GetMapping(value = "/Create")
	//@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	@Secured("ROLE_ADMIN")
	public String create(Model model) {
		model.addAttribute("priority",new Priority());
		return "Admin/Priority/Create";
	}
	
	@PostMapping("/Store")
	@Secured("ROLE_ADMIN")
	public String store(@Valid Priority priority,Errors errors) {
		if (errors.hasErrors()) {
			return "Admin/Priority/Create";
		}
		priorityService.saveOrUpdate(priority);
		return "redirect:Index";
	}
	
	@GetMapping("/Delete")
	@Secured("ROLE_ADMIN")
	public String Delete(Long id) {
		priorityService.getPriorityById(id).setIdPriorityInBugToNull();
		priorityService.delete(id);
		return "redirect:Index";
	}
	
	@GetMapping("/Edit")
	@Secured("ROLE_ADMIN")
	public String Edit(Long id,Model model) {
		Priority priority=priorityService.getPriorityById(id);
		model.addAttribute("priority", priority);
		return "Admin/Priority/Edit";
	}
	
	@PostMapping("/Update")
	@Secured("ROLE_ADMIN")
	public String update(@Valid Priority priority,Errors errors) {
		if(errors.hasErrors()) {
			return "Admin/Category/Edit";
		}
		else {
			priorityService.saveOrUpdate(priority);
		return "redirect:Index";
		}
	} 
}
