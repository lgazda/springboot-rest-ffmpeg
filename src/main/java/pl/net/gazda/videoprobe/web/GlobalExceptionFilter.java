package pl.net.gazda.videoprobe.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import pl.net.gazda.videoprobe.domain.ErrorLogger;
import pl.net.gazda.videoprobe.domain.RestApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Handling fatal global exceptions.
 */
@Component
public class GlobalExceptionFilter extends OncePerRequestFilter {
    private final ErrorLogger errorLogger;

    @Autowired
    public GlobalExceptionFilter(ErrorLogger errorLogger) {
        this.errorLogger = errorLogger;
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            UUID exceptionUUID = errorLogger.logError(exception);
            RestApiResponse error = RestApiResponse.internalServerError(exception.getMessage(), exceptionUUID);

            response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setContentType(MediaType.APPLICATION_JSON.toString());
            response.getWriter().write(convertObjectToJson(error));
        }
    }
}
