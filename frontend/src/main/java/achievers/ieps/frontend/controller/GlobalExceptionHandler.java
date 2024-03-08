package achievers.ieps.frontend.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SizeLimitExceededException.class)
    public ModelAndView handleSizeLimitExceededException(SizeLimitExceededException ex,
                                                         HttpServletRequest request,
                                                         Model model) {
        System.out.println("Masuk exception");
        // Determine the referring page
        String referringPage = request.getHeader("Referer");

        // Check if referringPage is null or does not contain "/vendor-assessment/edit"
        if (referringPage == null || !referringPage.contains("/vendor-assessment/edit")) {
            // Default to redirect to the form page
            referringPage = "/vendor-assessment/form";
        }
        System.out.println("Size: " + referringPage);
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add("Ukuran file melebihi 5MB!");
        model.addAttribute("error", errorMessages);

        // Return a ModelAndView with the redirect URL
        return new ModelAndView("redirect:" + referringPage);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ModelAndView handleMaxUploadSizeExceededException(SizeLimitExceededException ex,
                                                             HttpServletRequest request,
                                                             Model model) {
        System.out.println("Masuk exception");
        // Determine the referring page
        String referringPage = request.getHeader("Referer");

        // Check if referringPage is null or does not contain "/vendor-assessment/edit"
        if (referringPage == null || !referringPage.contains("/vendor-assessment/edit")) {
            // Default to redirect to the form page
            referringPage = "/vendor-assessment/form";
        }
        System.out.println("Max: " + referringPage);
        List<String> errorMessages = new ArrayList<>();
        errorMessages.add("Ukuran file melebihi 5MB!");
        model.addAttribute("error", errorMessages);

        // Return a ModelAndView with the redirect URL
        return new ModelAndView("redirect:" + referringPage);
    }
}
