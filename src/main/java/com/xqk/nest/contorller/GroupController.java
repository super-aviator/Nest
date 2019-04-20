package com.xqk.nest.contorller;

import com.xqk.nest.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/group")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @RequestMapping(value = "/get-members", method = RequestMethod.GET)
    public void GetGroupMembers(@RequestParam("id") long groupId
            , HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=utf-8");

        String result = groupService.getMembers(groupId);
        response.getWriter().write(result);
    }
}
