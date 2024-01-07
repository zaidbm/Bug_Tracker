package com.tracker.app.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import org.springframework.stereotype.Service;

import com.tracker.app.dao.CustomerSupportRepository;
import com.tracker.app.dao.DeveloperRepository;
import com.tracker.app.dao.EmployeRepository;
import com.tracker.app.dao.ManagerRepository;
import com.tracker.app.dao.TesterRepository;
import com.tracker.app.entities.CustomerSupport;
import com.tracker.app.entities.Developer;
import com.tracker.app.entities.Employe;
import com.tracker.app.entities.Manager;
import com.tracker.app.entities.Tester;

@Service
public class EmployeService {
	private ManagerRepository managerRepository;
	private DeveloperRepository developerRepository;
	private CustomerSupportRepository customerSupportRepository;
	private TesterRepository testerRepository;
	private EmployeRepository employeRepository;
	public EmployeService() {}
	@Autowired
	public EmployeService(ManagerRepository managerRepository,DeveloperRepository developerRepository,
			CustomerSupportRepository customerSupportRepository,TesterRepository testerRepository,
			EmployeRepository employeRepository) {
		this.developerRepository=developerRepository;
		this.customerSupportRepository=customerSupportRepository;
		this.testerRepository=testerRepository;
		this.managerRepository=managerRepository;
		this.employeRepository=employeRepository;
	}
	public Employe getEmployeById(Long id) {
		return employeRepository.getById(id);
	}
	public Employe findByName(String name) {
		return employeRepository.findByName(name);
	}
	//Managers
	public Page<Manager> getManagers(String mc,int page, int size){
		return managerRepository.findByNameContainsOrderByNameAsc(mc,PageRequest.of(page,size));
	}
	public List<Manager> getManagers(){
		return managerRepository.findAll();
	}
	public Manager oneManager(Long id) {
		return managerRepository.getById(id);
	}
	public void saveOrUpdate(Manager manager) {
		managerRepository.save(manager);
	}
	public void deleteManager(Long id) {
		managerRepository.deleteById(id);
	}
	public Set<Manager> getManagersById(Long[] managersId) {
		Set<Manager> managers=new HashSet<Manager>();
		for (long id : managersId) {
			managers.add(oneManager(id));
		}
		return managers;
	}
	//Developer
	public Page<Developer> getDevelopers(String mc,int page, int size){
		return developerRepository.findByNameContainsOrderByNameAsc(mc,PageRequest.of(page,size));
	}
	public List<Developer> getDevelopers(){
		return developerRepository.findAll();
	}
	public Developer oneDeveloper(Long id) {
		return developerRepository.getById(id);
	}
	public void saveOrUpdate(Developer developer) {
		developerRepository.save(developer);
	}
	public void deleteDeveloper(Long id) {
		developerRepository.deleteById(id);
	}
	public Set<Developer> getDevelopersById(Long[] developersId) {
		Set<Developer> developers=new HashSet<Developer>();
		for (long id : developersId) {
			developers.add(oneDeveloper(id));
		}
		return developers;
	}
	
	//Customer Support
	public Page<CustomerSupport> getCustomerSupports(String mc,int page, int size){
		return customerSupportRepository.findByNameContainsOrderByNameAsc(mc,PageRequest.of(page,size));
	}
	public List<CustomerSupport> getCustomerSupports(){
		return customerSupportRepository.findAll();
	}
	public CustomerSupport oneCustomerSupport(Long id) {
		return customerSupportRepository.getById(id);
	}
	public void saveOrUpdate(CustomerSupport customerSupport) {
		customerSupportRepository.save(customerSupport);
	}
	public void deleteCustomerSupport(Long id) {
		customerSupportRepository.deleteById(id);
	}
	//Testers
	public Page<Tester> getTesters(String mc,int page, int size){
		return testerRepository.findByNameContainsOrderByNameAsc(mc,PageRequest.of(page,size));
	}
	public List<Tester> getTesters(){
		return testerRepository.findAll();
	}
	public Tester oneTester(Long id) {
		return testerRepository.getById(id);
	}
	public void saveOrUpdate(Tester tester) {
		testerRepository.save(tester);
	}
	public void deleteTester(Long id) {
		testerRepository.deleteById(id);
	}
	/*@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Employe employe=employeRepository.findByName(username);
		Collection<SimpleGrantedAuthority> authorities=new ArrayList<SimpleGrantedAuthority>();
		employe.getRoles().forEach(role-> new SimpleGrantedAuthority(role.getName()));
		return new User(employe.getName(), employe.getPassword(), authorities);
	}*/
	
}
