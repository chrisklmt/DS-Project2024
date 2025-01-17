package gr.hua.dit.ds.dsproject.services;


import gr.hua.dit.ds.dsproject.entities.*;
import gr.hua.dit.ds.dsproject.repositories.AssignmentRepository;
import gr.hua.dit.ds.dsproject.repositories.ProjectRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AssignmentService assignmentService;
    private final AssignmentRepository assignmentRepository;

    public ProjectService(ProjectRepository projectRepository, AssignmentService assignmentService, AssignmentRepository assignmentRepository) {
        this.projectRepository = projectRepository;
        this.assignmentService = assignmentService;
        this.assignmentRepository = assignmentRepository;
    }

    @Transactional
    public List<Project> getProjects(){
        return projectRepository.findAll();
    }

    @Transactional
    public List<Project> getProjectsPending(){
        List<Project> projects = projectRepository.findAll();
        List<Project> projectsPending = new ArrayList<>();
        for (Project p : projects) {
            if(p.getProjectStatus().equals(Status.Pending)){
                projectsPending.add(p);
            }
        }
        return projectsPending;
    }

    @Transactional
    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    @Transactional
    public Project getProject(Integer projectId) {
        return projectRepository.findById(projectId).get();
    }

    @Transactional
    public List<Project> getAcceptedProjects() {
        List<Project> projects = projectRepository.findAll();
        List<Project> projectsAccepted = new ArrayList<>();
        for (Project p : projects) {
            if(p.getProjectStatus().equals(Status.Accepted)){
                projectsAccepted.add(p);
            }
        }
        return projectsAccepted;
    }

    @Transactional
    public List<Project> getRejectedProjects() {
        List<Project> projects = projectRepository.findAll();
        List<Project> projectsRejected = new ArrayList<>();
        for (Project p : projects) {
            if(p.getProjectStatus().equals(Status.Rejected)){
                projectsRejected.add(p);
            }
        }
        return projectsRejected;
    }

    @Transactional
    public List<Project> getRequestedProjects(List<Project> projects , List<Request> requests) {

        List<Project> requestedProjects = new ArrayList<>();

        for(Project project: projects){
            for(Request request : project.getRequests()){
                if (requests.contains(request)) {
                    requestedProjects.add(project);
                }
            }
        }

        return requestedProjects;
    }

    @Transactional
    public List<Project> getNotRequestedAndUnassignedProjects(List<Project> accProjects, List<Project> reqProjects ){
        List<Project> notRequestedNotAssignedProjects = new ArrayList<>();

        for (Project p : accProjects) {
            if(!reqProjects.contains(p) && p.getAssignment() == null){
                notRequestedNotAssignedProjects.add(p);
            }
        }
        return notRequestedNotAssignedProjects;
    }

    @Transactional
    public void assignRequestToProject(Integer projectId, Freelancer freelancer) {
        Request request = new Request();

        List<Request> freelancer_requests = freelancer.getRequests();
        freelancer_requests.add(request);
        freelancer.setRequests(freelancer_requests);
        request.setFreelancer(freelancer);

        Project project = projectRepository.findById(projectId).get();
        List<Request> project_requests = project.getRequests();
        project_requests.add(request);
        project.setRequests(project_requests);

        request.setProject(project);
    }

    @Transactional
    public boolean checkIfProjectExists(Project project) {
        List<Project> projects = projectRepository.findAll();
        for (Project p : projects) {
            if (p.getTitle().equals(projects.get(0).getTitle())) {
                System.out.println("Project " + p.getTitle() + " already exists ");
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void deleteProject(Integer projectId) {
        List<Project> projects = projectRepository.findAll();
        for(Project p : projects){
            if(p.getId().equals(projectId)){
                projectRepository.delete(p);
            }
        }
    }
    @Transactional
    public List<Project> getUnassignedProjects(Client  client) {
        List<Project> projects = projectRepository.findAll();
        List<Project> unassignedProjects = new ArrayList<>();

        for (Project project : projects) {
            if (project.getAssignment() == null && project.getClient() == client && project.getProjectStatus() == Status.Accepted) {
                unassignedProjects.add(project);
            }
        }
        return unassignedProjects;
    }


}