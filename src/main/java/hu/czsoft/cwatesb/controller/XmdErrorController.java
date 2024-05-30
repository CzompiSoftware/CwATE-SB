package hu.czsoft.cwatesb.controller;

import hu.czsoft.cwatesb.TemplatingEngineApplication;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
public class XmdErrorController implements ErrorController {
    Logger _logger = LogManager.getLogger(this);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        if (TemplatingEngineApplication.SITE_MANAGER.get().getSiteUrl() == null) {
            TemplatingEngineApplication.SITE_MANAGER.setBaseUrlFromRequest(request);
        }
        var status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        var message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        var requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        var exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        var exceptionType = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE);
        var servletName = request.getAttribute(RequestDispatcher.ERROR_SERVLET_NAME);
        var timestamp = LocalDateTime.now();

        model.addAttribute("engine", TemplatingEngineApplication.ENGINE_MANAGER.get());
        model.addAttribute("site", TemplatingEngineApplication.SITE_MANAGER.get());
        model.addAttribute("status", status);
        model.addAttribute("exception", exception);
        model.addAttribute("exceptionType", exceptionType);
        model.addAttribute("message", message);
        model.addAttribute("servletName", servletName);
        model.addAttribute("timestamp", timestamp);
        model.addAttribute("url", requestUri);
        return "error";
    }
}