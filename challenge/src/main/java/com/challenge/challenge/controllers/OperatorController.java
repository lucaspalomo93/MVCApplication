package com.challenge.challenge.controllers;


import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.challenge.challenge.controllers.services.DisplayService;
import com.challenge.challenge.controllers.services.OperatorService;

import com.challenge.challenge.models.Operator;






@Controller
@RequestMapping("/operator")
public class OperatorController {
    
    @Autowired
    private OperatorService operatorService;

    @Autowired
    private DisplayService displayService;


    @GetMapping("/list")
    public String listAll(Model model, @AuthenticationPrincipal User user){

        List<Operator> allOperators = operatorService.findAll();

  

        List<Operator> operators = displayService.showAllWithoutSuperAdmin(allOperators);
        

        model.addAttribute("user", user);
        model.addAttribute("operators", operators);
        return "index";
    }


    @GetMapping("/register")
    public String register(Operator operator, Model model){

        model.addAttribute("operator", operator);
        return "register";
    }

    @PostMapping("/save")
    public String save(@Valid Operator operator, Errors errors, RedirectAttributes redirectAttributes){
        if(errors.hasErrors()){
            return "/register";
        }
        operatorService.save(operator);

        return "redirect:/operator/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, Operator operator){

        operator = operatorService.findById(id).get();
        model.addAttribute("operator", operator);
        return "edit";
    }

    @PostMapping("/update")
    public String update(@Valid Operator operator, Errors errors, RedirectAttributes redirectAttributes){
        if(errors.hasErrors()){
            return "edit";
        }

        redirectAttributes.addFlashAttribute("updated", operator.getUserName());
        operatorService.update(operator.getId(), operator);
        return "redirect:/operator/list";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes){
        Operator op = operatorService.findById(id).get();
        
        redirectAttributes.addFlashAttribute("deletedUser", op.getUserName());
        operatorService.deleteById(id);
        return "redirect:/operator/list";
    }

    @GetMapping("/upgrade/{id}")
    public String upgrade(@PathVariable Long id, RedirectAttributes redirectAttributes){

        Operator op = operatorService.findById(id).get();
        String username = op.getUserName();

        if(operatorService.checkRole(op, "ROLE_ADMIN")){
            redirectAttributes.addFlashAttribute("alreadyUpgraded", username);
        }else{
            redirectAttributes.addFlashAttribute("upgraded", username);
        }

        operatorService.upgrade(id);
 
        return "redirect:/operator/list";
    }

    @GetMapping("/demote/{id}")
    public String downgrade(@PathVariable Long id, RedirectAttributes redirectAttributes){
        Operator op = operatorService.findById(id).get();

        if(operatorService.checkRole(op, "ROLE_ADMIN")){
            redirectAttributes.addFlashAttribute("demoted", op.getUserName());
        }else{
            redirectAttributes.addFlashAttribute("alreadyDemoted", op.getUserName());
        }

        operatorService.demote(id);
        return "redirect:/operator/list";
    }
}