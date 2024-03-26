package telran.java51.pulse.service;

import java.util.function.Function;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import telran.java51.pulse.dto.PulseDTO;

@Configuration
@RequiredArgsConstructor
public class PulseService {

	final StreamBridge streamBridge;

	@Value("${minPulse}")
	int minPulse;
	@Value("${maxPulse}")
	int maxPulse;

	@Bean
	Function<PulseDTO, PulseDTO> dispatchMinPulse() {
		return data -> {
			if (data.getPayload() < minPulse) {
				return data;
			}
			if (data.getPayload() > maxPulse) {
//				return dispatchMaxPulse().apply(data);
				return null;
			}
			long delay = System.currentTimeMillis() - data.getTimestamp();
			System.out.println("delay: " + delay + ", id: " + data.getId() + ", pulse: " + data.getPayload());
			return null;
		};
	}
	
	@Bean
	Function<PulseDTO, PulseDTO> dispatchMaxPulse() {
		return data -> {
			if (data.getPayload() > maxPulse) {
				return data;
			}
			if (data.getPayload() < minPulse) {
//				return dispatchMinPulse().apply(data);
				return null;		
			}
			long delay = System.currentTimeMillis() - data.getTimestamp();
			System.out.println("delay: " + delay + ", id: " + data.getId() + ", pulse: " + data.getPayload());
			return null;
		};
	} 

}

