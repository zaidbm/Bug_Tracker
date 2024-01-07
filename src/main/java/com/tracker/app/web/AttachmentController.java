package com.tracker.app.web;

import com.tracker.app.entities.Attachment;
import com.tracker.app.service.AttachmentService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/Attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    @Autowired
    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    @GetMapping("/Index")
    @Secured("ROLE_ADMIN")
    public String index(@RequestParam(name = "mc", defaultValue = "") String mc,
                        @RequestParam(name = "page", defaultValue = "0") int page,
                        @RequestParam(name = "size", defaultValue = "5") int size,
                        Model model) {
        Page<Attachment> attachments = attachmentService.getAttachments(mc, page, size);
        model.addAttribute("mc", mc);
        model.addAttribute("attachments", attachments.getContent());
        model.addAttribute("pages", new int[attachments.getTotalPages()]);
        model.addAttribute("currentPage", page);
        return "Attachment/Index";
    }

    @GetMapping("/Create")
    @Secured("ROLE_ADMIN")
    public String create(Model model) {
        model.addAttribute("attachment", new Attachment());
        return "Attachment/Create";
    }

    @PostMapping("/Store")
    @Secured("ROLE_ADMIN")
    public String store(@Valid Attachment attachment, Errors errors) {
        if (errors.hasErrors()) {
            return "Attachment/Create";
        }

        attachmentService.createOrUpdate(attachment);
        return "redirect:Index";
    }

    @DeleteMapping("/Delete")
    @Secured("ROLE_ADMIN")
    public String delete(Long id) {
        attachmentService.delete(id);
        return "redirect:Index";
    }

    @GetMapping("/Edit")
    @Secured("ROLE_ADMIN")
    public String edit(Long id, Model model) {
        Attachment attachment = attachmentService.oneAttachment(id);
        model.addAttribute("attachment", attachment);
        return "Attachment/Edit";
    }

    @PostMapping(value = "/Update")
    @Secured("ROLE_ADMIN")
    public String update(@Valid Attachment attachment, Errors errors) {
        if (errors.hasErrors()) {
            return "Attachment/Edit";
        } else {
            attachmentService.createOrUpdate(attachment);
            return "redirect:Index";
        }
    }
}
