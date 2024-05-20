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

@Controller
public class XmdErrorController implements ErrorController {
    Logger _logger = LogManager.getLogger(this);

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        if (TemplatingEngineApplication.SITE.getBaseUrl() == null) {
            TemplatingEngineApplication.SITE.setBaseUrlFromRequest(request);
        }
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object requestUri = request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        Object exceptionType = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION_TYPE);
        Object servletName = request.getAttribute(RequestDispatcher.ERROR_SERVLET_NAME);
        _logger.debug(TemplatingEngineApplication.SITE);

        model.addAttribute("engine", TemplatingEngineApplication.ENGINE);
        model.addAttribute("site", TemplatingEngineApplication.SITE);
        model.addAttribute("status", status);
        model.addAttribute("exception", exception);
        model.addAttribute("exceptionType", exceptionType);
        model.addAttribute("message", message);
        model.addAttribute("servletName", servletName);
        model.addAttribute("url", requestUri);
        _logger.debug(model.asMap().toString());
        return "error";
    }
}