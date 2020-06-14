package mvp.tinder.filters;

import lombok.extern.log4j.Log4j2;
import mvp.tinder.service.CookiesService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class CookieFilter implements Filter {
    private CookiesService cookiesService;

    @Override
    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        cookiesService = new CookiesService(req, resp);

        if (isCookieValid() && !req.getRequestURI().matches("/login")) {
            filterChain.doFilter(req, resp);
        } else if (isCookieValid() && req.getRequestURI().matches("/login")) {
            resp.sendRedirect("/users");
        } else if (!isCookieValid() && req.getRequestURI().matches("/login")) {
            filterChain.doFilter(req, resp);
        } else {
            resp.sendRedirect("/login");
        }
    }

    private boolean isCookieValid() {
        return cookiesService.getCookies() != null;
    }

    @Override
    public void destroy() {

    }
}
