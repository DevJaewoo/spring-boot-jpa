package jpabook.jpashop.controller;

import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final MemberService memberService;
    private final OrderService orderService;
    private final ItemService itemService;

    @GetMapping("/order")
    public String createForm(Model model) {
        model.addAttribute("orderForm", new OrderForm());
        model.addAttribute("members", memberService.findMembers());
        model.addAttribute("items", itemService.findItems());
        return "order/orderForm";
    }

    @PostMapping("/order")
    public String order(@Valid OrderForm orderForm) {
        orderService.order(orderForm.getMemberID(), orderForm.getItemID(), orderForm.getCount());
        return "redirect:/orders";
    }
}
