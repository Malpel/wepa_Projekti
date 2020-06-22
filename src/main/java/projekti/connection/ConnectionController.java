package projekti.connection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import projekti.account.Account;
import projekti.account.AccountService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ConnectionController {

    @Autowired
    ConnectionService connectionService;

    @Autowired
    AccountService accountService;

    @Secured("USER")
    @PostMapping("/users/{id}/connect")
    public String sendRequest(@PathVariable Long id, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Account sender = accountService.findByUsername(auth.getName());
        Account receiver = accountService.findById(id);

        connectionService.requestConnection(sender, receiver);

        String referer = request.getHeader("Referer");

        System.out.println("**********************");
        System.out.println(referer);
        System.out.println("**********************");

        return "redirect:" + referer;
    }

    @Secured("USER")
    @PostMapping("/connection/{connectionId}/accept")
    public String acceptRequest(@PathVariable Long connectionId) {
        connectionService.acceptConnection(connectionId);
        return "redirect:/";
    }

    @Secured("USER")
    @PostMapping("/connection/{connectionId}/decline")
    public String decline(@PathVariable Long connectionId) {
        connectionService.decline(connectionId);
        return "redirect:/";
    }

    @Secured("USER")
    @PostMapping("/connection/disconnect")
    public String disconnect(@RequestParam Long oneId, @RequestParam Long twoId, HttpServletRequest request) {
        Account one = accountService.findById(oneId);
        Account two = accountService.findById(twoId);

        connectionService.disconnect(one, two);

        String referer = request.getHeader("Referer");

        return "redirect:" + referer;
    }

}
