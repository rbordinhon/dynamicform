package rbprojects.dynamicform.web.controller;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import rbprojects.dynamicform.model.Template;
import rbprojects.dynamicform.model.Field;
import rbprojects.dynamicform.service.BussinessException;
import rbprojects.dynamicform.service.TemplateService;
import rbprojects.dynamicform.service.ValidationException;
import rbprojects.dynamicform.vo.DataVO;
import rbprojects.dynamicform.vo.ErrorMessageDTO;
import rbprojects.dynamicform.web.dto.ErrorVo;
import rbprojects.dynamicform.web.dto.FieldInfo;
import rbprojects.dynamicform.web.dto.TemplateDTO;

@RestController
@RequestMapping("/colector")
public class FormularioCadastroController {
	private static org.slf4j.Logger logger = LoggerFactory.getLogger(FormularioCadastroController.class);
	private static final String ERRO_NO_SERVIDOR = "Erro no servidor";
	private static final String O_REQUEST_0_PROPAGOU_A_EXCECAO_1 = "O request {0} propagou a exceção:{1}";
	public FormularioCadastroController() {

	}

	@Autowired
	public TemplateService service;

	@RequestMapping(method = { RequestMethod.GET }, value = "templates/{id}")
	public TemplateDTO buscaFormularioById(@PathVariable(value = "id") String id) throws BussinessException {
		Template template = service.buscaTemplateById(id);
		TemplateDTO mappedObj = new TemplateDTO();
		mappedObj._id = template.getId().toHexString();
		mappedObj.fields = template.getFields();
		mappedObj.title = template.getTitle();
		mappedObj.data = template.getData();
		return mappedObj;
	}
	
	@RequestMapping(method = { RequestMethod.DELETE }, value = "templates/{id}")
	public void excluir(@PathVariable(value = "id") String id) throws BussinessException {
		service.removeTemplate(id);
	}
	
	@RequestMapping(method = { RequestMethod.POST }, value = "templates/{id}/data")
	public void atualizaValores(@PathVariable(value = "id") String id,@RequestBody Map<String,Object> map) throws BussinessException, ValidationException, JsonParseException, JsonMappingException, IOException {
		service.atualizaDados(id, map);
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "templates/{id}/data")
	public DataVO buscaValores(@PathVariable(value = "id") String id) throws BussinessException, ValidationException, JsonParseException, JsonMappingException, IOException {
		return service.buscaDados(id);
	}
	
	@RequestMapping(method = { RequestMethod.GET }, value = "templates")
	public TemplateDTO[] buscaFormulario() throws BussinessException, ValidationException {
		final Template[] template = service.buscaTemplates();
		final TemplateDTO[] returned = new TemplateDTO[template.length];
		for (int i = 0; i < returned.length; i++) {
			final TemplateDTO mappedObj = new TemplateDTO();
			returned[i] = mappedObj;
			mappedObj._id = template[i].getId().toHexString();
			final Field[] fields =template[i].getFields();
			final FieldInfo[] infos = new FieldInfo[fields.length];
			mappedObj.fields = infos;
			for (int j = 0; j < fields.length; j++) {
				infos[j] = new FieldInfo();
				final Field field = new Field();
				field.setLabel(fields[j].getLabel());
				infos[j].label = field.getLabel();	
			}
			mappedObj.title = template[i].getTitle();
			mappedObj.dataCount = template[i].getData().length;
						
		}
		return returned;
	}
	
	

	@ExceptionHandler(rbprojects.dynamicform.service.ValidationException.class)
	public ResponseEntity<ErrorVo> handleValidateError(HttpServletRequest request, ValidationException exception) {
		ErrorVo error = new ErrorVo();
		error.errors = exception.getErrors();
		logger.error(MessageFormat.format(O_REQUEST_0_PROPAGOU_A_EXCECAO_1, request.getRequestURL().toString(),
				exception.getMessage()), exception);
		return new ResponseEntity<ErrorVo>(error, HttpStatus.PRECONDITION_FAILED);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorVo> handleError(HttpServletRequest request, Exception exception) {
		ErrorVo error = new ErrorVo();
		ErrorMessageDTO errorDto = new ErrorMessageDTO();
		logger.error(MessageFormat.format(O_REQUEST_0_PROPAGOU_A_EXCECAO_1, request.getRequestURL().toString(),
				exception.getMessage()), exception);
		if (exception instanceof BussinessException) {
			errorDto.message = exception.getMessage();
			errorDto.category = "Business";
			error.errors = new ErrorMessageDTO[]{errorDto};
			return new ResponseEntity<ErrorVo>(error, HttpStatus.SEE_OTHER);
		} else {
			errorDto.message = ERRO_NO_SERVIDOR;
			errorDto.category = "Server";
			error.errors = new ErrorMessageDTO[]{errorDto};
			return new ResponseEntity<ErrorVo>(error, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	
	}
	public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		String value = "\"{\"Nome Fantasia\":\"ertert\",\"RazÃ£o Social\":\"erter\",\"Endereco\":\"\",\"CNPJ\":\"53454\",\"Email\":\"dgdfg\",\"Telefone\":\"dgdfg\"}\"";
		Map<String,Object> map = mapper.readValue(value, Map.class);
		
	}

}
