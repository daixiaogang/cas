/**
 *<p>Copyright ® 太极信息国防BG版权所有。</p>
 *类名:RemoteLoginController
 *创建人:Duzh    创建时间:2017年8月14日
 */

package org.jasig.cas.web.flow;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.web.support.ArgumentExtractor;
import org.jasig.cas.web.support.CookieRetrievingCookieGenerator;
import org.jasig.cas.web.support.WebUtils;
import org.springframework.util.StringUtils;
import org.springframework.webflow.action.AbstractAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

/**
 * TODO <功能简述> <br/>
 * TODO <功能详细描述>
 * @author Duzh
 */
public class RemoteLoginAction extends AbstractAction{
    
    /** CookieGenerator for the Warnings. */  
    @NotNull  
    private CookieRetrievingCookieGenerator warnCookieGenerator;  
    /** CookieGenerator for the TicketGrantingTickets. */  
    @NotNull  
    private CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator;  
    /** Extractors for finding the service. */  
    @NotNull  
    @Size(min = 1)  
    private List<ArgumentExtractor> argumentExtractors;  
    /** Boolean to note whether we've set the values on the generators or not. */  
    private boolean pathPopulated = false;  
      
    protected Event doExecute(final RequestContext context) throws Exception  
    {  
        final HttpServletRequest request = WebUtils  
                .getHttpServletRequest(context);  
        if (!this.pathPopulated)  
        {  
            final String contextPath = context.getExternalContext()  
                    .getContextPath();  
            final String cookiePath = StringUtils.hasText(contextPath) ? contextPath  
                    : "/";  
            logger.info("Setting path for cookies to: " + cookiePath);  
            this.warnCookieGenerator.setCookiePath(cookiePath);  
            this.ticketGrantingTicketCookieGenerator.setCookiePath(cookiePath);  
            this.pathPopulated = true;  
        }  
        String ticketGrantingTicketId = this.ticketGrantingTicketCookieGenerator.retrieveCookieValue(request);
        boolean warnCookieValue = Boolean.valueOf(this.warnCookieGenerator.retrieveCookieValue(request));
        context.getFlowScope().put("ticketGrantingTicketId", ticketGrantingTicketId);  
        context.getFlowScope().put("warnCookieValue", warnCookieValue);  
          
        // 存放service url  
        // context.getFlowScope().put("serviceUrl", request.getParameter("service"));  
          
        final Service service = WebUtils.getService(this.argumentExtractors,  
                context);  
        if (service != null && logger.isDebugEnabled())
        {  
            logger.debug("Placing service in FlowScope: " + service.getId());  
        }  
        context.getFlowScope().put("service", service);  
          
        // 客户端必须传递loginUrl参数过来，否则无法确定登陆目标页面  
        if (StringUtils.hasText(request.getParameter("loginUrl")))  
        {  
        	String loginUrl = request.getParameter("loginUrl");
			System.out.println(loginUrl);
            context.getFlowScope().put("remoteLoginUrl",loginUrl);  
        } else  
        {  
            request.setAttribute("remoteLoginMessage",  
                    "loginUrl parameter must be supported.");  
            return error();  
        }  
          
        // 若参数包含submit则进行提交，否则进行验证  
        if (StringUtils.hasText(request.getParameter("submit")))  
        {  
            return result("submit");  
        } else  
        {   
        	Event event = result("checkTicketGrantingTicket");
            return event;  
        }  
    }  
      
    public void setTicketGrantingTicketCookieGenerator(  
            final CookieRetrievingCookieGenerator ticketGrantingTicketCookieGenerator)  
    {  
        this.ticketGrantingTicketCookieGenerator = ticketGrantingTicketCookieGenerator;  
    }  
      
    public void setWarnCookieGenerator(  
            final CookieRetrievingCookieGenerator warnCookieGenerator)  
    {  
        this.warnCookieGenerator = warnCookieGenerator;  
    }  
      
    public void setArgumentExtractors(  
            final List<ArgumentExtractor> argumentExtractors)  
    {  
        this.argumentExtractors = argumentExtractors;  
    }  
}
