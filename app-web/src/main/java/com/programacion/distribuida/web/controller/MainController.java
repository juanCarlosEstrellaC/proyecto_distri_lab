package com.programacion.distribuida.web.controller;

import com.programacion.distribuida.web.service.AuthorService;
import com.programacion.distribuida.web.service.BookService;
//import com.programacion.distribuida.web.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {
    @Autowired
    private AuthorService authorService;
    @Autowired
    private BookService bookService;
//    @Autowired
//    private CustomerService customerService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("allAuthors", authorService.findAll());
        model.addAttribute("allBooks", bookService.findAll());
//        model.addAttribute("customers", customerService.findAll());
        return "index";
    }

    @GetMapping("/author")
    public String author(@RequestParam(required = false) Integer id, @RequestParam(required = false) String isbn, Model model) {
        model.addAttribute("allAuthors", authorService.findAll());
        if (id != null) {
            Object author = authorService.findById(id);
            if (author != null) {
                model.addAttribute("filteredAuthors", java.util.List.of(author));
            } else {
                model.addAttribute("filteredAuthors", java.util.Collections.emptyList());
            }
        } else if (isbn != null) {
            model.addAttribute("filteredAuthors", authorService.findByBook(isbn));
        }
        return "index";
    }

    @GetMapping("/book")
    public String book(@RequestParam(required = false) String isbn, Model model) {
        model.addAttribute("allBooks", bookService.findAll());
        if (isbn != null) {
            Object book = bookService.findByIsbn(isbn);
            if (book != null) {
                model.addAttribute("filteredBooks", java.util.List.of(book));
            } else {
                model.addAttribute("filteredBooks", java.util.Collections.emptyList());
            }
        }
        return "index";
    }

//    @GetMapping("/customer")
//    public String customer(@RequestParam(required = false) Integer id, Model model) {
//        if (id != null) {
//            model.addAttribute("customers", customerService.findById(id));
//        } else {
//            model.addAttribute("customers", customerService.findAll());
//        }
//        return "index";
//    }
}
