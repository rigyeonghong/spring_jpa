package jpabook.jpashop;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {

    @GetMapping("hello") // hello 라는 url로 들어오면 해당 컨트롤 호출됨
    public String hello(Model model){ // model에 데이터 심어서 컨트롤러에서 view로 넘길 수 있음
        model.addAttribute("data", "hello!!!");
        return "hello"; // html이 자동으로 붙음
    }

}
