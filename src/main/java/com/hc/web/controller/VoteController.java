package com.hc.web.controller;

import com.hc.model.Choice;
import com.hc.model.User;
import com.hc.model.Vote;
import com.hc.service.UserService;
import com.hc.service.VoteService;
import com.hc.util.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SpringBootWebSecurityConfiguration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.authentication.AuthenticationManagerFactoryBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import static com.hc.util.TimeUtils.isNotExpired;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;

/**
 * Created by hexi on 16-10-3.
 */
@Controller
@RequestMapping("/")
public class VoteController {

    public static void main(String[] args) {
//        SecurityContextHolder.getContext().g
    }

    public static final String SPRING_SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private VoteService voteService;

    @GetMapping("/")
    public String index(Model model) {
        List<Vote> votes = voteService.listAll();
        model.addAttribute("votes", votes);
        return "index";
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        SecurityContext securityContext = (SecurityContext)session.getAttribute("SPRING_SECURITY_CONTEXT");
        Authentication authentication;
        if (securityContext != null &&
                (authentication = securityContext.getAuthentication()) != null)
            return "redirect:/admin/"+authentication.getPrincipal();
        return "login";
    }

    /**
     * 登录方法
     * @return
     */
    @PostMapping("/login")
    public String loginPost(@RequestParam(name = "username", defaultValue = "") String username,
                            @RequestParam(name = "password", defaultValue = "") String password,
                            @RequestParam(value = "rememberMe",required = false) String rememberMe,
                            HttpServletRequest request, HttpServletResponse response) {
        Authentication authenticate;
        logger.info("username:{}",username);
        logger.info("password:{}",password);
        logger.info("rememberMe:{}",rememberMe != null);
        if ( (authenticate = userService.verifyUser(new User(username,password), request)) != null ) {
            SecurityContextHolder.getContext().setAuthentication(authenticate);
            HttpSession session = request.getSession();
            session.setAttribute(SPRING_SECURITY_CONTEXT,SecurityContextHolder.getContext());
            return "redirect:/admin/"+authenticate.getPrincipal();
        }
        else {
            return "redirect:/login";
        }
    }

    /**
     * 登出方法
     * @return
     */
    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @GetMapping("/vote/{voteId}")
    public String voteDetail(@PathVariable("voteId") long voteId, Model model) {
        Vote vote = voteService.findVoteById(voteId);
        long time = new Date().getTime();
        if (vote != null && isNotExpired(vote)) {
            List<Choice> choices = new ArrayList<>();
            choices.addAll(vote.getChoices());
            model.addAttribute("choices",choices);
            model.addAttribute("voteName",vote.getTitle());
            return "vote_detail";
        }
        return "redirect:/";
    }

    @GetMapping("/admin/{username}")
    public String adminPage(Model model, @PathVariable("username") String username) {
        return "admin";
    }

    @GetMapping("/admin")
    public String adminPage(HttpServletRequest request) {
        HttpSession session = request.getSession();
        SecurityContext securityContext = (SecurityContext) session.getAttribute(SPRING_SECURITY_CONTEXT);
        Authentication au;
        if (securityContext != null &&
                (au = securityContext.getAuthentication()) != null){
            logger.info("133行：{}",au.getPrincipal());
            String username = (String) au.getPrincipal();
            return "redirect:/admin/"+username;
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping("/publish")
    public String publishPage() {
        return "publish";
    }

    @PostMapping("/publish/action")
    public String publishAction(HttpServletRequest request, RedirectAttributes attributes) {
        Vote vote = voteService.addAVote(request);
        if (vote != null) {
            return "redirect:/vote/"+vote.getId();
        } else {
            attributes.addAttribute("msg","还有信息没有填！");
            return "redirect:/publish";
        }
    }

    @GetMapping("/choice/{choiceId}/action")
    public String voteAction(@PathVariable long choiceId) {
        Vote vote = voteService.updateChoiceNumber(choiceId);
        if (vote != null)
            return "redirect:/vote/"+vote.getId()+"/detail";
        else return "redirect:/";
    }

    @GetMapping("/vote/{voteId}/detail")
    public String voteNumberDetail(Model model,
                                   @PathVariable long voteId) {
        Vote vote = voteService.findVoteById(voteId);
        if (vote != null && isNotExpired(vote)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            model.addAttribute("vote",vote);
            model.addAttribute("startTime",format.format(vote.getStartTime()));
            model.addAttribute("endTime",format.format(vote.getEndTime()));
            return "vote_number_detail";
        }
        return "redirect:/vote/"+voteId;
    }


}
