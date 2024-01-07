package com.tracker.app.web;

import java.util.List;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
import com.tracker.app.dao.PriorityRepository;
import com.tracker.app.dao.SeverityRepository;
import com.tracker.app.dao.TagRepository;
import com.tracker.app.entities.Bug;
import com.tracker.app.entities.Tag;
import com.tracker.app.service.BugService;
import com.tracker.app.service.TagService;

@Controller
@RequestMapping("/Tags")
public class TagController {
	private TagService tagService;
	private BugService bugService;
	public TagController() {}
	@Autowired
	public TagController(TagRepository tagRepository,BugRepository bugRepository,PriorityRepository priorityRepository
			,SeverityRepository severityRepository,CategoryRepository categoryRepository) {
		this.tagService=new TagService(tagRepository);
		this.bugService=new BugService(bugRepository, priorityRepository, severityRepository, categoryRepository);
		
	}
	@GetMapping("/Index")
	public String Index(@RequestParam(name="mc",defaultValue="") String mc
			,@RequestParam(name="page",defaultValue="0") int page
			,@RequestParam(name="size",defaultValue="5") int size
			,Model model) {
		Page<Tag> tags=tagService.getTags(mc,page,size);
		model.addAttribute("mc",mc);
		model.addAttribute("tags",tags.getContent());
		model.addAttribute("pages",new int[tags.getTotalPages()]);
		model.addAttribute("currentPage",page);
		return "Admin/Tag/Tags";
	}
	
	@GetMapping("/Create")
	public String create(Model model) {
		model.addAttribute("tag",new Tag());
		List<Tag> tags=tagService.getTags();
		model.addAttribute("tags",tags);
		return "Admin/Tag/Create";
	}
	
	@PostMapping(value = "/Store")
	public String store(@Valid Tag tag,Errors errors){
		if (errors.hasErrors()) {
			return "Admin/Tag/Create";
		}
		tagService.createOrUpdate(tag);
		return "redirect:Index";
	}
	
	@DeleteMapping("/Delete")
	public String Delete(Long id) {
		tagService.delete(id);
		return "redirect:Index";
	}
	
	@GetMapping("/Edit")
	public String Edit(Long id,Model model) {
		Tag tag=tagService.oneTag(id);
		List<Bug> bugs=bugService.getBugs();
		model.addAttribute("bugs",bugs);
		model.addAttribute("tag", tag);
		return "Admin/Tag/Edit";
	}
	
	@PutMapping("/Update")
	public String update(@Valid Tag tag,Errors errors) {
		if(errors.hasErrors()) {
			return "Admin/Tag/Edit";
		}
		else {
		tagService.createOrUpdate(tag);
		return "redirect:Index";
		}
	}
	
}
