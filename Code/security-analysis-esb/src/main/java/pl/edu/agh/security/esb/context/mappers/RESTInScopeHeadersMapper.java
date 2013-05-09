package pl.edu.agh.security.esb.context.mappers;

import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.switchyard.Context;
import org.switchyard.Property;
import org.switchyard.Scope;
import org.switchyard.component.resteasy.composer.RESTEasyBindingData;
import org.switchyard.component.resteasy.composer.RESTEasyContextMapper;

public class RESTInScopeHeadersMapper extends RESTEasyContextMapper {

	@SuppressWarnings("unchecked")
	@Override
	public void mapTo(Context context, RESTEasyBindingData target)
			throws Exception {
		MultivaluedMap<String, String> httpHeaders = target.getHeaders();
		for (Property property : context.getProperties(Scope.IN)) {
			String name = property.getName();
			if (matches(name)) {
				Object value = property.getValue();
				if (value != null) {
					if (value instanceof List) {
						httpHeaders.put(name, (List<String>) value);
					} else if (value instanceof String) {
						List<String> list = new HeaderValuesList<String>();
						list.add(String.valueOf(value));
						httpHeaders.put(name, list);
					}
				}
			}
		}
	}
}
