/**
 * 
 */
package spinner.sightly.use;

import javax.jcr.Node;
import javax.script.Bindings;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceUtil;
import org.apache.sling.api.scripting.SlingBindings;
import org.apache.sling.jcr.api.SlingRepository;
import org.apache.sling.scripting.sightly.render.RenderContext;
import org.apache.sling.scripting.sightly.use.ProviderOutcome;
import org.apache.sling.scripting.sightly.use.UseProvider;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Uses code from ResourceUseProvider.java 
 */
@Component(service = UseProvider.class, configurationPid = "spinner.sightly.use.UUIDResourceUseProvider", property = {
		Constants.SERVICE_RANKING + ":Integer=70" })
public class UUIDResourceUseProvider implements UseProvider {

	private SlingRepository slingRepository;

	@Reference
	void bindSlingRepository(SlingRepository slingRepository) {
		this.slingRepository = slingRepository;
	}

	@Reference
	void unbindSlingRepository(SlingRepository slingRepository) {
		this.slingRepository = slingRepository;
	}

	@interface Configuration {
		@AttributeDefinition(name = "Service Ranking", description = "The Service Ranking value acts as the priority with which this Use Provider is queried to return an "
				+ "Use-object. A higher value represents a higher priority.")
		int service_ranking() default 70;
	}

	@Override
	public ProviderOutcome provide(String identifier, RenderContext renderContext, Bindings arguments) {
		Logger logger = LoggerFactory.getLogger(this.getClass());
		logger.debug("identifier (uuid) is " + identifier);

		Bindings globalBindings = renderContext.getBindings();
		SlingHttpServletRequest request = (SlingHttpServletRequest) globalBindings.get(SlingBindings.REQUEST);
		ResourceResolver resourceResolver = request.getResourceResolver();

		try {
			Node node = slingRepository.login().getNodeByIdentifier(identifier);
			Resource resource = resourceResolver.getResource(node.getPath());
			if (resource != null && !ResourceUtil.isNonExistingResource(resource)) {
				return ProviderOutcome.success(resource);
			}
		} catch (Exception e) {
			return ProviderOutcome.failure(e);
		}
		return ProviderOutcome.failure();
	}

}
