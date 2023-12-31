package com.courseproject.courseproject.Controller.User;

import com.courseproject.courseproject.Entity.User;
import com.courseproject.courseproject.Service.*;
import com.courseproject.courseproject.Entity.dto.AddToLiked;
import com.courseproject.courseproject.Entity.dto.AddToSaved;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequiredArgsConstructor
public class UserController {
	private final CommentService commentService;
	private final SavedFilmsService savedFilmsService;
	private final LikedFilmService likedFilmService;
	
	@GetMapping("/UserMainPage")
	@PreAuthorize("hasRole('User')")
	public ModelAndView AllUsersPage(){
		return new ModelAndView("User/UserMainPage");
	}
	@GetMapping("/UserFilmView")
	@PreAuthorize("hasRole('User')")
	public ModelAndView AllUsersPage(@RequestParam("filmID") String filmID, Model model){
		ModelAndView modelAndView = new ModelAndView("User/FilmView");
		model.addAttribute("filmID",filmID);
		return modelAndView;
	}
	
	@PostMapping("/AddToSaved")
	@PreAuthorize("hasRole('User')")
	public void AddFilmToSaved(@RequestBody AddToSaved request, Model model){
		request.setUserId(User.Id.toString());
		savedFilmsService.AddFilmToSaved(request);
	}
	@PostMapping("/AddToLiked")
	@PreAuthorize("hasRole('User')")
	public void AddFilmToLiked(@RequestBody AddToLiked request, Model model){
		request.setUserId(User.Id.toString());
		likedFilmService.AddFilmToLiked(request);
	}
	
	@GetMapping("/LikedFilms")
	@PreAuthorize("hasAnyRole('User','Admin')")
	public ModelAndView LikedFilms(){
		return new ModelAndView("User/Liked");
	}
	
	@GetMapping("/SavedFilms")
	@PreAuthorize("hasAnyRole('User','Admin')")
	public ModelAndView SavedFilms(){
		return new ModelAndView("User/Saved");
	}
	
}
