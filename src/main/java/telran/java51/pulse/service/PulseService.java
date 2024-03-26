package telran.java51.pulse.service;

import java.util.function.Consumer;

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
	Consumer<PulseDTO> dispatchData(){
		return data -> {
			if(data.getPayload() < minPulse) {
				streamBridge.send("lowPulse-out-0", data);
				return;
			}
			if(data.getPayload() > maxPulse) {
				streamBridge.send("highPulse-out-0", data);
				return;
			}
			long delay = System.currentTimeMillis() - data.getTimestamp();
			System.out.println("delay: " + delay + ", id: " + data.getId() + ", pulse: " + data.getPayload());
		};
	}

}

//В Dispatcher дописать application properties, чтобы отправлял в разные partition
//Настроить @Value, чтобы он брал данные из applicationProperties
//Настроить интерфейс Функция (сделать 2), который будет сортировать и отправлять или данные или Null
