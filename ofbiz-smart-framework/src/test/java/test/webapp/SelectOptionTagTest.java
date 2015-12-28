package test.webapp;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import org.huihoo.ofbiz.smart.base.C;
import org.huihoo.ofbiz.smart.entity.Delegator;
import org.huihoo.ofbiz.smart.entity.EbeanDelegator;
import org.huihoo.ofbiz.smart.entity.GenericEntityException;
import org.huihoo.ofbiz.smart.service.GenericServiceException;
import org.huihoo.ofbiz.smart.webapp.taglib.SelectOptionTag;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import junit.framework.Assert;


public class SelectOptionTagTest {
  
  private static final String CURRENT_CONTEXT = "/TagLibTestContext";
  
  private static final String SEED_SQL_FILES = "/seed_data.sql";

  private SelectOptionTag tag = new SelectOptionTag();
  private final PageContext pageContext = Mockito.mock(PageContext.class);
  private final ServletContext servletContext = Mockito.mock(ServletContext.class);
  private final HttpServletRequest httpServletRequest = Mockito.mock(HttpServletRequest.class);
  private final JspWriter jspWriter = Mockito.mock(JspWriter.class);
  private Delegator delegator = null;
  
  @Before
  public void setup() throws GenericEntityException, GenericServiceException {
    delegator = new EbeanDelegator();
    delegator.loadSeedData(SEED_SQL_FILES);
    
    tag.setPageContext(pageContext);
    Mockito.when(pageContext.getServletContext()).thenReturn(servletContext);
    Mockito.when(pageContext.getRequest()).thenReturn(httpServletRequest);
    Mockito.when(pageContext.getServletContext().getAttribute(C.CTX_DELETAGOR)).thenReturn(delegator);
    Mockito.when(httpServletRequest.getContextPath()).thenReturn(CURRENT_CONTEXT);
    Mockito.when(pageContext.getOut()).thenReturn(jspWriter);
  }
  
  @Test
  public void testSelectOptions() throws JspException, IOException {
     tag.setClassName("test.entity.CustomerType");
     
     
     tag.setLabelName("name");
     tag.setValueName("id");
     tag.setLiveTimeInSeconds(3);
     tag.doStartTag();
     tag.doEndTag();
     
     StringBuilder sb = new StringBuilder();
     sb.append("<option value='1' >潜在客户</option>").append("\r\n");
     sb.append("<option value='2' >跟踪客户</option>").append("\r\n");
     sb.append("<option value='3' >VIP客户</option>").append("\r\n");
     sb.append("<option value='4' >非客户</option>").append("\r\n");
     
     Mockito.verify(jspWriter, Mockito.times(1)).println(sb.toString());
     
     sb = new StringBuilder();
     sb.append("<option value='1' selected='selected'>潜在客户</option>").append("\r\n");
     sb.append("<option value='2' >跟踪客户</option>").append("\r\n");
     sb.append("<option value='3' >VIP客户</option>").append("\r\n");
     sb.append("<option value='4' >非客户</option>").append("\r\n");
     tag.setCurrentId(1);
     tag.doStartTag();
     tag.doEndTag();
     Mockito.verify(jspWriter, Mockito.times(1)).println(sb.toString());
     
  }
}

