package employee.managment.controller;

import employee.managment.exceptions.DuplicateFieldsException;
import employee.managment.model.employeeDB;
import employee.managment.services.employeeService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class EmployeePageController {
    private final employeeService employeeService;
    EmployeePageController(employeeService employeeService){
        this.employeeService=employeeService;
    }
    @GetMapping("/ui/hello")
   public String helloPage(Model model){
        // A Model lets you pass data from Java to your HTML page.
       model.addAttribute("message","Welcome to Employee Managment System!");
       // adds a piece of data named message. The HTML template can access it using that name.
       return "hello"; // this tells spring to render hello.html
   }
   @GetMapping("/ui/employees")
   public String listEmployees(Model model){
        model.addAttribute("employees",employeeService.getAllEmployee());
        return "employees/list";
   }
   @GetMapping("/ui/employees/new")
    public String showAddForm(Model model){
        model.addAttribute("employee",new employeeDB());
        model.addAttribute("isEdit", false);
        return "employees/form";
   }
   @PostMapping("/ui/employees")
   public String createEmployee(
           @Valid @ModelAttribute("employee") employeeDB emp,
           BindingResult result,
           RedirectAttributes redirectAttributes){
        if(result.hasErrors()){
            return "employees/form";
        }
        try{
            employeeService.createEmployee(emp);
        }catch (DuplicateFieldsException ex){
            result.rejectValue(
                    ex.getField(),
                    "duplicate",
                    ex.getMessage()
            );
        }
       if (result.hasErrors()) {
           return "employees/form";
       }

       redirectAttributes.addFlashAttribute(
               "successMessage",
               "Employee created successfully!"
       );

       return "redirect:/ui/employees";
   }
    @GetMapping("ui/employees/{id}/edit")
    public String showEditForm(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "employee",
                employeeService.getEmployeeById(id)
        );

        model.addAttribute("isEdit", true);

        return "employees/form";
    }
    @PostMapping("ui/employees/{id}/edit")
    public String updateEmployee(
            @PathVariable Long id,
            @Valid @ModelAttribute("employee") employeeDB emp,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Preserve the ID when redisplaying the form after an error.
        emp.setEmployeeId(id);

        if (result.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "employees/form";
        }

        try {
            employeeService.updateEmployee(id, emp);
        } catch (DuplicateFieldsException ex) {
            result.rejectValue(
                    ex.getField(),
                    "duplicate",
                    ex.getMessage()
            );
        }

        if (result.hasErrors()) {
            model.addAttribute("isEdit", true);
            return "employees/form";
        }

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Employee updated successfully!"
        );

        return "redirect:/ui/employees";
    }

    @GetMapping("ui/employees/{id}")
    public String viewEmployee(
            @PathVariable Long id,
            Model model) {

        model.addAttribute(
                "employee",
                employeeService.getEmployeeById(id)
        );

        return "employees/view";
    }
    @PostMapping("ui/employees/{id}/delete")
    public String deleteEmployee(
             @Valid @PathVariable Long id,
             RedirectAttributes redirectAttributes){
        employeeService.deleteEmployee(id);
        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Employee deleted successfully"
        );
        return "redirect:/ui/employees";
    }


}
