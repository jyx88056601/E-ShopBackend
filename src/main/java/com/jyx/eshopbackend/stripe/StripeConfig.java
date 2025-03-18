package com.jyx.eshopbackend.stripe;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StripeConfig {
    @PostConstruct
    public void init() {  }
}
