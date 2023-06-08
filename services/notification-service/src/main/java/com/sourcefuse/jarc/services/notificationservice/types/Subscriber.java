package com.sourcefuse.jarc.services.notificationservice.types;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Subscriber {

	@NotNull
	private String id;

	private String name;

	private Object type;
}
