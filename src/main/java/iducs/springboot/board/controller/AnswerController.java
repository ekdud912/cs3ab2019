package iducs.springboot.board.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import iducs.springboot.board.domain.Answer;
import iducs.springboot.board.domain.Question;
import iducs.springboot.board.domain.User;
import iducs.springboot.board.service.AnswerService;
import iducs.springboot.board.service.QuestionService;

@Controller
@RequestMapping("/questions/{questionId}/answers")
public class AnswerController {
	@Autowired QuestionService questionService; // 의존성 주입(Dependency Injection) : 
	@Autowired AnswerService answerService;
	
	@PostMapping("")
	public String createAnswer(@PathVariable Long questionId, String contents, HttpSession session) {
		User sessionUser = (User) session.getAttribute("user");
		Question question = questionService.getQuestionById(questionId);
		Answer newAnswer = new Answer(sessionUser, question, contents);
		answerService.saveAnswer(newAnswer);
		return String.format("redirect:/questions/%d", questionId);
	}
	
	@GetMapping("/{id}/infoEdit")
	public String getUpdateForm(@PathVariable(value = "id") Long id, Model model, HttpSession session) {
		Answer answer = answerService.getAnswerById(id);
		model.addAttribute("answer", answer);
		User writer = (User) session.getAttribute("user");
		model.addAttribute("writer", writer);
		return "/questions/answerEdit";
	}
	@PutMapping("/{id}")
	public String updateAnswerById(@PathVariable(value = "id") Long id, @Valid Question formAnswer, Model model) {
		Answer answer = answerService.getAnswerById(id);
		answer.setContents(formAnswer.getContents());
		answerService.updateAnswer(answer);
		model.addAttribute("question", answer);
		return "redirect:/questions";
	}

}
