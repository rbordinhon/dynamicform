package rbprojects.dynamicform.service;

import java.util.Map;

import rbprojects.dynamicform.model.Template;
import rbprojects.dynamicform.vo.DataVO;

public interface TemplateService {
   public void salvaTemplate(Template formTamplate) throws ValidationException;

   public Template[] buscaTemplates() throws ValidationException;
   
   public Template buscaTemplateById(String formId) throws BussinessException;

   public void atualizaTemplate(String formId, Template atualizacao) throws ValidationException, BussinessException;

   public void removeTemplate(String formId) throws  BussinessException;

   public DataVO buscaDados(String formId) throws  BussinessException;

   public void atualizaDados(String formId, Map<String, Object> valores) throws ValidationException, BussinessException;

  
   
}
