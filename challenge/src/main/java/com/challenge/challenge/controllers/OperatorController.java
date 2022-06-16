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

import com.challenge.challenge.controllers.services.OperatorService;

import com.challenge.challenge.models.Operator;




@Controller
@RequestMapping("/operator")
public class OperatorController {
    
    @Autowired
    private OperatorService operatorService;


    @GetMapping("/list")
    public String listAll(Model model, @AuthenticationPrincipal User user){

        List<Operator> operators = operatorService.findAll();

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
    public String save(@Valid Operator operator, Errors errors){
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
    public String update(@Valid Operator operator, Errors errors){
        if(errors.hasErrors()){
            return "edit";
        }

        operatorService.update(operator.getId(), operator);
        return "redirect:/operator/list";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        operatorService.deleteById(id);
        return "redirect:/operator/list";
    }

    @GetMapping("/upgrade/{id}")
    public String upgrade(@PathVariable Long id){

        operatorService.upgrade(id);

        return "redirect:/operator/list";
    }

    @GetMapping("/demote/{id}")
    public String downgrade(@PathVariable Long id){
        operatorService.demote(id);

        return "redirect:/operator/list";
    }
}