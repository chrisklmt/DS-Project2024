package gr.hua.dit.ds.dsproject.controllers;


import gr.hua.dit.ds.dsproject.entities.Client;
import gr.hua.dit.ds.dsproject.entities.Freelancer;
import gr.hua.dit.ds.dsproject.entities.Request;
import gr.hua.dit.ds.dsproject.repositories.ProjectRepository;
import gr.hua.dit.ds.dsproject.services.ClientService;
import gr.hua.dit.ds.dsproject.services.FreelancerService;
import gr.hua.dit.ds.dsproject.services.ProjectService;
import gr.hua.dit.ds.dsproject.entities.Project;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static gr.hua.dit.ds.dsproject.entities.Status.Accepted;
import static gr.hua.dit.ds.dsproject.entities.Status.Rejected;


@Controller
@RequestMapping("/project")
public class  ProjectController {
    private final ProjectService projectService;
    private final ClientService clientService;
    private final FreelancerService freelancerService;
    private final ProjectRepository projectRepository;

    public ProjectController(ProjectService projectService, ClientService clientService, FreelancerService freelancerService, ProjectRepository projectRepository) {
        this.projectService = projectService;
        this.clientService = clientService;
        this.freelancerService = freelancerService;
        this.projectRepository = projectRepository;
    }

    @GetMapping("")
    public String showProjects(Model model){
        model.addAttribute("projects", projectService.getProjects());
        return "project/projects";
    }

    @GetMapping("/new")
    public String addProject(Model model){
        Project project = new Project();
        model.addAttribute("project", project);
        model.addAttribute("msg","");
        return "project/project";
    }

    @PostMapping("/new")
    public String saveProject(@Valid @ModelAttribute("project") Project project, BindingResult theBindingResult, Model model ){
        if (theBindingResult.hasErrors()) {
            System.out.println("error");
            return "project/project";

            // Ελεγχος για να μην υπαρχει το ιδιο Title Project απο τον ιδιο Client

        }
//        else if(projectService.checkIfProjectExists(project)){
//            System.out.println("error: The project already exists");
//            String msg = "You have another project with the same title";
//
//            model.addAttribute("msg", msg);
//            return "project/project";
//        }
        else {
            Client currentClient = clientService.getCurrentClient();
            project.setClient(currentClient);

            System.out.printf(project.getProjectStatus().toString());
            projectService.saveProject(project);
            model.addAttribute("projects", projectService.getProjects());
            return "redirect:/client/my-projects";
            /*
            Εδώ κάνουμε redirect έτσι ώστε σε περίπτωση που ο client
            κάνει reload page, να μην ξανά στείλουμε post request και
            να καταγράψουμε το ίδιο project πολλές φορές.
            */
        }
    }

    @PostMapping("/assignRequest/{projectId}")
    public String assignRequestToProject(@PathVariable int projectId, Model model) {

        Freelancer freelancer = freelancerService.getCurrentFreelancer();
        projectService.assignRequestToProject(projectId, freelancer);

        model.addAttribute("freelancerRequests", freelancer.getRequests());
        return "request/myrequests";
    }

    @PostMapping("accept-project/{projectId}")
    public String acceptProject(@PathVariable int projectId,Model model) {
        Project project = projectService.getProject(projectId);
        project.setProjectStatus(Accepted);
        projectService.saveProject(project);
        model.addAttribute("projects", projectService.getProjects());
        return "project/projects";
    }

    @PostMapping("reject-project/{projectId}")
    public String rejectProject(@PathVariable int projectId,Model model) {
        Project project = projectService.getProject(projectId);
        project.setProjectStatus(Rejected);
        projectService.saveProject(project);
        model.addAttribute("projects", projectService.getProjects());
        return "project/projects";
    }

    @GetMapping("/projectStatus")
    public String showProjectToEditStatus(Model model) {
        model.addAttribute("projects", projectService.getProjectsPending());
        return "project/projectStatus";
    }

    @GetMapping("/projectOutdated")
    public String showProjectsOutdated(Model model) {
        model.addAttribute("projects", projectService.getAllOutdatedProjects());
        return "project/projectOutdated";
    }

    @GetMapping("/projectPending")
    public String deleteProjectRejected(Model model) {
        model.addAttribute("projects", projectService.getRejectedProjects());
        return "project/projectsRejected";
    }
    @PostMapping("deleteProject/{projectId}")
    public String deleteProjectRejected(@PathVariable int projectId, Model model) {
        Project project = projectService.getProject(projectId);
        projectService.deleteProject(projectId);
        model.addAttribute("projects", projectService.getProjects());
        return "project/projects";
    }

    @GetMapping("/projectsUnassigned")
    public String showUnassignedProjects(Model model) {
        Client currentClient = clientService.getCurrentClient();
        model.addAttribute("projectsUnassigned", projectService.getUnassignedProjects(currentClient));
        return "project/projectsUnassigned";
    }

    @GetMapping("/unassignedANDoutdated")
    public String showUnassignedAndOutdatedProjects(Model model) {
        Client currentClient = clientService.getCurrentClient();
        model.addAttribute("projectsUnassignedAndOutdated", projectService.getUnassignedAndOutdatedProjects(currentClient));
        return "project/unassignedANDoutdated";
    }


    @GetMapping("/completedProjects")
    public String showCompletedProjects(Model model) {
        Client currentClient = clientService.getCurrentClient();
        model.addAttribute("completedProjects", projectService.getCompletedeProjects(currentClient));
        return "project/completedProjects";
    }
}

