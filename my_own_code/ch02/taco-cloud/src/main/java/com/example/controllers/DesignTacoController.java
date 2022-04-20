package com.example.controllers;

import com.example.model.Ingredient;
import com.example.model.Ingredient.Type;
import com.example.model.Taco;
import com.example.model.TacoOrder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public TacoOrder order() {
        return new TacoOrder();
    }

    // it is important to understand how this controller works, this GET mapping, when paired with the RequestMapping
    // above handles all GET requests with path /design.  When this path and HTTP verb is presented to the controller
    // then the controller will call the showDesignForm method and return the String "design" which is actually the
    // logical name of the view which will be used to render the model to the browser
    @GetMapping
    public String showDesignForm() { return "design"; }

    // as with the @GetMapping @PostMapping handles POST requests with "/design" as the path.  It too works with
    // the @RequestMapping annotation
    /*
        there is a lot going on with the POST request that bears explanation.  First, when the form is
        submitted all fields in the form are bound to the taco object.  "Which Taco object?" one might ask.  It
        is the taco object delivered by the public Taco taco() method, which returns a new taco object.  Here by
        way of method parameters, Spring is autowiring a relationship between the two methods.  When used as a
        method annotation, @ModelAttribute adds an attribute to the model and ties it to a name, "taco" in this
        case.  So before any controller actions take place, all the @ModelAttribute methods are run first so
        the model can be declared and initialized before the controllers act on them.  It is important to note
        that @ModelAttribute was used on one of the parameters in processTaco -> @ModelAttribute TacoOrder tacoOrder.
        We saw previously how the @ModelAttribute was used to annotate a method, here it is used to annotate a
        method parameter.  In this case it tells Spring to retrieve the tacoOrder object placed into the model
        via the public TacoOrder order() method which was annotated with @ModelAttribute(name = "tacoOrder").
    */
    /*
        Before the processTaco method is called @Valid ensures validation has been run on the design form, once
        the validation is run, then the errors object will be populated.  If there are any errors then the hasErrors()
        method will be true and the form will be loaded again.
    */
    @PostMapping
    public String processTaco(
            @Valid Taco taco, Errors errors,
            @ModelAttribute TacoOrder tacoOrder) {
        if (errors.hasErrors()) return "design";

        tacoOrder.addTaco(taco);
        log.info("Processing taco: {}", taco);

        return "redirect:/orders/current";
    }

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
