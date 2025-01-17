package gr.hua.dit.ds.dsproject.controllers;


import gr.hua.dit.ds.dsproject.entities.*;
import gr.hua.dit.ds.dsproject.services.AssignmentService;
import gr.hua.dit.ds.dsproject.services.FreelancerService;
import gr.hua.dit.ds.dsproject.services.ProjectService;
import gr.hua.dit.ds.dsproject.services.RequestService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/assignment")
public class AssignmentController {
    private final AssignmentService assignmentService;
    private final FreelancerService freelancerService;
    private final ProjectService projectService;
    private final RequestService requestService;

    public AssignmentController(AssignmentService assignmentService, FreelancerService freelancerService, ProjectService projectService, RequestService requestService) {
        this.assignmentService = assignmentService;
        this.freelancerService = freelancerService;
        this.projectService = projectService;
        this.requestService = requestService;
    }

    @GetMapping("")
    public String showAssignments(Model model){
        model.addAttribute("assignments", assignmentService.getAssignments());
        return "assignment/assignments";
    }

    @GetMapping("/new")
    public String addAssignment(Model model){
        Assignment assignment = new Assignment();
        model.addAttribute("assignment", assignment);
        return "assignment/assignment";
    }

//    @PostMapping("/new")
//    public String saveAssignment(@Valid @ModelAttribute("assignment") Assignment assignment,
//                              BindingResult theBindingResult, Model model ){
//        if (theBindingResult.hasErrors()) {
//            System.out.println("error");
//            return "assignment/assignment";
//        } else {
//            assignmentService.saveAssignment(assignment);
//            model.addAttribute("assignments", assignmentService.getAssignments());
//            return "assignment/assignments";
//        }
//    }
    @PostMapping("/assignFreelancer/{requestId}")
    public String assignFreelancerToProject(@PathVariable int requestId) {
        Request request = requestService.getRequest(requestId);

        Freelancer freelancer = freelancerService.getFreelancer(request.getFreelancer().getId());
        Project project = projectService.getProject(request.getProject().getId());

        Assignment assignment = new Assignment();
        assignment.setFreelancer(freelancer);
        assignment.setProject(project);
        assignment.setDateSubmitted(request.getDateSubmitted());
        assignmentService.saveAssignment(assignment);

        List<Request> allRequestsForProject = project.getRequests();
        List<Request> requestsToUpdate = new ArrayList<>(allRequestsForProject); // Create a copy of the list

        for (Request req : requestsToUpdate) {
            if (req.getId().equals(requestId)) {
                // Mark the selected request as "Accepted"
                req.setRequestStatus(Status.Accepted); // Assuming Status.Accepted is defined
            } else {
                // Mark other requests as "Rejected"
                req.setRequestStatus(Status.Rejected); // Assuming Status.Rejected is defined
            }
            requestService.saveRequest(req); // Save the updated request status
        }


        return "redirect:/client/my-projects";
    }
}
