package com.web.controller.education;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.persistence.model.User;
import com.persistence.model.education.Discount;
import com.persistence.model.education.Owner;
import com.persistence.model.education.Student;
import com.service.education.IDiscountService;
import com.service.education.IStudentService;
import com.web.dto.education.DiscountDTO;
import com.web.util.AppUtil;
import com.web.util.GenericResponse;
import com.web.util.RequestUtil;

@Controller
public class DiscountController {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	@Autowired
	private MessageSource messages;

	@Autowired
	IStudentService studentService;

	@Autowired
	IDiscountService discountService;
//	@Autowired
//	AppUtil appUtil;
	
	@Autowired
	RequestUtil requestUtil;

	ModelMapper modelMapper = new ModelMapper();
	
	@RequestMapping(value = "/getUserDiscount", method = RequestMethod.GET)
	@ResponseBody
	public GenericResponse getUserDiscount(final HttpServletRequest request) {
		try {
			Discount filterBy = new Discount();
			User user = requestUtil.getCurrentUser();
			filterBy.setUserId(user.getId());
	        Example<Discount> example = Example.of(filterBy);
			List<Discount> objs = discountService.findAll(example);
			if(AppUtil.isEmptyOrNull(objs))
				return new GenericResponse("NOT_FOUND",messages.getMessage("message.userNotFound", null, request.getLocale()));
			
			List<DiscountDTO> dtos=new ArrayList(); 
			objs.forEach(obj ->{
				DiscountDTO dto = modelMapper.map(obj, DiscountDTO.class);
				dto.setDatedStr(AppUtil.getDateStr(obj.getDated()));
				dto.setUpdatedStr(AppUtil.getDateStr(obj.getUpdated()));
				dtos.add(dto);
			});
			return new GenericResponse("SUCCESS",messages.getMessage("message.userNotFound", null, request.getLocale()),dtos);
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse("ERROR",messages.getMessage("message.userNotFound", null, request.getLocale()),
					e.getCause().toString());
		}
	}
	
	@RequestMapping(value = "/getUserDiscounts", method = RequestMethod.GET)
	@ResponseBody
	public String getUserDiscounts(final HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		try {
			Discount filterBy = new Discount();
			User user = requestUtil.getCurrentUser();
			filterBy.setUserId(user.getId());
	        Example<Discount> example = Example.of(filterBy);
			List<Discount> objs = discountService.findAll(example);
			objs.forEach(d -> {
				if(d!=null && d.getId()!=null) {
					sb.append("<option value="+d.getId()+">"+d.getName()+"</option>");
				}
			});
		    return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    return sb.toString();
	}

	@RequestMapping(value = "/getAllDiscount", method = RequestMethod.GET)
	@ResponseBody
	public GenericResponse getAllDiscount(final HttpServletRequest request) {
		try {
			List<Discount> objs = discountService.findAll();
			List<DiscountDTO> dtos=new ArrayList(); 
			objs.forEach(obj ->{
				dtos.add(modelMapper.map(obj, DiscountDTO.class));
			});
			if(AppUtil.isEmptyOrNull(objs)){
				return new GenericResponse("NOT_FOUND",messages.getMessage("message.userNotFound", null, request.getLocale()),dtos);
			}else {
				return new GenericResponse("SUCCESS",messages.getMessage("message.userNotFound", null, request.getLocale()),dtos);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse("ERROR",messages.getMessage("message.userNotFound", null, request.getLocale()),
					e.getCause().toString());
		}
	}
	
	@RequestMapping(value = "/addDiscount", method = RequestMethod.POST)
	@ResponseBody
	public GenericResponse addDiscount(@Validated final DiscountDTO dto, final HttpServletRequest request) {
		try {
			Discount obj= new Discount();
			LocalDateTime dated = LocalDateTime.now();
			User user = requestUtil.getCurrentUser();
			obj.setUserId(user.getId());
			obj.setName(dto.getName());
			Example<Discount> example = Example.of(obj);
			if(AppUtil.isEmptyOrNull(dto.getId()) && discountService.exists(example)) {
				return new GenericResponse("FOUND",messages.getMessage("The Owner "+dto.getName()+" already exist", null, request.getLocale()));
			}else if(!AppUtil.isEmptyOrNull(dto.getId())) {
				obj = discountService.getOne(dto.getId());
				dated = obj.getDated();
			}
			obj = modelMapper.map(obj,Discount.class);
			obj.setUserId(user.getId());
			if(AppUtil.isEmptyOrNull(dto.getId()))
				obj.setDated(dated);
			else
				obj.setDated(dated);
			obj.setUpdated(dated);

			Student student = studentService.getOne(dto.getStudentid());
			obj.setStudent(student);
			obj = discountService.save(obj);
			if(AppUtil.isEmptyOrNull(obj)) {
				return new GenericResponse("FAILED",messages.getMessage("message.userNotFound", null, request.getLocale()));
			}else {
				return new GenericResponse("SUCCESS",messages.getMessage("message.userNotFound", null, request.getLocale()));
			}
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
			return new GenericResponse("FOUND",messages.getMessage("The Owner "+dto.getName()+" already exist", null, request.getLocale()));
		} catch (Exception e) {
			e.printStackTrace();
			return new GenericResponse("ERROR",messages.getMessage(e.getMessage(), null, request.getLocale()),
					e.getCause().toString());
		}
	}
	
	@RequestMapping(value = "/deleteDiscount", method = RequestMethod.POST)
	@ResponseBody
	public boolean deleteDiscount( HttpServletRequest req, HttpServletResponse resp ){
		try {
		String ids = req.getParameter("checked");
			if(!StringUtils.isEmpty(ids)) {
				String idList[] = ids.split(",");
				for(String id:idList){
//					discountService.deleteById(Long.valueOf(id));
					discountService.deleteById(Long.valueOf(id));//.updateStatus("Inactive", id);
				}
				return true;//new GenericResponse(messages.getMessage("message.userNotFound", null, request.getLocale()),"SUCCESS");
			}else {
				return false;// new GenericResponse(messages.getMessage("message.userNotFound", null, request.getLocale()),"SUCCESS");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;//new GenericResponse(messages.getMessage("message.userNotFound", null, request.getLocale()),
		}
	}
}
