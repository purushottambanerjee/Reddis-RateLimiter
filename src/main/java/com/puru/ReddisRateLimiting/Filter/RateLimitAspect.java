package com.puru.ReddisRateLimiting.Filter;

import com.puru.ReddisRateLimiting.API.ProviderRolesService;
import com.puru.ReddisRateLimiting.ExceptionHandling.RateLimitException;
import com.puru.ReddisRateLimiting.RateLimiter.RateLimitService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Aspect class responsible for applying rate limiting functionality.
 * This will intercept methods annotated with @RateLimit and apply the rate limiting logic.
 */
@Aspect
@Component
@EnableAspectJAutoProxy
public class RateLimitAspect {

    @Autowired
    private RateLimitService rateLimitService;

    @Autowired
    private ProviderRolesService providerRolesService;

    /**
     * This method will execute around the target method annotated with @RateLimit.
     * It checks whether the user is whitelisted and if their rate limit has been exceeded.
     *
     * @param joinPoint the join point representing the method being intercepted
     * @return the result of the method execution if allowed, otherwise throws RateLimitException
     * @throws Throwable if the method execution fails or the rate limit is exceeded
     */
    @Around("@annotation(com.puru.ReddisRateLimiting.Filter.RateLimit)")  // Intercept methods annotated with @RateLimit
    public Object whiteListed(ProceedingJoinPoint joinPoint) throws Throwable {
        // Get method arguments and check if the first argument is a client name (String)
        Object[] args = joinPoint.getArgs();
        if (args.length > 0 && args[0] instanceof String) {
            String clientName = (String) args[0];

            // Check if the client exists in the provider roles
            Optional<ProviderRolesService.ProviderRoles> userPresent = providerRolesService.findbyClientName(clientName);
            if (userPresent.isPresent()) {
                // Check if the client is whitelisted
                boolean whiteListed = providerRolesService.isWhiteListed(clientName);

                // Check if the client is allowed to make the request based on rate limiting
                boolean allowed = rateLimitService.consumeRequest(clientName, whiteListed);
                if (!allowed) {
                    // If rate limit is exceeded, throw custom RateLimitException
                    throw new RateLimitException("Rate limit exceeded for user: " + clientName);
                }

                // Proceed with the original method execution if allowed
                return joinPoint.proceed();
            } else {
                // Throw an exception if the user is not found in the provider roles
                throw new IllegalArgumentException("User not found in the provider roles");
            }
        } else {
            // Throw an exception if the first argument is not a valid String representing clientName
            throw new IllegalArgumentException("Expected first argument to be a String representing userName");
        }
    }
}
