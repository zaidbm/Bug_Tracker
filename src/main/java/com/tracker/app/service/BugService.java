package com.tracker.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import com.tracker.app.dao.BugRepository;
import com.tracker.app.dao.CategoryRepository;
import com.tracker.app.dao.PriorityRepository;
import com.tracker.app.dao.SeverityRepository;
import com.tracker.app.entities.Bug;
import com.tracker.app.entities.Category;
import com.tracker.app.entities.Employe;
import com.tracker.app.entities.Priority;
import com.tracker.app.entities.Project;
import com.tracker.app.entities.Severity;
import com.tracker.app.entities.Team;

@Service
public class BugService {
	private BugRepository bugRepository;
	private PriorityRepository priorityRepository;
	private SeverityRepository severityRepository;
	private CategoryRepository categoryRepository;
	public BugService() {}
	
	@Autowired
	public BugService(BugRepository bugRepository,PriorityRepository priorityRepository,SeverityRepository severityRepository
			,CategoryRepository categoryRepository) {
		this.bugRepository = bugRepository;
		this.priorityRepository=priorityRepository;
		this.severityRepository=severityRepository;
		this.categoryRepository=categoryRepository;
	}
	 
	public Page<Bug> getBugs(String mc,int page, int size){
		return bugRepository.findByTitleContainsOrderByTitleAsc(mc,PageRequest.of(page,size));
	}
	public List<Bug> getBugs(){
		return bugRepository.findAll();
	}
	public Bug oneBug(Long id) {
		return bugRepository.getById(id);
	}
	
	 
	public void createOrUpdate(Bug bug) {
		bugRepository.save(bug);
		//Team team=getTeamFromBug(bug);
		
	}
	public void delete(Long id) {
		bugRepository.deleteById(id);
	}
	public List<Bug> getBugsById(Long[] bugsId) {
		List<Bug> bugs=new ArrayList<Bug>();
		for (Long id : bugsId) {
			bugs.add(oneBug(id));
		}
		return bugs;
	}
	
	public Page<Bug> getBugsByCategory(Category category,int page, int size) {
        return bugRepository.findByCategory(category,PageRequest.of(page,size));
    }
	
	public Page<Bug> getBugsByPriority(Priority priority,int page, int size) {
        return bugRepository.findByPriority(priority,PageRequest.of(page,size));
    }
	
	public Page<Bug> getBugsBySeverity(Severity severity,int page, int size) {
        return bugRepository.findBySeverity(severity,PageRequest.of(page,size));
    }
	
	public Page<Bug> getBugsByPlatform(String platform,int page, int size){
		return bugRepository.findByPlatform(platform,PageRequest.of(page,size));
	}
	
	public Page<Bug> getBugsByStatus(String status,int page, int size){
		return bugRepository.findByStatus(status,PageRequest.of(page,size));
	}
	
	public Page<Bug> getBugsByEmploye(Employe employe,int page, int size) {
        return bugRepository.findByBugCreator(employe,PageRequest.of(page,size));
    }
	
	public long Count(){
		return bugRepository.count();
	}
	
	public long CountByStatus(String Status){
		return bugRepository.countByStatus(Status);
	}
	public long CountByPriority(String Priority){
		return priorityRepository.countByName(Priority);
	}
	
	public Map<String, Long> BugCountByPriority() {
        Map<String, Long> PriorityBugCount = new LinkedHashMap<>();
        List<Priority> priorities = priorityRepository.findAll();
        for (Priority priority : priorities) {
        	Long BugCountPerPriority =(long) priority.getBugs().size();
            PriorityBugCount.put(priority.getName(), BugCountPerPriority);
        }
        return PriorityBugCount;
    }
	
	public Map<String, Long> BugCountBySeverity() {
        Map<String, Long> SeverityBugCount = new LinkedHashMap<>();
        List<Severity> Severities = severityRepository.findAll();
        for (Severity severity : Severities) {
        	Long BugCountPerSeverity =(long) severity.getBugs().size();
            SeverityBugCount.put(severity.getName(), BugCountPerSeverity);
        }
        return SeverityBugCount;
    }
	
	public Map<String, Long> BugCountByCategory() {
        Map<String, Long> CategoryBugCount = new LinkedHashMap<>();
        List<Category> categories = categoryRepository.findAll();
        for (Category category : categories) {
        	Long BugCountPerCategry =(long) category.getBugs().size();
            CategoryBugCount.put(category.getName(), BugCountPerCategry);
        }
        return CategoryBugCount;
    }
	
	public Team getTeamFromBug(Bug bug) {
		 Team team=bug.getProject().getTeam();
		 return team;
	 }
	
	
	
}
