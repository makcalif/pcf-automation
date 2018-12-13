package com.mumtaz.pcfautomation;

import org.cloudfoundry.client.CloudFoundryClient;
import org.cloudfoundry.operations.CloudFoundryOperations;
import org.cloudfoundry.operations.spaces.CreateSpaceRequest;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class PcfAutomationApplication {

	public static void main(String[] args) {
		SpringApplication.run(PcfAutomationApplication.class, args);
	}

}

@Component
class CreateSpaceAndBindServices {

	private final CloudFoundryOperations cloudFoundryOperations;
	private final CloudFoundryClient cloudFoundryClient;

	public CreateSpaceAndBindServices(CloudFoundryOperations cloudFoundryOperations, CloudFoundryClient cloudFoundryClient) {
		this.cloudFoundryOperations = cloudFoundryOperations;
		this.cloudFoundryClient = cloudFoundryClient;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void onReady(ApplicationReadyEvent event) {
		cloudFoundryOperations.applications()
				.list()
				.toStream()
				.forEach(System.out::println);

		CreateSpaceRequest.Builder createSpaceBuilder = CreateSpaceRequest.builder().name("fromApi3");
		CreateSpaceRequest createSpaceRequest = createSpaceBuilder.build();
		Mono<Void> response = cloudFoundryOperations.spaces().create(createSpaceRequest);
		System.out.println("before create space");
		response.subscribe(System.out::println);
		System.out.println("done");
	}

}
