package com.example.controllers;

import com.example.model.TacoOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("tacoOrder")
public class OrderController {

    @GetMapping("/current")
    public String orderForm() { return "orderForm"; }

/*
    This method also bears some explanation.  Like the taco object in DesignTacoController#processTaco, the order
    object is bound to the form data which originated it.  Note sessionStatus.setComplete(), this order was initially
    placed into the session when the user created their first taco, now that the order has been submitted, the
    session needs to be cleared thus enabling a new order to be placed without any data remaining from the prior
    session.
*/
/*
    The same routine is run for validation
*/
    @PostMapping
    public String processOrder(
            @Valid TacoOrder order,
            Errors errors,
            SessionStatus sessionStatus
    ) {
        if (errors.hasErrors()) return "orderForm";

        log.info("Order submitted {}", order);
        sessionStatus.setComplete();
        return "redirect:/";
    }
}
