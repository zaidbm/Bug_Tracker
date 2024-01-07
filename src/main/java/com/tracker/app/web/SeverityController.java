package com.tracker.app.web;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.tracker.app.dao.SeverityRepository;
import com.tracker.app.entities.Notification;
import com.tracker.app.entities.Severity;
import com.tracker.app.service.NotificationService;
import com.tracker.app.service.SeverityService;

@Controller
@RequestMapping("/Severities")
public class SeverityController {
	private SeverityService severityService;
	public SeverityController() {}
	@Autowired
	public SeverityController(SeverityRepository severityRepository) {
		this.severityService=new SeverityService(severityRepository);
	}
	@GetMapping("/Index")
	@Secured("ROLE_ADMIN")
	public String Index(@RequestParam(name="mc",defaultValue="") String mc
			,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model) {
		Page<Severity> severities=severityService.getSeverities(mc, page, size);
		model.addAttribute("mc",mc);
		model.addAttribute("severities",severities.getContent());
		model.addAttribute("pages",new int[severities.getTotalPages()]);
		model.addAttribute("currentPage",page);
		/*Notification notification=new Notification();
		notification.setContent("this is a notification");
		notificationService.sendNotification("admin",notification);*/
		return "Admin/Severity/Severities";
	}
	
	@GetMapping("/Create")
	@Secured("ROLE_ADMIN")
	public String create(Model model) {
		model.addAttribute("severity",new Severity());
		return "Admin/Severity/Create";
	}
	
	@PostMapping("/Store")
	@Secured("ROLE_ADMIN")
	public String store(@Valid Severity severity,Errors errors) {
		if (errors.hasErrors()) {
			return "Admin/Severity/Create";
		}
		severityService.saveOrUpdate(severity);
		/*Notification notification=new Notification();
		notification.setContent("this is a notification");
		notificationService.sendNotification("admin",notification);*/
		return "redirect:Index";
	}
	
	@GetMapping("/Delete")
	@Secured("ROLE_ADMIN")
	public String Delete(Long id) {
		severityService.getSeverityById(id).setIdSeverityInBugToNull();
		severityService.delete(id);
		return "redirect:Index";
	}
	
	@GetMapping("/Edit")
	@Secured("ROLE_ADMIN")
	public String Edit(Long id,Model model) {
		Severity severity=severityService.getSeverityById(id);
		model.addAttribute("severity", severity);
		return "Admin/Severity/Edit";
	}
	
	@PostMapping("/Update")
	@Secured("ROLE_ADMIN")
	public String update(@Valid Severity severity,Errors errors) {
		if(errors.hasErrors()) {
			return "Admin/Severity/Edit";
		}
		else {
			severityService.saveOrUpdate(severity);
		return "redirect:Index";
		}
	} 
}
