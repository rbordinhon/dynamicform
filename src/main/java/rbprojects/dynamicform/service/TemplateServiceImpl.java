package rbprojects.dynamicform.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import rbprojects.dynamicform.config.ApplicationConfig;
import rbprojects.dynamicform.model.Field;
import rbprojects.dynamicform.model.Radio;
import rbprojects.dynamicform.model.Template;
import rbprojects.dynamicform.repository.FormularioRepository;
import rbprojects.dynamicform.repository.TemplateRepository;
import rbprojects.dynamicform.vo.DataVO;
import rbprojects.dynamicform.vo.ErrorMessageDTO;

@Service
public class TemplateServiceImpl implements TemplateService {

	@Autowired
	private TemplateRepository repository;

	@Autowired
	private FormularioRepository dataLookup;

	@Autowired
	@Qualifier(ApplicationConfig.ERROR_PROPERTIES)
	private Properties errors;

	@Override
	public void salvaTemplate(Template formTamplate) throws ValidationException {
		validaTemplate(formTamplate);
		repository.save(formTamplate);
	}

	private static ErrorMessageDTO getError(String message, String field){
		return new ErrorMessageDTO(message, field);
	}
	
	private void validaTemplate(Template formTamplate) throws ValidationException {
		final List<ErrorMessageDTO> errorsList = new ArrayList();
		if (formTamplate.getTitle() == null || formTamplate.getTitle().trim().equals("")) {
			errorsList.add(getError(errors.getProperty("template.field.error.titulo.obrigatorio"), "Titulo"));
		}
		if (formTamplate.getFields().length == 0) {
			errorsList.add(getError(errors.getProperty("template.field.error.nenhum.campo"), "Campos"));
		}
		for (int i = 0; i < formTamplate.getFields().length; i++) {
			Field field = formTamplate.getFields()[i];
			if (field.getLabel() == null || field.getLabel().trim().equals("")) {
				errorsList.add(getError(errors.getProperty("template.field.error.label"), "Campos:"+(i+1)+":Label"));
			}
			if (field.getType() == null || field.getType().trim().equals("")) {
				errorsList.add(getError(String.format(errors.getProperty("template.field.error.tipo", field.getLabel())),
						"Campos:"+(i+1)+":Tipo"));
			} else {
				if (field.getType().trim().equals("radio") && field.getRadios().length == 0) {
					errorsList.add(getError(
							String.format(errors.getProperty("template.field.error.tipo.radio.valores"), field.getLabel()),"Campos:"+(i+1)+":Valores"));
				}
				for (int j = 0; j < field.getRadios().length; j++) {
					Radio radio = field.getRadios()[j];
					if (radio.getLabel() == null || radio.getLabel().trim().equals("")) {
						errorsList.add(getError(
								String.format(errors.getProperty("template.field.error.tipo.radio.valor.nome"), field.getLabel()),
								"Campos:"+(i+1)+":Valores:"+(j+1)+":Label"));
					}
					if (radio.getValue() == null || radio.getValue().trim().equals("")) {
						errorsList.add(getError(String
								.format(errors.getProperty("template.field.error.tipo.radio.valor"), radio.getLabel(), field.getLabel()),
								"Campos:"+(i+1)+":Valores:"+(j+1)+":Valor"));
					}
				}
				
			}
	
		}
		
		if(errorsList.size() > 0){
			throw new ValidationException(errorsList.toArray(new ErrorMessageDTO[errorsList.size()]));
		}
	}

	@Override
	public Template buscaTemplateById(String formId) throws BussinessException {
		Template template = repository.findById(new ObjectId(formId));
		if (template == null) {
			throw new BussinessException("O template com o id " + formId + " não foi encontrado");
		}
		return repository.findById(new ObjectId(formId));

	}

	@Override
	public DataVO buscaDados(String formId) throws BussinessException {
		final DataVO dataVo = dataLookup.findById(new ObjectId(formId));
		dataVo.id = null;
		if (dataVo == null || dataVo.data == null || dataVo.data.length == 0) {
			throw new BussinessException("Os dados do formulario " + formId + " não foram encontrados");
		}
		return dataVo;
	}

	@Override
	public void atualizaDados(String formId, Map<String, Object> valores)
			throws ValidationException, BussinessException {
		final List<ErrorMessageDTO> errorsList = new ArrayList();
		Template template = buscaTemplateById(formId);
		final Field[] mapedFields = template.getFields();
		for (Field field : mapedFields) {
			if (field.getRequired() && !field.getReadOnly()) {
				if (valores.get(field.getLabel()) == null || valores.get(field.getLabel()).toString().trim().equalsIgnoreCase("")) {
					errorsList.add(getError("O valor do campo " + field.getLabel()+  " é obrigatório", field.getLabel()));
				}
			}
			if(valores.containsKey(field.getLabel()) && field.getMaxLength() > 0 && valores.get(field.getLabel()).toString().length() > field.getMaxLength()){
				errorsList.add(getError("O campo "+field.getLabel()+" deve conter no máximo "+field.getMaxLength()+ " caracteres", field.getLabel()));
			}
		}
		if(errorsList.size() > 0){
			throw new ValidationException(errorsList.toArray(new ErrorMessageDTO[errorsList.size()]));
		}
		
		template.addData(valores);
		repository.save(template);
	}

	@Override
	public Template[] buscaTemplates() throws ValidationException {
		final List<Template> lst = repository.findAll();
		return lst.toArray(new Template[lst.size()]);
	}

	@Override
	public void atualizaTemplate(String formId, Template atualizacao) throws ValidationException, BussinessException {
		Template temp = buscaTemplateById(formId);
		atualizacao.setId(temp.getId());
		salvaTemplate(atualizacao);
	}

	@Override
	public void removeTemplate(String formId) {
		Template template = repository.findById(new ObjectId(formId));
		if (template != null) {
			repository.delete(template);
		}
	}

}
