package com.example.controllers;

import com.example.model.Ingredient;
import com.example.model.Ingredient.Type;
import com.example.model.Taco;
import com.example.model.TacoOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
// below annotation has the same effect as explicitly declaring and initializing the logger
// private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DesignTacoController.class);
@Slf4j
// Designates class as a controller it will be picked up by ComponentScan
@Controller
// Will handle all requests beginning with /design
@RequestMapping("/design")
// this says the TacoOrder object put into the model a bit later in the session should be maintained in session
@SessionAttributes("tacoOrder")
public class DesignTacoController {

    // this method, addIngredientsToModel is invoked when a request is handled, so when we have GET("/design")
    // this method get invoked.  My guess is it will be invoked before the showDesignForm methods is invoked
    // as the view needs to be populated with data.  In this case the ingredients are hardcoded in chapter 3
    // they will be retrieved from a database.
    @ModelAttribute
    public void addIngredientsToModel(Model model) {
        List<Ingredient> ingredients = Arrays.asList(
                new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP),
                new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP),
                new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN),
                new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN),
                new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES),
                new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES),
                new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE),
                new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE),
                new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE),
                new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE)
        );

        Type[] types = Ingredient.Type.values();
        for (Type type:types) {
            model.addAttribute(type.toString().toLowerCase(), this.filterByType(ingredients, type));
        }
    }

    // this ModelAttribute is very simple, create a new Taco instance
    @ModelAttribute(name = "taco")
    public Taco taco() {
        return new Taco();
    }

    // this, like the taco() method above returns a TacoOrder instance however, with the @SessionAttribute above
    // holds the state for the order across multiple requests.
    @ModelAttribute(name = "tacoOrder")
    public TacoOrder tacoOrder() {
        return new TacoOrder();
    }

    // it is important to understand how this controller works, this GET mapping, when paired with the RequestMapping
    // above handles all GET requests with path /design.  When this path and HTTP verb is presented to the controller
    // then the controller will call the showDesignForm method and return the String "design" which is actually the
    // logical name of the view which will be used to render the model to the browser
    @GetMapping
    public String showDesignForm() { return "design"; }

    // if I am not mistaken what this code does is to produce a list of ingredients for each ingredient type.  For
    // instance under the model attribute "wraps" there should be two ingredients: a Flour Tortilla and a Corn
    // Tortilla.
    private Iterable<Ingredient> filterByType(List<Ingredient> ingredients, Type type) {
        return ingredients
                .stream()
                .filter(x -> x.getType().equals(type))
                .collect(Collectors.toList());
    }
}
