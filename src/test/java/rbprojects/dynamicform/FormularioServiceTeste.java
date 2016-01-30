package rbprojects.dynamicform;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataAccessException;

import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import rbprojects.dynamicform.config.ApplicationConfig;
import rbprojects.dynamicform.config.PersitenceConfigs;
import rbprojects.dynamicform.model.Field;
import rbprojects.dynamicform.model.Radio;
import rbprojects.dynamicform.model.Template;
import rbprojects.dynamicform.repository.TemplateRepository;
import rbprojects.dynamicform.service.BussinessException;
import rbprojects.dynamicform.service.TemplateService;
import rbprojects.dynamicform.service.ValidationException;
import rbprojects.dynamicform.vo.DataVO;
import rbprojects.dynamicform.vo.ErrorMessageDTO;

@Configuration
@EnableAutoConfiguration
public class FormularioServiceTeste {

	private static final String PELO_MENOS_UM_CAMPO_DO_FORMULARIO_E_OBRIGATÓRIO = "Pelo menos um campo do formulario e obrigatório";
	private static final String O_VALOR_DO_TÍTULO_E_OBRIGATÓRIO = "O valor do título e obrigatório";
	private static ApplicationContext appCtx;
	private static TemplateService template;
	private static TemplateRepository repo;
 

	@BeforeClass
	public static void antesDoTeste() throws DataAccessException, Exception {
		SpringApplicationBuilder build =  new SpringApplicationBuilder();
		build.profiles("teste");
		build.sources( FormularioServiceTeste.class, PersitenceConfigs.class, ApplicationConfig.class );
		appCtx =build.run(new String[0]);
		template = appCtx.getBean(TemplateService.class);
		repo = appCtx.getBean(TemplateRepository.class);
		
		repo.deleteAll();
		PersitenceConfigs persist = appCtx.getBean(PersitenceConfigs.class);
		DB db = persist.mongoDbFactory().getDb(persist.getDatabaseName());
		DBCollection table = db.getCollection("template");
		BasicDBList list = (BasicDBList) JSON.parse(loadJsonData());
		for (Object object : list) {
			DBObject obdb = (DBObject) object;
			obdb.put("_id", new ObjectId((String)obdb.get("_id")));
            table.insert((DBObject)object);
		}
	}

	public static String loadJsonData() throws IOException{
		StringBuilder build = new StringBuilder();
		InputStream data = null;
		try {
			data  = ClassLoader.getSystemResourceAsStream("data.json");
			byte[] buffer = new byte[1024];
			for (int len =  data.read(buffer); len > -1;len = data.read(buffer)) {
				build.append(new String(Arrays.copyOfRange(buffer, 0, len)));
			}
			return build.toString();
			
		} finally {
			try {
				data.close();
			} catch (Exception e) {
				
			}
		}
	}
	
	@Test
	public void testAddTemplate() throws ValidationException {
		Template temp = new Template();
		try {
			template.salvaTemplate(temp);
			Assert.fail("Era esperado um erro de validacao");
		} catch (ValidationException e) {
			ErrorMessageDTO[] dto = e.getErrors();
			Assert.assertEquals(O_VALOR_DO_TÍTULO_E_OBRIGATÓRIO, dto[0].message);
			Assert.assertEquals("Titulo", dto[0].category);
			Assert.assertEquals(PELO_MENOS_UM_CAMPO_DO_FORMULARIO_E_OBRIGATÓRIO, dto[1].message);
			Assert.assertEquals("Campos", dto[1].category);
		}
		temp.setTitle("Teste Formulario");
		Field field = new Field();
		Field field2 = new Field();
		field2.setReadOnly(true);
		field.setRequired(false);
		Field field3 = new Field();
		field3.setLabel("completo");
		field3.setType("datetime");
		field3.setRequired(true);
		field3.setReadOnly(false);
		field3.setValue("Saiu de cima");
		field3.setMaxLength(200);
		field3.setPlaceholder("My place holder");
		field3.setPlaceholder("Joaozinho");
		field3.setValue("Caiu Rolando");
		field2.setLabel("Nome 2");
		temp.setFields(new Field[]{field,field2,field3});
		try {
			template.salvaTemplate(temp);
			Assert.fail("Era esperado um erro de validacao");
		} catch (ValidationException e) {
			Assert.assertEquals("A label do campo é obrigatória", e.getErrors()[0].message);
			Assert.assertEquals("Campos:1:Label", e.getErrors()[0].category);
			Assert.assertEquals("O tipo do campo e obrigatório", e.getErrors()[1].message);
			Assert.assertEquals("Campos:1:Tipo", e.getErrors()[1].category);
			Assert.assertEquals("O tipo do campo e obrigatório", e.getErrors()[2].message);
			Assert.assertEquals("Campos:2:Tipo", e.getErrors()[2].category);
		
		}
		field.setLabel("Nome");
		field2.setType("radio");
		
		try {
			template.salvaTemplate(temp);
			Assert.fail("Era esperado um erro de validacao");
		} catch (ValidationException e) {
			Assert.assertEquals("O tipo do campo e obrigatório", e.getErrors()[0].message);
			Assert.assertEquals("Campos:1:Tipo", e.getErrors()[0].category);
			Assert.assertEquals("Os valores de um campo do tipo radio é obrigatório", e.getErrors()[1].message);
			Assert.assertEquals("Campos:2:Valores", e.getErrors()[1].category);
		}
		field.setType("date");
		Radio[] radios = new Radio[]{new Radio(null, "F"),new Radio("Masculino",null)};
		field2.setRadios(radios);
		try {
			template.salvaTemplate(temp);
			Assert.fail("Era esperado um erro de validacao");
		} catch (ValidationException e) {
			Assert.assertEquals( "A label do valor de um campo do tipo radio é obrigatória",e.getErrors()[0].message);
			Assert.assertEquals("Campos:2:Valores:1:Label", e.getErrors()[0].category);
			Assert.assertEquals("O valor de um campo tipo radio é obrigatório",e.getErrors()[1].message);
			Assert.assertEquals("Campos:2:Valores:2:Valor", e.getErrors()[1].category);
			
		}
		radios[0].setLabel("Feminino");
		radios[1].setValue("M");
		
		template.salvaTemplate(temp);
		Template tempREpo = repo.findByTitle("Teste Formulario");
		Assert.assertNotNull(tempREpo);
		repo.delete(tempREpo);
	}
	
	@Test
	public void buscaFormById() throws ValidationException, BussinessException {
		try {
			template.buscaTemplateById("56a8cc1ba1971e98f0526d67");
			Assert.fail("Era esperado a exceção:BussinessException");
		} catch (BussinessException e) {
			Assert.assertEquals("O template com o id 56a8cc1ba1971e98f0526d67 não foi encontrado",e.getMessage());
		}
		Template temp = template.buscaTemplateById("56a8cc1ba1971e98f0526df0");
		Assert.assertEquals("Formulario Usuario", temp.getTitle());
	}
	
	@Test
	public void atualizaTemplate() throws ValidationException, BussinessException{
		Template temp = template.buscaTemplateById("56a8cc1ba1971e98f0526df0");
		temp.setId(null);
		Assert.assertEquals(temp.getFields()[0].getType(), "date");
		Assert.assertNull(temp.getFields()[0].getPlaceholder());
		temp.getFields()[0].setPlaceholder("New Place Holder");
		temp.getFields()[0].setType("text");
		template.atualizaTemplate("56a8cc1ba1971e98f0526df0", temp);
		Template tempBanco = template.buscaTemplateById("56a8cc1ba1971e98f0526df0");
		Assert.assertEquals("Formulario Usuario", tempBanco.getTitle());
		Assert.assertEquals("New Place Holder", tempBanco.getFields()[0].getPlaceholder());
		Assert.assertEquals("text", tempBanco.getFields()[0].getType());
	}
	
	@Test
	public void removeTemplate() throws ValidationException, BussinessException{
		Template temp = template.buscaTemplateById("56a8cc1ca1971e98f0526d30");
		Assert.assertEquals("Formulario Empresa Remove", temp.getTitle());
		template.removeTemplate("56a8cc1ca1971e98f0526d30");
		try {
			template.buscaTemplateById("56a8cc1ca1971e98f0526d30");
			Assert.fail("Era esperado a exceção:BussinessException");
		} catch (BussinessException e) {
			Assert.assertEquals("O template com o id 56a8cc1ca1971e98f0526d30 não foi encontrado",e.getMessage());
		}
		repo.save(temp);
	}
	
	@Test
	public void buscaTemplates() throws ValidationException{
		final Template[] templateList = template.buscaTemplates();
		Assert.assertEquals(3, templateList.length);
		Assert.assertEquals("Formulario Usuario", templateList[0].getTitle());
		Assert.assertEquals("Formulario Empresa Cadastro", templateList[1].getTitle());
		Assert.assertEquals("Formulario Empresa Remove", templateList[2].getTitle());
			
	}
	@Test
	public void buscaDados() throws ValidationException, BussinessException{
		final DataVO templateList = template.buscaDados("56a8cc1ca1971e98f0526df1");
		Assert.assertEquals(2, templateList.data.length);
			
	}
	
	@Test
	public void atualizaDados() throws ValidationException, BussinessException{
		Map<String, Object> dados =  new HashMap();
		dados.put("Nome Fantasia","RBO");
		dados.put("CNPJ","123123123");
		dados.put("Telefone","234234234234234234234234");
		try {
			template.atualizaDados("56a8cc1ca1971e98f0526df1",dados);
		} catch (ValidationException e) {
			ErrorMessageDTO[] errors = e.getErrors();
			Assert.assertEquals("O valor do campo Razao Social é obrigatório", errors[0].message);
			Assert.assertEquals("Razao Social",errors[0].category);
			Assert.assertEquals("O campo Telefone deve conter no máximo 13 caracteres", errors[1].message);
			Assert.assertEquals("Telefone",errors[1].category);
		
		}
		dados.put("Razao Social","RBO industria de Ferramentas LTDA");
		dados.put("Telefone","12312313312");
		template.atualizaDados("56a8cc1ca1971e98f0526df1",dados);
	}

}
