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
    private OperatorService oS;

    @GetMapping("/list")
    public String listAll(Model model, @AuthenticationPrincipal User user){

        List<Operator> operators = oS.findAll();

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
        oS.save(operator);
        return "redirect:/operator/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model, Operator operator){

        operator = oS.findById(id).get();
        model.addAttribute("operator", operator);
        return "edit";
    }

    @PostMapping("/update")
    public String update(@Valid Operator operator, Errors errors){
        if(errors.hasErrors()){
            return "edit";
        }

        oS.update(operator.getId(), operator);
        return "redirect:/operator/list";
    }
    
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        oS.deleteById(id);
        return "redirect:/operator/list";
    }

    /*
    @PutMapping("/update/{id}")
    public ResponseEntity<Operator> updateOperator(@PathVariable Long id, @RequestBody Operator operator){
        if(operator != null){
            oS.update(id, operator);
            return ResponseEntity.ok().body(operator);
        }
        return ResponseEntity.badRequest().build();
    }
    /*
    @GetMapping("/list/{id}")
    public ResponseEntity<Operator> getById(@PathVariable Long id){
        Operator op = oS.findById(id).get();

        if(op != null){
            return ResponseEntity.ok().body(op);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    public ResponseEntity<Operator> createOperator(@RequestBody Operator operator){
        if(oS.save(operator) != null){
            oS.save(operator);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.badRequest().build();
    }



    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteOperator(@PathVariable Long id){
        System.out.println(id);
        if(oS.deleteById(id)){
            oS.deleteById(id);

            return ResponseEntity.ok().body("Operator removed");
        }
        return ResponseEntity.notFound().build();
    }
    */
}