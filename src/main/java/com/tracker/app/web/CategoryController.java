package com.tracker.app.web;

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
import com.tracker.app.dao.CategoryRepository;
import com.tracker.app.entities.Category;
import com.tracker.app.service.CategoryService;
import com.tracker.app.service.NotificationService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/Categories")
public class CategoryController {
	private CategoryService categoryService;
	private NotificationService notificationService;
	//private SimpMessagingTemplate messagingTemplate;
	
	public CategoryController() {}
	@Autowired
	public CategoryController(CategoryRepository categoryRepository) {
		this.categoryService=new CategoryService(categoryRepository);
		//this.notificationService=new NotificationService(messagingTemplate);
	}
	@GetMapping(value = "/Index")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String Index(@RequestParam(name="mc",defaultValue="") String mc
			,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model) {
		Page<Category> cats=categoryService.getCategories(mc,page,size);
		model.addAttribute("mc",mc);
		model.addAttribute("cats",cats.getContent());
		model.addAttribute("pages",new int[cats.getTotalPages()]);
		model.addAttribute("currentPage",page);
		//notificationService.sendtouser();
		return "Admin/Category/Categories";
	}
	
	@GetMapping("/Create")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String create(Model model) {
		model.addAttribute("category",new Category());
		return "Admin/Category/Create";
	}
	
	@PostMapping("/Store")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String Store(@Valid Category category,Errors errors) {
		if (errors.hasErrors()) {
			return "Admin/category/Create";
		}
		categoryService.saveOrUpdate(category);
		return "redirect:Index";
	}
	
	@GetMapping("/Delete")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String Delete(Long id) {
		categoryService.delete(id);
		return "redirect:Index";
	}
	
	@GetMapping("/Edit")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String Edit(Long id,Model model) {
		Category category=categoryService.getCategoryById(id);
		model.addAttribute("category", category);
		return "Admin/category/Edit";
	}
	
	@PostMapping("/Update")
	@Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
	public String update(@Valid Category category,Errors errors) {
		if(errors.hasErrors()) {
			return "Admin/category/Edit";
		}
		categoryService.saveOrUpdate(category);
		return "redirect:Index";
		}
	} 

