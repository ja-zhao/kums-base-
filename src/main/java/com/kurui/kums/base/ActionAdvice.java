package com.kurui.kums.base;

import javax.servlet.http.HttpServletRequest;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.struts.action.ActionMapping;

import com.kurui.kums.base.right.BaseRightInfo;

public class ActionAdvice implements MethodInterceptor {

	public ActionAdvice() {
	}

	public Object invoke(MethodInvocation arg0) throws Throwable {
		BaseRightInfo uri = (BaseRightInfo) getRequest(arg0).getSession()
				.getAttribute("URI");
		if (uri == null)
			return getMapping(arg0).findForward("invalid");
		else
			return arg0.proceed();
	}

	private HttpServletRequest getRequest(MethodInvocation arg0) {
		for (int i = 0; i < arg0.getArguments().length; i++)
			if (arg0.getArguments()[i].getClass().getSimpleName().equals(
					"HttpServletRequest"))
				return (HttpServletRequest) arg0.getArguments()[i];

		return null;
	}

	private ActionMapping getMapping(MethodInvocation arg0) {
		for (int i = 0; i < arg0.getArguments().length; i++)
			if (arg0.getArguments()[i].getClass().getSimpleName().equals(
					"ActionMapping"))
				return (ActionMapping) arg0.getArguments()[i];

		return null;
	}

	private String getMethodName(MethodInvocation arg0) {
		return arg0.getMethod().getName();
	}
}
