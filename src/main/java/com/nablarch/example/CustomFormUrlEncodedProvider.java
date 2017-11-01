package com.nablarch.example;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import javax.ws.rs.ConstrainedTo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Encoded;
import javax.ws.rs.Produces;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nablarch.example.util.MultivaluedMapUtil;
import org.jboss.resteasy.plugins.providers.FormUrlEncodedProvider;
import org.jboss.resteasy.resteasy_jaxrs.i18n.LogMessages;
import org.jboss.resteasy.util.Encode;
import org.jboss.resteasy.util.FindAnnotation;

/**
 * @author Takehito Amanuma
 */
@Provider
@Produces("application/x-www-form-urlencoded")
@Consumes("application/x-www-form-urlencoded")
@ConstrainedTo(RuntimeType.CLIENT)
public class CustomFormUrlEncodedProvider implements MessageBodyReader<Object>
{
    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return aClass.equals(type);
    }

    @Override
    public Object readFrom(Class<Object> aClass, Type type, Annotation[] annotations, MediaType mediaType, MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        LogMessages.LOGGER.debugf("Provider : %s,  Method : readFrom", getClass().getName());
        boolean encoded = FindAnnotation.findAnnotation(annotations, Encoded.class) != null;
        String charset = mediaType.getParameters().get(MediaType.CHARSET_PARAMETER);
        if (charset == null) {
            charset = StandardCharsets.UTF_8.name();
        }
        
        FormUrlEncodedProvider formUrlEncodedProvider = new FormUrlEncodedProvider();
        ObjectMapper mapper = new ObjectMapper(); 
        if (encoded) {
            Map<String, String> flatMap = MultivaluedMapUtil.convertFlatMap(formUrlEncodedProvider.parseForm(inputStream, charset));
            return mapper.convertValue(flatMap, aClass);
            // return BeanUtil.createAndCopy(aClass, formUrlEncodedProvider.parseForm(inputStream, charset));
        } else {
            Map<String, String> flatMap = MultivaluedMapUtil.convertFlatMap(Encode.decode(formUrlEncodedProvider.parseForm(inputStream, charset), charset));
            return mapper.convertValue(flatMap, aClass);
            // return BeanUtil.createAndCopy(aClass, Encode.decode(formUrlEncodedProvider.parseForm(inputStream, charset), charset));
        }
    }
}

